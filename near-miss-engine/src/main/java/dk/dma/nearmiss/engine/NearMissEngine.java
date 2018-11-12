package dk.dma.nearmiss.engine;

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.message.AisMessageException;
import dk.dma.ais.message.AisPositionMessage;
import dk.dma.ais.message.AisStaticCommon;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.tracker.Target;
import dk.dma.ais.tracker.targetTracker.TargetInfo;
import dk.dma.ais.tracker.targetTracker.TargetTracker;
import dk.dma.nearmiss.db.entity.EllipticSafetyZone;
import dk.dma.nearmiss.db.entity.VesselState;
import dk.dma.nearmiss.db.repository.VesselStateRepository;
import dk.dma.nearmiss.engine.engineParts.*;
import dk.dma.nearmiss.engine.geometry.VesselGeometryService;
import dk.dma.nearmiss.engine.nmeaBasedServices.CourseOverGroundService;
import dk.dma.nearmiss.engine.nmeaBasedServices.HeadingService;
import dk.dma.nearmiss.engine.nmeaBasedServices.SpeedOverGroundService;
import dk.dma.nearmiss.helper.Position;
import dk.dma.nearmiss.helper.PositionDecConverter;
import dk.dma.nearmiss.nmea.GpgllHelper;
import dk.dma.nearmiss.observer.Observer;
import dk.dma.nearmiss.tcp.client.TcpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import static dk.dma.nearmiss.db.entity.VesselState.SensorType.*;
import static java.lang.Double.NaN;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
public class NearMissEngine implements Observer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // Components
    private final TcpClient tcpClient;
    private final VesselStateRepository vesselStateRepository;
    private final NearMissEngineConfiguration conf;
    private final TargetTracker tracker;
    private final CourseOverGroundService courseOverGroundService;
    private final SpeedOverGroundService speedOverGroundService;
    private final HeadingService headingService;

    // Engine parts
    private final VesselGeometryService geometryService;
    private final TargetToVesselConverter targetToVesselConverter;
    private final TargetPropertyScreener targetPropertyScreener;
    private final PositionPredicter positionPredicter;
    private final VesselVicinityScreener vicinityScreener;
    private final NearMissDetector detector;

    // Own vessel state
    private final Vessel ownVessel;

    public NearMissEngine(TcpClient tcpClient,
                          VesselStateRepository vesselStateRepository,
                          TargetTracker tracker,
                          NearMissEngineConfiguration conf,
                          CourseOverGroundService courseOverGroundService,
                          SpeedOverGroundService speedOverGroundService,
                          HeadingService headingService,
                          VesselGeometryService geometryService,
                          TargetToVesselConverter targetToVesselConverter,
                          TargetPropertyScreener targetPropertyScreener,
                          PositionPredicter positionPredicter,
                          VesselVicinityScreener vicinityScreener,
                          NearMissDetector detector,
                          @Qualifier("ownVessel") Vessel ownVessel) {
        this.tcpClient = tcpClient;
        this.vesselStateRepository = vesselStateRepository;
        this.tracker = tracker;
        this.conf = conf;
        this.courseOverGroundService = courseOverGroundService;
        this.speedOverGroundService = speedOverGroundService;
        this.headingService = headingService;
        this.geometryService = geometryService;
        this.targetToVesselConverter = targetToVesselConverter;
        this.targetPropertyScreener = targetPropertyScreener;
        this.positionPredicter = positionPredicter;
        this.vicinityScreener = vicinityScreener;
        this.detector = detector;
        this.ownVessel = ownVessel;

        tcpClient.addListener(this);
    }

    TcpClient getTcpClient() {
        return tcpClient;
    }

    @Override
    public void update() {
        String receivedMessage = tcpClient.getMessage();

        if (isNotBlank(receivedMessage)) {
            logger.trace(String.format("NearMissEngine Received: %s", receivedMessage));
            if (isOwnVesselUpdate(receivedMessage)) {
                updateOwnVessel(receivedMessage);
                Set<Vessel> detectedNearMisses = detectNearMisses();
                storeNearMisses(detectedNearMisses);
            } else if (isOtherVesselUpdate(receivedMessage))
                updateOtherVessel(receivedMessage);
            else
                logger.error("Unsupported message received");
        }
    }

    private Set<Vessel> detectNearMisses() {
        // Iterate through all known other vessels and identify near misses
        Set<Vessel> nearMisses = tracker.stream()
                .filter(targetPropertyScreener)
                .map(targetToVesselConverter)
                .map(positionPredicter)
                .filter(vicinityScreener)
                .filter(detector::nearMissDetected)
                .collect(Collectors.toSet());

        logger.info("{} near misses detected.", nearMisses.size());

        return nearMisses;
    }

    private void storeNearMisses(Set<Vessel> nearMisses) {
        dk.dma.enav.model.geometry.Position ownPosition = dk.dma.enav.model.geometry.Position.create(ownVessel.getCenterPosition().getLat(), ownVessel.getCenterPosition().getLon());

        nearMisses.forEach(otherVessel -> {

            VesselState vesselState = new VesselState(
                    PREDICTED,
                    otherVessel.getMmsi(),
                    otherVessel.getName(),
                    otherVessel.getLoa(),
                    otherVessel.getBeam(),
                    otherVessel.getCenterPosition().getLat(),
                    otherVessel.getCenterPosition().getLon(),
                    (int) otherVessel.getHdg(),
                    (int) otherVessel.getCog(),
                    (int) otherVessel.getSog(),
                    otherVessel.getLastReport(),
                    true,
                    null
            );

            VesselState savedVesselState = vesselStateRepository.save(vesselState);

            double distance = ownPosition.geodesicDistanceTo(dk.dma.enav.model.geometry.Position.create(otherVessel.getCenterPosition().getLat(), otherVessel.getCenterPosition().getLon())) / 1852;
            logger.info(String.format("NEAR MISS detected with %s in position [%f, %f]. Own position is [%f, %f]. Distance is %f nautical miles.", otherVessel.getName(), otherVessel.getCenterPosition().getLat(), otherVessel.getCenterPosition().getLon(), ownVessel.getCenterPosition().getLat(), ownVessel.getCenterPosition().getLon(), distance));
        });
    }

    private void updateOwnVessel(String message) {
        logger.trace("Updating own vessel");
        // Further handling from received messages to be added here.
        // Update own ship
        // Save position for own ship.

        courseOverGroundService.update(message);
        speedOverGroundService.update(message);
        headingService.update(message);

        if (conf.isSaveAllPositions()) {
            GpgllHelper gpgllHelper = new GpgllHelper(message);
            String dmsLat = gpgllHelper.getDmsLat();
            String dmsLon = gpgllHelper.getDmsLon();
            PositionDecConverter toDec = new PositionDecConverter(dmsLat, dmsLon);
            Position pos = toDec.convert();
            LocalDateTime timestamp = gpgllHelper.getLocalDateTime(conf.getDate());

            int cog = courseOverGroundService.courseOverGround();
            int sog = speedOverGroundService.speedOverGround();
            int hdg = headingService.heading();

            VesselState ownVesselState = new VesselState(
                    GPS,
                    conf.getOwnShipMmsi(),
                    conf.getOwnShipName(),
                    conf.getOwnShipLoa(),
                    conf.getOwnShipBeam(),
                    pos.getLat(),
                    pos.getLon(),
                    hdg,
                    cog,
                    sog,
                    timestamp,
                    false,
                    new EllipticSafetyZone()
            );

            vesselStateRepository.save(ownVesselState);

            Position geometricCenter = geometryService.calulateGeometricCenter(new Position(pos.getLat(), pos.getLon()), ownVessel.getCog(), -1, -1);

            this.ownVessel.setCenterPosition(geometricCenter);
            this.ownVessel.setCog(ownVesselState.getCog());
            this.ownVessel.setSog(ownVesselState.getSog());
            this.ownVessel.setHdg(ownVesselState.getHdg());
            this.ownVessel.setLastReport(LocalDateTime.now());
        }
    }

    private void updateOtherVessel(String message) {
        logger.trace("Updating other ship");
        // Update state
        String multiLineMessage = message.replace("__r__n", "\r\n");
        AisPacket packet = AisPacket.from(multiLineMessage);
        Target target = null;
        int targetMmsi = -1;
        try {
            targetMmsi = packet.getAisMessage().getUserId();
            tracker.update(packet);
            target = tracker.get(targetMmsi);
        } catch (Exception e) {
            logger.error("Error adding AIS message to tracker.");
            e.printStackTrace();
        }

        if (target instanceof TargetInfo) {
            TargetInfo info = (TargetInfo) target;

            if (conf.isSaveAllPositions() && info != null) {
                AisStaticCommon aisStatic = null;
                AisPositionMessage aisDynamic = null;
                try {
                    if (info.hasStaticInfo())
                        aisStatic = (AisStaticCommon) info.getStaticAisPacket1().getAisMessage();

                    if (info.hasPositionInfo())
                        aisDynamic = (AisPositionMessage) info.getPositionPacket().getAisMessage();
                } catch (AisMessageException|SixbitException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }

                final int mmsi = info.getMmsi();
                final double lat = aisDynamic != null ? info.getPosition().getLatitude() : NaN;
                final double lon = aisDynamic != null ? info.getPosition().getLongitude() : NaN;
                final int hdg = aisDynamic != null ? info.getHeading() : 0;
                final int cog = aisDynamic != null ? ((int) info.getCog()) / 10 : 0;
                final int sog = aisDynamic != null ? ((int) info.getSog()) / 10 : 0;

                String name = aisStatic != null ? aisStatic.getName() : null;
                int loa = aisStatic != null ? aisStatic.getDimBow() + aisStatic.getDimStern() : 0;
                int beam = aisStatic != null ? aisStatic.getDimPort() + aisStatic.getDimStarboard() : 0;

                Position pos = new Position(info.getPosition().getLatitude(), info.getPosition().getLongitude());
                Date positionTimestamp = new Date(info.getPositionTimestamp());
                LocalDateTime timestamp = positionTimestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

                VesselState otherVesselState = new VesselState(
                    AIS, mmsi, name, loa, beam, lat, lon, hdg, cog, sog, timestamp, false, null
                );

                vesselStateRepository.save(otherVesselState);
            }
        } else {
            if (target != null)
                logger.warn("Don't know how to handle targets of type {}", target.getClass().getName());
            else
                logger.warn("Mmsi {} not found in tracker.", targetMmsi);
        }

        logger.info("Tracking {} other vessels", tracker.size());
    }

    private boolean isOwnVesselUpdate(String message) {
        return message.startsWith("$GPGLL");
    }

    private boolean isOtherVesselUpdate(String message) {
        return message.startsWith("$PGHP");
    }
}

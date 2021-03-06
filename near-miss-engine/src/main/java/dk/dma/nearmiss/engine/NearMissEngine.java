package dk.dma.nearmiss.engine;

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.message.*;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.tracker.targetTracker.TargetInfo;
import dk.dma.ais.tracker.targetTracker.TargetTracker;
import dk.dma.nearmiss.db.entity.EllipticSafetyZone;
import dk.dma.nearmiss.db.entity.VesselState;
import dk.dma.nearmiss.db.repository.VesselStateRepository;
import dk.dma.nearmiss.engine.engineParts.*;
import dk.dma.nearmiss.engine.geometry.GeometryService;
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
    private final WallclockService wallclockService;
    private final CourseOverGroundService courseOverGroundService;
    private final SpeedOverGroundService speedOverGroundService;
    private final HeadingService headingService;

    // Engine parts
    private final GeometryService geometryService;
    private final TargetToVesselConverter targetToVesselConverter;
    private final TargetPropertyScreener targetPropertyScreener;
    private final PositionPredictor positionPredictor;
    private final VesselVicinityScreener vicinityScreener;
    private final NearMissDetector detector;


    // Own vessel state
    private final Vessel ownVessel;

    public NearMissEngine(TcpClient tcpClient,
                          VesselStateRepository vesselStateRepository,
                          TargetTracker tracker,
                          NearMissEngineConfiguration conf,
                          WallclockService wallclockService,
                          CourseOverGroundService courseOverGroundService,
                          SpeedOverGroundService speedOverGroundService,
                          HeadingService headingService,
                          GeometryService geometryService,
                          TargetToVesselConverter targetToVesselConverter,
                          TargetPropertyScreener targetPropertyScreener,
                          PositionPredictor positionPredictor,
                          VesselVicinityScreener vicinityScreener,
                          NearMissDetector detector,
                          @Qualifier("ownVessel") Vessel ownVessel) {
        this.tcpClient = tcpClient;
        this.vesselStateRepository = vesselStateRepository;
        this.tracker = tracker;
        this.conf = conf;
        this.wallclockService = wallclockService;
        this.courseOverGroundService = courseOverGroundService;
        this.speedOverGroundService = speedOverGroundService;
        this.headingService = headingService;
        this.geometryService = geometryService;
        this.targetToVesselConverter = targetToVesselConverter;
        this.targetPropertyScreener = targetPropertyScreener;
        this.positionPredictor = positionPredictor;
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
                wallclockService.setCurrentDateTime(ownVessel.getLastPositionReport());
                EllipticSafetyZone safetyZone = calculateEllipticSafetyZone(ownVessel);
                storeOwnVesselAndSafetyZone(safetyZone);
                Set<Vessel> detectedNearMisses = detectNearMisses();
                storeNearMisses(detectedNearMisses);
            } else if (isOtherVesselUpdate(receivedMessage)) {
                TargetInfo target = updateTracker(receivedMessage);
                // Time is controlled by GPS alone - wallclockService.setCurrentDateTime(toLocalDateTime(target.getAisTarget().getLastReport()));
                storeOtherVessel(target);
            } else
                logger.error("Unsupported message received");
        }
    }

    private EllipticSafetyZone calculateEllipticSafetyZone(Vessel ownVessel) {
        return new EllipticSafetyZone(
                ownVessel.getCenterPosition().getLat(),
                ownVessel.getCenterPosition().getLon(),
                (int) ownVessel.getCog(),
                ownVessel.getBeam() * 1.25,
                ownVessel.getSog() * 4 + ownVessel.getLoa()
        );
    }

    private Set<Vessel> detectNearMisses() {
        // Iterate through all known other vessels and identify near misses
        Set<Vessel> nearMisses = tracker.stream()
                .filter(targetPropertyScreener)
                .map(targetToVesselConverter)
                .map(positionPredictor)
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
                    PREDICTOR,
                    otherVessel.getMmsi(),
                    otherVessel.getName(),
                    otherVessel.getLoa(),
                    otherVessel.getBeam(),
                    otherVessel.getCenterPosition().getLat(),
                    otherVessel.getCenterPosition().getLon(),
                    (int) otherVessel.getHdg(),
                    (int) otherVessel.getCog(),
                    (int) otherVessel.getSog(),
                    otherVessel.getLastPositionReport(),
                    true,
                    null
            );

            vesselStateRepository.save(vesselState);

            double distance = ownPosition.geodesicDistanceTo(dk.dma.enav.model.geometry.Position.create(otherVessel.getCenterPosition().getLat(), otherVessel.getCenterPosition().getLon())) / 1852;
            logger.info(String.format("NEAR MISS detected with %s in position [%f, %f]. Own position is [%f, %f]. Distance is %f nautical miles.", otherVessel.getName(), otherVessel.getCenterPosition().getLat(), otherVessel.getCenterPosition().getLon(), ownVessel.getCenterPosition().getLat(), ownVessel.getCenterPosition().getLon(), distance));
        });
    }

    private void storeOwnVesselAndSafetyZone(EllipticSafetyZone safetyZone) {
        if (conf.isSaveAllPositions()) {
            VesselState ownVesselState = new VesselState(
                    GPS,
                    this.ownVessel.getMmsi(),
                    this.ownVessel.getName(),
                    this.ownVessel.getLoa(),
                    this.ownVessel.getBeam(),
                    this.ownVessel.getCenterPosition().getLat(),
                    this.ownVessel.getCenterPosition().getLon(),
                    (int) this.ownVessel.getHdg(),
                    (int) this.ownVessel.getCog(),
                    (int) this.ownVessel.getSog(),
                    this.ownVessel.getLastPositionReport(),
                    false,
                    safetyZone
            );

            vesselStateRepository.save(ownVesselState);
        }
    }

    private void updateOwnVessel(String message) {
        logger.trace("Updating own vessel");

        courseOverGroundService.update(message);
        speedOverGroundService.update(message);

        GpgllHelper gpgllHelper = new GpgllHelper(message);
        String dmsLat = gpgllHelper.getDmsLat();
        String dmsLon = gpgllHelper.getDmsLon();
        PositionDecConverter toDec = new PositionDecConverter(dmsLat, dmsLon);
        Position pos = toDec.convert();
        LocalDateTime timestamp = gpgllHelper.getLocalDateTime(conf.getDate());

        Position geometricCenter = geometryService.calculateGeometricCenter(new Position(pos.getLat(), pos.getLon()), ownVessel.getHdg(), ownVessel.getBeam() / 2, ownVessel.getBeam() / 2, ownVessel.getLoa() / 2, ownVessel.getLoa() / 2);

        this.ownVessel.setCenterPosition(geometricCenter);
        this.ownVessel.setCog(courseOverGroundService.courseOverGround());
        this.ownVessel.setSog(speedOverGroundService.speedOverGround());
        ownVessel.setHdg(headingService.update((int) ownVessel.getHdg(), (int) ownVessel.getCog(), message));
        this.ownVessel.setLastPositionReport(timestamp);
    }

    private TargetInfo updateTracker(String message) {
        String multiLineMessage = message.replace("__r__n", "\r\n");
        AisPacket packet = AisPacket.from(multiLineMessage);
        TargetInfo target = null;

        try {
            int targetMmsi = packet.getAisMessage().getUserId();
            tracker.update(packet);
            target = tracker.get(targetMmsi);
        } catch (Exception e) {
            logger.error("Error adding AIS message to tracker.");
            e.printStackTrace();
        }

        logger.info("Tracking {} other vessels", tracker.size());

        return target;
    }

    private void storeOtherVessel(TargetInfo target) {
        logger.trace("Updating other ship");

        if (conf.isSaveAllPositions() && target != null) {
            AisStaticCommon aisStatic = null;
            IPositionMessage aisDynamic = null;
            try {
                if (target.hasStaticInfo())
                    aisStatic = (AisStaticCommon) target.getStaticAisPacket1().getAisMessage();

                if (target.hasPositionInfo()) {
                    AisMessage aisMessage = target.getPositionPacket().getAisMessage();
                    if (aisMessage instanceof IPositionMessage)
                        aisDynamic = (IPositionMessage) aisMessage;
                }
            } catch (AisMessageException | SixbitException e) {
                throw new RuntimeException(e.getMessage(), e);
            }

            final int mmsi = target.getMmsi();
            final double lat = aisDynamic != null ? target.getPosition().getLatitude() : NaN;
            final double lon = aisDynamic != null ? target.getPosition().getLongitude() : NaN;
            final int hdg = aisDynamic != null ? target.getHeading() : 0;
            final int cog = aisDynamic != null ? ((int) target.getCog()) / 10 : 0;
            final int sog = aisDynamic != null ? ((int) target.getSog()) / 10 : 0;

            String name = aisStatic != null ? aisStatic.getName() : null;
            name = name != null ? name.replace("@", "").trim() : null;
            int loa = aisStatic != null ? aisStatic.getDimBow() + aisStatic.getDimStern() : 0;
            int beam = aisStatic != null ? aisStatic.getDimPort() + aisStatic.getDimStarboard() : 0;

            Date positionTimestamp = new Date(target.getPositionTimestamp());
            LocalDateTime timestamp = positionTimestamp.getTime() < 0 ? null : LocalDateTimeHelper.toLocalDateTime(positionTimestamp);

            VesselState otherVesselState = new VesselState(
                    AIS, mmsi, name, loa, beam, lat, lon, hdg, cog, sog, timestamp, false, null
            );

            vesselStateRepository.save(otherVesselState);
        }
    }

    private boolean isOwnVesselUpdate(String message) {
        return message.startsWith("$GPGLL");
    }

    private boolean isOtherVesselUpdate(String message) {
        return message.startsWith("$PGHP");
    }
}

package dk.dma.nearmiss.engine;

import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.tracker.Target;
import dk.dma.ais.tracker.targetTracker.TargetInfo;
import dk.dma.ais.tracker.targetTracker.TargetTracker;
import dk.dma.nearmiss.db.entity.Message;
import dk.dma.nearmiss.db.entity.VesselPosition;
import dk.dma.nearmiss.db.repository.MessageRepository;
import dk.dma.nearmiss.db.repository.VesselPositionRepository;
import dk.dma.nearmiss.engine.engineParts.*;
import dk.dma.nearmiss.engine.geometry.VesselGeometryService;
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

import static java.lang.Double.NaN;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
public class NearMissEngine implements Observer {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // Components
    private final TcpClient tcpClient;
    private final MessageRepository messageRepository;
    private final VesselPositionRepository vesselPositionRepository;
    private final NearMissEngineConfiguration conf;
    private final TargetTracker tracker;

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
                          MessageRepository messageRepository,
                          VesselPositionRepository vesselPositionRepository,
                          TargetTracker tracker,
                          NearMissEngineConfiguration conf,
                          VesselGeometryService geometryService,
                          TargetToVesselConverter targetToVesselConverter,
                          TargetPropertyScreener targetPropertyScreener,
                          PositionPredicter positionPredicter,
                          VesselVicinityScreener vicinityScreener,
                          NearMissDetector detector,
                          @Qualifier("ownVessel") Vessel ownVessel) {
        this.tcpClient = tcpClient;
        this.messageRepository = messageRepository;
        this.vesselPositionRepository = vesselPositionRepository;
        this.tracker = tracker;
        this.conf = conf;
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
                detectNearMisses();
            } else if (isOtherVesselUpdate(receivedMessage))
                updateOtherVessel(receivedMessage);
            else
                logger.error("Unsupported message received");

            Message savedMessage = messageRepository.save(new Message(receivedMessage));
            logger.debug(String.format("Saved: %s", savedMessage));
            //logger.trace(String.format("Newest: {%s}", messageRepository.listNewest()));
        }
    }

    private void detectNearMisses() {
        // Iterate through all known other vessels and identify near misses
        Set<Vessel> nearMisses = tracker.stream()
                .filter(targetPropertyScreener)
                .map(targetToVesselConverter)
                .map(positionPredicter)
                .filter(vicinityScreener)
                .filter(detector::nearMissDetected)
                .collect(Collectors.toSet());

        // Act on detected near misses
        logger.info("{} near misses detected.", nearMisses.size());

        dk.dma.enav.model.geometry.Position ownPosition = dk.dma.enav.model.geometry.Position.create(ownVessel.getCenterPosition().getLat(), ownVessel.getCenterPosition().getLon());

        nearMisses.forEach(otherVessel -> {
            vesselPositionRepository.save(new VesselPosition(otherVessel.getMmsi(), otherVessel.getCenterPosition().getLat(), otherVessel.getCenterPosition().getLon(), (int) otherVessel.getHdg(), ownVessel.getLastReport()));

            double distance = ownPosition.geodesicDistanceTo(dk.dma.enav.model.geometry.Position.create(otherVessel.getCenterPosition().getLat(), otherVessel.getCenterPosition().getLon())) / 1852;
            logger.info(String.format("NEAR MISS detected with %s in position [%f, %f]. Own position is [%f, %f]. Distance is %f nautical miles.", otherVessel.getName(), otherVessel.getCenterPosition().getLat(), otherVessel.getCenterPosition().getLon(), ownVessel.getCenterPosition().getLat(), ownVessel.getCenterPosition().getLon(), distance));
        });
    }

    private void updateOwnVessel(String message) {
        logger.trace("Updating own vessel");
        // Further handling from received messages to be added here.
        // Update own ship
        // Save position for own ship.

        if (conf.isSaveAllPositions()) {
            GpgllHelper gpgllHelper = new GpgllHelper(message);
            String dmsLat = gpgllHelper.getDmsLat();
            String dmsLon = gpgllHelper.getDmsLon();
            PositionDecConverter toDec = new PositionDecConverter(dmsLat, dmsLon);
            Position pos = toDec.convert();
            LocalDateTime timestamp = gpgllHelper.getLocalDateTime(conf.getDate());
            vesselPositionRepository.save(new VesselPosition(conf.getOwnShipMmsi(), pos.getLat(), pos.getLon(), 0, timestamp));

            Position geometricCenter = geometryService.calulateGeometricCenter(new Position(pos.getLat(), pos.getLon()), ownVessel.getCog(), -1, -1);

            this.ownVessel.setCenterPosition(geometricCenter);
            this.ownVessel.setCog(NaN); // TODO calculate or get own cog
            this.ownVessel.setSog(NaN); // TODO calculate or get own sog
            this.ownVessel.setHdg(NaN); // TODO calculate or get own hdg
            this.ownVessel.setLastReport(LocalDateTime.now());
        }
    }

    private void updateOtherVessel(String message) {
        logger.trace("Updating other ship");
        // Update state
        String multiLineMessage = message.replace("__r__n", "\r\n");
        AisPacket packet = AisPacket.from(multiLineMessage);
        Target target = null;
        try {
            int mmsi = packet.getAisMessage().getUserId();
            tracker.update(packet);
            target = tracker.get(mmsi);
        } catch (Exception e) {
            logger.error("Error adding AIS message to tracker.");
            e.printStackTrace();
        }

        if (target instanceof TargetInfo) {
            TargetInfo info = (TargetInfo) target;

            if (conf.isSaveAllPositions() && info != null && info.getPosition() != null) {
                Position pos = new Position(info.getPosition().getLatitude(), info.getPosition().getLongitude());
                Date positionTimestamp = new Date(info.getPositionTimestamp());
                LocalDateTime timestamp = positionTimestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                vesselPositionRepository.save(new VesselPosition(info.getMmsi(), pos.getLat(), pos.getLon(), 0, timestamp));
            }
        } else {
            logger.warn("Don't know how to handle targets of type {}", target.getClass().getName());
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

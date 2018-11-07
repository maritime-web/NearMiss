package dk.dma.nearmiss.engine;

import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.tracker.Target;
import dk.dma.ais.tracker.targetTracker.TargetInfo;
import dk.dma.ais.tracker.targetTracker.TargetTracker;
import dk.dma.nearmiss.db.entity.Message;
import dk.dma.nearmiss.db.entity.VesselPosition;
import dk.dma.nearmiss.db.repository.MessageRepository;
import dk.dma.nearmiss.db.repository.VesselPositionRepository;
import dk.dma.nearmiss.helper.Position;
import dk.dma.nearmiss.helper.PositionDecConverter;
import dk.dma.nearmiss.nmea.GpgllHelper;
import dk.dma.nearmiss.observer.Observer;
import dk.dma.nearmiss.tcp.client.TcpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.Double.NaN;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
public class NearMissEngine implements Observer {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final TcpClient tcpClient;
    private final MessageRepository messageRepository;
    private final VesselPositionRepository vesselPositionRepository;
    private final NearMissEngineConfiguration conf;
    private final TargetTracker tracker;
    private final Detector detector;
    private final DistanceBasedScreener nearMissScreener;

    private final Vessel ownVessel;

    public NearMissEngine(TcpClient tcpClient, MessageRepository messageRepository,
                          VesselPositionRepository vesselPositionRepository,
                          TargetTracker tracker, NearMissEngineConfiguration conf, Detector detector,
                          DistanceBasedScreener nearMissScreener) {
        this.tcpClient = tcpClient;
        this.messageRepository = messageRepository;
        this.vesselPositionRepository = vesselPositionRepository;
        this.tracker = tracker;
        this.conf = conf;
        this.detector = detector;
        this.nearMissScreener = nearMissScreener;
        this.ownVessel = new Vessel(0, "UNKNOWN", 0, 0);

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
            if (isOwnShipUpdate(receivedMessage)) {
                updateOwnVessel(receivedMessage);
                detectNearMisses();
            } else if (isOtherShipUpdate(receivedMessage))
                updateOtherShip(receivedMessage);
            else
                logger.error("Unsupported message received");

            Message savedMessage = messageRepository.save(new Message(receivedMessage));
            logger.debug(String.format("Saved: %s", savedMessage));
            //logger.trace(String.format("Newest: {%s}", messageRepository.listNewest()));
        }
    }

    /** Iterate through all known other vessels and identify near misses */
    private void detectNearMisses() {
        Set<Vessel> nearMisses = tracker.stream()
                .map(this::toVessel)
                .filter(v -> isRecentlyUpdated(v))
                .filter(v -> isRelevantType(v))
                .map(v -> projectForward(v) )
                .filter(v -> nearMissScreener.nearMissCandidate(ownVessel, v))
                .filter(v -> detector.nearMissDetected(ownVessel, v))
                .collect(Collectors.toSet());

        logger.info("{} near misses detected.", nearMisses.size());
    }

    /** Determine whether a vessel updated recently enough to be considered for near-miss analysis */
    private boolean isRecentlyUpdated(Vessel v) {
        return Duration.between(v.getLastReport(), LocalDateTime.now()).get(ChronoUnit.MINUTES) > 15;
    }

    /** Determine whether a vessel is of a type relevant for near-miss analysis */
    private boolean isRelevantType(Vessel v) {
        return true;
    }

    /** Project vessel's position forward in time */
    private Vessel projectForward(Vessel v) {
        return v;
    }

    /** Convert TargetInfo to Vessel */
    private Vessel toVessel(TargetInfo t) {
        return new Vessel(t.getMmsi(), "UNKNOWN", 0, 0);
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

            this.ownVessel.setLat(pos.getLat());
            this.ownVessel.setLon(pos.getLon());
            this.ownVessel.setCog(NaN);
            this.ownVessel.setSog(NaN);
            this.ownVessel.setHdg(NaN);
            this.ownVessel.setLastReport(LocalDateTime.now());
        }
    }

    private void updateOtherShip(String message) {
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

    private boolean isOwnShipUpdate(String message) {
        return message.startsWith("$GPGLL");
    }

    private boolean isOtherShipUpdate(String message) {
        return message.startsWith("$PGHP");
    }
}

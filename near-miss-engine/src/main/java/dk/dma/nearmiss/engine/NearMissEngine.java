package dk.dma.nearmiss.engine;

import dk.dma.ais.packet.AisPacket;
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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

@Component
public class NearMissEngine implements Observer {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final TcpClient tcpClient;
    private final MessageRepository messageRepository;
    private final VesselPositionRepository vesselPositionRepository;
    private final NearMissVesselState state;
    private final NearMissEngineConfiguration conf;
    private final TargetTracker tracker = new TargetTracker();

    public NearMissEngine(TcpClient tcpClient, MessageRepository messageRepository,
                          VesselPositionRepository vesselPositionRepository, NearMissVesselState state,
                          NearMissEngineConfiguration conf) {
        this.tcpClient = tcpClient;
        this.messageRepository = messageRepository;
        this.vesselPositionRepository = vesselPositionRepository;
        this.state = state;
        this.conf = conf;
        tcpClient.addListener(this);
    }

    TcpClient getTcpClient() {
        return tcpClient;
    }


    @Override
    public void update() {
        String receivedMessage = tcpClient.getMessage();

        logger.trace(String.format("NearMissEngine Received: %s", receivedMessage));
        if (isOwnShipUpdate(receivedMessage))
            handleGpsUpdate(receivedMessage);
        else if (isOtherShipUpdate(receivedMessage))
            updateOtherShip(receivedMessage);
        else
            logger.error("Unsupported message received");

        Message savedMessage = messageRepository.save(new Message(receivedMessage));
        logger.debug(String.format("Saved: %s", savedMessage));
        //logger.trace(String.format("Newest: {%s}", messageRepository.listNewest()));

    }

    private void handleGpsUpdate(String message) {
        logger.trace("Updating own ship");
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
        }

        // Run screening
        Map<String, NearMissVessel> vessels = new NearMissScreener(state.getOwnVessel(), state.getOtherVessels()).screen();
        // Kick-start save position for screened ships.
        // Kick-start near-miss calculations on screening result.
        logger.debug(String.format("%s ships has been screened for near-miss calculation", vessels.size()));
    }

    private void updateOtherShip(String message) {
        logger.trace("Updating other ship");
        // Update state
        String multiLineMessage = message.replace("__r__n", "\r\n");
        AisPacket packet = AisPacket.from(multiLineMessage);
        TargetInfo info = null;
        try {
            int mmsi = packet.getAisMessage().getUserId();
            tracker.update(packet);
            info = tracker.get(mmsi);
        } catch (Exception e) {
            logger.error("Error adding AIS message to tracker.");
            e.printStackTrace();
        }

        if (conf.isSaveAllPositions() && info != null && info.getPosition() != null) {
            Position pos = new Position(info.getPosition().getLatitude(), info.getPosition().getLongitude());
            Date positionTimestamp = new Date(info.getPositionTimestamp());
            LocalDateTime timestamp = positionTimestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            vesselPositionRepository.save(new VesselPosition(info.getMmsi(), pos.getLat(), pos.getLon(), 0, timestamp));
        }


        // Run screening to obtain map of all relevant other ships.

        // Further handling from received messages to be added here.
    }

    private boolean isOwnShipUpdate(String message) {
        return message.startsWith("$GPGLL");
    }

    private boolean isOtherShipUpdate(String message) {
        return message.startsWith("$PGHP");
    }
}

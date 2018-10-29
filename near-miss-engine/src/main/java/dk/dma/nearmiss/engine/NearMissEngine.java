package dk.dma.nearmiss.engine;

import dk.dma.nearmiss.db.entity.Message;
import dk.dma.nearmiss.db.repository.MessageRepository;
import dk.dma.nearmiss.observer.Observer;
import dk.dma.nearmiss.tcp.client.TcpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NearMissEngine implements Observer {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final TcpClient tcpClient;
    private final MessageRepository messageRepository;
    private final NearMissVesselState state;

    public NearMissEngine(TcpClient tcpClient, MessageRepository messageRepository, NearMissVesselState state) {
        this.tcpClient = tcpClient;
        this.messageRepository = messageRepository;
        this.state = state;
        tcpClient.addListener(this);
    }

    TcpClient getTcpClient() {
        return tcpClient;
    }


    @Override
    public void update() {
        String receivedMessage = tcpClient.getMessage();

        logger.trace(String.format("NearMissEngine Received: %s", receivedMessage));
        if (isOwnShipUpdate(receivedMessage)) updateOwnShip();

        // Further handling from received messages to be added here.
        Message savedMessage = messageRepository.save(new Message(receivedMessage));
        logger.debug(String.format("Saved: %s", savedMessage));
        logger.trace(String.format("Newest: {%s}", messageRepository.listNewest()));

    }

    private void updateOwnShip() {
        logger.trace("Updating own ship");
        //More to come...
    }

    private boolean isOwnShipUpdate(String message) {
        return message.startsWith("$GPGLL");
    }

}

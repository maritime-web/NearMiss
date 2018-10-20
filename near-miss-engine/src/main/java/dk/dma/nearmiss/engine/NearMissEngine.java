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

    public NearMissEngine(TcpClient tcpClient, MessageRepository messageRepository) {
        this.tcpClient = tcpClient;
        this.messageRepository = messageRepository;
        tcpClient.addListener(this);
    }

    TcpClient getTcpClient() {
        return tcpClient;
    }

    @Override
    public void update() {
        String receivedMessage = tcpClient.getMessage();

        logger.info(String.format("NearMissEngine Received: %s", receivedMessage));

        // Further handling from received messages to be added here.
        Message savedMessage = messageRepository.save(new Message(receivedMessage));
        logger.debug(String.format("Saved message: %s", savedMessage));
        logger.trace(String.format("Newest: {%s}", messageRepository.listNewest()));

    }
}

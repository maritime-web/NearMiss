package dk.dma.nearmiss.engine;

import dk.dma.nearmiss.observer.Observer;
import dk.dma.nearmiss.tcp.client.TcpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NearMissEngine implements Observer {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final TcpClient tcpClient;

    public NearMissEngine(TcpClient tcpClient) {
        this.tcpClient = tcpClient;
        tcpClient.addListener(this);
    }

    TcpClient getTcpClient() {
        return tcpClient;
    }

    @Override
    public void update() {
        logger.info(String.format("NearMissEngine Received: %s", tcpClient.getMessage()));
        // Further handling from received messages to be added here.
    }
}

package dk.dma.nearmiss.aissimulator;

import dk.dma.nearmiss.tcp.client.TcpClient;
import dk.dma.nearmiss.observer.Observer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AisSimulator implements Observer {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final TcpClient tcpClient;

    public AisSimulator(TcpClient tcpClient) {
        this.tcpClient = tcpClient;
        tcpClient.addListener(this);
    }

    @Override
    public void update() {
        logger.info(String.format("AisSimulator has received: %s", tcpClient.getMessage()));
    }

    TcpClient getTcpClient() {
        return tcpClient;
    }
}

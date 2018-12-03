package dk.dma.nearmiss.tcp.server;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("tcp.server")
public class TcpServerConfiguration {
    private int localPort;

    public int getLocalPort() {
        return localPort;
    }

    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }
}

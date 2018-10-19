package dk.dma.nearmiss.aissimulator;

import dk.dma.nearmiss.tcp.server.TcpServer;
import dk.dma.nearmiss.tcp.server.TcpServerConfiguration;
import org.springframework.stereotype.Component;

@Component
public class AisSimulatorServer extends TcpServer {

    public AisSimulatorServer(AisSimulator aisSimulator, TcpServerConfiguration configuration) {
        super(aisSimulator, configuration);
    }
}

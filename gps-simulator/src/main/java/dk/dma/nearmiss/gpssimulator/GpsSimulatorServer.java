package dk.dma.nearmiss.gpssimulator;

import dk.dma.nearmiss.tcp.server.TcpServer;
import dk.dma.nearmiss.tcp.server.TcpServerConfiguration;
import org.springframework.stereotype.Component;

@Component
public final class GpsSimulatorServer extends TcpServer {

    GpsSimulatorServer(GpsSimulator gpsSimulator, TcpServerConfiguration configuration) {
        super(gpsSimulator, configuration);
    }

}

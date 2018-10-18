package dk.dma.nearmiss.aissimulator;

import dk.dma.nearmiss.simulator.Simulator;
import dk.dma.nearmiss.tcp.client.TcpClient;
import dk.dma.nearmiss.observer.Observer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AisSimulator extends Simulator implements Observer {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final TcpClient tcpClient;

    private String input;
    private String output;

    public AisSimulator(TcpClient tcpClient) {
        this.tcpClient = tcpClient;
        tcpClient.addListener(this);
    }

    @Override
    public void update() {
        input = tcpClient.getMessage();
        logger.info(String.format("Received: %s", input));
        run(); // run AisServer server part.
    }

    TcpClient getTcpClient() {
        return tcpClient;
    }

    @Override
    public String getMessage() {
        return output;
    }

    @Override
    public void run() {
        // Do something with input/state.
        output = input;
        notifyListeners();
    }
}

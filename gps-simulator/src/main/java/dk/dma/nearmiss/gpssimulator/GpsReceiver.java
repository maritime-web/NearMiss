package dk.dma.nearmiss.gpssimulator;

import dk.dma.nearmiss.gpssimulator.client.GpsSimulatorClient;
import dk.dma.nearmiss.gpssimulator.observer.Observer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GpsReceiver implements Observer {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final GpsSimulatorClient client;

    public GpsReceiver(GpsSimulatorClient client) {
        this.client = client;
        client.addListener(this);
    }

    @Override
    public void update() {
        logger.info(String.format("Client has received: %s", client.getAnswer()));
    }

    public GpsSimulatorClient getClient() {
        return client;
    }
}

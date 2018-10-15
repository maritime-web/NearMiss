package dk.dma.nearmiss.aissimulator;

import dk.dma.nearmiss.gpssimulator.client.GpsSimulatorClient;
import dk.dma.nearmiss.gpssimulator.observer.Observer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AisSimulator implements Observer {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final GpsSimulatorClient gpsSimulatorClient;

    public AisSimulator(GpsSimulatorClient gpsSimulatorClient) {
        this.gpsSimulatorClient = gpsSimulatorClient;
        gpsSimulatorClient.addListener(this);
    }

    @Override
    public void update() {
        logger.info(String.format("AisSimulator has received: %s", gpsSimulatorClient.getAnswer()));
    }

    GpsSimulatorClient getGpsSimulatorClient() {
        return gpsSimulatorClient;
    }
}

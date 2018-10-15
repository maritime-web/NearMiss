package dk.dma.nearmiss.gpssimulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GpsLogSimulator implements Observer {
    private Gps gps;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    GpsLogSimulator(Gps gps) {
        this.gps = gps;
    }

    @Override
    public void update() {
        logger.info(String.format("%s: GpsLogSimulator", gps.getMessage()));
    }
}

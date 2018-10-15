package dk.dma.nearmiss.gpssimulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GpsLogSimulator implements Observer {
    private GpsSimulator gps;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    GpsLogSimulator(GpsSimulator gps) {
        this.gps = gps;
        this.gps.addListener(this);
    }

    @Override
    public void update() {
        logger.info(String.format("%s: GpsLogSimulator", gps.getMessage()));
    }
}

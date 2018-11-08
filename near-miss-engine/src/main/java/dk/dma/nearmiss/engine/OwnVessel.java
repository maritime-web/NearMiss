package dk.dma.nearmiss.engine;

import org.springframework.stereotype.Component;

@Component
public class OwnVessel extends Vessel {
    public OwnVessel() {
        super(0); // TODO make own mmsi configurable
    }
}

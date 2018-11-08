package dk.dma.nearmiss.engine;

import org.springframework.stereotype.Component;

@Component
public class OwnVessel extends Vessel {

    {
        setLoa(100); // TODO make LOA of own vessel configurable
        setBeam(25); // TODO make beam of own vessel configurable
    }

    public OwnVessel() {
        super(0); // TODO make own mmsi configurable
    }
}

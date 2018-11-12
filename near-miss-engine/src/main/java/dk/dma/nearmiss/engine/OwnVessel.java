package dk.dma.nearmiss.engine;

import org.springframework.stereotype.Component;

@Component
public class OwnVessel extends Vessel {

    public OwnVessel(NearMissEngineConfiguration conf) {
        super(conf.getOwnShipMmsi());
        this.setName(conf.getOwnShipName());
        this.setLoa(conf.getOwnShipLoa());
        this.setBeam(conf.getOwnShipBeam());
    }

}

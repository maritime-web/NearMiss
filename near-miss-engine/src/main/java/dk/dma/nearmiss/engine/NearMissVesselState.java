package dk.dma.nearmiss.engine;

import org.springframework.stereotype.Component;

import java.util.Hashtable;
import java.util.Map;

@SuppressWarnings("ALL")
@Component
public class NearMissVesselState {
    private final NearMissVessel ownVessel;
    private final Map<String, NearMissVessel> otherVessels;

    public NearMissVesselState() {
        this.ownVessel = new NearMissVessel();
        this.otherVessels = new Hashtable<>();
    }

    public NearMissVessel getOwnVessel() {
        return ownVessel;
    }

    public Map<String, NearMissVessel> getOtherVessels() {
        return otherVessels;
    }
}

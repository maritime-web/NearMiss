package dk.dma.nearmiss.engine;

import java.util.Hashtable;
import java.util.Map;

/**
 * The function this class is to limit near-miss calculations by reducing the set of
 * other vessels to only contains the relevant ones.
 * <p>
 * Rules are to not include:
 * Ships too far away?
 * Ships with mmsi mentioned in the mmsi.ignore configuration (yet to be created)
 * Ships with an earlier near-miss detected and still in interval (interval configuration).
 */
class NearMissScreener {
    // Use a form of immutable map?
    private final NearMissVessel ownVessel;
    private final Map<String, NearMissVessel> otherVessels;

    NearMissScreener(NearMissVessel ownVessel, Map<String, NearMissVessel> otherVessels) {
        this.ownVessel = ownVessel;
        this.otherVessels = otherVessels;
    }

    Map<String, NearMissVessel> screen() {
        Map<String, NearMissVessel> result = new Hashtable<>();

        for (String mmsi : otherVessels.keySet()) {
            NearMissVessel otherVessel = otherVessels.get(mmsi);
            if (screenVessel(ownVessel, ownVessel)) {
                result.put(mmsi, otherVessel);
            }
        }
        return result;
    }

    private boolean screenVessel(NearMissVessel ownVessel, NearMissVessel otherVessel) {
        //distance
        //interval
        //ignore list
        return true;
    }

}

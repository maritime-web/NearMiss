package dk.dma.nearmiss.engine;

import org.springframework.stereotype.Component;

/**
 * The function this class is to limit near-miss calculations by reducing the set of
 * other vessels to only contains the relevant ones.
 * <p>
 * Rules are to not include:
 * Ships too far away?
 * Ships with mmsi mentioned in the mmsi.ignore configuration (yet to be created)
 * Ships with an earlier near-miss detected and still in interval (interval configuration).
 */
@Component
class DistanceBasedScreener implements Screener {

    private final int maximumDistance;

    public DistanceBasedScreener() {
        this.maximumDistance = 2000;
    }

    @Override
    public boolean nearMissCandidate(Vessel ownVessel, Vessel otherVessel) {
        //distance
        //interval
        //ignore list
        return true;
    }

}

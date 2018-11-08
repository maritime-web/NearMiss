package dk.dma.nearmiss.engine.engineParts;

import dk.dma.nearmiss.engine.Vessel;

public interface NearMissDetector {
    /**
     * Determines with precision wether a near-miss situation is present between
     * own vessel and another vessel.
     *
     * The method returns true if, and only if, a near-miss situation is present.
     *
     * @param otherVessel State of other vessel.
     * @return true if a near miss situation is at hand; false otherwise.
     */
    boolean nearMissDetected(Vessel otherVessel);
}

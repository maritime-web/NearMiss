package dk.dma.nearmiss.engine;

public interface Detector {
    /**
     * Determines with precision wether a near-miss situation is present between
     * own vessel and another vessel.
     *
     * The method returns true if, and only if, a near-miss situation is present.
     *
     * @param ownVessel State of own vessel.
     * @param otherVessel State of other vessel.
     * @return true if a near miss situation is at hand; false otherwise.
     */
    boolean nearMissDetected(Vessel ownVessel, Vessel otherVessel);
}

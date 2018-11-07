package dk.dma.nearmiss.engine;

/**
 * Screener's may quick screen two vessels to determine wheter or not a near-miss situation
 * might be possible.
 */
public interface Screener {

    /**
     * Return true if otherVessel is in a possible near-miss situation with ownVessel.
     *
     * This method may return false positives; but never false negatives.
     *
     * @param ownVessel State of own vessel
     * @param otherVessel State of other vessel
     * @return true if there is possibility of a near miss situation; false otherwise.
     */
    boolean nearMissCandidate(Vessel ownVessel, Vessel otherVessel);

}

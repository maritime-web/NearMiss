package dk.dma.nearmiss.engine.engineParts;

import dk.dma.ais.tracker.targetTracker.TargetInfo;

import java.util.function.Predicate;

/**
 * Quickly screen the input to determine whether or not a near-miss situation
 * with other vessel's might be possible.
 */
public class TargetPropertyScreener implements Predicate<TargetInfo> {

    /**
     * Return true if targetInfo has properties which qualifies it for near-miss analysis.
     *
     * This method may return false positives; but never false negatives.
     *
     * @param targetInfo Target to be considered.
     * @return true if there is possibility of a near miss situation; false otherwise.
     */
    @Override
    public boolean test(TargetInfo targetInfo) {
        return IS_BLACK_LISTED.and(HAS_POSITION_INFO.and(IS_MOVING)).test(targetInfo);
    }

    // TODO make black list configurable
    private final static Predicate<TargetInfo> IS_BLACK_LISTED = t -> false;

    // TODO make speed configurable
    private final static Predicate<TargetInfo> IS_MOVING = t-> t.getSog() > 3f;

    private final static Predicate<TargetInfo> HAS_POSITION_INFO = t -> t.hasPositionInfo();

}

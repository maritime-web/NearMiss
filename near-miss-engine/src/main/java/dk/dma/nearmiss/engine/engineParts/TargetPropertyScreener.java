package dk.dma.nearmiss.engine.engineParts;

import dk.dma.ais.tracker.targetTracker.TargetInfo;
import dk.dma.nearmiss.engine.NearMissEngineConfiguration;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.function.Predicate;

import static dk.dma.nearmiss.engine.engineParts.LocalDateTimeHelper.toLocalDateTime;

/**
 * Quickly screen the input to determine whether or not a near-miss situation
 * with other vessel's might be possible.
 */
@Component
public class TargetPropertyScreener implements Predicate<TargetInfo> {

    public TargetPropertyScreener(NearMissEngineConfiguration configuration, WallclockService wallclock) {
        IS_BLACK_LISTED = t -> false;
        IS_MOVING = t -> t.getSog() / 10f > configuration.getScreenerMinSpeed();
        HAS_POSITION_INFO = t -> t.hasPositionInfo();
        IS_RELEVANT_TYPE = t -> true;
        IS_RECENTLY_UPDATED = t -> Duration.between(toLocalDateTime(t.getAisTarget().getLastReport()), wallclock.getCurrentDateTime()).toMinutes() <= configuration.getScreenerMaxMinutesSinceLastUpdate();
    }

    /**
     * Return true if targetInfo has properties which qualifies it for near-miss analysis.
     * <p>
     * This method may return false positives; but never false negatives.
     *
     * @param targetInfo Target to be considered.
     * @return true if there is possibility of a near miss situation; false otherwise.
     */
    @Override
    public boolean test(TargetInfo targetInfo) {
        return IS_BLACK_LISTED.negate().and(HAS_POSITION_INFO).and(IS_RELEVANT_TYPE).and(IS_RECENTLY_UPDATED).and(IS_MOVING).test(targetInfo);
    }

    // TODO make black list configurable
    private final Predicate<TargetInfo> IS_BLACK_LISTED;

    private final Predicate<TargetInfo> IS_MOVING;

    private final Predicate<TargetInfo> HAS_POSITION_INFO;

    // TODO make relevant types configurable
    /**
     * Determine whether a vessel is of a type relevant for near-miss analysis
     */
    private final Predicate<TargetInfo> IS_RELEVANT_TYPE;

    /**
     * Determine whether a vessel updated recently enough to be considered for near-miss analysis
     */
    private final Predicate<TargetInfo> IS_RECENTLY_UPDATED;

}

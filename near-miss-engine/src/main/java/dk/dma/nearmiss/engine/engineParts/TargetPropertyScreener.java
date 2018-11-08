package dk.dma.nearmiss.engine.engineParts;

import dk.dma.ais.tracker.targetTracker.TargetInfo;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.function.Predicate;

/**
 * Quickly screen the input to determine whether or not a near-miss situation
 * with other vessel's might be possible.
 */
@Component
public class TargetPropertyScreener implements Predicate<TargetInfo> {

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
    private final static Predicate<TargetInfo> IS_BLACK_LISTED = t -> false;

    // TODO make speed configurable
    private final static Predicate<TargetInfo> IS_MOVING = t -> t.getSog() > 3f;

    private final static Predicate<TargetInfo> HAS_POSITION_INFO = t -> t.hasPositionInfo();

    // TODO make relevant types configurable
    /**
     * Determine whether a vessel is of a type relevant for near-miss analysis
     */
    private final static Predicate<TargetInfo> IS_RELEVANT_TYPE = t -> true;

    // TODO make duration threshold configurable
    /**
     * Determine whether a vessel updated recently enough to be considered for near-miss analysis
     */
    private final static Predicate<TargetInfo> IS_RECENTLY_UPDATED = t -> Duration.between(toLocalDateTime(t.getAisTarget().getLastReport()), LocalDateTime.now()).toMinutes() > 15;

    private static LocalDateTime toLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

}

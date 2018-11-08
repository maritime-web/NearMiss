package dk.dma.nearmiss.engine.engineParts;

import dk.dma.nearmiss.engine.Vessel;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.function.Predicate;

/**
 * Quickly screen the input to determine whether or not a near-miss situation
 * with other vessel's might be possible.
 */
public class VesselPropertyScreener implements Predicate<Vessel> {

    /**
     * Return true if vessel has properties which qualifies it for near-miss analysis.
     *
     * This method may return false positives; but never false negatives.
     *
     * @param vessel Vessel to be considered.
     * @return true if there is possibility of a near miss situation; false otherwise.
     */
    @Override
    public boolean test(Vessel vessel) {
        return IS_RELEVANT_TYPE.and(IS_RECENTLY_UPDATED).test(vessel);
    }

    // TODO make relevant types configurable
    /** Determine whether a vessel is of a type relevant for near-miss analysis */
    private final static Predicate<Vessel> IS_RELEVANT_TYPE = v -> true;

    // TODO make duration threshold configurable
    /** Determine whether a vessel updated recently enough to be considered for near-miss analysis */
    private final static Predicate<Vessel> IS_RECENTLY_UPDATED = v -> Duration.between(v.getLastReport(), LocalDateTime.now()).toMinutes() > 15;

}

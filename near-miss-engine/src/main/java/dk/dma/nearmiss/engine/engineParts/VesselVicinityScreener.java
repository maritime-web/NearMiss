package dk.dma.nearmiss.engine.engineParts;

import dk.dma.nearmiss.engine.Vessel;
import dk.dma.nearmiss.helper.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Predicate;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Determine whether or not a vessel is in vicinity of a given position.
 */
public class VesselVicinityScreener implements Predicate<Vessel> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Position position;

    /**
     * @param position The position to consider vicinity of.
     */
    public VesselVicinityScreener(Position position) {
        this.position = position;
    }

    /**
     * Return true if vessel is in vicinity of given position.
     *
     * @param vessel Vessel to be considered to be in vicinity of position.
     * @return true if vessel is in vicinity of position; false otherwise.
     */
    @Override
    public boolean test(Vessel vessel) {
        dk.dma.enav.model.geometry.Position vesselPosition = dk.dma.enav.model.geometry.Position.create(vessel.getCenterPosition().getLat(), vessel.getCenterPosition().getLon());
        double distance = vesselPosition.geodesicDistanceTo(dk.dma.enav.model.geometry.Position.create(position.getLat(), position.getLon())) / 1852;

        logger.debug(String.format("Distance to %s (in position [%f %f]) is %f nautical miles", nameOrMmsi(vessel), vessel.getCenterPosition().getLat(), vessel.getCenterPosition().getLon(), distance));

        // TODO make vicinity threshold configurable
        return distance < 3.0; // nautical miles
    }

    private String nameOrMmsi(Vessel v) {
        return isBlank(v.getName()) ? String.valueOf(v.getMmsi()) : v. getName();
    }

}

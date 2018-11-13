package dk.dma.nearmiss.engine.engineParts;

import dk.dma.enav.model.geometry.Position;
import dk.dma.nearmiss.engine.Vessel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.function.Function;

/** Project vessel's position forward in time */
@Component
public class PositionPredictor implements Function<Vessel, Vessel> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public PositionPredictor(WallclockService wallclock) {
        this.wallclock = wallclock;
    }

    @Override
    public Vessel apply(Vessel vessel) {
        final LocalDateTime lastPositionReport = vessel.getLastPositionReport();
        final LocalDateTime now = wallclock.getCurrentDateTime();
        final Duration t = Duration.between(lastPositionReport, now);
        final double cog = vessel.getCog();
        final double sog = vessel.getSog();
        final double distanceInNauticalMiles = sog * t.getSeconds() / 3600;
        final double distanceInMeters = distanceInNauticalMiles * 1852;
        final Position lastKnownPosition = Position.create(vessel.getCenterPosition().getLat(), vessel.getCenterPosition().getLon());
        final Position predictedPosition = lastKnownPosition.positionAt(vessel.getCog(), distanceInMeters);

        logger.debug("Vessel {} at {} is predicted {} secs ahead along course {} and speed {} kts to position {}", vessel.getMmsi(), lastKnownPosition, t.getSeconds(), cog, sog, predictedPosition);

        vessel.setCenterPosition(new dk.dma.nearmiss.helper.Position(predictedPosition.getLatitude(), predictedPosition.getLongitude()));
        vessel.setLastPositionReport(now);

        return vessel;
    }

    private final WallclockService wallclock;

}

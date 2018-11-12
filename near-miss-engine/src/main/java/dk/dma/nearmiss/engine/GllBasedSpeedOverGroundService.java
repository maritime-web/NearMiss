package dk.dma.nearmiss.engine;

import dk.dma.nearmiss.helper.Position;
import dk.dma.nearmiss.helper.PositionDecConverter;
import dk.dma.nearmiss.nmea.GpgllHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Service
@Primary
public class GllBasedSpeedOverGroundService implements SpeedOverGroundService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Position lastPosition;
    private LocalTime timeOfLastPosition;

    @Override
    public int speedOverGround(String message) {
        GpgllHelper gllHelper = new GpgllHelper(message);
        String currentLat = gllHelper.getDmsLat();
        String currentLon = gllHelper.getDmsLon();
        LocalTime timeOfCurrentPosition = gllHelper.getLocalTime();

        PositionDecConverter positionDecConverter = new PositionDecConverter(currentLat, currentLon);
        Position currentPosition = positionDecConverter.convert();

        int sog = -1;

        try {
            if (timeOfLastPosition == null) {
                logger.debug("Initializing last position with {}",  currentLat);
            } else if (timeOfLastPosition.until(timeOfCurrentPosition, ChronoUnit.MINUTES) > 5) {
                logger.warn("Last position is too old - reinitializing with {}",  currentLat);
            } else {
                dk.dma.enav.model.geometry.Position p0 = dk.dma.enav.model.geometry.Position.create(lastPosition.getLat(), lastPosition.getLon());
                dk.dma.enav.model.geometry.Position p1 = dk.dma.enav.model.geometry.Position.create(currentPosition.getLat(), currentPosition.getLon());
                double distanceMeters = p0.geodesicDistanceTo(p1);
                double distanceNauticalMiles = distanceMeters / 1852;
                double timeSeconds = timeOfLastPosition.until(timeOfCurrentPosition, ChronoUnit.SECONDS);
                double speedNauticalMilesPerSecond = distanceNauticalMiles / timeSeconds;
                double speedNauticalMilesPerHour = speedNauticalMilesPerSecond * 3600;
                sog = (int) speedNauticalMilesPerHour;
            }
        } finally {
            timeOfLastPosition = timeOfCurrentPosition;
            lastPosition = currentPosition;
            logger.debug("sog = {}", sog);
        }

        return sog;
    }

}

package dk.dma.nearmiss.engine.nmeaBasedServices;

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
public class GllBasedCourseOverGroundService implements CourseOverGroundService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Position lastPosition;
    private LocalTime timeOfLastUpdate;
    private int courseOverGround = -1;

    @Override
    public void update(String message) {
        GpgllHelper gllHelper = new GpgllHelper(message);
        String currentLat = gllHelper.getDmsLat();
        String currentLon = gllHelper.getDmsLon();
        LocalTime timeOfCurrentPosition = gllHelper.getLocalTime();

        PositionDecConverter positionDecConverter = new PositionDecConverter(currentLat, currentLon);
        Position currentPosition = positionDecConverter.convert();

        int tmpCog = -1;

        try {
            if (timeOfLastUpdate == null) {
                logger.debug("Initializing last position with {}",  currentLat);
            } else if (timeOfLastUpdate.until(timeOfCurrentPosition, ChronoUnit.MINUTES) > 5) {
                logger.warn("Last position is too old - reinitializing with {}",  currentLat);
            } else {
                dk.dma.enav.model.geometry.Position p0 = dk.dma.enav.model.geometry.Position.create(lastPosition.getLat(), lastPosition.getLon());
                dk.dma.enav.model.geometry.Position p1 = dk.dma.enav.model.geometry.Position.create(currentPosition.getLat(), currentPosition.getLon());
                tmpCog = (int) p0.geodesicInitialBearingTo(p1);
            }
        } finally {
            timeOfLastUpdate = timeOfCurrentPosition;
            lastPosition = currentPosition;
            logger.debug("cog = {}", tmpCog);
        }

        courseOverGround = tmpCog;
    }

    @Override
    public int courseOverGround() {
        return courseOverGround;
    }

    @Override
    public LocalTime timeOfLastUpdate() {
        return timeOfLastUpdate;
    }

}

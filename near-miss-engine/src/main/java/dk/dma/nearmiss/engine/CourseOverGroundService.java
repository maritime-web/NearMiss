package dk.dma.nearmiss.engine;

import java.time.LocalTime;

public interface CourseOverGroundService {
    /** Feed service with new NMEA message */
    void update(String message);

    /** Return estimated course over ground */
    int courseOverGround();

    LocalTime timeOfLastUpdate();
}

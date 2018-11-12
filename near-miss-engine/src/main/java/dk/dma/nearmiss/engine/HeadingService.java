package dk.dma.nearmiss.engine;

import java.time.LocalTime;

public interface HeadingService {
    /** Feed service with new NMEA message */
    void update(String message);

    /** Return estimated true heading */
    int heading();

    LocalTime timeOfLastUpdate();
}

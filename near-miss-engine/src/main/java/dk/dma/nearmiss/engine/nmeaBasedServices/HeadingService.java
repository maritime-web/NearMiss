package dk.dma.nearmiss.engine.nmeaBasedServices;

import java.time.LocalTime;

public interface HeadingService {
    /** Feed service with new NMEA message */
    void update(String message);

    /** Return estimated true heading */
    int heading();

    @SuppressWarnings("unused")
    LocalTime timeOfLastUpdate();
}

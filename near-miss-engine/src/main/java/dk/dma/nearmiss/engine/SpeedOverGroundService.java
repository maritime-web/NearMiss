package dk.dma.nearmiss.engine;

import java.time.LocalTime;

public interface SpeedOverGroundService {
    /** Feed service with new NMEA message */
    void update(String message);

    /** Return estimated speed over ground */
    int speedOverGround();

    LocalTime timeOfLastUpdate();
}

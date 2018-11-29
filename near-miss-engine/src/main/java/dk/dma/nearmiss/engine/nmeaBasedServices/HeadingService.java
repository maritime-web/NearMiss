package dk.dma.nearmiss.engine.nmeaBasedServices;

public interface HeadingService {

    /**
     * Calculate new heading from latest and NMEA message
     */
    int update(int heading, int courseOverGround, String message);

}

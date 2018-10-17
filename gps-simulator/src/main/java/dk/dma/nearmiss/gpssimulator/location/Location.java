package dk.dma.nearmiss.gpssimulator.location;

import java.time.LocalDateTime;

/**
 * Initial code. Thank you to: Mobility Services Lab
 * See <a href="https://www.mobility-services.in.tum.de/?p=2335">https://www.mobility-services.in.tum.de/?p=2335</a>
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Location {

    private final String name;
    private final double latitude;
    private final double longitude;

    public Location(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public boolean sameLocationAs(Location location) {
        if (location == null) return false;
        return location.latitude == latitude && location.longitude == longitude;
    }

    public Location copy(String name) {
        return new Location(name, latitude, longitude);
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }



}
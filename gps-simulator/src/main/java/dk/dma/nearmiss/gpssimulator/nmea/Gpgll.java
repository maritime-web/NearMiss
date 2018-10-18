package dk.dma.nearmiss.gpssimulator.nmea;

import dk.dma.nearmiss.gpssimulator.location.Location;
import dk.dma.nearmiss.gpssimulator.location.LocationConverter;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Gpgll {
    @SuppressWarnings("WeakerAccess")
    public static final String MESSAGE_TYPE = "GPGLL";
    private final Location location;
    private final Instant instant;

    public Gpgll(Location location) {
        this.instant = Instant.now();
        this.location = location;
    }

    private String getTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmmss").withZone(ZoneId.of("UTC"));
        return formatter.format(instant);
    }

    private String getGpsLocation() {
        return new LocationConverter(location).toDegreeMinutesSeconds();
    }

    private String calculateChecksum() {
        return "XX";
    }

    @Override
    public String toString() {
        return String.format("$%s,%s,%s,A,*%s", MESSAGE_TYPE, getGpsLocation(), getTime(), calculateChecksum());
    }
}

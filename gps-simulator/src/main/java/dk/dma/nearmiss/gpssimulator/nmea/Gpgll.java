package dk.dma.nearmiss.gpssimulator.nmea;

import dk.dma.nearmiss.gpssimulator.location.Location;
import dk.dma.nearmiss.gpssimulator.location.LocationConverter;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Gpgll {
    @SuppressWarnings("WeakerAccess")
    public static final String MESSAGE_TYPE = "GPGLL";
    private final Location location;
    private final LocalTime time;

    public Gpgll(Location location, LocalTime time) {
        this.time = time;
        this.location = location;
    }

    public String getTime() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmmss").withZone(ZoneId.of("UTC"));
        return formatter.format(time);
    }

    public String getFormattedTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss VV").withZone(ZoneId.of("UTC"));
        return formatter.format(time);
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

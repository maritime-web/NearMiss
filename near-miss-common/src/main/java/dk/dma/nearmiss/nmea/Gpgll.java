package dk.dma.nearmiss.nmea;

import dk.dma.nearmiss.helper.PositionHelper;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Gpgll {
    @SuppressWarnings("WeakerAccess")
    public static final String MESSAGE_TYPE = "GPGLL";
    private final double lat;
    private final double lon;
    private final LocalTime time;

    public Gpgll(double lat, double lon, LocalTime time) {
        this.lat = lat;
        this.lon = lon;
        this.time = time;
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
        return new PositionHelper(lat, lon).toDegreeMinutesSeconds();
    }

    private String calculateChecksum() {
        return "XX";
    }

    @Override
    public String toString() {
        return String.format("$%s,%s,%s,A,*%s", MESSAGE_TYPE, getGpsLocation(), getTime(), calculateChecksum());
    }
}

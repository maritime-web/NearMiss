package dk.dma.nearmiss.nmea;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@SuppressWarnings("WeakerAccess")
public final class GpgllHelper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmmss");

    private final String gpgllString;

    public GpgllHelper(String gpgllString) {
        this.gpgllString = gpgllString;
    }

    public LocalTime getLocalTime() {
        String[] separated = gpgllString.split(",");
        String timeString = separated[5];
        return LocalTime.parse(timeString, formatter);
    }

    public String getDmsLat() {
        String[] separated = gpgllString.split(",");
        return String.format("%s,%s", separated[1], separated[2]);
    }

    public String getDmsLon() {
        String[] separated = gpgllString.split(",");
        return String.format("%s,%s", separated[3], separated[4]);
    }

    public LocalDateTime getLocalDateTime(LocalDate localDate) {
        return LocalDateTime.of(localDate, getLocalTime());
    }

}

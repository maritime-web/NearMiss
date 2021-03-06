package dk.dma.nearmiss.helper;


/**
 * This class converts a GPS position to Decimal format from Degreees Minutes Seconds.
 * Input Strings to look like "5712.40,N" for latitude or "01143.60,E" for longitude.
 */
//todo: To be cleaned up! (NMEA minutes/fraction)
@SuppressWarnings("WeakerAccess")
public final class PositionDecConverter {
    private final String dmsLat;
    private final String dmsLon;

    public PositionDecConverter(String dmsLat, String dmsLon) {
        this.dmsLat = dmsLat;
        this.dmsLon = dmsLon;
    }

    public Position convert() {
        if (dmsLat == null || dmsLon == null) {
            return new Position(0, 0);
        }
        return new Position(fromDms(dmsLat, true), fromDms(dmsLon, false));
    }

    double degrees(String degreesMinutesSeconds, boolean isLat) {
        int endIndex = isLat ? 2 : 3;
        return Double.valueOf(degreesMinutesSeconds.substring(0, endIndex));
    }

    double minutes(String degreesMinutesSeconds, boolean isLat) {
        int startIndex = isLat ? 2 : 3;
        int endIndex = isLat ? 4 : 5;
        return Double.valueOf(degreesMinutesSeconds.substring(startIndex, endIndex));
    }

    double seconds(String degreesMinutesSeconds, boolean isLat) {
        int startIndex = isLat ? 5 : 6;
        int endIndex = isLat ? 9 : 10;
        return Double.valueOf(degreesMinutesSeconds.substring(startIndex, endIndex));
    }

    double letter(String degreesMinutesSeconds) {
        String letter = degreesMinutesSeconds.substring(degreesMinutesSeconds.length() - 1);
        return ("N".equals(letter) || "E".equals(letter)) ? 1D : -1D;
    }

    double fromDms(String degreesMinutesSeconds, boolean isLat) {
        // Decimal Degrees = Degrees + minutes/60 + seconds/3600

        // Not seconds, but fraction of minutes! NMEA
        double seconds = seconds(degreesMinutesSeconds, isLat) / 10000; // todo: forkert?
        double minutes = (minutes(degreesMinutesSeconds, isLat) + seconds) / 60;

        //double fraction = minutes + seconds;
        return letter(degreesMinutesSeconds) * (degrees(degreesMinutesSeconds, isLat) + minutes);
    }

}

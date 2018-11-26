package dk.dma.nearmiss.helper;

/**
 * This class converts a GPS position to Degree Minutes Seconds from Decimal format.
 */
public final class PositionDmsConverter {
    private final double lat;
    private final double lon;

    public PositionDmsConverter(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    @SuppressWarnings("unused")
    public PositionDmsConverter(Position position) {
        this.lat = position.getLat();
        this.lon = position.getLon();
    }

    private String convert(double decimalDegrees, boolean isLongtitude) {
        double absDecimalDegrees = Math.abs(decimalDegrees);
        int degrees = Double.valueOf(absDecimalDegrees).intValue();

        double decimal = absDecimalDegrees - degrees;
        double upscaled = decimal * 3600; // milliseconds
        double minutes = upscaled / 60; // minutes
        double seconds = upscaled % 60; //

        String strDegrees = (isLongtitude) ? pad(degrees, 3) : pad(degrees, 2);
        return String.format("%s%s.%s", strDegrees, pad(Double.valueOf(minutes).intValue(), 2), pad((int) Math.round(seconds), 2));
    }

    String pad(int number, int padLength) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < padLength; i++) {
            sb.append("0");
        }

        String num = String.valueOf(number);
        return (sb.toString() + num).substring(num.length());
    }

    private String latitudeLetter() {
        return (lat < 0) ? "S" : "N";
    }

    private String longtitudeLetter() {
        return (lon < 0) ? "W" : "E";
    }

    public String convert() {
        String latitude = convert(lat, false);
        String longtitude = convert(lon, true);
        return String.format("%s,%s,%s,%s", latitude, latitudeLetter(), longtitude, longtitudeLetter());
    }
}


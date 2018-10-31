package dk.dma.nearmiss.helper;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * This classe converts a GPS position to Degree Minutes Seconds from Decimal format.
 */
public final class PositionDmsConverter {
    private final double lat;
    private final double lon;

    public PositionDmsConverter(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public PositionDmsConverter(Position position) {
        this.lat = position.getLat();
        this.lon = position.getLon();
    }

    private String convert(double decimalDegrees, boolean isLongtitude) {
        BigDecimal absDecimalDegrees = BigDecimal.valueOf(decimalDegrees).abs();
        int degrees = absDecimalDegrees.intValue();

        BigDecimal decimal = absDecimalDegrees.subtract(BigDecimal.valueOf(degrees));
        decimal = decimal.setScale(5, RoundingMode.FLOOR);

        BigDecimal bd60 = BigDecimal.valueOf(60L);
        BigDecimal minutes = decimal.multiply(bd60);

        BigDecimal upscaled = decimal.multiply(bd60).multiply(bd60);
        BigDecimal seconds = upscaled.remainder(bd60);
        seconds = seconds.setScale(0, RoundingMode.HALF_UP);

        String strDegrees = (isLongtitude) ? pad(degrees, 3) : pad(degrees, 2);
        return String.format("%s%s.%s", strDegrees, pad(minutes.intValue(), 2), pad(seconds.intValue(), 2));
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


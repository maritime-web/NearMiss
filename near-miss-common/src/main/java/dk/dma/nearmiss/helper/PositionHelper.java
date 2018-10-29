package dk.dma.nearmiss.helper;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PositionHelper {
    private final double lat;
    private final double lon;

    public PositionHelper(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    private String toDegreeMinutesSeconds(double decimalDegrees, boolean isLongtitude) {
        BigDecimal absDecimalDegrees = BigDecimal.valueOf(decimalDegrees).abs();
        int degrees = absDecimalDegrees.intValue();
        BigDecimal decimal = absDecimalDegrees.subtract(BigDecimal.valueOf(degrees));

        BigDecimal bd60 = BigDecimal.valueOf(60L);
        int minutes = decimal.multiply(bd60).intValue();

        BigDecimal upscaled = decimal.multiply(bd60).multiply(bd60);
        BigDecimal seconds = upscaled.remainder(bd60).multiply(bd60);
        seconds = seconds.setScale(0, RoundingMode.HALF_UP);

        String strDegrees = (isLongtitude) ? pad(degrees, 3) : pad(degrees, 2);
        return String.format("%s%s.%s", strDegrees, pad(minutes, 2), pad(seconds.intValue(), 2));
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

    public String toDegreeMinutesSeconds() {
        String latitude = toDegreeMinutesSeconds(lat, false);
        String longtitude = toDegreeMinutesSeconds(lon, true);
        return String.format("%s,%s,%s,%s", latitude, latitudeLetter(), longtitude, longtitudeLetter());
    }
}


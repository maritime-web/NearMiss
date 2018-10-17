package dk.dma.nearmiss.gpssimulator.location;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class LocationConverter {
    private final Location location;

    public LocationConverter(Location location) {
        this.location = location;
    }

    String pad(int number, int padLength) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < padLength; i++) {
            sb.append("0");
        }

        String num = String.valueOf(number);
        return (sb.toString() + num).substring(num.length());
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

    private String latitudeLetter() {
        return (location.getLatitude() < 0) ? "S" : "N";
    }

    private String longtitudeLetter() {
        return (location.getLongitude() < 0) ? "W" : "E";
    }

    public String toDegreeMinutesSeconds() {
        // $GPGLL,4916.45,N,12311.12,W,225444,A,*1D

        String latitude = toDegreeMinutesSeconds(location.getLatitude(), false);
        String longtitude = toDegreeMinutesSeconds(location.getLongitude(), true);
        return String.format("%s,%s,%s,%s", latitude, latitudeLetter(), longtitude, longtitudeLetter());
    }
}

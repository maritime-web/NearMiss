package dk.dma.nearmiss.helper;

@SuppressWarnings("WeakerAccess")
public class Position {
    private final double lat;
    private final double lon;

    public Position(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}

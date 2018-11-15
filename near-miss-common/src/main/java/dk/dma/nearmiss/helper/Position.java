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

    @Override
    public String toString() {
        return "Position{" +
                "lat=" + lat +
                ", lon=" + lon +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return Double.compare(position.lat, lat) == 0 &&
                Double.compare(position.lon, lon) == 0;
    }

}

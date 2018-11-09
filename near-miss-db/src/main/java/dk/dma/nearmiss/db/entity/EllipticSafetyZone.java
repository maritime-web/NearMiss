package dk.dma.nearmiss.db.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class EllipticSafetyZone {

    /** Latitude of center */
    @NotNull
    @Column(name = "ESZ_CENTER_LATITUDE")
    private final Double latitude;

    /** Longitude of center */
    @NotNull
    @Column(name = "ESZ_CENTER_LONGITUDE")
    private final Double longitude;

    /** Orientation of major axis (in true degrees; 0 = north) */
    @NotNull
    @Column(name = "ESZ_ORIENT_MAJOR")
    private final Integer orientationOfMajor;

    /** Length of semi major axis (in meters) */
    @NotNull
    @Column(name = "ESZ_A")
    private final Double semiMajor;

    /** Length of semi minor axis (in meters) */
    @NotNull
    @Column(name = "ESZ_B")
    private final Double semiMinor;

    public EllipticSafetyZone() {
        this.latitude = null;
        this.longitude = null;
        this.orientationOfMajor = null;
        this.semiMinor = null;
        this.semiMajor = null;
    }

    public EllipticSafetyZone(double latitude, double longitude, int orientationOfMajor, double semiMinor, double semiMajor) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.orientationOfMajor = orientationOfMajor;
        this.semiMinor = semiMinor;
        this.semiMajor = semiMajor;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Integer getOrientationOfMajor() {
        return orientationOfMajor;
    }

    public Double getSemiMinor() {
        return semiMinor;
    }

    public Double getSemiMajor() {
        return semiMajor;
    }

    @Override
    public String toString() {
        return "EllipticSafetyZone{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", orientationOfMajor=" + orientationOfMajor +
                ", semiMinor=" + semiMinor +
                ", semiMajor=" + semiMajor +
                '}';
    }
}

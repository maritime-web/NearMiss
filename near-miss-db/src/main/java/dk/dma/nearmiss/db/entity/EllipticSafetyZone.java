package dk.dma.nearmiss.db.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class EllipticSafetyZone {

    /**
     * Latitude of center
     */
    @NotNull
    @Column(name = "ESZ_CENTER_LATITUDE")
    private Double latitude;

    /**
     * Longitude of center
     */
    @NotNull
    @Column(name = "ESZ_CENTER_LONGITUDE")
    private Double longitude;

    /**
     * Orientation of major axis (in true degrees; 0 = north)
     */
    @NotNull
    @Column(name = "ESZ_ORIENT_MAJOR")
    private Integer orientationOfMajor;

    /**
     * Length of semi major axis (in meters)
     */
    @NotNull
    @Column(name = "ESZ_A")
    private Double semiMajor;

    /**
     * Length of semi minor axis (in meters)
     */
    @NotNull
    @Column(name = "ESZ_B")
    private Double semiMinor;

    @SuppressWarnings("unused")
    public EllipticSafetyZone() {
        this.latitude = null;
        this.longitude = null;
        this.orientationOfMajor = null;
        this.semiMinor = null;
        this.semiMajor = null;
    }

    public EllipticSafetyZone(double latitude, double longitude, int orientationOfMajor, double semiMinor, double semiMajor) {
        this.latitude = Double.isNaN(latitude) ? null : latitude;
        this.longitude = Double.isNaN(longitude) ? null : longitude;
        this.orientationOfMajor = orientationOfMajor;
        this.semiMinor = Double.isNaN(semiMinor) ? null : semiMinor;
        this.semiMajor = Double.isNaN(semiMajor) ? null : semiMajor;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    @SuppressWarnings("unused")
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

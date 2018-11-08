package dk.dma.nearmiss.db.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class VesselState {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private final int mmsi;
    private final String name;
    private final int loa;
    private final int beam;

    private final double latitude;
    private final double longitude;
    private final int hdg;
    private final float cog;
    private final int sog;
    private final LocalDateTime positionTime;

    private final boolean positionPredicted;
    private final boolean isNearMiss;

    public VesselState(int mmsi, String name, int loa, int beam, double latitude, double longitude, int hdg, float cog, int sog, LocalDateTime positionTime, boolean positionPredicted, boolean isNearMiss) {
        this.mmsi = mmsi;
        this.name = name;
        this.loa = loa;
        this.beam = beam;
        this.latitude = latitude;
        this.longitude = longitude;
        this.hdg = hdg;
        this.cog = cog;
        this.sog = sog;
        this.positionTime = positionTime;
        this.positionPredicted = positionPredicted;
        this.isNearMiss = isNearMiss;
    }

    /** JPA entity id */
    public Long getId() {
        return id;
    }

    /** Get vessel's MMSI number */
    public int getMmsi() {
        return mmsi;
    }

    /** Get vessel's name */
    public String getName() {
        return name;
    }

    /** Get vessel's length-overall (meters) */
    public int getLoa() {
        return loa;
    }

    /** Get vessel's beam (meters) */
    public int getBeam() {
        return beam;
    }

    /** Get vessel's latitude (decimal degrees) */
    public double getLatitude() {
        return latitude;
    }

    /** Get vessel's longitude (decimal degrees) */
    public double getLongitude() {
        return longitude;
    }

    /** Get vessel's true heading (degrees) */
    public int getHdg() {
        return hdg;
    }

    /** Get vessel's true course over ground (degrees) */
    public float getCog() {
        return cog;
    }

    /** Get vessel's speed over ground (knots) */
    public int getSog() {
        return sog;
    }

    /** Get time of vessel's position */
    public LocalDateTime getPositionTime() {
        return positionTime;
    }

    /** Is position predicted or reported? */
    public boolean isPositionPredicted() {
        return positionPredicted;
    }

    /** Is other vessel in near-miss situation with own vessel? */
    public boolean isNearMiss() {
        return isNearMiss;
    }

    @Override
    public String toString() {
        return "VesselState{" +
                "id=" + id +
                ", mmsi=" + mmsi +
                ", name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", hdg=" + hdg +
                ", cog=" + cog +
                ", sog=" + sog +
                ", positionTime=" + positionTime +
                ", positionPredicted=" + positionPredicted +
                ", isNearMiss=" + isNearMiss +
                '}';
    }
}


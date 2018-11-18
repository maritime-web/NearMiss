package dk.dma.nearmiss.db.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@SuppressWarnings({"JpaQlInspection", "unused"})
@Entity
@NamedQueries(value = {
    @NamedQuery(
            name = "listVesselStates",
            query = "select vs from VesselState vs where vs.latitude <> 'NaN' and vs.longitude <> 'NaN' " +
                    " and vs.positionTime between :from and :to and vs.isNearMiss in :nearMissStates "
    ),
    @NamedQuery(
            name = "listVesselStatesByMmsi",
            query = "select vs from VesselState vs where vs.mmsi = :mmsi " +
                    " and vs.latitude <> 'NaN' and vs.longitude <> 'NaN' " +
                    " and vs.positionTime between :from and :to and vs.isNearMiss in :nearMissStates "
    )

})
public class VesselState {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SensorType sensorType;

    @NotNull
    private final int mmsi;
    private final String name;
    private final int loa;
    private final int beam;
    private final double latitude;
    private final double longitude;
    private final int hdg;
    private final int cog;
    private final int sog;
    private final LocalDateTime positionTime;

    private boolean isNearMiss;

    @Embedded
    private EllipticSafetyZone safetyZone;

    public VesselState(SensorType sensorType, int mmsi, String name, int loa, int beam, double latitude, double longitude, int hdg, int cog, int sog, LocalDateTime positionTime, boolean isNearMiss, EllipticSafetyZone safetyZone) {
        this.sensorType = sensorType;
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
        this.isNearMiss = isNearMiss;
        this.safetyZone = safetyZone;
    }

    @SuppressWarnings("unused")
    public VesselState() {
        // Dummy values to allow final on fields.
        this(SensorType.AIS, -1, null, -1, -1, -1.0, -1.0, -1, -1, -1, null, false, null);
    }

    /**
     * JPA entity id
     */
    public Long getId() {
        return id;
    }

    /**
     * Get sensor type
     */
    public SensorType getSensorType() {
        return sensorType;
    }

    /**
     * Get vessel's MMSI number
     */
    public int getMmsi() {
        return mmsi;
    }

    /**
     * Get vessel's name
     */
    public String getName() {
        return name;
    }

    /**
     * Get vessel's length-overall (meters)
     */
    public int getLoa() {
        return loa;
    }

    /**
     * Get vessel's beam (meters)
     */
    public int getBeam() {
        return beam;
    }

    /**
     * Get vessel's latitude (decimal degrees)
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Get vessel's longitude (decimal degrees)
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Get vessel's true heading (degrees)
     */
    public int getHdg() {
        return hdg;
    }

    /**
     * Get vessel's true course over ground (degrees)
     */
    public int getCog() {
        return cog;
    }

    /**
     * Get vessel's speed over ground (knots)
     */
    public int getSog() {
        return sog;
    }

    /**
     * Get time of vessel's position
     */
    public LocalDateTime getPositionTime() {
        return positionTime;
    }

    /**
     * Is other vessel in near-miss situation with own vessel?
     */
    public boolean isNearMiss() {
        return isNearMiss;
    }

    public EllipticSafetyZone getSafetyZone() {
        return safetyZone;
    }

    @Override
    public String toString() {
        return "VesselState{" +
                "id=" + id +
                ", sensorType=" + sensorType +
                ", mmsi=" + mmsi +
                ", name='" + name + '\'' +
                ", loa=" + loa +
                ", beam=" + beam +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", hdg=" + hdg +
                ", cog=" + cog +
                ", sog=" + sog +
                ", positionTime=" + positionTime +
                ", isNearMiss=" + isNearMiss +
                ", safetyZone=" + safetyZone +
                '}';
    }

    public enum SensorType {
        /**
         * Information is predicted or assumed
         */
        PREDICTOR,
        /**
         * Information received from GPS sensor
         */
        GPS,
        /**
         * Information received from AIS sensor
         */
        AIS;
    }

}

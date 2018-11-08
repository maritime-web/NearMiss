package dk.dma.nearmiss.db.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class VesselState {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private int mmsi;
    private double latitude;
    private double longitude;
    private int heading;
    private LocalDateTime positionTime;

    public VesselState() {
    }

    public VesselState(int mmsi, double latitude, double longitude, int heading, LocalDateTime positionTime) {
        this.mmsi = mmsi;
        this.latitude = latitude;
        this.longitude = longitude;
        this.heading = heading;
        this.positionTime = positionTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getMmsi() {
        return mmsi;
    }

    public void setMmsi(int mmsi) {
        this.mmsi = mmsi;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getHeading() {
        return heading;
    }

    public void setHeading(int heading) {
        this.heading = heading;
    }

    public LocalDateTime getPositionTime() {
        return positionTime;
    }

    public void setPositionTime(LocalDateTime positionTime) {
        this.positionTime = positionTime;
    }

    @Override
    public String toString() {
        return "VesselState{" +
                "id=" + id +
                ", mmsi=" + mmsi +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", heading=" + heading +
                ", positionTime=" + positionTime +
                '}';
    }
}


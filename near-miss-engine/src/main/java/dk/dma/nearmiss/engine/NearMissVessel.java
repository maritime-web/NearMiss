package dk.dma.nearmiss.engine;

import java.time.LocalDateTime;

@SuppressWarnings("WeakerAccess")
public class NearMissVessel {
    private String mmsi; // Maritime Mobile Service Identity
    private String name;

    private Double lat;
    private Double lon;

    private Double cog; // Assuming heading to be same as cog(?)
    private Double sog; // Speed

    private Double length; // loa
    private Double width; // beam

    private LocalDateTime lastReport; // Updated time in UTC
    private LocalDateTime latestNearMissDetect;

    public NearMissVessel() {
        super();
    }

    public NearMissVessel(String name, Double lat, Double lon, Double cog, Double sog, Double length, Double width, LocalDateTime lastReport) {
        this(null, name, lat, lon, cog, sog, length, width, lastReport);
    }

    public NearMissVessel(String mmsi, String name, Double lat, Double lon, Double cog, Double sog, Double length, Double width, LocalDateTime lastReport) {
        this.mmsi = mmsi;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.cog = cog;
        this.sog = sog;
        this.length = length;
        this.width = width;
        this.lastReport = lastReport;
    }

    public String getMmsi() {
        return mmsi;
    }

    public void setMmsi(String mmsi) {
        this.mmsi = mmsi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getCog() {
        return cog;
    }

    public void setCog(Double cog) {
        this.cog = cog;
    }

    public Double getSog() {
        return sog;
    }

    public void setSog(Double sog) {
        this.sog = sog;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public LocalDateTime getLastReport() {
        return lastReport;
    }

    public void setLastReport(LocalDateTime lastReport) {
        this.lastReport = lastReport;
    }

    public LocalDateTime getLatestNearMissDetect() {
        return latestNearMissDetect;
    }

    public void setLatestNearMissDetect(LocalDateTime latestNearMissDetect) {
        this.latestNearMissDetect = latestNearMissDetect;
    }

    @Override
    public String toString() {
        return "NearMissVessel{" +
                "mmsi='" + mmsi + '\'' +
                ", name='" + name + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", cog=" + cog +
                ", sog=" + sog +
                ", length=" + length +
                ", width=" + width +
                ", lastReport=" + lastReport +
                ", latestNearMissDetect=" + latestNearMissDetect +
                '}';
    }
}

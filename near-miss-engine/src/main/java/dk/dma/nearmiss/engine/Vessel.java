package dk.dma.nearmiss.engine;

import java.time.LocalDateTime;

import static java.lang.Double.NaN;

@SuppressWarnings("WeakerAccess")
public class Vessel {
    // Immutable state
    private final int mmsi;    // Maritime Mobile Service Identity
    private final String name; // Vessel name
    private final int loa;     // Length over all (in meters)
    private final int beam;    // Beam (in meters)

    // Mutable state
    private double lat = NaN;
    private double lon = NaN;
    private double cog = NaN; // Assuming heading to be same as cog(?)
    private double hdg = NaN; // Assuming heading to be same as cog(?)
    private double sog = NaN; // Speed

    private LocalDateTime lastReport; // Updated time in UTC

    public Vessel(int mmsi, String name, int loa, int beam) {
        this.mmsi = mmsi;
        this.name = name;
        this.loa = loa;
        this.beam = beam;
    }

    public int getMmsi() {
        return mmsi;
    }

    public String getName() {
        return name;
    }

    public int getLoa() {
        return loa;
    }

    public int getBeam() {
        return beam;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getCog() {
        return cog;
    }

    public void setCog(double cog) {
        this.cog = cog;
    }

    public double getHdg() {
        return hdg;
    }

    public void setHdg(double hdg) {
        this.hdg = hdg;
    }

    public double getSog() {
        return sog;
    }

    public void setSog(double sog) {
        this.sog = sog;
    }

    public LocalDateTime getLastReport() {
        return lastReport;
    }

    public void setLastReport(LocalDateTime lastReport) {
        this.lastReport = lastReport;
    }
}

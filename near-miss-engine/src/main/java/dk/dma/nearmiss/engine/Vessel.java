package dk.dma.nearmiss.engine;

import dk.dma.nearmiss.helper.Position;

import java.time.LocalDateTime;

import static java.lang.Double.NaN;

@SuppressWarnings("WeakerAccess")
public class Vessel {
    // Immutable state
    private final int mmsi;    // Maritime Mobile Service Identity

    // Mutable state
    private String name; // Vessel name
    private Position centerPosition;  // Position of vessel's geometric center
    private int loa;     // Length over all (in meters)
    private int beam;    // Beam (in meters)
    private double cog = NaN; // Assuming heading to be same as cog(?)
    private double hdg = NaN; // Assuming heading to be same as cog(?)
    private double sog = NaN; // Speed

    private LocalDateTime lastPositionReport; // Updated time in UTC

    public Vessel(int mmsi) {
        this.mmsi = mmsi;
    }

    public int getMmsi() {
        return mmsi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Position getCenterPosition() {
        return centerPosition;
    }

    public void setCenterPosition(Position centerPosition) {
        this.centerPosition = centerPosition;
    }

    public int getLoa() {
        return loa;
    }

    public void setLoa(int loa) {
        this.loa = loa;
    }

    public int getBeam() {
        return beam;
    }

    public void setBeam(int beam) {
        this.beam = beam;
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

    public LocalDateTime getLastPositionReport() {
        return lastPositionReport;
    }

    public void setLastPositionReport(LocalDateTime lastPositionReport) {
        this.lastPositionReport = lastPositionReport;
    }
}

package dk.dma.nearmiss.gpssimulator.location;

/**
 * Initial code. Thank you to: Mobility Services Lab
 * See <a href="https://www.mobility-services.in.tum.de/?p=2335">https://www.mobility-services.in.tum.de/?p=2335</a>
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Route {

    private final Location initialLocation;
    private final Location[] waypoints;

    public Route(Location initialLocation, Location... waypoints) {
        this.initialLocation = initialLocation;
        this.waypoints = waypoints;
    }

    public Location getInitialLocation() {
        return initialLocation;
    }

    public Location[] getWaypoints() {
        return waypoints;
    }

    public Location lastWaypoint() {
        return waypoints[waypoints.length - 1];
    }


}
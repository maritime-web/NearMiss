package dk.dma.nearmiss.gpssimulator.location;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Initial code. Thank you to: Mobility Services Lab
 * See <a href="https://www.mobility-services.in.tum.de/?p=2335">https://www.mobility-services.in.tum.de/?p=2335</a>
 */
@SuppressWarnings("WeakerAccess")
public class RouteSimulator {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    public static final double SIMULATOR_MOVEMENT_STEP_DISTANCE_METERS = 5;  // 5 m/s=18 km/h=10 knots; 30 m/s=108 km/h=58 knots
    public static final double ARRIVAL_RADIUS_IN_KM = 0.01;  // 10m

    public Location currentLocation;

    public int waypointCounter = 0;

    public final Route simulatedRoute;

    private boolean arrived = false;

    public RouteSimulator(Route route) {
        this.simulatedRoute = route;
        currentLocation = route.getInitialLocation();
    }

    public void move(){
        if (hasArrived()) return;

        Location nextWaypoint = simulatedRoute.getWaypoints()[waypointCounter];
        if (GeoHelper.calcGeoDistanceInKm(currentLocation, nextWaypoint) < ARRIVAL_RADIUS_IN_KM) {
            waypointCounter++;

            if (waypointCounter > simulatedRoute.getWaypoints().length-1) {
                arrived = true;
                return;
            }
            nextWaypoint = simulatedRoute.getWaypoints()[waypointCounter];
        }
        logger.trace("Moving to " + nextWaypoint.getName() + ". Distance = " + GeoHelper.calcGeoDistanceInKm(currentLocation, nextWaypoint) * 1000 + "m");
        double angle = GeoHelper.calcAngleBetweenGeoLocationsInRadians(currentLocation, nextWaypoint);
        double newLat = currentLocation.getLatitude() + Math.sin(angle) * SIMULATOR_MOVEMENT_STEP_DISTANCE_METERS / (10000000/90);
        double newLon = currentLocation.getLongitude() + Math.cos(angle) * SIMULATOR_MOVEMENT_STEP_DISTANCE_METERS / (10000000/90);
        currentLocation = new Location("currentLocation", newLat, newLon);
    }

   public boolean hasArrived() {
       return arrived;
   }

}
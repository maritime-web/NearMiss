package dk.dma.nearmiss.gpssimulator.location;


/**
 * Initial code. Thank you to: Mobility Services Lab
 * See <a href="https://www.mobility-services.in.tum.de/?p=2335">https://www.mobility-services.in.tum.de/?p=2335</a>
 */
@SuppressWarnings("WeakerAccess")
public class GeoHelper {

    public static final double EARTH_RADIUS_IN_KM = 6.371;

    /**
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference. If you are not interested in height
     * difference pass 0.0. Uses Haversine method as its base.
     * <p>
     * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
     * el2 End altitude in meters
     * <p>
     * From stackoverflow since the original method from Mobility Services Lab failed to calculate correct distance.
     * See <a href="https://stackoverflow.com/questions/3694380/calculating-distance-between-two-points-using-latitude-longitude-what-am-i-doi">https://stackoverflow.com/questions/3694380/calculating-distance-between-two-points-using-latitude-longitude-what-am-i-doi</a>
     *
     * @return Distance in Km
     */
    public static double calcGeoDistanceInKm(double lat1, double lat2, double lon1,
                                             double lon2, double el1, double el2) {


        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS_IN_KM * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

    public static double calcGeoDistanceInKm(Location location1, Location location2) {
        return calcGeoDistanceInKm(location1.getLatitude(), location2.getLatitude(), location1.getLongitude(), location2.getLongitude(), 0, 0);
    }

    public static double calcAngleBetweenGeoLocationsInRadians(double lat1, double lat2, double lon1, double lon2) {
        double dlat = lat2 - lat1;
        double dlon = lon2 - lon1;
        double angle = (Math.atan2(dlat, dlon) * 180) / Math.PI;
        return Math.toRadians(angle);
    }

    public static double calcAngleBetweenGeoLocationsInRadians(Location location1, Location location2) {
        return calcAngleBetweenGeoLocationsInRadians(location1.getLatitude(), location2.getLatitude(), location1.getLongitude(), location2.getLongitude());
    }

    public static void main(String[] args) {
        Location tripStart = new Location("Trip Start", 56.02250000, 11.73222222);
        Location tripEnd = new Location("Trip End", 57.67500000, 11.73222222);
        double distanceKm = calcGeoDistanceInKm(tripStart, tripEnd);
        //double distanceM = distance(tripStart.getLatitude(),tripEnd.getLatitude(), tripStart.getLongitude(), tripEnd.getLongitude(), 0, 0);
        System.out.println(String.format("Distance %s km", distanceKm));
        //System.out.println(String.format("Distance %s km", distanceM));
    }

}
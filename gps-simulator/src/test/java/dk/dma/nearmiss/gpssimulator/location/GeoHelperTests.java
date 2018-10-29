package dk.dma.nearmiss.gpssimulator.location;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

//@RunWith(SpringRunner.class)
public class GeoHelperTests {

    @Test
    public void calcGeoDistanceInKm_location() {
        Location tripStart = new Location("Hundested", 56.02250000, 11.73222222);
        Location tripEnd = new Location("Göteborg", 57.67500000, 11.73222222);

        double distanceKm = GeoHelper.calcGeoDistanceInKm(tripStart, tripEnd);

        assertTrue("Expected distance > 183 km", distanceKm > 183);
        assertTrue("Expected distance < 184 km", distanceKm < 184);
    }


}

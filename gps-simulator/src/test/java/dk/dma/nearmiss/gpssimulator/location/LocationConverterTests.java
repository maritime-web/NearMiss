package dk.dma.nearmiss.gpssimulator.location;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
public class LocationConverterTests {
    private static final Location TRIP_START = new Location("Hundested", 56.02250000, 11.73222222);
    private static final Location TRIP_END = new Location("Göteborg", 57.67500000, 11.73222222);

    @Test
    public void pad_2() {
        LocationConverter conv = new LocationConverter(TRIP_START);

        assertEquals("Expected correct padding", "01", conv.pad(1, 2));
        assertEquals("Expected correct padding", "02", conv.pad(2, 2));
        assertEquals("Expected correct padding", "10", conv.pad(10, 2));
    }

    @Test
    public void pad_3() {
        LocationConverter conv = new LocationConverter(TRIP_START);

        assertEquals("Expected correct padding", "001", conv.pad(1, 3));
        assertEquals("Expected correct padding", "002", conv.pad(2, 3));
        assertEquals("Expected correct padding", "010", conv.pad(10, 3));
        assertEquals("Expected correct padding", "100", conv.pad(100, 3));
    }

    @Test
    public void toDegreeMinutesSeconds_trip_start() {
        String  expectedResult = "5601.60,N,01143.60,E";
        LocationConverter conv = new LocationConverter(TRIP_START);
        assertEquals("Expected correct NMEA GPS coordinate", expectedResult, conv.toDegreeMinutesSeconds());
    }

    @Test
    public void toDegreeMinutesSeconds_trip_end() {
        String  expectedResult = "5740.00,N,01143.60,E";
        LocationConverter conv = new LocationConverter(TRIP_END);
        assertEquals("Expected correct NMEA GPS coordinate", expectedResult, conv.toDegreeMinutesSeconds());
    }

}

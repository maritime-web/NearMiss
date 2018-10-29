package dk.dma.nearmiss.helper;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PositionHelperTests {

    private static final double TRIP_START_LAT = 56.02250000;
    private static final double TRIP_START_LON = 11.73222222;

    private static final double TRIP_END_LAT = 57.67500000;
    private static final double TRIP_END_LON = 11.73222222;


    @Test
    public void pad_2() {
        PositionHelper conv = new PositionHelper(TRIP_START_LAT, TRIP_START_LON);

        assertEquals("Expected correct padding", "01", conv.pad(1, 2));
        assertEquals("Expected correct padding", "02", conv.pad(2, 2));
        assertEquals("Expected correct padding", "10", conv.pad(10, 2));
    }

    @Test
    public void pad_3() {
        PositionHelper conv = new PositionHelper(TRIP_START_LAT, TRIP_START_LON);

        assertEquals("Expected correct padding", "001", conv.pad(1, 3));
        assertEquals("Expected correct padding", "002", conv.pad(2, 3));
        assertEquals("Expected correct padding", "010", conv.pad(10, 3));
        assertEquals("Expected correct padding", "100", conv.pad(100, 3));
    }

    @Test
    public void toDegreeMinutesSeconds_trip_start() {
        String  expectedResult = "5601.60,N,01143.60,E";
        PositionHelper conv = new PositionHelper(TRIP_START_LAT, TRIP_START_LON);
        assertEquals("Expected correct NMEA GPS coordinate", expectedResult, conv.toDegreeMinutesSeconds());
    }

    @Test
    public void toDegreeMinutesSeconds_trip_end() {
        String  expectedResult = "5740.00,N,01143.60,E";
        PositionHelper conv = new PositionHelper(TRIP_END_LAT, TRIP_END_LON);
        assertEquals("Expected correct NMEA GPS coordinate", expectedResult, conv.toDegreeMinutesSeconds());
    }

}

package dk.dma.nearmiss.engine.geometry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class VesselContourTest {

    // Visualize WKT at https://arthur-e.github.io/Wicket/sandbox-gmaps3.html
    // (Beware of "compressed" latitudal pixel-lengths further away from equator)

    @Test
    public void vesselHeadingNorth() {
        VesselContour vesselContour = new VesselContour(55, 10, 150, 35, 0);
        String wkt = vesselContour.toWkt();
        assertEquals("POLYGON ((9.99972653686283 54.99932628477493, 10.00027346313717 54.99932628477493, 10.00027346313717 55.00040422913504, 10 55.00067371522507, 9.99972653686283 55.00040422913504, 9.99972653686283 54.99932628477493))", wkt);
    }

    @Test
    public void vesselHeadingSouth() {
        VesselContour vesselContour = new VesselContour(55, 10, 150, 35, 180);
        String wkt = vesselContour.toWkt();
        assertEquals("POLYGON ((10.00027346313717 55.00067371522507, 9.99972653686283 55.00067371522507, 9.99972653686283 54.99959577086496, 10 54.99932628477493, 10.00027346313717 54.99959577086496, 10.00027346313717 55.00067371522507))", wkt);
    }

    @Test
    public void vesselHeadingEast() {
        VesselContour vesselContour = new VesselContour(55, 10, 150, 35, 90);
        String wkt = vesselContour.toWkt();
        assertEquals("POLYGON ((9.999326284774934 55.00027346313717, 9.999326284774934 54.99972653686283, 10.00040422913504 54.99972653686283, 10.000673715225066 55, 10.00040422913504 55.00027346313717, 9.999326284774934 55.00027346313717))", wkt);
    }

    @Test
    public void vesselHeadingNorthWest() {
        VesselContour vesselContour = new VesselContour(55, 10, 150, 35, 315);
        String wkt = vesselContour.toWkt();
        assertEquals("POLYGON ((10.000283020965536 54.99933024375707, 10.00066975624293 54.99971697903447, 9.999907534476158 55.00047920080124, 9.999523611395768 55.000476388604234, 9.999520799198764 55.00009246552384, 10.000283020965536 54.99933024375707))", wkt);
    }

}
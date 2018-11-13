package dk.dma.nearmiss.engine.geometry.geometries;

import dk.dma.nearmiss.engine.geometry.GeometryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class VesselContourTest {

    // Visualize WKT at https://arthur-e.github.io/Wicket/sandbox-gmaps3.html
    // (Beware of "compressed" latitudal pixel-lengths further away from equator)

    final double loaDegrees = 150 / new GeometryService().metersPerDegreeLatitude();
    final double beamDegrees = 35 / new GeometryService().metersPerDegreeLongitude(55);

    @Test
    public void vesselHeadingNorth() {
        VesselContour vesselContour = new VesselContour(10, 55, loaDegrees, beamDegrees, 0);
        String wkt = vesselContour.toWkt();
        assertEquals("POLYGON ((9.99972540712969 54.999324999325, 10.00027459287031 54.999324999325, 10.00027459287031 55.000405000405, 10 55.000675000675, 9.99972540712969 55.000405000405, 9.99972540712969 54.999324999325))", wkt);
    }

    @Test
    public void vesselHeadingSouth() {
        VesselContour vesselContour = new VesselContour(10, 55, loaDegrees, beamDegrees, 180);
        String wkt = vesselContour.toWkt();
        assertEquals("POLYGON ((10.00027459287031 55.000675000675, 9.99972540712969 55.000675000675, 9.99972540712969 54.999594999595, 10 54.999324999325, 10.00027459287031 54.999594999595, 10.00027459287031 55.000675000675))", wkt);
    }

    @Test
    public void vesselHeadingEast() {
        VesselContour vesselContour = new VesselContour(10, 55, loaDegrees, beamDegrees, 90);
        String wkt = vesselContour.toWkt();
        assertEquals("POLYGON ((9.999324999325 55.00027459287031, 9.999324999325 54.99972540712969, 10.000405000405001 54.99972540712969, 10.000675000675 55, 10.000405000405001 55.00027459287031, 9.999324999325 55.00027459287031))", wkt);
    }

    @Test
    public void vesselHeadingNorthWest() {
        VesselContour vesselContour = new VesselContour(10, 55, loaDegrees, beamDegrees, 315);
        String wkt = vesselContour.toWkt();
        assertEquals("POLYGON ((10.000283131073937 54.99932853596474, 10.00067146403526 54.999716868926065, 9.999907787947903 55.000480545013424, 9.999522702445402 55.000477297554596, 9.99951945498658 55.000092212052095, 10.000283131073937 54.99932853596474))", wkt);
    }

}
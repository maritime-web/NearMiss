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
        assertEquals("POLYGON ((9.99972540712969 54.999325, 10.00027459287031 54.999325, 10.00027459287031 55.000405, 10 55.000675, 9.99972540712969 55.000405, 9.99972540712969 54.999325))", wkt);
    }

    @Test
    public void vesselHeadingSouth() {
        VesselContour vesselContour = new VesselContour(10, 55, loaDegrees, beamDegrees, 180);
        String wkt = vesselContour.toWkt();
        assertEquals("POLYGON ((10.00027459287031 55.000675, 9.99972540712969 55.000675, 9.99972540712969 54.999595, 10 54.999325, 10.00027459287031 54.999595, 10.00027459287031 55.000675))", wkt);
    }

    @Test
    public void vesselHeadingEast() {
        VesselContour vesselContour = new VesselContour(10, 55, loaDegrees, beamDegrees, 90);
        String wkt = vesselContour.toWkt();
        assertEquals("POLYGON ((9.999325 55.00027459287031, 9.999325 54.99972540712969, 10.000405 54.99972540712969, 10.000675 55, 10.000405 55.00027459287031, 9.999325 55.00027459287031))", wkt);
    }

    @Test
    public void vesselHeadingNorthWest() {
        VesselContour vesselContour = new VesselContour(10, 55, loaDegrees, beamDegrees, 315);
        String wkt = vesselContour.toWkt();
        assertEquals("POLYGON ((10.000283130596639 54.999328536442036, 10.000671463557962 54.99971686940336, 9.999907788234282 55.00048054472704, 9.999522702922699 55.0004772970773, 9.999519455272958 55.00009221176572, 10.000283130596639 54.999328536442036))", wkt);
    }

}
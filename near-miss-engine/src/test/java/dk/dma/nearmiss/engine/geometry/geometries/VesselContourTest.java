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
    public void contourRotated0Degrees() {
        VesselContour vesselContour = new VesselContour(10, 55, loaDegrees, beamDegrees, 0);
        String wkt = vesselContour.toWkt();
        assertEquals("POLYGON ((9.99972540712969 54.999325, 10.00027459287031 54.999325, 10.00027459287031 55.000405, 10 55.000675, 9.99972540712969 55.000405, 9.99972540712969 54.999325))", wkt);
    }

    @Test
    public void contourRotated180Degrees() {
        VesselContour vesselContour = new VesselContour(10, 55, loaDegrees, beamDegrees, 180);
        String wkt = vesselContour.toWkt();
        assertEquals("POLYGON ((10.00027459287031 55.000675, 9.99972540712969 55.000675, 9.99972540712969 54.999595, 10 54.999325, 10.00027459287031 54.999595, 10.00027459287031 55.000675))", wkt);
    }

    @Test
    public void contourRotated90Degrees() {
        VesselContour vesselContour = new VesselContour(10, 55, loaDegrees, beamDegrees, 90);
        String wkt = vesselContour.toWkt();
        assertEquals("POLYGON ((10.000675 54.99972540712969, 10.000675 55.00027459287031, 9.999595 55.00027459287031, 9.999325 55, 9.999595 54.99972540712969, 10.000675 54.99972540712969))", wkt);
    }

    @Test
    public void contourRotated315Degrees() {
        VesselContour vesselContour = new VesselContour(10, 55, loaDegrees, beamDegrees, 315);
        String wkt = vesselContour.toWkt();
        assertEquals("POLYGON ((9.999328536442038 54.99971686940336, 9.999716869403361 54.999328536442036, 10.000480544727042 55.00009221176572, 10.000477297077301 55.0004772970773, 10.000092211765718 55.00048054472704, 9.999328536442038 54.99971686940336))", wkt);
    }

}
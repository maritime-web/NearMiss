package dk.dma.nearmiss.engine.geometry;

import dk.dma.nearmiss.engine.geometry.geometries.EllipticSafetyZone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class GeometryServiceTest {

    private GeometryService sut; // http://xunitpatterns.com/SUT.html

    @Before
    public void before() {
        sut = new GeometryService();
    }

    @Test
    public void test() {
        EllipticSafetyZone safetyZone = sut.createEllipticSafetyZone(55, 10, 100, 25, 15);
        assertEquals(new EllipticSafetyZone(10, 55, 9.0E-4, 3.922755290147471E-4, 15), safetyZone);

        safetyZone = sut.createEllipticSafetyZone(15, 175, 100, 25, 15);
        assertEquals(new EllipticSafetyZone(175, 15, 9.0E-4, 2.3293714059226866E-4, 15), safetyZone);
    }

    @Test
    public void metersPerDegreeLatitude() {
        assertEquals(111111.11111111111, sut.metersPerDegreeLatitude(), 1e-6);
    }

    @Test
    public void metersPerDegreeLongitude() {
        assertEquals(111111.11111111111, sut.metersPerDegreeLongitude(0), 1e-6);
        assertEquals(63730.71515011623, sut.metersPerDegreeLongitude(55), 1e-6);
        assertEquals(0.0, sut.metersPerDegreeLongitude(90), 1e-6);
    }

}
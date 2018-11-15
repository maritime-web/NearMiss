package dk.dma.nearmiss.engine.geometry;

import dk.dma.nearmiss.engine.geometry.geometries.EllipticSafetyZone;
import dk.dma.nearmiss.helper.Position;
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

    @Test
    public void calculateGeometricCenter() {
        // GPS in geometric center, not translated, not rotated
        assertEquals(new Position(0, 0), sut.calculateGeometricCenter(new Position(0, 0), 0, 25, 25, 100, 100));

        // GPS not in geometric center, not translated, not rotated
        assertEquals(new Position(4.5E-4, 1.1250000000000001E-4), sut.calculateGeometricCenter(new Position(0, 0), 0, 10, 35, 150, 50));

        // GPS in geometric center, translated, not rotated
        assertEquals(new Position(55, 10), sut.calculateGeometricCenter(new Position(55, 10), 0, 25, 25, 100, 100));

        // GPS not in geometric center, translated, not rotated
        assertEquals(new Position(55.00045, 10.000196137764506), sut.calculateGeometricCenter(new Position(55, 10), 0, 10, 35, 150, 50));

        // GPS in geometric center, rotated, not translated
        assertEquals(new Position(0, 0), sut.calculateGeometricCenter(new Position(0, 0), 45, 25, 25, 100, 100));
        assertEquals(new Position(0, 0), sut.calculateGeometricCenter(new Position(0, 0), 90, 25, 25, 100, 100));
        assertEquals(new Position(0, 0), sut.calculateGeometricCenter(new Position(0, 0), 335, 25, 25, 100, 100));

        // GPS in geometric center, rotated, translated
        assertEquals(new Position(55, 10), sut.calculateGeometricCenter(new Position(55, 10), 45, 25, 25, 100, 100));
        assertEquals(new Position(55, 10), sut.calculateGeometricCenter(new Position(55, 10), 90, 25, 25, 100, 100));
        assertEquals(new Position(55, 10), sut.calculateGeometricCenter(new Position(55, 10), 335, 25, 25, 100, 100));

        // GPS not in geometric center, rotated, translated
        assertEquals(new Position(55.000179507708204, 10.000456888394863), sut.calculateGeometricCenter(new Position(55, 10), 45, 10, 35, 150, 50));
        assertEquals(new Position(54.999803862235495, 10.00045), sut.calculateGeometricCenter(new Position(55, 10), 90, 10, 35, 150, 50));
        assertEquals(new Position(55.000490729905266, 9.99998758296552), sut.calculateGeometricCenter(new Position(55, 10), 335, 10, 35, 150, 50));
    }

}
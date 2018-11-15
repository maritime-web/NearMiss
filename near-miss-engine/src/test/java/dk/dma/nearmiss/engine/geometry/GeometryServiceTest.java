package dk.dma.nearmiss.engine.geometry;

import dk.dma.nearmiss.engine.geometry.geometries.EllipticSafetyZone;
import dk.dma.nearmiss.engine.geometry.geometries.VesselContour;
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
    public void createVesselContour() {
        VesselContour vesselContour = sut.createVesselContour(55, 10, 100, 25, 45);
        assertEquals(new VesselContour(10, 55, 9.0E-4, 3.922755290147471E-4, 315.0), vesselContour);

        String wkt = vesselContour.toWkt();  // Not strictly necessary - but useful to get convinced
        assertEquals("POLYGON ((9.999543111605137 54.999820492291796, 9.999820492291796 54.999543111605135, 10.00032960917425 55.00005222848759, 10.000318198051534 55.00031819805153, 10.00005222848759 55.00032960917425, 9.999543111605137 54.999820492291796))", wkt);
    }

    @Test
    public void createEllipticSafetyZone() {
        EllipticSafetyZone safetyZone = sut.createEllipticSafetyZone(55, 10, 100, 25, 15);
        assertEquals(new EllipticSafetyZone(10, 55, 9.0E-4, 3.922755290147471E-4, 345), safetyZone);

        String wkt = safetyZone.toWkt();  // Not strictly necessary - but useful to get convinced
        assertEquals("POLYGON ((10.000189454532249 54.99994923581108, 10.00020853610742 55.00003501048195, 10.00021960375693 55.000119439719654, 10.000222232157219 55.0001992789559, 10.00021632030033 55.00027146001363, 10.000202095375618 55.00033320901527, 10.000180104038956 55.00038215298132, 10.00015119140508 55.00041641102261, 10.000116468570296 55.00043466662183, 10.000077269913673 55.00043621822654, 10.000035101817597 55.00042100620943, 9.999991584778362 55.0003896151598, 9.999948391131374 55.00034325141808, 9.99990718078426 55.000283696716856, 9.999869537427555 55.00021323970991, 9.999836907674348 55.00013458802048, 9.999810545467751 55.00005076418892, 9.999791463892583 54.99996498951804, 9.99978039624307 54.999880560280346, 9.999777767842785 54.9998007210441, 9.999783679699666 54.99972853998637, 9.999797904624382 54.999666790984726, 9.999819895961044 54.999617847018676, 9.999848808594917 54.99958358897739, 9.9998835314297 54.99956533337817, 9.999922730086327 54.99956378177346, 9.999964898182403 54.999578993790564, 10.000008415221641 54.999610384840196, 10.000051608868626 54.99965674858192, 10.00009281921574 54.999716303283144, 10.000130462572441 54.99978676029009, 10.000163092325652 54.99986541197951, 10.000189454532249 54.99994923581108))", wkt);

        safetyZone = sut.createEllipticSafetyZone(15, 175, 100, 25, 15);
        assertEquals(new EllipticSafetyZone(175, 15, 9.0E-4, 2.3293714059226866E-4, 345), safetyZone);

        wkt = safetyZone.toWkt(); // Not strictly necessary - but useful to get convinced
        assertEquals("POLYGON ((175.0001125 14.999969855715854, 175.00013306023493 15.00005523418104, 175.00014850703965 15.00013849002763, 175.00015824680213 15.000216423780124, 175.00016190522874 15.000286040488117, 175.00015934172822 15.000344664820567, 175.00015065481446 15.000390043877253, 175.00013617832062 15.000420433766475, 175.0001164685703 15.000434666621828, 175.0000922829982 15.00043219548267, 175.00006455104216 15.000413115313506, 175.0000343384258 15.000378159354504, 175.00000280620299 15.000328670943585, 174.99997116613937 15.00026655189263, 174.99994063414488 15.000194189401917, 174.99991238354687 15.000114364321398, 174.9998875 15.000030144284153, 174.9998669397651 14.999944765818956, 174.99985149296037 14.999861509972366, 174.9998417531979 14.999783576219876, 174.9998380947713 14.99971395951188, 174.9998406582718 14.999655335179433, 174.9998493451856 14.999609956122754, 174.99986382167944 14.999579566233521, 174.9998835314297 14.999565333378168, 174.99990771700186 14.999567804517326, 174.9999354489579 14.9995868846865, 174.99996566157424 14.999621840645496, 174.99999719379704 14.999671329056412, 175.00002883386065 14.99973344810737, 175.00005936585518 14.99980581059808, 175.00008761645316 14.999885635678599, 175.0001125 14.999969855715854))", wkt);
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

    @Test
    public void rotate() {
        assertEquals(new Position(55.0, 10.0), sut.rotate(new Position(55.0, 10.0), new Position(55.0, 10.0), 0.0));
        assertEquals(new Position(55.0, 10.0), sut.rotate(new Position(55.0, 10.0), new Position(55.0, 10.0), 45.0));
        assertEquals(new Position(54.0, 9.0), sut.rotate(new Position(54.0, 9.0), new Position(55.0, 10.0), 0.0));
        assertEquals(new Position(56.0, 9.0), sut.rotate(new Position(54.0, 9.0), new Position(55.0, 10.0), 90.0));
        assertEquals(new Position(56.0, 11.0), sut.rotate(new Position(54.0, 9.0), new Position(55.0, 10.0), 180.0));
        assertEquals(new Position(54.95064465844333, 11.41335206168167), sut.rotate(new Position(54.0, 9.0), new Position(55.0, 10.0), 227.0));
    }

    @Test
    public void translate() {
        assertEquals(new Position(0, 0), sut.translate(new Position(0, 0), 0, 0));
        assertEquals(new Position(0.08993216059187306, 0), sut.translate(new Position(0, 0), 0, 10000));
        assertEquals(new Position(5.506754369312176E-18, 0.08993216059183451), sut.translate(new Position(0, 0), 90, 10000));

        assertEquals(new Position(55.00127181265175, 10.002217443136736), sut.translate(new Position(55.0, 10.0), 45, 200));
        assertEquals(new Position(54.99952772819094, 9.99911704859372), sut.translate(new Position(55.0, 10.0), 227, 77));
    }

}
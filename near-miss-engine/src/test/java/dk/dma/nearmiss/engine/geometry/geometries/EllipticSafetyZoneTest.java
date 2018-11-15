package dk.dma.nearmiss.engine.geometry.geometries;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(MockitoJUnitRunner.class)
public class EllipticSafetyZoneTest {

    // Debugging tips
    // Use https://www.latlong.net/Show-Latitude-Longitude.html to show position on map
    // Use http://arthur-e.github.io/Wicket/sandbox-gmaps3.html to visualize WKT on map

    public EllipticSafetyZoneTest() {
    }

    @Test
    public void canProduceWellKnownText() {
        EllipticSafetyZone e = new EllipticSafetyZone(10.0, 55.0, 0.010, 0.005, 0.0);
        assertEquals("POLYGON ((10.0025 55, 10.002451963201008 55.00097545161008, 10.002309698831278 55.00191341716182, 10.002078674030756 55.0027778511651, 10.001767766952966 55.00353553390593, 10.001388925582548 55.00415734806151, 10.000956708580912 55.00461939766256, 10.00048772580504 55.004903926402015, 10 55.005, 9.99951227419496 55.004903926402015, 9.999043291419088 55.00461939766256, 9.998611074417452 55.00415734806151, 9.998232233047034 55.00353553390593, 9.997921325969244 55.0027778511651, 9.997690301168722 55.00191341716182, 9.997548036798992 55.00097545161008, 9.9975 55, 9.997548036798992 54.99902454838992, 9.997690301168722 54.99808658283818, 9.997921325969244 54.9972221488349, 9.998232233047034 54.99646446609407, 9.998611074417452 54.99584265193849, 9.999043291419088 54.99538060233744, 9.99951227419496 54.995096073597985, 10 54.995, 10.00048772580504 54.995096073597985, 10.000956708580912 54.99538060233744, 10.001388925582548 54.99584265193849, 10.001767766952966 54.99646446609407, 10.002078674030756 54.9972221488349, 10.002309698831278 54.99808658283818, 10.002451963201008 54.99902454838992, 10.0025 55))", e.toWkt());

        e = new EllipticSafetyZone(10.0, 55.0, 0.010, 0.005, 45.0);
        assertEquals("POLYGON ((10.001767766952966 55.001767766952966, 10.001044051358441 55.00242354825486, 10.000280213455724 55.00298619395647, 9.999505607107054 55.003434081899, 9.998750000000001 55.003750000000004, 9.998042429691932 55.00392180770403, 9.99741008771299 55.00394290253738, 9.9968772746108 55.003812473837414, 9.996464466094068 55.00353553390594, 9.996187526162593 55.003122725389204, 9.996057097462625 55.00258991228702, 9.996078192295968 55.001957570308065, 9.99625 55.001250000000006, 9.996565918101005 55.00049439289295, 9.997013806043533 54.999719786544276, 9.997576451745136 54.99895594864156, 9.998232233047034 54.998232233047034, 9.998955948641555 54.99757645174515, 9.999719786544262 54.99701380604355, 10.00049439289294 54.99656591810101, 10.001249999999995 54.99625, 10.001957570308065 54.996078192295975, 10.002589912287007 54.996057097462625, 10.003122725389197 54.99618752616259, 10.003535533905932 54.99646446609407, 10.003812473837403 54.9968772746108, 10.003942902537371 54.99741008771299, 10.003921807704028 54.99804242969194, 10.003749999999997 54.99875, 10.003434081898988 54.99950560710706, 10.002986193956453 55.00028021345574, 10.00242354825486 55.00104405135845, 10.001767766952966 55.001767766952966))", e.toWkt());
    }

    @Test
    public void intersectsSelf() {
        EllipticSafetyZone e = new EllipticSafetyZone(10.0, 55.0, 20, 10, 0.0);
        assertTrue(e.intersects(e));
    }

    @Test
    public void intersectsNotDistantEllipse() {
        EllipticSafetyZone e1 = new EllipticSafetyZone(10.0, 55.0, 20, 10, 0.0);
        EllipticSafetyZone e2 = new EllipticSafetyZone(-10.0, -55.0, 20, 10, 0.0);

        assertFalse(e1.intersects(e2));
        assertFalse(e2.intersects(e1));
    }

    @Test
    public void intersectsContainedEllipse() {
        EllipticSafetyZone e1 = new EllipticSafetyZone(10.0, 55.0, 20, 10, 0.0);
        EllipticSafetyZone e2 = new EllipticSafetyZone(10.0, 55.0, 19999, 9999, 0.0);

        assertTrue(e1.intersects(e2));
        assertTrue(e2.intersects(e1));
    }

    @Test
    public void intersectsIdenticalRotatedEllipse() {
        EllipticSafetyZone e1 = new EllipticSafetyZone(10.0, 55.0, 20, 10, 0.0);
        EllipticSafetyZone e2 = new EllipticSafetyZone(10.0, 55.0, 20, 10, 45.0);

        assertTrue(e1.intersects(e2));
        assertTrue(e2.intersects(e1));
    }

    @Test
    public void correctlyDetectsIntersectionOfEllipsesPlacedVerticallyAboveEachOther() {
        EllipticSafetyZone e1 = new EllipticSafetyZone(10.0, 55.0, 5, 2, 0.0);

        EllipticSafetyZone e2;

        e2 = new EllipticSafetyZone(10.0, 60.01, 5, 2, 0.0);
        assertFalse(e1.intersects(e2));
        assertFalse(e2.intersects(e1));

        e2 = new EllipticSafetyZone(10.0, 59.99, 5, 2, 0.0);
        assertTrue(e1.intersects(e2));
        assertTrue(e2.intersects(e1));
    }

    @Test
    public void correctlyDetectsIntersectionOfEllipsesPlacedHorizontallyNextToEachOther() {
        EllipticSafetyZone e1 = new EllipticSafetyZone(10.0, 55.0, 5, 2, 0.0);

        EllipticSafetyZone e2;

        e2 = new EllipticSafetyZone(7.99, 55.0, 5, 2, 0.0);
        assertFalse(e1.intersects(e2));
        assertFalse(e2.intersects(e1));

        e2 = new EllipticSafetyZone(8.01, 55.0, 5, 2, 0.0);
        assertTrue(e1.intersects(e2));
        assertTrue(e2.intersects(e1));
    }

}
package dk.dma.nearmiss.engine.geometry;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKTWriter;

import static java.lang.Math.PI;

/**
 * This class represents the contour of a VesselContour.
 *
 *           D
 *           ^         -
 *          / \        |
 *         /   \       |
 *        /     \      |
 *     E /       \ C   |
 *       |       |     | l
 *       |       |     | o
 *       |   M   |     | a
 *       |       |     |
 *       |       |     |
 *       |       |     |
 *     A +-------+ B   -
 *
 *       |-------|
 *          beam
 *
 *
 * The ellipse is intended to support geometric calculations of the surface of the Earth; but limited to distances
 * at the surface where the Earth can the surface can safely be assumed to a linear, flat plane.
 *
 */
@SuppressWarnings("WeakerAccess")
public class VesselContour {

    final Geometry _internalRepresentation;

    final static int METERS_PER_DEGREE_LATITUDE = 111323;
    final static int METERS_PER_DEGREE_LONGITUDE = 63994;// not precise - only valid around 55N - http://www.csgnetwork.com/degreelenllavcalc.html

    /**
     * Create the contour of a VesselContour
     *
     * @param centreLatitudeDegrees  Latitude of vessel's geometric centre in degrees.
     * @param centreLongitudeDegrees Longitude of vessel's geometric center in degrees.
     * @param loaMeters              VesselContour's length over all in meters.
     * @param beamMeters             VesselContour's beam in meters.
     * @param headingDegrees         VesselContour's heading in degrees true north.
     */
    public VesselContour(double centreLatitudeDegrees, double centreLongitudeDegrees, int loaMeters, int beamMeters, int headingDegrees) {

        final double theta = ((360 - headingDegrees) % 360.0) * (PI / 180.0);

        final double loa = latitudeMetersToDegrees(loaMeters);
        final double beam = longitudeMetersToDegrees(beamMeters);

        final double Mx = 0.0;
        final double My = 0.0;

        final double Ax = -0.5 * beam;
        final double Ay = -0.5 * loa;

        final double Bx = Ax + beam;
        final double By = Ay;

        final double Cx = Bx;
        final double Cy = By + 0.8 * loa;

        final double Dx = Mx;
        final double Dy = By + loa;

        final double Ex = Ax;
        final double Ey = Cy;

        // Rotate

        final double rAx = rotateX(theta, Ax, Ay);
        final double rAy = rotateY(theta, Ax, Ay);

        final double rBx = rotateX(theta, Bx, By);
        final double rBy = rotateY(theta, Bx, By);

        final double rCx = rotateX(theta, Cx, Cy);
        final double rCy = rotateY(theta, Cx, Cy);

        final double rDx = rotateX(theta, Dx, Dy);
        final double rDy = rotateY(theta, Dx, Dy);

        final double rEx = rotateX(theta, Ex, Ey);
        final double rEy = rotateY(theta, Ex, Ey);

        // Translate

        final double trAx = rAx + centreLongitudeDegrees;
        final double trAy = rAy + centreLatitudeDegrees;

        final double trBx = rBx + centreLongitudeDegrees;
        final double trBy = rBy + centreLatitudeDegrees;

        final double trCx = rCx + centreLongitudeDegrees;
        final double trCy = rCy + centreLatitudeDegrees;

        final double trDx = rDx + centreLongitudeDegrees;
        final double trDy = rDy + centreLatitudeDegrees;

        final double trEx = rEx + centreLongitudeDegrees;
        final double trEy = rEy + centreLatitudeDegrees;

        // Create

        Coordinate A = new Coordinate(trAx, trAy);
        Coordinate B = new Coordinate(trBx, trBy);
        Coordinate C = new Coordinate(trCx, trCy);
        Coordinate D = new Coordinate(trDx, trDy);
        Coordinate E = new Coordinate(trEx, trEy);

        GeometryFactory gf = new GeometryFactory();
        Polygon contour = gf.createPolygon(new Coordinate[]{A, B, C, D, E, A});

        this._internalRepresentation = contour;
    }

    private double rotateX(double theta, double x, double y) {
        return x * Math.cos(theta) - y * Math.sin(theta);
    }

    private double rotateY(double theta, double x, double y) {
        return x * Math.sin(theta) + y * Math.cos(theta);
    }

    private static double longitudeMetersToDegrees(double meters) {
        return meters / METERS_PER_DEGREE_LONGITUDE;
    }

    private static double latitudeMetersToDegrees(double meters) {
        return meters / METERS_PER_DEGREE_LATITUDE;
    }

    public String toWkt() {
        return new WKTWriter().write(_internalRepresentation);
    }

}

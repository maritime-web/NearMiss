package dk.dma.nearmiss.engine.geometry.geometries;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;

/**
 * This class represents the contour of a vessel.
 *
 * It is intended to model the contour of "other vessels" when analysing whether they are in the
 * safety zone of own ship.
 *
 * Points are named like this:
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
 * After constructing the points, the contour is rotated and translated to fit heading and position.
 *
 */
@SuppressWarnings("WeakerAccess")
public class VesselContour extends dk.dma.nearmiss.engine.geometry.geometries.Geometry {

    /**
     * Create the contour of a VesselContour
     * @param x Longitude of vessel's geometric center in degrees.
     * @param y Latitude of vessel's geometric centre in degrees.
     * @param l VesselContour's length over all in meters.
     * @param b VesselContour's beam in decimal degrees.
     * @param thetaDeg Counter-clockwise rotation angle of the VesselContour in degrees (0 = bow up).
     */
    @SuppressWarnings({"unused", "UnnecessaryLocalVariable"})
    public VesselContour(double x, double y, double l, double b, double thetaDeg) {
        final double theta = thetaDegToRad(thetaDeg);

        final double loa = l;
        final double beam = b;

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

        final double trAx = rAx + x;
        final double trAy = rAy + y;

        final double trBx = rBx + x;
        final double trBy = rBy + y;

        final double trCx = rCx + x;
        final double trCy = rCy + y;

        final double trDx = rDx + x;
        final double trDy = rDy + y;

        final double trEx = rEx + x;
        final double trEy = rEy + y;

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

}

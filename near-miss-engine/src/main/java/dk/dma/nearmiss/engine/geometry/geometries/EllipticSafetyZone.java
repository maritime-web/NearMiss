package dk.dma.nearmiss.engine.geometry.geometries;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.util.AffineTransformation;
import org.locationtech.jts.io.WKTWriter;
import org.locationtech.jts.util.GeometricShapeFactory;

import java.util.Objects;

import static java.lang.Math.PI;

/**
 * This class represents an EllipticSafetyZone.
 *
 * The ellipse is intended to support geometric calculations of the surface of the Earth; but limited to distances
 * at the surface where the Earth can the surface can safely be assumed to a linear, flat plane.
 *
 */
@SuppressWarnings("WeakerAccess")
public class EllipticSafetyZone {

    final Polygon _internalRepresentation;

    /**
     * Create an EllipticSafetyZone
     * @param x Cartesian x coordinate of the ellipse's centre.
     * @param y Cartesian y coordinate of the ellipse's centre.
     * @param a Length of the ellipse's vertical axis before rotation (in cartesian units).
     * @param b Length of the ellipse's horizontal axis before rotation (in cartesian units).
     * @param thetaDeg Counter-clockwise rotation angle of the ellipse in degrees.
     */
    public EllipticSafetyZone(double x, double y, double a, double b, double thetaDeg) {
        if (a <= 0)
            throw new IllegalArgumentException("a must be a positive number");
        if (b <= 0)
            throw new IllegalArgumentException("b must be a positive number");

        final double orientDeg = (360 - thetaDeg) % 360.0;
        final double orientRad = orientDeg * (PI/180.0);

        Coordinate centre = new Coordinate(x, y);
        GeometricShapeFactory gsf = new GeometricShapeFactory();
        gsf.setCentre(centre);
        gsf.setWidth(b);
        gsf.setHeight(a);
        gsf.setNumPoints(100);
        Polygon ellipse = gsf.createEllipse();

        AffineTransformation trans = AffineTransformation.rotationInstance(orientRad, centre.x, centre.y);
        ellipse.apply(trans);

        this._internalRepresentation = ellipse;
    }

    public boolean intersects(EllipticSafetyZone otherEllipse) {
        return _internalRepresentation.intersects(otherEllipse._internalRepresentation);
    }

    public boolean intersects(VesselContour vesselContour) {
        return _internalRepresentation.intersects(vesselContour._internalRepresentation);
    }

    public String toWkt() {
        return new WKTWriter().write(_internalRepresentation);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EllipticSafetyZone that = (EllipticSafetyZone) o;
        return Objects.equals(_internalRepresentation, that._internalRepresentation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_internalRepresentation);
    }

}

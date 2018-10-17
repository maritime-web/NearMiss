package dk.dma.nearmiss.engine;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.util.AffineTransformation;
import org.locationtech.jts.io.WKTWriter;
import org.locationtech.jts.util.GeometricShapeFactory;

import static java.lang.Math.PI;

/**
 * This class represents an Ellipse.
 *
 * The ellipse is intended to support geometric calculations of the surface of the Earth; but limited to distances
 * at the surface where the Earth can the surface can safely be assumed to a linear, flat plane.
 *
 */
public class Ellipse {

    private final Polygon _internalRepresentation;

    final static int METERS_PER_MINUTE_LATITUDE = 1852;
    final static int MINUTES_PER_DEGREE_LATITUDE = 60;
    final static int METERS_PER_DEGREE_LATITUDE = METERS_PER_MINUTE_LATITUDE * MINUTES_PER_DEGREE_LATITUDE;

    final static int METERS_PER_MINUTE_LONGITUDE = 1000; // not precise - only valid around 55N
    final static int MINUTES_PER_DEGREE_LONGITUDE = 60;
    final static int METERS_PER_DEGREE_LONGITUDE = METERS_PER_MINUTE_LONGITUDE * MINUTES_PER_DEGREE_LONGITUDE;

    /**
     * Create an Ellipse
     *
     * @param centreLatitude Latitude of the ellipse's centre in degrees.
     * @param centreLongitude Longitude of the ellipse's center in degrees.
     * @param lengthOfAxisAlongCourse Length of the ellipse's vertical axis before rotation in meters.
     * @param lengthOfAxisAcrossCourse Length of the ellipse's horizontal axis before rotation in meters. The values supplied for this parameters are only valid in meters around latitude 55 degrees North or South.
     * @param course Counter-clockwise rotation angle of the ellipse in degrees.
     */
    public Ellipse(double centreLatitude, double centreLongitude, double lengthOfAxisAlongCourse, double lengthOfAxisAcrossCourse, double course) {
        if (lengthOfAxisAlongCourse <= 0)
            throw new IllegalArgumentException("lengthOfAxisAlongCourse must be a positive number");
        if (lengthOfAxisAcrossCourse <= 0)
            throw new IllegalArgumentException("lengthOfAxisAcrossCourse must be a positive number");

        final double vAxis = lengthOfAxisAlongCourse / METERS_PER_DEGREE_LATITUDE;
        final double hAxis = lengthOfAxisAcrossCourse / METERS_PER_DEGREE_LONGITUDE;
        final double orientDeg = (360 - course) % 360.0;
        final double orientRad = orientDeg * (PI/180.0);

        Coordinate centre = new Coordinate(centreLongitude, centreLatitude);
        GeometricShapeFactory gsf = new GeometricShapeFactory();
        gsf.setCentre(centre);
        gsf.setWidth(hAxis);
        gsf.setHeight(vAxis);
        gsf.setNumPoints(100);
        Polygon ellipse = gsf.createEllipse();

        AffineTransformation trans = AffineTransformation.rotationInstance(orientRad, centre.x, centre.y);
        ellipse.apply(trans);

        this._internalRepresentation = ellipse;
    }

    public boolean intersects(Ellipse otherEllipse) {
        return _internalRepresentation.intersects(otherEllipse._internalRepresentation);
    }

    public String toWkt() {
        return new WKTWriter().write(_internalRepresentation);
    }

}

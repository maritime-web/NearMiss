package dk.dma.nearmiss.engine.geometry;

import dk.dma.nearmiss.engine.geometry.geometries.EllipticSafetyZone;
import dk.dma.nearmiss.engine.geometry.geometries.VesselContour;
import dk.dma.nearmiss.helper.Position;
import org.springframework.stereotype.Service;

import static java.lang.Math.*;

@Service
public class GeometryService {

    public GeometryService() {
    }

    /**
     * Create an elliptic safety zone.
     *
     * @param centreLatitude Latitude of the ellipse's center (in decimal degrees)
     * @param centreLongitude Longitude of the ellipse's center (in decimal degrees)
     * @param lengthOfAxisAlongCourse Length of the ellipse along course (in meters)
     * @param lengthOfAxisAcrossCourse Length of the ellipse across course (in meters)
     * @param course Course (in degrees true heading)
     * @return An elliptic safety zone.
     */
    public EllipticSafetyZone createEllipticSafetyZone(double centreLatitude, double centreLongitude, double lengthOfAxisAlongCourse, double lengthOfAxisAcrossCourse, double course) {
        final double a = lengthOfAxisAlongCourse / metersPerDegreeLatitude();
        final double b = lengthOfAxisAcrossCourse / metersPerDegreeLongitude(centreLatitude);

        return new EllipticSafetyZone(centreLongitude, centreLatitude, a, b, course);
    }

    /**
     * Create a vessel contour.
     *
     * @param centreLatitudeDegrees  Latitude of vessel's geometric centre in degrees.
     * @param centreLongitudeDegrees Longitude of vessel's geometric center in degrees.
     * @param loaMeters              VesselContour's length over all in meters.
     * @param beamMeters             VesselContour's beam in meters.
     * @param headingDegrees         VesselContour's heading in degrees true north.
     * @return A vessel contour.
     */
    public VesselContour createVesselContour(double centreLatitudeDegrees, double centreLongitudeDegrees, int loaMeters, int beamMeters, int headingDegrees) {
        final double loaDegrees = loaMeters / metersPerDegreeLatitude();
        final double beamDegrees = beamMeters / metersPerDegreeLongitude(centreLatitudeDegrees);

        return new VesselContour(centreLongitudeDegrees, centreLatitudeDegrees, loaDegrees, beamDegrees, headingDegrees);
    }

    /**
     * Calculate the geometric center of a vessel given the position of its GPS antenna and the location of this
     * antenna relative to the vessel.
     *
     * @param gpsReceiverPosition Position of the GPS antenne (GPS reference position),
     * @param heading             Vessel's heading in degrees true north.
     * @param dimPort             Distance in meters from GPS antenna to vessel's port side.
     * @param dimStarbord         Distance in meters from GPS antenna to vessel's starboard side.
     * @param dimBow              Distance in meters from GPS antenna to vessel's bow.
     * @param dimStern            Distance in meters from GPS antenna to vessel's stern.
     * @return Geodetic position of vessel's geometric center.
     */
    public Position calculateGeometricCenter(Position gpsReceiverPosition, double heading, int dimPort, int dimStarbord, int dimBow, int dimStern) {
        /*
         * Points are named like this:
         *
         *               (bow)
         *     D |---------------------| C   -
         *       |                     |     |
         *       |                     |     |
         *       |                     |     |                         heading = 25
         *       |                     |     |                        -\
         *       |                     |     |                        /|
         *       |                     |     | dimBow                /
         *       |          C          |     |                      /
         *       |                     |     |                     /
         *       |                     |     |
         *       |                     |     |
         *       |       G             |     -
         *       |                     |     | dimStern
         *       |                     |     |
         *     A |-------|-------------| B   -
         *        dimPort dimStarboardx
         *
         *              (stern)
         *
         *     G = position of GPS receiver
         *     C = geometric center of vessel
         */

        // Convert dim from meters to degrees

        final double dimPortDeg = dimPort / metersPerDegreeLongitude(gpsReceiverPosition.getLat());
        final double dimStarboardDeg = dimStarbord / metersPerDegreeLongitude(gpsReceiverPosition.getLat());
        final double dimBowDeg = dimBow / metersPerDegreeLatitude();
        final double dimSternDeg = dimStern / metersPerDegreeLatitude();

        // Calculate C facing North

        final double Gx = gpsReceiverPosition.getLon();
        final double Gy = gpsReceiverPosition.getLat();

        final double Cx = Gx - dimPortDeg + (dimPortDeg + dimStarboardDeg) / 2d;
        final double Cy = Gy - dimSternDeg + (dimBowDeg + dimSternDeg) / 2d;

        // Rotate C to heading around G
        // https://academo.org/demos/rotation-about-point/

        final double thetaRad = ((360 - heading) % 360.0) * (PI / 180.0);

        final double rCx = rotateX(thetaRad, Cx - Gx, Cy - Gy) + Gx;
        final double rCy = rotateY(thetaRad, Cx - Gx, Cy - Gy) + Gy;

        return new Position(rCy, rCx);
    }

    private double rotateX(double theta, double x, double y) {
        return x * Math.cos(theta) - y * Math.sin(theta);
    }

    private double rotateY(double theta, double x, double y) {
        return x * Math.sin(theta) + y * Math.cos(theta);
    }

    /**
     * Computes the approximate distance in meters for 1 degree of latitude.
     *
     * @return
     */
    public double metersPerDegreeLatitude() {
        return METERS_PER_DEGREE_LATITUDE;
    }

    /**
     * Computes the approximate distance in meters for 1 degree of longitude at the given latitude.
     *
     * @param latitude Latitude in decimal degrees.
     * @return
     */
    public double metersPerDegreeLongitude(double latitude) {
        final double theta = latitude * (PI/180.0);
        return abs(cos(theta) * METERS_PER_DEGREE_LATITUDE);
    }

    private static double METERS_PER_DEGREE_LATITUDE = 1000d * 10000d / 90d; // 10.000 km per 90 degrees latitude

}

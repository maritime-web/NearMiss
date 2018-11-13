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

    public Position calculateGeometricCenter(Position gpsReceiverPosition, double cog, int dimPort, int dimStern) {
        // TODO implement
        return gpsReceiverPosition;
    }

    /**
     * Computes the approximate distance in meters for 1 degree of latitude.
     *
     * @return
     */
    public double metersPerDegreeLatitude() {
        return 1000*10000/90; // 10.000 km per 90 degrees
    }

    /**
     * Computes the approximate distance in meters for 1 degree of longitude at the given latitude.
     *
     * @param latitude Latitude in decimal degrees.
     * @return
     */
    public double metersPerDegreeLongitude(double latitude) {
        final double theta = latitude * (PI/180.0);
        return abs(cos(theta) * 1000*10000/90);
    }

}

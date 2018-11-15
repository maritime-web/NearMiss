package dk.dma.nearmiss.engine.engineParts;

import dk.dma.nearmiss.engine.Vessel;
import dk.dma.nearmiss.engine.geometry.GeometryService;
import dk.dma.nearmiss.engine.geometry.geometries.EllipticSafetyZone;
import dk.dma.nearmiss.engine.geometry.geometries.VesselContour;
import dk.dma.nearmiss.helper.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class EllipticSafetyZoneDetector implements NearMissDetector {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final GeometryService geometryService;
    private final Vessel ownVessel;

    public EllipticSafetyZoneDetector(GeometryService geometryService, @Qualifier("ownVessel") Vessel ownVessel) {
        this.geometryService = geometryService;
        this.ownVessel = ownVessel;
    }

    @Override
    public boolean nearMissDetected(Vessel otherVessel) {
        VesselContour contourOfOtherVessel = createContour(otherVessel);
        EllipticSafetyZone safetyZoneOfOwnVessel = createSafetyZone(ownVessel);

        if (contourOfOtherVessel != null && safetyZoneOfOwnVessel != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("WKT contour of other vessel: {}", contourOfOtherVessel.toWkt());
                logger.debug("WKT safety zone of own vessel: {}", safetyZoneOfOwnVessel.toWkt());
            }

            return safetyZoneOfOwnVessel.intersects(contourOfOtherVessel);
        } else
            return false;
    }

    VesselContour createContour(Vessel vessel) {
        VesselContour contour = null;

        double centreLatitude = vessel.getCenterPosition().getLat();
        double centreLongitude = vessel.getCenterPosition().getLon();
        int loa = vessel.getLoa();
        int beam = vessel.getBeam();
        int hdg = (int) vessel.getHdg();

        if (loa == 0) {
            logger.debug("LOA of vessel {} is unknown. Cannot calculate vessel contour.", vessel.getMmsi());
        } else if (beam == 0) {
            logger.debug("Beam of vessel {} is unknown. Cannot calculate vessel contour.", vessel.getMmsi());
        } else {
            contour = geometryService.createVesselContour(centreLatitude, centreLongitude, loa, beam, hdg);
        }

        return contour;
    }

    EllipticSafetyZone createSafetyZone(Vessel vessel) {
        EllipticSafetyZone safetyZone = null;

        double sog = vessel.getSog();
        double cog = vessel.getCog();
        int loa = vessel.getLoa();
        int beam = vessel.getBeam();

        if (Double.isNaN(sog)) {
            logger.debug("SOG of vessel {} is unknown. Cannot calculate safety zone.", vessel.getMmsi());
        } else if (Double.isNaN(cog)) {
            logger.debug("COG of vessel {} is unknown. Cannot calculate safety zone.", vessel.getMmsi());
        } else if (loa == 0) {
            logger.debug("LOA of vessel {} is unknown. Cannot calculate safety zone.", vessel.getMmsi());
        } else if (beam == 0) {
            logger.debug("Beam of vessel {} is unknown. Cannot calculate safety zone.", vessel.getMmsi());
        } else {
            // TODO confirm with domain expert that chosen constants are reasonably or correct
            final double a = 1.2d;
            final double b = 20d;
            final double c = 4d;
            final double d = 3d;

            int lengthOfAxisAlongCourse = (int) ((loa * a + sog * b));
            int lengthOfAxisAcrossCourse = (int) (beam * c);

            Position ellipticCentre = geometryService.translate(vessel.getCenterPosition(), vessel.getCog(), vessel.getLoa() * vessel.getSog() / 50d + vessel.getSog() * d);
            double centreLatitude = ellipticCentre.getLat();
            double centreLongitude = ellipticCentre.getLon();

            safetyZone = geometryService.createEllipticSafetyZone(centreLatitude, centreLongitude, lengthOfAxisAlongCourse, lengthOfAxisAcrossCourse, cog);

            if (logger.isDebugEnabled()) {
                String contourWkt = geometryService.createVesselContour(centreLatitude, centreLongitude, loa, beam, (int) vessel.getHdg()).toWkt();
                String safetyZoneWkt = safetyZone.toWkt();

                contourWkt = contourWkt.replace("POLYGON", "");
                safetyZoneWkt = safetyZoneWkt.replace("POLYGON", "");

                String combinedWkt = String.format("MULTIPOLYGON (%s,%s)", contourWkt, safetyZoneWkt);

                logger.debug("Own vessel and safety zone: {}", combinedWkt);
            }

        }

        return safetyZone;
    }

}

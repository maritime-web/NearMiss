package dk.dma.nearmiss.engine.engineParts;

import dk.dma.nearmiss.engine.Vessel;
import dk.dma.nearmiss.engine.geometry.Ellipse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class EllipseShapedSafetyZoneDetector implements NearMissDetector {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Vessel ownVessel;

    public EllipseShapedSafetyZoneDetector(@Qualifier("ownVessel") Vessel ownVessel) {
        this.ownVessel = ownVessel;
    }

    @Override
    public boolean nearMissDetected(Vessel otherVessel) {
        Ellipse ellipticContourOfOtherVessel = createContour(otherVessel);
        Ellipse safetyZoneOfOwnVessel = createSafetyZone(ownVessel);

        if (ellipticContourOfOtherVessel != null && safetyZoneOfOwnVessel != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("WKT contour of other vessel: {}", ellipticContourOfOtherVessel.toWkt());
                logger.debug("WKT safety zone of own vessel: {}", safetyZoneOfOwnVessel.toWkt());
            }

            return safetyZoneOfOwnVessel.intersects(ellipticContourOfOtherVessel);
        } else
            return false;
    }

    private Ellipse createContour(Vessel vessel) {
        Ellipse contour = null;

        double centreLatitude = vessel.getCenterPosition().getLat();
        double centreLongitude = vessel.getCenterPosition().getLon();
        int loa = vessel.getLoa();
        int beam = vessel.getBeam();
        double cog = vessel.getCog();

        if (loa == 0) {
            logger.debug("LOA of vessel {} is unknown. Cannot calculate safety zone.", vessel.getMmsi());
        } else if (beam == 0) {
            logger.debug("Beam of vessel {} is unknown. Cannot calculate safety zone.", vessel.getMmsi());
        } else if (Double.isNaN(cog)) {
            logger.debug("COG of vessel {} is unknown. Cannot calculate safety zone.", vessel.getMmsi());
        } else {
            contour = new Ellipse(centreLatitude, centreLongitude, loa, beam, cog);
        }

        return contour;
    }

    private Ellipse createSafetyZone(Vessel vessel) {
        Ellipse safetyZone = null;

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
            double centreLatitude = vessel.getCenterPosition().getLat();  // TODO offset forward relative to vessel
            double centreLongitude = vessel.getCenterPosition().getLon(); // TODO offset forward relative to vessel
            int lengthOfAxisAlongCourse = loa + ((int) sog * 10);         // TODO make factor configurable; linear relation to sog? loa?
            int lengthOfAxisAcrossCourse = beam * 4;                      // TODO make factor configurable;

            safetyZone = new Ellipse(centreLatitude, centreLongitude, lengthOfAxisAlongCourse, lengthOfAxisAcrossCourse, cog);
        }

        return safetyZone;
    }

}

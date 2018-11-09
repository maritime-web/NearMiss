package dk.dma.nearmiss.engine.engineParts;

import dk.dma.nearmiss.engine.Vessel;
import dk.dma.nearmiss.engine.geometry.EllipticSafetyZone;
import dk.dma.nearmiss.engine.geometry.VesselContour;
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
        VesselContour otherVesselCountour = createContour(otherVessel);
        EllipticSafetyZone safetyZoneOfOwnVessel = createSafetyZone(ownVessel);

        if (otherVesselCountour != null && safetyZoneOfOwnVessel != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("WKT contour of other vessel: {}", otherVesselCountour.toWkt());
                logger.debug("WKT safety zone of own vessel: {}", safetyZoneOfOwnVessel.toWkt());
            }

            return safetyZoneOfOwnVessel.intersects(otherVesselCountour);
        } else
            return false;
    }

    private VesselContour createContour(Vessel vessel) {
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
            contour = new VesselContour(centreLatitude, centreLongitude, loa, beam, hdg);
        }

        return contour;
    }

    private EllipticSafetyZone createSafetyZone(Vessel vessel) {
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
            double centreLatitude = vessel.getCenterPosition().getLat();  // TODO offset forward relative to vessel
            double centreLongitude = vessel.getCenterPosition().getLon(); // TODO offset forward relative to vessel
            int lengthOfAxisAlongCourse = loa + ((int) sog * 10);         // TODO make factor configurable; linear relation to sog? loa?
            int lengthOfAxisAcrossCourse = beam * 4;                      // TODO make factor configurable;

            safetyZone = new EllipticSafetyZone(centreLatitude, centreLongitude, lengthOfAxisAlongCourse, lengthOfAxisAcrossCourse, cog);
        }

        return safetyZone;
    }

}

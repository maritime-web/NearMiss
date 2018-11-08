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

        if (logger.isDebugEnabled()) {
            logger.debug("WKT contour of other vessel: {}", ellipticContourOfOtherVessel.toWkt());
            logger.debug("WKT safety zone of own vessel: {}", safetyZoneOfOwnVessel.toWkt());
        }

        return safetyZoneOfOwnVessel.intersects(ellipticContourOfOtherVessel);
    }

    private Ellipse createContour(Vessel vessel) {
        double centreLatitude = vessel.getCenterPosition().getLat();
        double centreLongitude = vessel.getCenterPosition().getLon();
        double lengthOfAxisAlongCourse = vessel.getLoa();
        double lengthOfAxisAcrossCourse = vessel.getBeam();
        double course = vessel.getCog();

        return new Ellipse(centreLatitude, centreLongitude, lengthOfAxisAlongCourse, lengthOfAxisAcrossCourse, course);
    }

    private Ellipse createSafetyZone(Vessel vessel) {
        double centreLatitude = vessel.getCenterPosition().getLat();             // TODO offset forward relative to vessel
        double centreLongitude = vessel.getCenterPosition().getLon();            // TODO offset forward relative to vessel
        double lengthOfAxisAlongCourse = vessel.getLoa() + vessel.getSog() * 10; // TODO make factor configurable; linear relation to sog? loa?
        double lengthOfAxisAcrossCourse = vessel.getBeam() * 4;                  // TODO make factor configurable;
        double course = vessel.getCog();

        return new Ellipse(centreLatitude, centreLongitude, lengthOfAxisAlongCourse, lengthOfAxisAcrossCourse, course);
    }

}

package dk.dma.nearmiss.engine.engineParts;

import dk.dma.nearmiss.engine.Vessel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class EllipseShapedSafetyZoneDetector implements NearMissDetector {

    private final Vessel ownVessel;

    public EllipseShapedSafetyZoneDetector(@Qualifier("ownVessel") Vessel ownVessel) {
        this.ownVessel = ownVessel;
    }

    @Override
    public boolean nearMissDetected(Vessel otherVessel) {
        // TODO implement
        return Math.random() < 0.5;
    }
}

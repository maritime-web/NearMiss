package dk.dma.nearmiss.engine;

import org.springframework.stereotype.Component;

@Component
public class EllipseShapedSafetyZoneDetector implements NearMissDetector {
    @Override
    public boolean nearMiss(Vessel ownVessel, Vessel otherVessel) {
        return Math.random() < 0.5;
    }
}

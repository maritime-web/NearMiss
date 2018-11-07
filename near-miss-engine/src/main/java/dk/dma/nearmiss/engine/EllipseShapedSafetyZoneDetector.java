package dk.dma.nearmiss.engine;

import org.springframework.stereotype.Component;

@Component
public class EllipseShapedSafetyZoneDetector implements Detector {

    @Override
    public boolean nearMissDetected(Vessel ownVessel, Vessel otherVessel) {
        return Math.random() < 0.5;
    }
}

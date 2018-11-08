package dk.dma.nearmiss.engine.engineParts;

import dk.dma.nearmiss.engine.Vessel;

public class EllipseShapedSafetyZoneDetector implements NearMissDetector {

    private final Vessel ownVessel;

    public EllipseShapedSafetyZoneDetector(Vessel ownVessel) {
        this.ownVessel = ownVessel;
    }

    @Override
    public boolean nearMissDetected(Vessel otherVessel) {
        // TODO implement
        return Math.random() < 0.5;
    }
}

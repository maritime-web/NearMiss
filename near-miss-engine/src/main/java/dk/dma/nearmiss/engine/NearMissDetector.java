package dk.dma.nearmiss.engine;

public interface NearMissDetector {
    boolean nearMiss(Vessel ownVessel, Vessel otherVessel);
}

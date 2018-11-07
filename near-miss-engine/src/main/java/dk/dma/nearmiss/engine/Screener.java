package dk.dma.nearmiss.engine;

public interface Screener {

    boolean isCandidate(Vessel ownVessel, Vessel otherVessel);

}

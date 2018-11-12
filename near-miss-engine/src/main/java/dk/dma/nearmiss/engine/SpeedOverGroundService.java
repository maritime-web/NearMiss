package dk.dma.nearmiss.engine;

public interface SpeedOverGroundService {
    /** Estimate speed over ground based on message */
    int speedOverGround(String message);
}

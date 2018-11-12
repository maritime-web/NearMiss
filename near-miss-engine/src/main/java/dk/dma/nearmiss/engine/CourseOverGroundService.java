package dk.dma.nearmiss.engine;

public interface CourseOverGroundService {
    /** Estimate course over ground based on message */
    int courseOverGround(String message);
}

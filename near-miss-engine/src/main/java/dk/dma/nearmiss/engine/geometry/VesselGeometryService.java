package dk.dma.nearmiss.engine.geometry;

import dk.dma.nearmiss.helper.Position;
import org.springframework.stereotype.Component;

@Component
public class VesselGeometryService {

    public Position calulateGeometricCenter(Position gpsReceiverPosition, double cog, int dimPort, int dimStern) {
        // TODO implement
        return gpsReceiverPosition;
    }

}

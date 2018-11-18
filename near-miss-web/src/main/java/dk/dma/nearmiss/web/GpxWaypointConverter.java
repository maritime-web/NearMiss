package dk.dma.nearmiss.web;

import dk.dma.nearmiss.db.entity.VesselState;
import io.jenetics.jpx.WayPoint;

import java.time.ZoneOffset;

public class GpxWaypointConverter {
    private final dk.dma.nearmiss.db.entity.VesselState entity;

    GpxWaypointConverter(VesselState entity) {
        this.entity = entity;
    }

    public WayPoint convert() {
        final long epochMilli = entity.getPositionTime().toEpochSecond(ZoneOffset.UTC) * 1000;
        return WayPoint.of(entity.getLatitude(), entity.getLongitude(), epochMilli);
    }
}

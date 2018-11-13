package dk.dma.nearmiss.web;

import dk.dma.nearmiss.db.entity.EllipticSafetyZone;
import dk.dma.nearmiss.rest.generated.model.Dimensions;
import dk.dma.nearmiss.rest.generated.model.Position;
import dk.dma.nearmiss.rest.generated.model.SafetyZone;
import dk.dma.nearmiss.rest.generated.model.VesselState;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * This class converts one VesselState entity to the VesselState model type.
 */
public class VesselStateConverter {
    private final dk.dma.nearmiss.db.entity.VesselState entity;

    public VesselStateConverter(dk.dma.nearmiss.db.entity.VesselState entity) {
        this.entity = entity;
    }

    public VesselState convert() {
        VesselState model = new VesselState();
        model.setMmsi((long) entity.getMmsi());
        model.setPosition(makePosition(entity.getLatitude(), entity.getLongitude()));

        model.setTime(OffsetDateTime.of(entity.getPositionTime(), ZoneOffset.UTC));
        model.setSog((double) entity.getSog());
        model.setCog((double) entity.getCog());
        model.setHdg((double) entity.getHdg());
        model.setNearMissFlag(entity.isNearMiss());
        model.setSafetyZone(makeSafetyZone(entity.getSafetyZone()));

        Dimensions d = new Dimensions();
        d.setBeam(entity.getBeam());
        d.setLoa(entity.getLoa());
        model.setDimensions(d);
        return model;
    }

    private SafetyZone makeSafetyZone(EllipticSafetyZone esz) {
        if (esz == null) return null;
        SafetyZone s = new SafetyZone();
        s.setA(esz.getSemiMajor());
        s.setB(esz.getSemiMinor());
        s.setCenterPosition(makePosition(esz.getLatitude(), esz.getLongitude()));
        return s;
    }

    private Position makePosition(Double lat, Double lon) {
        Position p = new Position();
        p.setLat(lat);
        p.setLon(lon);
        return p;
    }

}



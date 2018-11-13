package dk.dma.nearmiss.engine.geometry.geometries;

import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKTWriter;

import java.util.Objects;

public abstract class Geometry {

    public boolean intersects(Geometry otherGeometry) {
        return _internalRepresentation.intersects(otherGeometry._internalRepresentation);
    }

    public String toWkt() {
        return new WKTWriter().write(_internalRepresentation);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Geometry that = (Geometry) o;
        return Objects.equals(_internalRepresentation, that._internalRepresentation);
    }

    protected Geometry() {
    }

    protected Polygon _internalRepresentation;

}

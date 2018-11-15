package dk.dma.nearmiss.engine.geometry.geometries;

import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKTWriter;

import java.util.Objects;

import static java.lang.Math.PI;

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

    protected double thetaDegToRad(double thetaDeg) {
        thetaDeg = -(360 - thetaDeg) % 360.0;
        final double thetaRad = thetaDeg * (PI / 180.0);
        return thetaRad;
    }

    protected Polygon _internalRepresentation;

}

package nl.enjarai.doabarrelroll.flight.util;

@FunctionalInterface
public interface ConfiguresRotation {
    RotationInstant apply(RotationInstant rotationInstant);
}

package nl.enjarai.doabarrelroll.config;

@FunctionalInterface
public interface ConfiguresRotation {
    RotationInstant apply(RotationInstant rotationInstant);
}

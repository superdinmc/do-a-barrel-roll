package nl.enjarai.doabarrelroll.flight.util;

import nl.enjarai.doabarrelroll.config.Sensitivity;

import java.util.function.BooleanSupplier;

public class RotationInstant {
    private final double pitch;
    private final double yaw;
    private final double roll;
    private final double renderDelta;

    public RotationInstant(double pitch, double yaw, double roll, double renderDelta) {
        this.pitch = pitch;
        this.yaw = yaw;
        this.roll = roll;
        this.renderDelta = renderDelta;
    }

    public double getPitch() {
        return pitch;
    }

    public double getYaw() {
        return yaw;
    }

    public double getRoll() {
        return roll;
    }

    public double getRenderDelta() {
        return renderDelta;
    }

    public RotationInstant add(double pitch, double yaw, double roll) {
        return new RotationInstant(this.pitch + pitch, this.yaw + yaw, this.roll + roll, this.renderDelta);
    }

    // Add absolute upright rotation to this rolled rotation, taking roll into account.
    public RotationInstant addAbsolute(double x, double y, double currentRoll) {
        double cos = Math.cos(currentRoll);
        double sin = Math.sin(currentRoll);
        return new RotationInstant(this.pitch - y * cos - x * sin, this.yaw - y * sin + x * cos, this.roll, this.renderDelta);
    }

    public RotationInstant applySensitivity(Sensitivity sensitivity) {
        return new RotationInstant(
                pitch * sensitivity.pitch,
                yaw * sensitivity.yaw,
                roll * sensitivity.roll,
                renderDelta
        );
    }

    public RotationInstant useModifier(ConfiguresRotation modifier, BooleanSupplier condition) {
        return condition.getAsBoolean() ? modifier.apply(this) : this;
    }

    public RotationInstant useModifier(ConfiguresRotation modifier) {
        return useModifier(modifier, () -> true);
    }
}

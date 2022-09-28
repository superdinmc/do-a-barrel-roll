package nl.enjarai.doabarrelroll.config;

import net.minecraft.client.util.SmoothUtil;

import java.util.function.Function;

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

    public RotationInstant smooth(SmoothUtil pitchSmoother, SmoothUtil yawSmoother, SmoothUtil rollSmoother, Sensitivity smoothness) {
        return new RotationInstant(
                pitchSmoother.smooth(pitch, smoothness.pitch * renderDelta),
                yawSmoother.smooth(yaw, smoothness.yaw * renderDelta),
                rollSmoother.smooth(roll, smoothness.roll * renderDelta),
                renderDelta
        );
    }

    public RotationInstant applySensitivity(Sensitivity sensitivity) {
        return new RotationInstant(
                pitch * sensitivity.pitch,
                yaw * sensitivity.yaw,
                roll * sensitivity.roll,
                renderDelta
        );
    }

    public RotationInstant applyConfig(ConfiguresRotation config) {
        return config.configureRotation(this);
    }

    public RotationInstant useModifier(Function<RotationInstant, RotationInstant> modifier) {
        return modifier.apply(this);
    }
}

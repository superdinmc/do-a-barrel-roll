package nl.enjarai.doabarrelroll.flight;

import net.minecraft.client.util.SmoothUtil;
import net.minecraft.util.math.MathHelper;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.DoABarrelRollClient;
import nl.enjarai.doabarrelroll.ModKeybindings;
import nl.enjarai.doabarrelroll.api.event.RollContext;
import nl.enjarai.doabarrelroll.api.rotation.RotationInstant;
import nl.enjarai.doabarrelroll.config.ModConfig;
import nl.enjarai.doabarrelroll.config.Sensitivity;
import nl.enjarai.doabarrelroll.util.MagicNumbers;

public class RotationModifiers {
    public static RollContext.ConfiguresRotation buttonControls(double power) {
        return (rotationInstant, context) -> {
            var delta = power * context.getRenderDelta();
            var pitch = 0;
            var yaw = 0;
            var roll = 0;

            if (ModKeybindings.PITCH_UP.isPressed()) {
                pitch -= delta;
            }
            if (ModKeybindings.PITCH_DOWN.isPressed()) {
                pitch += delta;
            }
            if (ModKeybindings.YAW_LEFT.isPressed()) {
                yaw -= delta;
            }
            if (ModKeybindings.YAW_RIGHT.isPressed()) {
                yaw += delta;
            }
            if (ModKeybindings.ROLL_LEFT.isPressed()) {
                roll -= delta;
            }
            if (ModKeybindings.ROLL_RIGHT.isPressed()) {
                roll += delta;
            }

            // Putting this in the roll value, since it'll be swapped later
            return rotationInstant.add(pitch, yaw, roll);
        };
    }

    public static RollContext.ConfiguresRotation smoothing(SmoothUtil pitchSmoother, SmoothUtil yawSmoother, SmoothUtil rollSmoother, Sensitivity smoothness) {
        return (rotationInstant, context) -> RotationInstant.of(
                pitchSmoother.smooth(rotationInstant.pitch(), smoothness.pitch * context.getRenderDelta()),
                yawSmoother.smooth(rotationInstant.yaw(), smoothness.yaw * context.getRenderDelta()),
                rollSmoother.smooth(rotationInstant.roll(), smoothness.roll * context.getRenderDelta())
        );
    }

    public static RotationInstant banking(RotationInstant rotationInstant, RollContext context) {
        var delta = context.getRenderDelta();
        var currentRotation = context.getCurrentRotation();
        var currentRoll = currentRotation.roll() * MagicNumbers.TORAD;
        var strength = 10 * Math.cos(currentRotation.pitch() * MagicNumbers.TORAD) * ModConfig.INSTANCE.getBankingStrength();

        var dX = Math.sin(currentRoll) * strength;
        var dY = -strength + Math.cos(currentRoll) * strength;

        // check if we accidentally got NaN, for some reason this happens sometimes
        if (Double.isNaN(dX)) dX = 0;
        if (Double.isNaN(dY)) dY = 0;

        return rotationInstant.addAbsolute(dX * delta, dY * delta, currentRoll);
    }

    public static RotationInstant manageThrottle(RotationInstant rotationInstant, RollContext context) {
        var delta = context.getRenderDelta();

        if (ModKeybindings.THRUST_FORWARD.isPressed()) {
            DoABarrelRollClient.throttle += 0.1 * delta;
        } else if (ModKeybindings.THRUST_BACKWARD.isPressed()) {
            DoABarrelRollClient.throttle -= 0.1 * delta;
        } else {
            DoABarrelRollClient.throttle -= DoABarrelRollClient.throttle * 0.95 * delta;
        }

        DoABarrelRollClient.throttle = MathHelper.clamp(DoABarrelRollClient.throttle, 0, ModConfig.INSTANCE.getMaxThrust());

        return rotationInstant;
    }

    public static RollContext.ConfiguresRotation fixNaN(String name) {
        return (rotationInstant, context) -> {
            if (Double.isNaN(rotationInstant.pitch())) {
                rotationInstant = RotationInstant.of(0, rotationInstant.yaw(), rotationInstant.roll());
                DoABarrelRoll.LOGGER.warn("NaN found in pitch for " + name + ", setting to 0 as fallback");
            }
            if (Double.isNaN(rotationInstant.yaw())) {
                rotationInstant = RotationInstant.of(rotationInstant.pitch(), 0, rotationInstant.roll());
                DoABarrelRoll.LOGGER.warn("NaN found in yaw for " + name + ", setting to 0 as fallback");
            }
            if (Double.isNaN(rotationInstant.roll())) {
                rotationInstant = RotationInstant.of(rotationInstant.pitch(), rotationInstant.yaw(), 0);
                DoABarrelRoll.LOGGER.warn("NaN found in roll for " + name + ", setting to 0 as fallback");
            }
            return rotationInstant;
        };
    }
}

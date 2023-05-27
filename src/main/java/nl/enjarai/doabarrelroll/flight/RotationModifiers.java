package nl.enjarai.doabarrelroll.flight;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.SmoothUtil;
import net.minecraft.util.math.MathHelper;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.DoABarrelRollClient;
import nl.enjarai.doabarrelroll.api.RollEntity;
import nl.enjarai.doabarrelroll.api.event.RollContext;
import nl.enjarai.doabarrelroll.api.rotation.RotationInstant;
import nl.enjarai.doabarrelroll.config.ModConfig;
import nl.enjarai.doabarrelroll.config.Sensitivity;

public class RotationModifiers {
    public static RollContext.ConfiguresRotation strafeButtons(double power) {
        return (rotationInstant, context) -> {
            var client = MinecraftClient.getInstance();

            var yawDelta = power * context.getRenderDelta();
            var yaw = 0;

            if (client.options.leftKey.isPressed()) {
                yaw -= yawDelta;
            }
            if (client.options.rightKey.isPressed()) {
                yaw += yawDelta;
            }

            // Putting this in the roll value, since it'll be swapped later
            return rotationInstant.add(0, 0, yaw);
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
        var client = MinecraftClient.getInstance();
        var player = client.player;
        var rollPlayer = (RollEntity) player;
        if (player == null) return rotationInstant;

        var delta = context.getRenderDelta();
        var currentRoll = rollPlayer.doABarrelRoll$getRoll() * ElytraMath.TORAD;
        var strength = 10 * Math.cos(player.getPitch() * ElytraMath.TORAD) * ModConfig.INSTANCE.getBankingStrength();

        var dX = Math.sin(currentRoll) * strength;
        var dY = -strength + Math.cos(currentRoll) * strength;

        // check if we accidentally got NaN, for some reason this happens sometimes
        if (Double.isNaN(dX)) dX = 0;
        if (Double.isNaN(dY)) dY = 0;

        return rotationInstant.addAbsolute(dX * delta, dY * delta, currentRoll);
    }

    public static RotationInstant manageThrottle(RotationInstant rotationInstant, RollContext context) {
        var client = MinecraftClient.getInstance();

        var delta = context.getRenderDelta();

        if (client.options.forwardKey.isPressed()) {
            DoABarrelRollClient.throttle += 0.1 * delta;
        } else if (client.options.backKey.isPressed()) {
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

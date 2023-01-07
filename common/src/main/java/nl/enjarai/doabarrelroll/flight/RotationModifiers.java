package nl.enjarai.doabarrelroll.flight;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;
import nl.enjarai.doabarrelroll.DoABarrelRollClient;
import nl.enjarai.doabarrelroll.config.ModConfig;
import nl.enjarai.doabarrelroll.flight.util.RotationInstant;

public class RotationModifiers {
    public static RotationInstant strafeButtons(RotationInstant rotationInstant) {
        var client = MinecraftClient.getInstance();

        var yawDelta = 1800 * rotationInstant.getRenderDelta();
        var yaw = 0;

        if (client.options.leftKey.isPressed()) {
            yaw -= yawDelta;
        }
        if (client.options.rightKey.isPressed()) {
            yaw += yawDelta;
        }

        // Putting this in the roll value, since it'll be swapped later
        return rotationInstant.add(0, 0, yaw);
    }

    public static RotationInstant banking(RotationInstant rotationInstant) {
        var client = MinecraftClient.getInstance();
        var player = client.player;
        if (player == null) return rotationInstant;

        var delta = rotationInstant.getRenderDelta();
        var currentRoll = ElytraMath.getRoll(player.getYaw(), DoABarrelRollClient.left) * ElytraMath.TORAD;
        var strength = 10 * Math.cos(player.getPitch() * ElytraMath.TORAD) * ModConfig.INSTANCE.getBankingStrength();

        var dX = Math.sin(currentRoll) * strength;
        var dY = -strength + Math.cos(currentRoll) * strength;

        // check if we accidentally got NaN, for some reason this happens sometimes
        if (Double.isNaN(dX)) dX = 0;
        if (Double.isNaN(dY)) dY = 0;

        return rotationInstant.addAbsolute(dX * delta, dY * delta, currentRoll);
    }

    public static RotationInstant manageThrottle(RotationInstant rotationInstant) {
        var client = MinecraftClient.getInstance();

        var delta = rotationInstant.getRenderDelta();

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
}

package nl.enjarai.doabarrelroll;

import com.mojang.blaze3d.Blaze3D;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.SmoothDouble;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import nl.enjarai.doabarrelroll.config.ModConfig;
import nl.enjarai.doabarrelroll.config.RotationInstant;
import nl.enjarai.doabarrelroll.config.Sensitivity;

public class DoABarrelRollClient {
    public static final String MODID = "do-a-barrel-roll";
    public static final Sensitivity ROTATION_SMOOTHNESS = new Sensitivity(1, 0.4, 1);

    public static final SmoothDouble pitchSmoother = new SmoothDouble();
    public static final SmoothDouble yawSmoother = new SmoothDouble();
    public static final SmoothDouble rollSmoother = new SmoothDouble();
    private static double lastLookUpdate;
    private static double lastLerpUpdate;
    public static double landingLerp = 1;
    public static Vec3 left;
    public static Vec2 mouseTurnVec = Vec2.ZERO;
    public static double throttle = 0;

    // TODO triple jump to activate???

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MODID, path);
    }


    public static boolean updateMouse(LocalPlayer player, double cursorDeltaX, double cursorDeltaY) {

        double time = Blaze3D.getTime();
        double lerpDelta = time - lastLerpUpdate;
        lastLerpUpdate = time;

        // smoothly lerp left vector to the assumed upright left if not in flight
        if (!isFallFlying()) {

            landingLerp = Mth.lerp(Mth.clamp(lerpDelta * 2, 0, 1), landingLerp, 1);

            // round the lerp off when done to hopefully avoid world flickering
            if (landingLerp > 0.9) landingLerp = 1;

            clearValues();
            left = left.lerp(ElytraMath.getAssumedLeft(player.getYRot()), landingLerp);

            return true;
        }


        // reset the landing animation when flying
        landingLerp = 0;

        if (ModConfig.INSTANCE.momentumBasedMouse) {

            // add the mouse movement to the current vector and normalize if needed
            var turnVec = mouseTurnVec.add(new Vec2((float) cursorDeltaX, (float) cursorDeltaY).scale(1f / 300));
            if (turnVec.lengthSquared() > 1) {
                turnVec = turnVec.normalized();
            }
            mouseTurnVec = turnVec;

            // enlarge the vector and apply it to the camera
            var delta = getDelta();
            var readyTurnVec = mouseTurnVec.scale(1200 * (float) delta);
            changeElytraLook(readyTurnVec.y, 0, readyTurnVec.x, ModConfig.INSTANCE.desktopSensitivity, delta);

        } else {

            // if we are not using a momentum based mouse, we can reset it and apply the values directly
            mouseTurnVec = Vec2.ZERO;
            changeElytraLook(cursorDeltaY, 0, cursorDeltaX, ModConfig.INSTANCE.desktopSensitivity);
        }

        return false;
    }

    public static void onWorldRender(Minecraft client, float tickDelta, PoseStack matrix) {

        if (!isFallFlying()) {

            clearValues();

        } else {

            if (client.isPaused()) {

                // keep updating the last look update time when paused to prevent large jumps after unpausing
                lastLookUpdate = Blaze3D.getTime();

            } else {

                // update the camera rotation every frame to keep it smooth
                changeElytraLook(0, 0, 0, ModConfig.INSTANCE.desktopSensitivity);

            }
        }

        if (client.player != null && landingLerp < 1) {

            // calculate the camera angle and apply it
            double angle = -Math.acos(Mth.clamp(left.dot(ElytraMath.getAssumedLeft(client.player.getYRot())), -1, 1)) * ElytraMath.TODEG;
            if (left.y() < 0) angle *= -1;
            matrix.mulPose(Vector3f.ZP.rotationDegrees((float) angle));

        }
    }

    public static void onRenderCrosshair(PoseStack matrices, int scaledWidth, int scaledHeight) {
        if (!isFallFlying() || !ModConfig.INSTANCE.momentumBasedMouse || !ModConfig.INSTANCE.showMomentumWidget) return;

        MomentumCrosshairWidget.render(matrices, scaledWidth, scaledHeight, mouseTurnVec);
    }


    private static void clearValues() {
        pitchSmoother.reset();
        yawSmoother.reset();
        rollSmoother.reset();
        mouseTurnVec = Vec2.ZERO;
        lastLookUpdate = Blaze3D.getTime();
        throttle = 0;
    }

    /**
     * Returns the time since the last look update.
     *
     * <p>
     * <b>Only call if you're going to call
     * {@link DoABarrelRollClient#changeElytraLook(double, double, double, Sensitivity, double)}
     * right after this using the returned value.</b>
     * Neglecting to do this will disrupt the smoothness of the camera.
     * </p>
     */
    private static double getDelta() {
        double time = Blaze3D.getTime();
        double delta = time - lastLookUpdate;
        lastLookUpdate = time;
        return delta;
    }

    /**
     * Only use if you <b>haven't</b> called {@link DoABarrelRollClient#getDelta()} before this.
     */
    public static void changeElytraLook(double pitch, double yaw, double roll, Sensitivity sensitivity) {
        changeElytraLook(pitch, yaw, roll, sensitivity, getDelta());
    }

    public static void changeElytraLook(double pitch, double yaw, double roll, Sensitivity sensitivity, double delta) {
        var player = Minecraft.getInstance().player;
        if (player == null) return;

        var rotDelta = new RotationInstant(pitch, yaw, roll, delta);

        ElytraMath.changeElytraLookDirectly(player, rotDelta
                .useModifier(DoABarrelRollClient::manageThrottle)
                .useModifier(DoABarrelRollClient::strafeButtons)
                .applySensitivity(sensitivity)
                .applyConfig(ModConfig.INSTANCE)
                .smooth(pitchSmoother, yawSmoother, rollSmoother, ROTATION_SMOOTHNESS)
                .useModifier(DoABarrelRollClient::banking, () -> ModConfig.INSTANCE.enableBanking)
        );
    }

    public static RotationInstant strafeButtons(RotationInstant rotationInstant) {
        var client = Minecraft.getInstance();

        var yawDelta = 1800 * rotationInstant.getRenderDelta();
        var yaw = 0;

        if (client.options.keyLeft.isDown()) {
            yaw -= yawDelta;
        }
        if (client.options.keyRight.isDown()) {
            yaw += yawDelta;
        }

        return rotationInstant.add(0, yaw, 0);
    }

    public static RotationInstant banking(RotationInstant rotationInstant) {
        var client = Minecraft.getInstance();
        var player = client.player;
        if (player == null) return rotationInstant;

        var delta = rotationInstant.getRenderDelta();
        var currentRoll = ElytraMath.getRoll(player.getYRot(), left) * ElytraMath.TORAD;
        var strength = 10 * Math.cos(player.getXRot() * ElytraMath.TORAD) * ModConfig.getBankingStrength();

        var dX = Math.sin(currentRoll) * strength;
        var dY = -strength + Math.cos(currentRoll) * strength;

        // check if we accidentally got NaN, for some reason this happens sometimes
        if (Double.isNaN(dX)) dX = 0;
        if (Double.isNaN(dY)) dY = 0;

        return rotationInstant.addAbsolute(dX * delta, dY * delta, currentRoll);
    }

    public static RotationInstant manageThrottle(RotationInstant rotationInstant) {
        var client = Minecraft.getInstance();

        var delta = rotationInstant.getRenderDelta();

        if (client.options.keyUp.isDown()) {
            throttle += 0.1 * delta;
        } else if (client.options.keyDown.isDown()) {
            throttle -= 0.1 * delta;
        } else {
            throttle -= throttle * 0.95 * delta;
        }

        throttle = Mth.clamp(throttle, 0, ModConfig.getMaxThrust());

        return rotationInstant;
    }

    public static boolean isFallFlying() {
        var player = Minecraft.getInstance().player;
        return player != null && player.isFallFlying() && ModConfig.INSTANCE.modEnabled;
    }
}

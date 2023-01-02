package nl.enjarai.doabarrelroll;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.GlfwUtil;
import net.minecraft.client.util.SmoothUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import nl.enjarai.doabarrelroll.config.ActivationBehaviour;
import nl.enjarai.doabarrelroll.config.ModConfig;
import nl.enjarai.doabarrelroll.config.Sensitivity;
import nl.enjarai.doabarrelroll.flight.ElytraMath;
import nl.enjarai.doabarrelroll.flight.RotationModifiers;
import nl.enjarai.doabarrelroll.flight.util.RotationInstant;
import nl.enjarai.doabarrelroll.util.MixinHooks;
import nl.enjarai.doabarrelroll.util.Vec2d;

public class DoABarrelRollClient {
    public static final SmoothUtil pitchSmoother = new SmoothUtil();
    public static final SmoothUtil yawSmoother = new SmoothUtil();
    public static final SmoothUtil rollSmoother = new SmoothUtil();
    private static double lastLookUpdate;
    private static double lastLerpUpdate;
    public static double landingLerp = 1;
    public static Vec3d left;
    public static Vec2d mouseTurnVec = Vec2d.ZERO;
    public static double throttle = 0;

    // TODO triple jump to activate???


    public static void clientTick(MinecraftClient client) {
        while (ModKeybindings.TOGGLE_ENABLED.wasPressed()) {
            ModConfig.INSTANCE.setModEnabled(!ModConfig.INSTANCE.getModEnabled());
            ModConfig.INSTANCE.save();

            if (client.player != null) {
                client.player.sendMessage(
                        Text.translatable(
                                "key.do_a_barrel_roll." +
                                        (ModConfig.INSTANCE.getModEnabled() ? "toggle_enabled.enable" : "toggle_enabled.disable")
                        ),
                        true
                );
            }
        }
    }

    public static boolean updateMouse(ClientPlayerEntity player, double cursorDeltaX, double cursorDeltaY) {

        if (!isFallFlying()) return true;

        // reset the landing animation when flying
        landingLerp = 0;

        if (ModConfig.INSTANCE.getMomentumBasedMouse()) {

            // add the mouse movement to the current vector and normalize if needed
            Vec2d turnVec = mouseTurnVec.add(new Vec2d(cursorDeltaX, cursorDeltaY).multiply(1f / 300));
            if (turnVec.lengthSquared() > 1) {
                turnVec = turnVec.normalize();
            }
            mouseTurnVec = turnVec;

            // enlarge the vector and apply it to the camera
            var delta = getDelta();
            var readyTurnVec = mouseTurnVec.multiply(1200 * (float) delta);
            changeElytraLook(readyTurnVec.y, 0, readyTurnVec.x, ModConfig.INSTANCE.getDesktopSensitivity(), delta);

        } else {

            // if we are not using a momentum based mouse, we can reset it and apply the values directly
            mouseTurnVec = Vec2d.ZERO;
            changeElytraLook(cursorDeltaY, 0, cursorDeltaX, ModConfig.INSTANCE.getDesktopSensitivity());
        }

        return false;
    }

    public static void onWorldRender(MinecraftClient client, float tickDelta, MatrixStack matrix) {

        double time = GlfwUtil.getTime();
        double lerpDelta = time - lastLerpUpdate;
        lastLerpUpdate = time;

        if (!isFallFlying()) {

            landingLerp = MathHelper.lerp(MathHelper.clamp(lerpDelta * 2, 0, 1), landingLerp, 1);

            // round the lerp off when done to hopefully avoid world flickering
            if (landingLerp > 0.9) landingLerp = 1;

            clearValues();

            if (client.player != null) {
                left = left.lerp(ElytraMath.getAssumedLeft(client.player.getYaw()), landingLerp);
            }

        } else {

            if (client.isPaused()) {

                // keep updating the last look update time when paused to prevent large jumps after unpausing
                lastLookUpdate = GlfwUtil.getTime();

            } else {

                // update the camera rotation every frame to keep it smooth
                changeElytraLook(0, 0, 0, ModConfig.INSTANCE.getDesktopSensitivity());

            }
        }

        if (client.player != null && landingLerp < 1) {

            // calculate the camera angle and apply it
            double angle = -Math.acos(MathHelper.clamp(left.dotProduct(ElytraMath.getAssumedLeft(client.player.getYaw())), -1, 1)) * ElytraMath.TODEG;
            if (left.getY() < 0) angle *= -1;
            matrix.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float) angle));

        }
    }

    public static void onRenderCrosshair(MatrixStack matrices, int scaledWidth, int scaledHeight) {
        if (!isFallFlying()
                || !ModConfig.INSTANCE.getMomentumBasedMouse()
                || !ModConfig.INSTANCE.getShowMomentumWidget()
        ) return;

        MomentumCrosshairWidget.render(matrices, scaledWidth, scaledHeight, mouseTurnVec);
    }


    private static void clearValues() {
        pitchSmoother.clear();
        yawSmoother.clear();
        rollSmoother.clear();
        mouseTurnVec = Vec2d.ZERO;
        lastLookUpdate = GlfwUtil.getTime();
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
        double time = GlfwUtil.getTime();
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
        var player = MinecraftClient.getInstance().player;
        if (player == null) return;

        var rotDelta = new RotationInstant(pitch, yaw, roll, delta);

        ElytraMath.changeElytraLookDirectly(player, rotDelta
                .useModifier(RotationModifiers::manageThrottle, ModConfig.INSTANCE::getEnableThrust)
                .useModifier(RotationModifiers::strafeButtons)
                .applySensitivity(sensitivity)
                .useModifier(ModConfig.INSTANCE::configureRotation)
                .smooth(pitchSmoother, yawSmoother, rollSmoother, ModConfig.INSTANCE.getSmoothing())
                .useModifier(RotationModifiers::banking, ModConfig.INSTANCE::getEnableBanking)
        );
    }

    public static boolean isFallFlying() {
        var hybrid = ModConfig.INSTANCE.getActivationBehaviour() == ActivationBehaviour.HYBRID ||
                ModConfig.INSTANCE.getActivationBehaviour() == ActivationBehaviour.HYBRID_TOGGLE;
        if (hybrid && !MixinHooks.thirdJump) {
            return false;
        }

        var player = MinecraftClient.getInstance().player;
        return player != null
                && player.isFallFlying()
                && ModConfig.INSTANCE.getModEnabled();
    }
}

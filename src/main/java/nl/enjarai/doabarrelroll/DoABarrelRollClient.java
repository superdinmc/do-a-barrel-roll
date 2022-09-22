package nl.enjarai.doabarrelroll;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.GlfwUtil;
import net.minecraft.client.util.SmoothUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import nl.enjarai.doabarrelroll.config.ModConfig;
import nl.enjarai.doabarrelroll.config.Sensitivity;

public class DoABarrelRollClient implements ClientModInitializer {
	public static final String MODID = "do-a-barrel-roll";

	public static final SmoothUtil pitchSmoother = new SmoothUtil();
	public static final SmoothUtil yawSmoother = new SmoothUtil();
	public static final SmoothUtil rollSmoother = new SmoothUtil();
	private static double lastLookUpdate;
	private static double lastLerpUpdate;
	public static double landingLerp = 1;
	public static Vec3d left;
	public static Vec2f mouseTurnVec = Vec2f.ZERO;
	

	@Override
    public void onInitializeClient() { // TODO triple jump to activate???
		ModConfig.init();
    }

	public static Identifier id(String path) {
		return new Identifier(MODID, path);
	}
	

	public static boolean updateMouse(ClientPlayerEntity player, double cursorDeltaX, double cursorDeltaY) {
		
		double time = GlfwUtil.getTime();
		double lerpDelta = time - lastLerpUpdate;
		lastLerpUpdate = time;

		// smoothly lerp left vector to the assumed upright left if not in flight
		if (!isFallFlying()) {

			landingLerp = MathHelper.lerp(MathHelper.clamp(lerpDelta * 2, 0, 1), landingLerp, 1);

			// round the lerp off when done to hopefully avoid world flickering
			if (landingLerp > 0.9) landingLerp = 1;
			
			clearValues();
			left = left.lerp(ElytraMath.getAssumedLeft(player.getYaw()), landingLerp);

			return true;
		}


		// reset the landing animation when flying
		landingLerp = 0;

		if (ModConfig.INSTANCE.momentumBasedMouse) {

			// add the mouse movement to the current vector and normalize if needed
			var turnVec = mouseTurnVec.add(new Vec2f((float) cursorDeltaX, (float) cursorDeltaY).multiply(1f / 300));
			if (turnVec.lengthSquared() > 1) {
				turnVec = turnVec.normalize();
			}
			mouseTurnVec = turnVec;

			// enlarge the vector and apply it to the camera
			var delta = getDelta();
			var readyTurnVec = mouseTurnVec.multiply(1200 * (float) delta);
			changeElytraLook(readyTurnVec.y, 0, readyTurnVec.x, ModConfig.INSTANCE.desktopSensitivity, delta);

		} else {

			// if we are not using a momentum based mouse, we can reset it and apply the values directly
			mouseTurnVec = Vec2f.ZERO;
			changeElytraLook(cursorDeltaY, 0, cursorDeltaX, ModConfig.INSTANCE.desktopSensitivity);
		}

		return false;
	}
	
	public static void onWorldRender(MinecraftClient client, float tickDelta, long limitTime, MatrixStack matrix) {

		if (!isFallFlying()) {

			clearValues();

		} else {

			if (client.isPaused()) {

				// keep updating the last look update time when paused to prevent large jumps after unpausing
				lastLookUpdate = GlfwUtil.getTime();

			} else {

				var yawDelta = 25f;
				var yaw = 0;

				// Strafe buttons
				if (client.options.leftKey.isPressed()) {
					yaw -= yawDelta;
				}
				if (client.options.rightKey.isPressed()) {
					yaw += yawDelta;
				}

				changeElytraLook(0, yaw, 0, ModConfig.INSTANCE.desktopSensitivity);
			}
		}

		if (client.player != null && landingLerp < 1) {

			// calculate the camera angle and apply it
			double angle = -Math.acos(left.dotProduct(ElytraMath.getAssumedLeft(client.player.getYaw()))) * ElytraMath.TODEG;
			if (left.getY() < 0) angle *= -1;
			matrix.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion((float) angle));

		}
	}

	public static void onRenderCrosshair(MatrixStack matrices, int scaledWidth, int scaledHeight) {
		if (!isFallFlying() || !ModConfig.INSTANCE.momentumBasedMouse || !ModConfig.INSTANCE.showMomentumWidget) return;

		MomentumCrosshairWidget.render(matrices, scaledWidth, scaledHeight, mouseTurnVec);
	}


	private static void clearValues() {
		pitchSmoother.clear();
		yawSmoother.clear();
		rollSmoother.clear();
		mouseTurnVec = Vec2f.ZERO;
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

		// smooth the look changes
		pitch = pitchSmoother.smooth(pitch, sensitivity.pitch * delta);
		yaw = yawSmoother.smooth(yaw, sensitivity.yaw * delta);
		roll = rollSmoother.smooth(roll, sensitivity.roll * delta);

		// apply the look changes
		if (ModConfig.INSTANCE.switchRollAndYaw) {
			ElytraMath.changeElytraLookDirectly(player, pitch, roll, yaw, sensitivity);
		} else {
			ElytraMath.changeElytraLookDirectly(player, pitch, yaw, roll, sensitivity);
		}
	}
	
	public static boolean isFallFlying() {
		var player = MinecraftClient.getInstance().player;
		return player != null && player.isFallFlying() && ModConfig.INSTANCE.modEnabled;
	}
}

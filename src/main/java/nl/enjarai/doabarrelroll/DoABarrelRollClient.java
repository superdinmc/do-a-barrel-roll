package nl.enjarai.doabarrelroll;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.GlfwUtil;
import net.minecraft.client.util.SmoothUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

public class DoABarrelRollClient implements ClientModInitializer {

	public static final double TORAD = Math.PI / 180;
	public static final double TODEG = 1 / TORAD;
	
	public static final SmoothUtil yawSmoother = new SmoothUtil();
	public static double lastTurnTime;
	public static double landingLerp = 1;
	public static Vec3d left;
	

	@Override
    public void onInitializeClient() {
    }
	

	public static void updateMouse(ClientPlayerEntity player, double cursorDeltaX, double cursorDeltaY) {
		
		double time = GlfwUtil.getTime();
		double delta = time - lastTurnTime;
		lastTurnTime = time;

		// smoothly lerp left vector to the assumed upright left if not in flight
		if (!player.isFallFlying()) {
			landingLerp = MathHelper.lerp(MathHelper.clamp(delta, 0, 1), landingLerp, 1);
			// make sure we don't get any funky behaviour
			if (landingLerp > 0.9) landingLerp = 1;
			
			left = left.lerp(ElytraMath.getAssumedLeft(player.getYaw()), landingLerp);
			yawSmoother.clear();
			player.changeLookDirection(cursorDeltaX, cursorDeltaY);
			return;
		}

		landingLerp = 0;

		ElytraMath.changeElytraLook(player, cursorDeltaY, yawSmoother.smooth(0, delta), cursorDeltaX);
	}
	
	public static void onWorldRender(MinecraftClient client, float tickDelta, long limitTime, MatrixStack matrix) {

		if (client.player == null || !client.player.isFallFlying()) {

			yawSmoother.clear();

		} else {

			double time = GlfwUtil.getTime();
			double delta = time - lastTurnTime;
			lastTurnTime = time;

			var yawDelta = 10f;
			var yaw = 0;
			if (client.options.leftKey.isPressed()) {
				yaw -= yawDelta;
			}
			if (client.options.rightKey.isPressed()) {
				yaw += yawDelta;
			}

			ElytraMath.changeElytraLook(client.player, 0, yawSmoother.smooth(yaw, delta), 0);

		}

		if (landingLerp < 1) {

			double angle = -Math.acos(left.dotProduct(ElytraMath.getAssumedLeft(client.player.getYaw()))) * TODEG;
			if (left.getY() < 0) angle *= -1;

			matrix.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion((float) angle));

		}
	}
	
	
	public static boolean isFallFlying() {
		var player = MinecraftClient.getInstance().player;
		return player != null && player.isFallFlying();
	}

	public static boolean shouldSmooth() {
		return isFallFlying();
	}
}

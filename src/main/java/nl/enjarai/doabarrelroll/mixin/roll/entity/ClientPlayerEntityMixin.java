package nl.enjarai.doabarrelroll.mixin.roll.entity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.stat.StatHandler;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import nl.enjarai.doabarrelroll.DoABarrelRollClient;
import nl.enjarai.doabarrelroll.api.event.RollEvents;
import nl.enjarai.doabarrelroll.config.Sensitivity;
import nl.enjarai.doabarrelroll.flight.ElytraMath;
import nl.enjarai.doabarrelroll.flight.RotationModifiers;
import nl.enjarai.doabarrelroll.flight.util.RotationInstant;
import org.joml.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static nl.enjarai.doabarrelroll.flight.ElytraMath.TODEG;
import static nl.enjarai.doabarrelroll.flight.ElytraMath.TORAD;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntityMixin {
	@Shadow public float renderYaw;
	@Shadow public float lastRenderYaw;

	@Unique
	private float prevRoll;
	@Unique
	private float roll;

	@Unique
	private float lastRoll;
	@Unique
	private float renderRoll;
	@Unique
	private float lastRenderRoll;

	@Unique
	private float landingLerp = 1;

	@Inject(
			method = "<init>",
			at = @At("RETURN")
	)
	public void doABarrelRoll$init(MinecraftClient client, ClientWorld world, ClientPlayNetworkHandler networkHandler, StatHandler stats, ClientRecipeBook recipeBook, boolean lastSneaking, boolean lastSprinting, CallbackInfo ci) {
//		DoABarrelRollClient.left = ElytraMath.getAssumedLeft(getYaw()); // TODO
	}

	@Override
	protected void doABarrelRoll$baseTickTail(CallbackInfo ci) {
		prevRoll = doABarrelRoll$getRoll();

		if (doABarrelRoll$isRolling()) {
			// reset the landing animation when flying
			landingLerp = 0;
		}
	}

	@Override
	public void doABarrelRoll$changeElytraLook(double pitch, double yaw, double roll, Sensitivity sensitivity, double delta) {
		var player = MinecraftClient.getInstance().player;
		if (player == null) return;

		var rotDelta = new RotationInstant(pitch, yaw, roll, delta);
		var currentRoll = ElytraMath.getRoll(player.getYaw(), DoABarrelRollClient.left);
		var currentRotation = new RotationInstant(
				player.getPitch(),
				player.getYaw(),
				currentRoll,
				0
		);

		rotDelta = RollEvents.lateCameraModifiers(
				RollEvents.earlyCameraModifiers(rotDelta
								.useModifier(RotationModifiers.fixNaN("INPUT")), currentRotation)
						.useModifier(RotationModifiers.fixNaN("EARLY_CAMERA_MODIFIERS"))
						.applySensitivity(sensitivity)
						.useModifier(RotationModifiers.fixNaN("SENSITIVITY")),
				currentRotation
		).useModifier(RotationModifiers.fixNaN("LATE_CAMERA_MODIFIERS"));

		doABarrelRoll$changeElytraLook((float) rotDelta.getPitch(), (float) rotDelta.getYaw(), (float) rotDelta.getRoll());
	}

	@Override
	public void doABarrelRoll$changeElytraLook(float pitch, float yaw, float roll) {
		var currentPitch = getPitch();
		var currentYaw = getYaw();
		var currentRoll = doABarrelRoll$getRoll();

		var pitchYaw = new Vector2f(yaw, pitch);
		var rollMatrix = new Matrix2f();
		rollMatrix.rotate((float) (currentRoll * TORAD));
		pitchYaw.mul(rollMatrix);

//
//		System.out.println("currentYaw: " + currentYaw);
//
//		var rotation = new Quaterniond();
//		rotation.rotateX(-currentPitch * TORAD);
//		rotation.rotateY(currentYaw * TORAD);
//		rotation.rotateZ(currentRoll * TORAD);
//
//		var deltaRotation = new Quaterniond();
//		deltaRotation.rotateX(0.15 * -pitch * TORAD);
//		deltaRotation.rotateY(0.15 * yaw * TORAD);
//		deltaRotation.rotateZ(0.15 * roll * TORAD);
//		//		deltaRotation.rotateX(0.15 * pitch * TORAD);
//
//		rotation.mul(deltaRotation);
//
//		var matrix = rotation.get(new Matrix3d());
//		var angles = matrix.getEulerAnglesXYZ(new Vector3d());
//
//		double newPitch = TODEG * -angles.x - currentPitch;
//		double newYaw = TODEG * angles.y - currentYaw;
//		double newRoll = TODEG * angles.z - currentRoll;
//
		// ===============

//		double newPitch = TODEG * Math.asin(2 * (deltaRotation.w * deltaRotation.x - deltaRotation.y * deltaRotation.z));
//		double newYaw = TODEG * Math.atan2(2 * (deltaRotation.w * deltaRotation.y + deltaRotation.x * deltaRotation.z), 1 - 2 * (deltaRotation.y * deltaRotation.y + deltaRotation.x * deltaRotation.x));
//		double newRoll = TODEG * Math.atan2(2 * (deltaRotation.w * deltaRotation.z + deltaRotation.x * deltaRotation.y), 1 - 2 * (deltaRotation.z * deltaRotation.z + deltaRotation.x * deltaRotation.x));

//		var deltaPitch = (float) Math.toDegrees(deltaRotation.() - rotation.getPitch());
//
//
//		Vec3d facing = player.getRotationVecClient();
//		DoABarrelRollClient.left = DoABarrelRollClient.left.subtract(facing.multiply(DoABarrelRollClient.left.dotProduct(facing))).normalize();
//
//		// pitch
//		facing = rotateAxisAngle(facing, DoABarrelRollClient.left, -0.15 * pitch * TORAD);
//
//		// yaw
//		Vec3d up = facing.crossProduct(DoABarrelRollClient.left);
//		facing = rotateAxisAngle(facing, up, 0.15 * yaw * TORAD);
//		DoABarrelRollClient.left = rotateAxisAngle(DoABarrelRollClient.left, up, 0.15 * yaw * TORAD); // TODO THIS THIS THIS
//
//		// roll
//		DoABarrelRollClient.left = rotateAxisAngle(DoABarrelRollClient.left, facing, 0.15 * roll * TORAD);
//
//
//		double deltaY = -Math.asin(facing.getY()) * TODEG - player.getPitch();
//		double   = -Math.atan2(facing.getX(), facing.getZ()) * TODEG - player.getYaw();

		// Apply vanilla pitch and yaw
		changeLookDirection(pitchYaw.x, pitchYaw.y);
//		float deltaPitch = (float) (newPitch - currentPitch);
//		float deltaYaw = (float) (newYaw - currentYaw);
//		setPitch(getPitch() + deltaPitch);
//		setYaw(getYaw() + deltaYaw);
//		setPitch(MathHelper.clamp(getPitch(), -90.0f, 90.0f));
//		prevPitch += deltaPitch;
//		prevYaw += deltaYaw;
//		prevPitch = MathHelper.clamp(prevPitch, -90.0f, 90.0f);
//		if (getVehicle() != null) {
//			getVehicle().onPassengerLookAround((Entity) (Object) this);
//		}

		// Apply roll
		var deltaRoll = 0.15f * roll;
		doABarrelRoll$setRoll(doABarrelRoll$getRoll() + deltaRoll);
		prevRoll += deltaRoll;

		// fix hand spasm when wrapping yaw value
		if (getYaw() < -90 && renderYaw > 90) {
			renderYaw -= 360;
			lastRenderYaw -= 360;
		} else if (getYaw() > 90 && renderYaw < -90) {
			renderYaw += 360;
			lastRenderYaw += 360;
		}
	}

	@Override
	public boolean doABarrelRoll$isRolling() {
		return RollEvents.shouldRoll();
	}

	@Override
	public float doABarrelRoll$getRoll() {
		return roll;
	}

	@Override
	public float doABarrelRoll$getRoll(float tickDelta) {
		if (tickDelta == 1.0f) {
			return doABarrelRoll$getRoll();
		}
		return MathHelper.lerp(tickDelta, prevRoll, doABarrelRoll$getRoll());
	}

	public void doABarrelRoll$setRoll(float roll) {
		if (!Float.isFinite(roll)) {
			Util.error("Invalid entity rotation: " + roll + ", discarding.");
			return;
		}
		this.roll = roll;
	}
}
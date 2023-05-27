package nl.enjarai.doabarrelroll.mixin.roll.entity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import nl.enjarai.doabarrelroll.api.event.RollContext;
import nl.enjarai.doabarrelroll.api.event.RollEvents;
import nl.enjarai.doabarrelroll.api.rotation.RotationInstant;
import nl.enjarai.doabarrelroll.config.Sensitivity;
import nl.enjarai.doabarrelroll.flight.RotationModifiers;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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

	@Override
	protected void doABarrelRoll$baseTickTail(CallbackInfo ci) {
		prevRoll = doABarrelRoll$getRoll();

		if (!doABarrelRoll$isRolling()) {
			doABarrelRoll$setRoll(0.0f);
		}
	}

	@Override
	public void doABarrelRoll$changeElytraLook(double pitch, double yaw, double roll, Sensitivity sensitivity, double delta) {
		var player = MinecraftClient.getInstance().player;
		if (player == null) return;

		var rotDelta = RotationInstant.of(pitch, yaw, roll);
		var currentRoll = doABarrelRoll$getRoll();
		var currentRotation = RotationInstant.of(
				player.getPitch(),
				player.getYaw(),
				currentRoll
		);
		var context = RollContext.of(currentRotation, rotDelta, delta);

		context.useModifier(RotationModifiers.fixNaN("INPUT"));
		RollEvents.earlyCameraModifiers(context);
		context.useModifier(RotationModifiers.fixNaN("EARLY_CAMERA_MODIFIERS"));
		context.useModifier((rotation, ctx) -> rotation.applySensitivity(sensitivity));
		context.useModifier(RotationModifiers.fixNaN("SENSITIVITY"));
		RollEvents.lateCameraModifiers(context);
		context.useModifier(RotationModifiers.fixNaN("LATE_CAMERA_MODIFIERS"));

		rotDelta = context.getRotationDelta();

		doABarrelRoll$changeElytraLook((float) rotDelta.pitch(), (float) rotDelta.yaw(), (float) rotDelta.roll());
	}

	@Override
	public void doABarrelRoll$changeElytraLook(float pitch, float yaw, float roll) {
		var currentPitch = getPitch();
		var currentYaw = getYaw();
		var currentRoll = doABarrelRoll$getRoll();

		// Convert pitch, yaw, and roll to a facing and left vector
		var facing = new Vector3d(getRotationVecClient().toVector3f());
		var left = new Vector3d(1, 0, 0);
		left.rotateZ(-currentRoll * TORAD);
		left.rotateX(-currentPitch * TORAD);
		left.rotateY(-(currentYaw + 180) * TORAD);


		// Apply pitch
		facing.rotateAxis(-0.15 * pitch * TORAD, left.x, left.y, left.z);

		// Apply yaw
		var up = facing.cross(left, new Vector3d());
		facing.rotateAxis(0.15 * yaw * TORAD, up.x, up.y, up.z);
		left.rotateAxis(0.15 * yaw * TORAD, up.x, up.y, up.z);

		// Apply roll
		left.rotateAxis(0.15 * roll * TORAD, facing.x, facing.y, facing.z);


		// Extract new pitch, yaw, and roll
		double newPitch = -Math.asin(facing.y) * TODEG;
		double newYaw = -Math.atan2(facing.x, facing.z) * TODEG;

		var normalLeft = new Vector3d(1, 0, 0).rotateY(-(newYaw + 180) * TORAD);
		double newRoll = -Math.atan2(left.cross(normalLeft, new Vector3d()).dot(facing), left.dot(normalLeft)) * TODEG;

		// Calculate deltas
		double deltaY = newPitch - currentPitch;
		double deltaX = newYaw - currentYaw;
		double deltaRoll = newRoll - currentRoll;

		// Apply vanilla pitch and yaw
		changeLookDirection(deltaX / 0.15, deltaY / 0.15);

		// Apply roll
		doABarrelRoll$setRoll((float) (doABarrelRoll$getRoll() + deltaRoll));
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
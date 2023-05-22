package nl.enjarai.doabarrelroll.mixin.roll;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;
import nl.enjarai.doabarrelroll.api.RollCamera;
import nl.enjarai.doabarrelroll.api.RollEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static nl.enjarai.doabarrelroll.flight.ElytraMath.TORAD;

@Mixin(Camera.class)
public abstract class CameraMixin implements RollCamera {
    @Shadow private Entity focusedEntity;

    @Unique
    private float roll;
    @Unique
    private final ThreadLocal<Float> tempRoll = new ThreadLocal<>();

    @Inject(
            method = "update",
            at = @At("HEAD")
    )
    private void doABarrelRoll$captureTickDelta(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci, @Share("tickDelta") LocalFloatRef tickDeltaRef) {
        tickDeltaRef.set(tickDelta);
    }

    @WrapWithCondition(
            method = "update",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V",
                    ordinal = 0
            )
    )
    private boolean doABarrelRoll$addRoll1(Camera thiz, float yaw, float pitch, @Share("tickDelta") LocalFloatRef tickDelta) {
        tempRoll.set(((RollEntity) focusedEntity).doABarrelRoll$getRoll(tickDelta.get()));
        return true;
    }

    @WrapWithCondition(
            method = "update",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V",
                    ordinal = 1
            )
    )
    private boolean doABarrelRoll$addRoll2(Camera thiz, float yaw, float pitch) {
        tempRoll.set(-this.roll);
        return true;
    }

    @WrapWithCondition(
            method = "update",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V",
                    ordinal = 2
            )
    )
    private boolean doABarrelRoll$addRoll3(Camera thiz, float yaw, float pitch) {
        tempRoll.set(0.0f);
        return true;
    }

    @ModifyArg(
            method = "setRotation",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/joml/Quaternionf;rotationYXZ(FFF)Lorg/joml/Quaternionf;"
            ),
            index = 2
    )
    private float doABarrelRoll$setRoll(float original) {
        this.roll = tempRoll.get();
        return (float) (this.roll * TORAD);
    }

    @Override
    public float doABarrelRoll$getRoll() {
        return roll;
    }
}

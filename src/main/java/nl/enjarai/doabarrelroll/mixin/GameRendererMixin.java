package nl.enjarai.doabarrelroll.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.GlfwUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import nl.enjarai.doabarrelroll.api.RollCamera;
import nl.enjarai.doabarrelroll.api.RollEntity;
import nl.enjarai.doabarrelroll.config.ModConfig;
import nl.enjarai.doabarrelroll.flight.ElytraMath;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow @Final private Camera camera;

    @Inject(
            method = "renderWorld",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/math/RotationAxis;rotationDegrees(F)Lorg/joml/Quaternionf;",
                    ordinal = 2
            )
    )
    public void doABarrelRoll$renderWorld(float tickDelta, long limitTime, MatrixStack matrix, CallbackInfo ci) {
//        double time = GlfwUtil.getTime();
//        double lerpDelta = time - lastLerpUpdate;
//        lastLerpUpdate = time;
//
//        if (!((RollEntity) client.getCameraEntity()).doABarrelRoll$isRolling()) {
//
//            landingLerp = MathHelper.lerp(MathHelper.clamp(lerpDelta * 2, 0, 1), landingLerp, 1);
//
//            // round the lerp off when done to hopefully avoid world flickering
//            if (landingLerp > 0.9) landingLerp = 1;
//
//            clearValues();
//
//            if (client.player != null) {
//                left = left.lerp(ElytraMath.getAssumedLeft(client.player.getYaw()), landingLerp);
//            }
//
//        } TODO do something with this

        matrix.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(((RollCamera) camera).doABarrelRoll$getRoll()));
    }
}

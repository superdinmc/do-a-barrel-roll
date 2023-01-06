package nl.enjarai.doabarrelroll.fabric.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;
import nl.enjarai.doabarrelroll.fabric.data.Components;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin {

    private AbstractClientPlayerEntity player;
    private float tickDelta;

    @Inject(
            method = "setupTransforms(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/util/math/MatrixStack;FFF)V",
            at = @At("HEAD")
    )
    private void doABarrelRoll$captureOtherPlayer(AbstractClientPlayerEntity abstractClientPlayerEntity, MatrixStack matrixStack, float f, float g, float h, CallbackInfo ci) {
        player = abstractClientPlayerEntity;
        tickDelta = h;
    }

    @ModifyArg(
            method = "setupTransforms(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/util/math/MatrixStack;FFF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lnet/minecraft/util/math/Quaternion;)V",
                    ordinal = 1
            ),
            index = 0
    )
    private Quaternion doABarrelRoll$modifyOthersRoll(Quaternion original) {
        if (player != null && player instanceof OtherClientPlayerEntity) {
            var component = Components.ROLL.get(player);

            if (component.hasClient() && component.isFallFlying()) {
                var roll = component.getRoll(tickDelta);

                return Vec3f.POSITIVE_Y.getDegreesQuaternion((float) roll);
            }
        }

        return original;
    }
}

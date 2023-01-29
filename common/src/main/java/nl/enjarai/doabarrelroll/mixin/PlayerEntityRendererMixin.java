package nl.enjarai.doabarrelroll.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import nl.enjarai.doabarrelroll.DoABarrelRollClient;
import nl.enjarai.doabarrelroll.config.ModConfig;
import nl.enjarai.doabarrelroll.flight.ElytraMath;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin {

    private AbstractClientPlayerEntity player;

    @Inject(
            method = "setupTransforms(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/util/math/MatrixStack;FFF)V",
            at = @At("HEAD")
    )
    private void doABarrelRoll$capturePlayer(AbstractClientPlayerEntity abstractClientPlayerEntity, MatrixStack matrixStack, float f, float g, float h, CallbackInfo ci) {
        player = abstractClientPlayerEntity;
    }

    @ModifyArg(
            method = "setupTransforms(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/util/math/MatrixStack;FFF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lorg/joml/Quaternionf;)V",
                    ordinal = 1
            ),
            index = 0
    )
    private Quaternionf doABarrelRoll$modifyRoll(Quaternionf original) {
        if (
                !DoABarrelRollClient.isRolling() ||
                !(player instanceof ClientPlayerEntity) // ||
                // !player.equals(MinecraftClient.getInstance().getCameraEntity())
        ) return original;

        var roll = ElytraMath.getRoll(player.getYaw(), DoABarrelRollClient.left);

        return RotationAxis.POSITIVE_Y.rotationDegrees((float) roll);
    }
}

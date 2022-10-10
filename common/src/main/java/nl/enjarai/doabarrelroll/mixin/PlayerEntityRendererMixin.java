package nl.enjarai.doabarrelroll.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import nl.enjarai.doabarrelroll.DoABarrelRollClient;
import nl.enjarai.doabarrelroll.ElytraMath;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class PlayerEntityRendererMixin {

    private AbstractClientPlayer player;

    @Inject(
            method = "setupRotations(Lnet/minecraft/client/player/AbstractClientPlayer;Lcom/mojang/blaze3d/vertex/PoseStack;FFF)V",
            at = @At("HEAD")
    )
    private void doABarrelRoll$capturePlayer(AbstractClientPlayer abstractClientPlayerEntity, PoseStack matrixStack, float f, float g, float h, CallbackInfo ci) {
        player = abstractClientPlayerEntity;
    }

    @ModifyArg(
            method = "setupRotations(Lnet/minecraft/client/player/AbstractClientPlayer;Lcom/mojang/blaze3d/vertex/PoseStack;FFF)V",
            at = @At(
                    value = "INVOKE",
                    target = "mulPose",
                    ordinal = 1
            ),
            index = 0
    )
    private Quaternion doABarrelRoll$modifyRoll(Quaternion original) {
        if (!(player instanceof LocalPlayer)) return original;

        var roll = ElytraMath.getRoll(player.getYRot(), DoABarrelRollClient.left);

        return Vector3f.YP.rotationDegrees((float) roll);
    }
}

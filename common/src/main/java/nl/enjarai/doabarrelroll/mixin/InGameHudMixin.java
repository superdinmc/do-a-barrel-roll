package nl.enjarai.doabarrelroll.mixin;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import nl.enjarai.doabarrelroll.DoABarrelRollClient;
import nl.enjarai.doabarrelroll.util.StarFoxUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper {

    @Shadow private int scaledWidth;

    @Shadow private int scaledHeight;

    @Inject(
            method = "renderCrosshair",
            at = @At(value = "HEAD")
    )
    private void doABarrelRoll$renderCrosshairHead(MatrixStack matrices, CallbackInfo ci) {
        matrices.push();
        DoABarrelRollClient.onRenderCrosshair(matrices, scaledWidth, scaledHeight);
    }

    @Inject(
            method = "renderCrosshair",
            at = @At(value = "RETURN")
    )
    private void doABarrelRoll$renderCrosshairReturn(MatrixStack matrices, CallbackInfo ci) {
        matrices.pop();
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/math/MathHelper;lerp(FFF)F"
            )
    )
    private void doABarrelRoll$renderPeppy(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        StarFoxUtil.renderPeppy(matrices, tickDelta, scaledWidth, scaledHeight);
    }
}

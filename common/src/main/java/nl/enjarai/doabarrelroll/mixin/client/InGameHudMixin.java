package nl.enjarai.doabarrelroll.mixin.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import nl.enjarai.doabarrelroll.EventCallbacksClient;
import nl.enjarai.doabarrelroll.util.StarFoxUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Inject(
            method = "renderCrosshair",
            at = @At(
                    value = "HEAD"
            )
    )
    private void doABarrelRoll$captureTickDelta(DrawContext context, float tickDelta, CallbackInfo ci) {
        context.getMatrices().push();
        EventCallbacksClient.onRenderCrosshair(context, tickDelta, context.getScaledWindowWidth(), context.getScaledWindowHeight());
    }

    @Inject(
            method = "renderCrosshair",
            at = @At(
                    value = "RETURN"
            )
    )
    private void doABarrelRoll$renderCrosshairReturn(DrawContext context, float tickDelta, CallbackInfo ci) {
        context.getMatrices().pop();
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/LayeredDrawer;render(Lnet/minecraft/client/gui/DrawContext;F)V",
                    ordinal = 0
            )
    )
    private void doABarrelRoll$renderPeppy(DrawContext context, float tickDelta, CallbackInfo ci) {
        StarFoxUtil.renderPeppy(context, tickDelta, context.getScaledWindowWidth(), context.getScaledWindowHeight());
    }
}

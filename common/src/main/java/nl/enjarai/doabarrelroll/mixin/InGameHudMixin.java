package nl.enjarai.doabarrelroll.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import nl.enjarai.doabarrelroll.DoABarrelRollClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class InGameHudMixin extends GuiComponent {

    @Shadow private int screenWidth;

    @Shadow private int screenHeight;

    //use renderCrossHairEvent for forge?
    @Inject(
            method = "renderCrosshair",
            at = @At(value = "HEAD")
    )
    private void doABarrelRoll$renderCrosshairHead(PoseStack matrices, CallbackInfo ci) {
        matrices.pushPose();
        DoABarrelRollClient.onRenderCrosshair(matrices, screenWidth, screenHeight);
    }

    @Inject(
            method = "renderCrosshair",
            at = @At(value = "RETURN")
    )
    private void doABarrelRoll$renderCrosshairReturn(PoseStack matrices, CallbackInfo ci) {
        matrices.popPose();
    }
}

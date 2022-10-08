package nl.enjarai.doabarrelroll.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import nl.enjarai.doabarrelroll.DoABarrelRollClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

	@Shadow @Final private Minecraft minecraft;

	//Use render onRenderWorld event for forge?
	@Inject(at = @At("HEAD"), method = "renderLevel")
	public void doABarrelRoll$renderWorld(float tickDelta, long limitTime, PoseStack matrix, CallbackInfo ci) {
		DoABarrelRollClient.onWorldRender(minecraft, tickDelta, matrix);
	}
}

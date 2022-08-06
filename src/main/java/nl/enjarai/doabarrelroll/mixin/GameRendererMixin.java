package nl.enjarai.doabarrelroll.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import nl.enjarai.doabarrelroll.DoABarrelRollClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

	@Final @Shadow private MinecraftClient client;
	
	@Inject(at = @At("HEAD"), method = "renderWorld")
	public void renderWorld(float tickDelta, long limitTime, MatrixStack matrix, CallbackInfo ci) {
		DoABarrelRollClient.onWorldRender(client, tickDelta, limitTime, matrix);
	}
}

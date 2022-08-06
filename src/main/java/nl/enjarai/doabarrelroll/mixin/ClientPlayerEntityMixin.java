package nl.enjarai.doabarrelroll.mixin;

import com.mojang.authlib.GameProfile;
import nl.enjarai.doabarrelroll.DoABarrelRollClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.stat.StatHandler;
import nl.enjarai.doabarrelroll.ElytraMath;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {

	public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile, PlayerPublicKey publicKey) { super(world, profile, publicKey); }

	@Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/client/network/ClientPlayNetworkHandler;Lnet/minecraft/stat/StatHandler;Lnet/minecraft/client/recipebook/ClientRecipeBook;ZZ)V")
	public void init(MinecraftClient client, ClientWorld world, ClientPlayNetworkHandler networkHandler, StatHandler stats, ClientRecipeBook recipeBook, boolean lastSneaking, boolean lastSprinting, CallbackInfo ci) {
		DoABarrelRollClient.left = ElytraMath.getAssumedLeft(this.getYaw());
	}
}
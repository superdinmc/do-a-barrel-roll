package nl.enjarai.doabarrelroll.compat.controlify.mixin;

import dev.isxander.controlify.ingame.InGameInputHandler;
import nl.enjarai.doabarrelroll.DoABarrelRollClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameInputHandler.class)
public abstract class InGameInputHandlerMixin {
    @Inject(
            method = "processPlayerLook",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void doABarrelRoll$cancelDefaultLookHandling(CallbackInfo ci) {
        if (DoABarrelRollClient.isRolling()) {
            ci.cancel();
        }
    }
}

package nl.enjarai.doabarrelroll.mixin;

import net.minecraft.client.Mouse;
import net.minecraft.client.network.ClientPlayerEntity;
import nl.enjarai.doabarrelroll.DoABarrelRollClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Mouse.class)
public abstract class MouseMixin {

//    @Shadow @Final private SmoothUtil cursorXSmoother;
//    @Shadow @Final private SmoothUtil cursorYSmoother;
//
//
//    // force enable the smooth camera while flying
//    @Redirect(
//            method = "updateMouse",
//            at = @At(
//                    value = "FIELD",
//                    target = "Lnet/minecraft/client/option/GameOptions;smoothCameraEnabled:Z"
//            )
//    )
//    private boolean smoothCameraEnabled(GameOptions options) {
//        return options.smoothCameraEnabled || DoABarrelRollClient.shouldSmooth();
//    }
//
//
//    // use our own smoothing while in flight
//
//    //   X axis
//    @Redirect(
//            method = "updateMouse",
//            at = @At(
//                    value = "FIELD",
//                    target = "Lnet/minecraft/client/Mouse;cursorXSmoother:Lnet/minecraft/client/util/SmoothUtil;"
//            )
//    )
//    private SmoothUtil modXSmoother(Mouse thiz) {
//        return DoABarrelRollClient.shouldSmooth() ? DoABarrelRollClient.rollSmoother : cursorXSmoother;
//    }
//
//    //   Y axis
//    @Redirect(
//            method = "updateMouse",
//            at = @At(
//                    value = "FIELD",
//                    target = "Lnet/minecraft/client/Mouse;cursorYSmoother:Lnet/minecraft/client/util/SmoothUtil;"
//            )
//    )
//    private SmoothUtil modYSmoother(Mouse thiz) {
//        return DoABarrelRollClient.shouldSmooth() ? DoABarrelRollClient.pitchSmoother : cursorYSmoother;
//    }


    // redirect mouse handling to our own code
    @Redirect(
            method = "updateMouse",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerEntity;changeLookDirection(DD)V"
            )
    )
    private void changeLookDirection(ClientPlayerEntity player, double cursorDeltaX, double cursorDeltaY) {
        DoABarrelRollClient.updateMouse(player, cursorDeltaX, cursorDeltaY);
    }
}

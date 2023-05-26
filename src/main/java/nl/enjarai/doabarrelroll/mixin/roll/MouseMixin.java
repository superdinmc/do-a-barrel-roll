package nl.enjarai.doabarrelroll.mixin.roll;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Mouse;
import net.minecraft.client.network.ClientPlayerEntity;
import nl.enjarai.doabarrelroll.api.RollEntity;
import nl.enjarai.doabarrelroll.config.ModConfig;
import org.joml.Vector2d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Mouse.class)
public abstract class MouseMixin {
    @Unique
    private final Vector2d mouseTurnVec = new Vector2d();

    @WrapWithCondition(
            method = "updateMouse",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerEntity;changeLookDirection(DD)V"
            )
    )
    private boolean doABarrelRoll$changeLookDirection(ClientPlayerEntity player, double cursorDeltaX, double cursorDeltaY, @Local(index = 3) double mouseDelta) {
        var rollPlayer = (RollEntity) player;

        if (rollPlayer.doABarrelRoll$isRolling()) {

            if (ModConfig.INSTANCE.getMomentumBasedMouse()) {

                // add the mouse movement to the current vector and normalize if needed
                mouseTurnVec.add(new Vector2d(cursorDeltaX, cursorDeltaY).mul(1f / 300));
                if (mouseTurnVec.lengthSquared() > 1.0) {
                    mouseTurnVec.normalize();
                }

                // enlarge the vector and apply it to the camera
                var readyTurnVec = new Vector2d(mouseTurnVec).mul(1200 * (float) mouseDelta);
                rollPlayer.doABarrelRoll$changeElytraLook(readyTurnVec.y, readyTurnVec.x, 0, ModConfig.INSTANCE.getDesktopSensitivity(), mouseDelta);

            } else {

                // if we are not using a momentum based mouse, we can reset it and apply the values directly
                mouseTurnVec.zero();
                rollPlayer.doABarrelRoll$changeElytraLook(cursorDeltaY, cursorDeltaX, 0, ModConfig.INSTANCE.getDesktopSensitivity(), mouseDelta);
            }

            return false;
        }

        mouseTurnVec.zero();
        return true;
    }
}

package nl.enjarai.doabarrelroll.compat.midnightcontrols.mixin;

import eu.midnightdust.midnightcontrols.client.MidnightControlsConfig;
import eu.midnightdust.midnightcontrols.client.MidnightInput;
import eu.midnightdust.midnightcontrols.client.controller.ButtonBinding;
import net.minecraft.client.MinecraftClient;
import nl.enjarai.doabarrelroll.api.RollEntity;
import nl.enjarai.doabarrelroll.compat.midnightcontrols.MidnightControlsCompat;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static nl.enjarai.doabarrelroll.compat.midnightcontrols.MidnightControlsCompat.AXIS_VALUES;
import static org.lwjgl.glfw.GLFW.*;

@Pseudo
@Mixin(MidnightInput.class)
public abstract class MidnightInputMixin {

    @Unique
    private static MidnightControlsCompat compat;

    @Dynamic
    @Inject(
            method = "handleLook(Lnet/minecraft/client/MinecraftClient;IFI)V",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private void handleElytraLook(@NotNull MinecraftClient client, int axis, float value, int state, CallbackInfo ci) {
        if (compat == null)
            compat = new MidnightControlsCompat();
        if (client.player instanceof RollEntity rollEntity && rollEntity.doABarrelRoll$isRolling()) {
            ci.cancel();
        }
    }

    @Dynamic
    @Inject(
            method = "handleAxe(Lnet/minecraft/client/MinecraftClient;IFFI)V",
            at = @At(value = "HEAD")
    )
    private void handleAxe(@NotNull MinecraftClient client, int axis, float value, float absValue, int state, CallbackInfo ci) {
        if(client.currentScreen != null) {
            AXIS_VALUES.put(ButtonBinding.axisAsButton(axis, true), 0f);
            AXIS_VALUES.put(ButtonBinding.axisAsButton(axis, false), 0f);
            return;
        }

        double deadZone = (axis == GLFW_GAMEPAD_AXIS_LEFT_X || axis == GLFW_GAMEPAD_AXIS_LEFT_Y) ?
                MidnightControlsConfig.leftDeadZone : MidnightControlsConfig.rightDeadZone;
        float axisValue = absValue < deadZone ? 0.f : (float) (absValue - deadZone);
        axisValue /= (1.0 - deadZone);

        axisValue = (float) Math.min(axisValue / MidnightControlsConfig.getAxisMaxValue(axis), 1);
        AXIS_VALUES.put(ButtonBinding.axisAsButton(axis, true), value > 0 ? axisValue : 0f);
        AXIS_VALUES.put(ButtonBinding.axisAsButton(axis, false), value < 0 ? axisValue : 0f);
    }

}

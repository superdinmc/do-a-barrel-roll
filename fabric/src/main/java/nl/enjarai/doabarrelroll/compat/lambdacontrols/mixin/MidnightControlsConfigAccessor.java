package nl.enjarai.doabarrelroll.compat.lambdacontrols.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Pseudo
@Mixin(targets = "eu.midnightdust.midnightcontrols.client.MidnightControlsConfig")
public interface MidnightControlsConfigAccessor {

    @Accessor
    static double getRotationSpeed() {
        throw new AssertionError();
    }

    @Invoker
    static double callGetRightXAxisSign() {
        throw new AssertionError();
    }

    @Invoker
    static double callGetRightYAxisSign() {
        throw new AssertionError();
    }
}

package nl.enjarai.doabarrelroll.mixin;

import net.minecraft.entity.LivingEntity;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @ModifyVariable(
            method = "travel",
            at = @At("STORE"),
            name = "o"
    )
    private float doABarrelRoll$modifyKineticDamage(float original) {
        var damageType = DoABarrelRoll.CONFIG_HOLDER.instance.kineticDamage();

        return switch (damageType) {
            case VANILLA -> original;
            case HIGH_SPEED -> original - 2.0f;
            case NONE -> 0.0f;
            case INSTANT_KILL -> Float.MAX_VALUE;
        };
    }
}

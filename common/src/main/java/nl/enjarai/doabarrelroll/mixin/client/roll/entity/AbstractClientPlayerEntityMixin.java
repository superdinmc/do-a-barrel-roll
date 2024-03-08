package nl.enjarai.doabarrelroll.mixin.client.roll.entity;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import nl.enjarai.doabarrelroll.mixin.roll.entity.PlayerEntityMixin;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin extends PlayerEntityMixin {
    // Mixin to fill out inheritance tree and support subclass mixins
}

package nl.enjarai.doabarrelroll.mixin;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import nl.enjarai.doabarrelroll.DoABarrelRollClient;
import nl.enjarai.doabarrelroll.config.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }


    @ModifyArg(
            method = "travel",
            at = @At(
                    value = "INVOKE",
                    target = "setDeltaMovement",
                    ordinal = 6
            )
    )
    private Vec3 doABarrelRoll$wrapElytraVelocity(Vec3 original) {
        if (!(((LivingEntity) (Object) this) instanceof LocalPlayer) || !ModConfig.INSTANCE.enableThrust) return original;

        Vec3 rotation = getLookAngle();
        Vec3 velocity = getDeltaMovement();

        int particleDensity = (int) Mth.clamp(DoABarrelRollClient.throttle * 10, 0, 10);
        if (DoABarrelRollClient.throttle > 0.1 && level.getGameTime() % (11 - particleDensity) == 0) {
            var pPos = position().add(velocity.scale(0.5).reverse());
            level.addParticle(
                    ParticleTypes.CAMPFIRE_SIGNAL_SMOKE,
                    pPos.x(), pPos.y(), pPos.z(),
                    random.nextGaussian() * 0.05, -velocity.y * 0.5, random.nextGaussian() * 0.05
            );
        }

        return original.add(
                (rotation.x * 0.1 + (rotation.x * 1.5 - velocity.x) * 0.5) * DoABarrelRollClient.throttle,
                (rotation.y * 0.1 + (rotation.y * 1.5 - velocity.y) * 0.5) * DoABarrelRollClient.throttle,
                (rotation.z * 0.1 + (rotation.z * 1.5 - velocity.z) * 0.5) * DoABarrelRollClient.throttle
        );
    }
}

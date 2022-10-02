package nl.enjarai.doabarrelroll.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import nl.enjarai.doabarrelroll.DoABarrelRollClient;
import nl.enjarai.doabarrelroll.config.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyArg(
            method = "travel(Lnet/minecraft/util/math/Vec3d;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V",
                    ordinal = 6
            )
    )
    private Vec3d doABarrelRoll$wrapElytraVelocity(Vec3d original) {
        if (!(((LivingEntity) (Object) this) instanceof ClientPlayerEntity) || !ModConfig.INSTANCE.enableThrust) return original;

        Vec3d rotation = getRotationVector();
        Vec3d velocity = getVelocity();

        int particleDensity = (int) MathHelper.clamp(DoABarrelRollClient.throttle * 10, 0, 10);
        if (DoABarrelRollClient.throttle > 0.1 && world.getTime() % (11 - particleDensity) == 0) {
            var pPos = getPos().add(velocity.multiply(0.5).negate());
            world.addParticle(
                    ParticleTypes.CAMPFIRE_SIGNAL_SMOKE,
                    pPos.getX(), pPos.getY(), pPos.getZ(),
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

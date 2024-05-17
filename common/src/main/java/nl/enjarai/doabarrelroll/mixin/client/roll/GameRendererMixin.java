package nl.enjarai.doabarrelroll.mixin.client.roll;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import nl.enjarai.doabarrelroll.api.RollCamera;
import nl.enjarai.doabarrelroll.math.MagicNumbers;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = GameRenderer.class, priority = 900)
public abstract class GameRendererMixin {
    @Shadow @Final private Camera camera;

    @WrapOperation(
            method = "renderWorld",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/joml/Matrix4f;rotationXYZ(FFF)Lorg/joml/Matrix4f;",
                    ordinal = 0
            )
    )
    public Matrix4f doABarrelRoll$renderWorld(Matrix4f instance, float angleX, float angleY, float angleZ, Operation<Matrix4f> original) {
        var roll = ((RollCamera) camera).doABarrelRoll$getRoll() * MagicNumbers.TORAD;
        if (roll != 0) {
            return instance.rotateZ((float) roll)
                    .rotateX(angleX)
                    .rotateY(angleY);
        }
        return original.call(instance, angleX, angleY, angleZ);
    }
}

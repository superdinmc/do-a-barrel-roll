package nl.enjarai.doabarrelroll.mixin.client.roll;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import nl.enjarai.doabarrelroll.api.RollCamera;
import nl.enjarai.doabarrelroll.math.MagicNumbers;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow @Final private Camera camera;

    @ModifyExpressionValue(
            method = "renderWorld",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/joml/Matrix4f;rotationXYZ(FFF)Lorg/joml/Matrix4f;",
                    ordinal = 0
            )
    )
    public Matrix4f doABarrelRoll$renderWorld(Matrix4f original) {
        var roll = ((RollCamera) camera).doABarrelRoll$getRoll() * MagicNumbers.TORAD;
        var newMatrix = new Matrix4f();
        return newMatrix
                .rotateZ((float) roll)
                .mul(original);
    }
}

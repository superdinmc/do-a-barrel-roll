package nl.enjarai.doabarrelroll.mixin.client.roll;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import nl.enjarai.doabarrelroll.api.RollCamera;
import nl.enjarai.doabarrelroll.math.MagicNumbers;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow @Final private Camera camera;

    @Redirect(
            method = "renderWorld",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/joml/Matrix4f;rotationXYZ(FFF)Lorg/joml/Matrix4f;",
                    ordinal = 0
            )
    )
    public Matrix4f doABarrelRoll$renderWorld(Matrix4f instance, float angleX, float angleY, float angleZ) {
        return instance.rotateZ((float) (((RollCamera) camera).doABarrelRoll$getRoll() * MagicNumbers.TORAD))
                .rotateX(angleX)
                .rotateY(angleY);
    }
}

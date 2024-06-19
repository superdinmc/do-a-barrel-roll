package nl.enjarai.doabarrelroll.mixin.client.roll;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import nl.enjarai.doabarrelroll.api.RollCamera;
import nl.enjarai.doabarrelroll.math.MagicNumbers;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Debug(export = true)
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow @Final private Camera camera;

    @ModifyExpressionValue(
            method = "renderWorld",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/joml/Quaternionf;conjugate(Lorg/joml/Quaternionf;)Lorg/joml/Quaternionf;"
            )
    )
    public Quaternionf doABarrelRoll$renderWorld(Quaternionf original) {
        //TODO: RAI FIX MATH!!!!
        var roll = ((RollCamera) camera).doABarrelRoll$getRoll() * MagicNumbers.TORAD;
        return new Quaternionf()
                .rotateZ((float) roll)
                .mul(original);
    }
}

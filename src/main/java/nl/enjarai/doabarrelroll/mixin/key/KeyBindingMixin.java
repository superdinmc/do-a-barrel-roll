package nl.enjarai.doabarrelroll.mixin.key;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import nl.enjarai.doabarrelroll.impl.key.InputContextImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

@Mixin(KeyBinding.class)
public abstract class KeyBindingMixin {
    private static KeyBinding getContextKeyBinding(InputUtil.Key key) {
        for (var context : InputContextImpl.CONTEXTS) {
            if (context.isActive()) {
                var binding = context.getKeyBinding(key);

                if (binding != null) {
                    return binding;
                }
            }
        }

        return null;
    }

    @WrapOperation(
            method = "onKeyPressed",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"
            )
    )
    private static KeyBinding doABarrelRoll$applyKeybindContext(Map<InputUtil.Key, KeyBinding> map, InputUtil.Key key, Operation<KeyBinding> original) {
        var binding = getContextKeyBinding(key);
        if (binding != null) return binding;

        return original.call(key);
    }

    @WrapOperation(
            method = "setKeyPressed",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"
            )
    )
    private static KeyBinding doABarrelRoll$applyKeybindContext2(Map<InputUtil.Key, KeyBinding> map, InputUtil.Key key, Operation<KeyBinding> original) {
        var binding = getContextKeyBinding(key);
        if (binding != null) return binding;

        return original.call(key);
    }

    @WrapWithCondition(
            method = "updateKeysByCode",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"
            )
    )
    private static boolean doABarrelRoll$skipAddingContextualKeys(Map<InputUtil.Key, KeyBinding> map, InputUtil.Key key, KeyBinding keyBinding) {
        for (var context : InputContextImpl.CONTEXTS) {
            if (context.getKeyBindings().contains(keyBinding)) {
                return false;
            }
        }

        return true;
    }
}

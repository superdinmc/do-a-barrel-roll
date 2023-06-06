package nl.enjarai.doabarrelroll.mixin.key;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import nl.enjarai.doabarrelroll.impl.key.InputContextImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
    private static Object doABarrelRoll$applyKeybindContext(Map<InputUtil.Key, KeyBinding> map, Object key, Operation<KeyBinding> original) {
        var binding = getContextKeyBinding((InputUtil.Key) key);
        if (binding != null) return binding;

        return original.call(map, key);
    }

    @WrapOperation(
            method = "setKeyPressed",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"
            )
    )
    private static Object doABarrelRoll$applyKeybindContext2(Map<InputUtil.Key, KeyBinding> map, Object key, Operation<KeyBinding> original) {
        var binding = getContextKeyBinding((InputUtil.Key) key);
        if (binding != null) return binding;

        return original.call(map, key);
    }

    @Inject(
            method = "updateKeysByCode",
            at = @At("HEAD")
    )
    private static void doABarrelRoll$updateContextualKeys(CallbackInfo ci) {
        for (var context : InputContextImpl.CONTEXTS) {
            context.updateKeysByCode();
        }
    }

    @WrapWithCondition(
            method = "updateKeysByCode",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"
            )
    )
    private static boolean doABarrelRoll$skipAddingContextualKeys(Map<InputUtil.Key, KeyBinding> map, Object key, Object keyBinding) {
        for (var context : InputContextImpl.CONTEXTS) {
            if (context.getKeyBindings().contains((KeyBinding) keyBinding)) {
                return false;
            }
        }

        return true;
    }
}

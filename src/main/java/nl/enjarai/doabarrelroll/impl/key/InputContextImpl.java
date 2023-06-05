package nl.enjarai.doabarrelroll.impl.key;

import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import nl.enjarai.doabarrelroll.api.key.InputContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public final class InputContextImpl implements InputContext {
    public static final List<InputContext> CONTEXTS = new ReferenceArrayList<>();

    private final Identifier id;
    private final Supplier<Boolean> activeCondition;
    private final List<KeyBinding> keyBindings = new ReferenceArrayList<>();
    private final Map<InputUtil.Key, KeyBinding> bindingsByKey = new HashMap<>();

    public InputContextImpl(Identifier id, Supplier<Boolean> activeCondition) {
        this.id = id;
        this.activeCondition = activeCondition;
        CONTEXTS.add(this);
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public boolean isActive() {
        return activeCondition.get();
    }

    @Override
    public void addKeyBinding(KeyBinding keyBinding) {
        Objects.requireNonNull(keyBinding);
        keyBindings.add(keyBinding);
        bindingsByKey.put(keyBinding.boundKey, keyBinding);
    }

    @Override
    public void removeKeyBinding(KeyBinding keyBinding) {
        Objects.requireNonNull(keyBinding);
        keyBindings.remove(keyBinding);
        bindingsByKey.remove(keyBinding.boundKey);
    }

    @Override
    public List<KeyBinding> getKeyBindings() {
        return keyBindings;
    }

    @Override
    public KeyBinding getKeyBinding(InputUtil.Key key) {
        return bindingsByKey.get(key);
    }
}

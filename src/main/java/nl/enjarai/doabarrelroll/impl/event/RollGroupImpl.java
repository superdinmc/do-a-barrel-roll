package nl.enjarai.doabarrelroll.impl.event;

import net.minecraft.util.Identifier;
import nl.enjarai.doabarrelroll.api.event.BooleanFlow;
import nl.enjarai.doabarrelroll.api.event.RollEvents;
import nl.enjarai.doabarrelroll.api.event.RollGroup;

import java.util.HashMap;
import java.util.function.Supplier;

public class RollGroupImpl extends EventImpl<RollGroup.RollCondition> implements RollGroup {
    public static final HashMap<Identifier, RollGroup> instances = new HashMap<>();

    public RollGroupImpl() {
        RollEvents.SHOULD_ROLL_CHECK.register(this::get);
    }

    @Override
    public void trueIf(Supplier<Boolean> condition, int priority) {
        register(() -> condition.get() ? BooleanFlow.TRUE : BooleanFlow.PASS, priority);
    }

    @Override
    public void trueIf(Supplier<Boolean> condition) {
        trueIf(condition, 0);
    }

    @Override
    public void falseUnless(Supplier<Boolean> condition, int priority) {
        register(() -> condition.get() ? BooleanFlow.PASS : BooleanFlow.FALSE, priority);
    }

    @Override
    public void falseUnless(Supplier<Boolean> condition) {
        falseUnless(condition, 0);
    }

    @Override
    public Boolean get() {
        for (var condition : getListeners()) {
            var result = condition.shouldRoll();
            if (result != BooleanFlow.PASS) {
                return result == BooleanFlow.TRUE;
            }
        }
        return false;
    }
}

package nl.enjarai.doabarrelroll.api.event;

import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.function.Supplier;

/**
 * A group of conditions that determine whether the camera should be rolling and what effects should be applied.
 * Instances can be directly used as conditions for registered events.
 *
 * <p>Usually, you'll want to use {@link RollGroup#of(Identifier)} to get an instance of this class.
 * You can then use {@link RollGroup#trueIf(Supplier)}, {@link RollGroup#falseUnless(Supplier)} or
 * {@link RollGroup#register(RollCondition)} to add conditions.
 *
 * <p>It is usually recommended to store your instance in a static final field, so you can easily use it in your
 * event registrations.
 *
 * <p>A basic example:
 * <pre>{@code
 * class ModClass {
 *     public static final RollGroup ROLL_GROUP = RollGroup.of(new Identifier("my_mod", "my_roll_group"));
 *
 *     public static void onInitialize() {
 *         ROLL_GROUP.trueIf(() -> yourCondition);
 *
 *         RollEvents.EARLY_CAMERA_MODIFIERS.register((delta, current) -> delta
 *                 .useModifier(Your::modifier),
 *                 10, ROLL_GROUP);
 *     }
 * }
 * }</pre>
 *
 * <p>The conditions are checked in order of priority, and the first condition that returns {@link BooleanFlow#TRUE}
 * will cause the camera to roll. If no condition returns {@link BooleanFlow#TRUE}, the camera will not roll.
 * If a condition returns {@link BooleanFlow#FALSE}, the camera will not roll and no further conditions will be
 * checked. If a condition returns {@link BooleanFlow#PASS}, the next condition will be checked.
 */
public class RollGroup extends Event<RollGroup.RollCondition> implements Supplier<Boolean> {
    private static final HashMap<Identifier, RollGroup> instances = new HashMap<>();

    /**
     * Gets the RollGroup instance for the given identifier.
     * If no instance exists, a new one is created and registered to {@link RollEvents#SHOULD_ROLL_CHECK}
     */
    public static RollGroup of(Identifier id) {
        return instances.computeIfAbsent(id, id2 -> new RollGroup());
    }

    private RollGroup() {
        RollEvents.SHOULD_ROLL_CHECK.register(this::get);
    }

    public void trueIf(Supplier<Boolean> condition, int priority) {
        register(() -> condition.get() ? BooleanFlow.TRUE : BooleanFlow.PASS, priority);
    }

    public void trueIf(Supplier<Boolean> condition) {
        trueIf(condition, 0);
    }

    public void falseUnless(Supplier<Boolean> condition, int priority) {
        register(() -> condition.get() ? BooleanFlow.PASS : BooleanFlow.FALSE, priority);
    }

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

    interface RollCondition {
        BooleanFlow shouldRoll();
    }
}

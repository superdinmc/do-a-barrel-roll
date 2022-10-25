package nl.enjarai.doabarrelroll.moonlightconfigs.forge;

import net.minecraft.util.Identifier;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import nl.enjarai.doabarrelroll.moonlightconfigs.ConfigBuilder;
import nl.enjarai.doabarrelroll.moonlightconfigs.ConfigType;
import nl.enjarai.doabarrelroll.util.CombinedValue;
import nl.enjarai.doabarrelroll.util.Value;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ConfigBuilderImpl extends ConfigBuilder {

    public static ConfigBuilder create(Identifier name, ConfigType type) {
        return new ConfigBuilderImpl(name, type);
    }

    private final ForgeConfigSpec.Builder builder;

    private String cat = null;

    public ConfigBuilderImpl(Identifier name, ConfigType type) {
        super(name, type);
        this.builder = new ForgeConfigSpec.Builder();
    }

    @Override
    protected String currentCategory() {
        return cat;
    }


    @Override
    public ConfigSpecWrapper build() {
        return new ConfigSpecWrapper(this.getName(), this.builder.build(), this.type, this.synced, this.changeCallback);
    }

    @Override
    public ConfigBuilderImpl push(String category) {
        assert cat == null;
        builder.push(category);
        cat = category;
        return this;
    }

    @Override
    public ConfigBuilderImpl pop() {
        assert cat != null;
        builder.pop();
        cat = null;
        return this;
    }

    @Override
    public Value<Boolean> define(String name, boolean defaultValue) {
        maybeAddTranslationString(name);
        var value = builder.define(name, defaultValue);
        return new CombinedValue<>(value::set, value);
    }

    @Override
    public Value<Double> define(String name, double defaultValue, double min, double max) {
        maybeAddTranslationString(name);
        var value = builder.defineInRange(name, defaultValue, min, max);
        return new CombinedValue<>(value::set, value);
    }

    @Override
    public Value<Integer> define(String name, int defaultValue, int min, int max) {
        maybeAddTranslationString(name);
        var value = builder.defineInRange(name, defaultValue, min, max);
        return new CombinedValue<>(value::set, value);
    }

    @Override
    public Value<Integer> defineColor(String name, int defaultValue) {
        maybeAddTranslationString(name);
        var stringConfig = builder.define(name, Integer.toHexString(defaultValue), ConfigBuilder.COLOR_CHECK);
        return new CombinedValue<>(
                (value) -> stringConfig.set(Integer.toHexString(value)),
                () -> Integer.parseUnsignedInt(stringConfig.get().replace("0x", ""), 16)
        );
    }

    @Override
    public Value<String> define(String name, String defaultValue, Predicate<Object> validator) {
        maybeAddTranslationString(name);
        ForgeConfigSpec.ConfigValue<String> stringConfig = builder.define(name, defaultValue, validator);
        return new CombinedValue<>(stringConfig::set, stringConfig);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends String> Value<List<String>> define(String name, List<? extends T> defaultValue, Predicate<Object> predicate) {
        maybeAddTranslationString(name);
        var value = builder.defineList(name, defaultValue, predicate);
        return new CombinedValue<>((val) -> {
            throw new UnsupportedOperationException("Forge is bad");
        }, () -> (List<String>) value.get());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Value<List<? extends T>> defineForgeList(String name, List<? extends T> defaultValue, Predicate<Object> predicate) {
        maybeAddTranslationString(name);
        var value = builder.defineList(name, defaultValue, predicate);
        return new CombinedValue<>((val) -> {
            throw new UnsupportedOperationException("Forge is bad");
        }, (Supplier<List<? extends T>>) value);
    }

    @Override
    public <V extends Enum<V>> Value<V> define(String name, V defaultValue) {
        maybeAddTranslationString(name);
        var value = builder.defineEnum(name, defaultValue);
        return new CombinedValue<>(value::set, value);
    }

    @Override
    public ConfigBuilder comment(String comment) {
        builder.comment(comment); //.translationKey(getTranslationName());
        //TODO: choose. either add a translation or a comment literal not both
        return super.comment(comment);
    }
}

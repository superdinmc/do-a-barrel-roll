package nl.enjarai.doabarrelroll.moonlightconfigs.fabric.values;

import net.minecraft.text.TranslatableText;
import nl.enjarai.doabarrelroll.moonlightconfigs.fabric.ConfigEntry;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Supplier;
import net.minecraft.text.Text;

public abstract class ConfigValue<T> extends ConfigEntry implements Supplier<T> {

    protected final T defaultValue;
    protected T value;
    private String descriptionKey;

    public ConfigValue(String name, T defaultValue) {
        super(name);
        this.defaultValue = defaultValue;
        Objects.requireNonNull(defaultValue, "default value cant be null");
        assert this.isValid(defaultValue) : "default value is invalid";
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public abstract boolean isValid(T value);

    public void set(T newValue) {
        this.value = newValue;
    }

    @Override
    public T get() {
        return value;
    }

    public void setDescriptionKey(String descriptionKey) {
        this.descriptionKey = descriptionKey;
    }

    @Nullable
    public Text getDescription() {
        if (this.descriptionKey == null) return null;
        return new TranslatableText(descriptionKey);
    }


}

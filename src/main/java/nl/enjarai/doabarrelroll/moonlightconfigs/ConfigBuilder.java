package nl.enjarai.doabarrelroll.moonlightconfigs;

import dev.architectury.injectables.annotations.ExpectPlatform;
import nl.enjarai.doabarrelroll.util.Value;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * Author: MehVahdJukaar. These come from Moonlgith Lib and are used here in a simpler manner
 * A loader independent config builder
 * Support common config syncing
 */
public abstract class ConfigBuilder {

    public static final Logger LOGGER = LogManager.getLogger();
    protected final Map<String, String> comments = new HashMap<>();
    private String currentComment;
    private String currentKey;
    protected boolean currentlyDescription;
    protected boolean synced;
    protected Runnable changeCallback;

    @ExpectPlatform
    public static ConfigBuilder create(Identifier name, ConfigType type) {
        throw new AssertionError();
    }

    private final Identifier name;
    protected final ConfigType type;

    public ConfigBuilder(Identifier name, ConfigType type) {
        this.name = name;
        this.type = type;
        //Consumer<AfterLanguageLoadEvent> consumer = e -> {
        //    if (e.isDefault()) comments.forEach(e::addEntry);
        //};
        //MoonlightEventsHelper.addListener(consumer, AfterLanguageLoadEvent.class);
    }

    public ConfigSpec buildAndRegister() {
        var spec = this.build();
        spec.register();
        return spec;
    }

    public abstract ConfigSpec build();

    public Identifier getName() {
        return name;
    }


    public abstract ConfigBuilder push(String category);

    public abstract ConfigBuilder pop();

    public abstract Value<Boolean> define(String name, boolean defaultValue);

    public abstract Value<Double> define(String name, double defaultValue, double min, double max);

    public abstract Value<Integer> define(String name, int defaultValue, int min, int max);

    public abstract Value<Integer> defineColor(String name, int defaultValue);

    public abstract Value<String> define(String name, String defaultValue, Predicate<Object> validator);

    public Value<String> define(String name, String defaultValue) {
        return define(name, defaultValue, STRING_CHECK);
    }

    public <T extends String> Value<List<String>> define(String name, List<? extends T> defaultValue) {
        return define(name, defaultValue, s -> true);
    }

    protected abstract String currentCategory();

    public abstract <T extends String> Value<List<String>> define(String name, List<? extends T> defaultValue, Predicate<Object> predicate);

    public abstract <V extends Enum<V>> Value<V> define(String name, V defaultValue);

    public abstract <T> Supplier<List<? extends T>> defineForgeList(String path, List<? extends T> defaultValue, Predicate<Object> elementValidator);

    public Text description(String name) {
        return Text.translatable(translationKey(name));
    }

    public Text tooltip(String name) {
        return Text.translatable(tooltipKey(name));
    }

    public String tooltipKey(String name) {
        return "config." + this.name.getNamespace() + "." + currentCategory() + "." + name + ".description";
    }

    public String translationKey(String name) {
        return "config." + this.name.getNamespace() + "." + currentCategory() + "." + name;
    }

    public String categoryTranslationKey(String name) {
        return "config." + this.name.getNamespace() + "." + name;
    }


    /**
     * Try not to use this. Just here to make porting easier
     * Will add entries manually to the english language file
     */
    public ConfigBuilder comment(String comment) {
        this.currentComment = comment;
        if (this.currentComment != null && this.currentKey != null) {
            comments.put(currentKey, currentComment);
            this.currentComment = null;
            this.currentKey = null;
        }
        return this;
    }

    public ConfigBuilder withDescription() {
        currentlyDescription = true;
        return this;
    }

    /**
     * Not Yet Implemented here
     */
    public ConfigBuilder setSynced() {
        if (this.type == ConfigType.CLIENT) {
            throw new UnsupportedOperationException("Config syncing cannot be used for client config as its not needed");
        }
        this.synced = true;
        return this;
    }

    public ConfigBuilder onChange(Runnable callback) {
        this.changeCallback = callback;
        return this;
    }

    protected void maybeAddTranslationString(String name) {
        this.currentKey = this.tooltipKey(name);
        if (this.currentComment != null && this.currentKey != null) {
            comments.put(currentKey, currentComment);
            this.currentComment = null;
            this.currentKey = null;
        }
    }

    public static final Predicate<Object> STRING_CHECK = o -> o instanceof String;

    public static final Predicate<Object> LIST_STRING_CHECK = (s) -> {
        if (s instanceof List<?>) {
            return ((Collection<?>) s).stream().allMatch(o -> o instanceof String);
        }
        return false;
    };

    public static final Predicate<Object> COLOR_CHECK = s -> {
        try {
            Integer.parseUnsignedInt(((String) s).replace("0x", ""), 16);
            return true;
        } catch (Exception e) {
            return false;
        }
    };

    public static String getReadableName(String name) {
        return Arrays.stream((name).replace(":", "_").split("_"))
                .map(StringUtils::capitalize).collect(Collectors.joining(" "));
    }
}

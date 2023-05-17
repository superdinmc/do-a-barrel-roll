package nl.enjarai.doabarrelroll.config;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class ConfigBuilder {
    private final Stack<Category> catStack = new Stack<>();

    public ConfigBuilder() {
        catStack.push(new Category("root", new HashMap<>()));
    }

    public void push(String name) {
        catStack.push(new Category(name, new HashMap<>()));
    }


    private record Category(String name, HashMap<String, Option<?>> options) implements Option<Category> {
        @Override
        public Codec<Category> codec() {
            return options.entrySet().stream()
                    .map(entry -> {
                        var key = entry.getKey();
                        var option = entry.getValue();
                        return option.codec().optionalFieldOf(option.name());
                    })
                    .reduce(Codec::either);
        }

        public Option<?> getOption(String name) {
            return options.get(name);
        }
    }

    private interface Option<T> {
        String name();

        Codec<T> codec();
    }
}

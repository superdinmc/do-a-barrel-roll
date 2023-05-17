package nl.enjarai.doabarrelroll.config.builder;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Stack;

public class ConfigBuilder {
    private final Stack<Category> catStack = new Stack<>();
    private final Gson gson = new Gson();

    public ConfigBuilder() {
        catStack.push(new Category("root", new HashMap<>()));
    }

    public void push(String name) {
        Category newCat = new Category(name, new HashMap<>());
        catStack.peek().addElement(newCat);
        catStack.push(newCat);
    }

    public void pop() {
        catStack.pop();
    }

    public <T> Value<T> entry(String name, T defaultValue) {
        return new Value<>(name, defaultValue);
    }

    private record Category(String name, HashMap<String, Element> elements) implements Element {
        public Element getElement(String name) {
            return elements.get(name);
        }

        public void addElement(Element element) {
            elements.put(element.name(), element);
        }

        @Override
        public void encode(JsonObject parent) {
            var obj = new JsonObject();

            for (var entry : elements.entrySet()) {
                entry.getValue().encode(obj);
            }

            parent.add(name(), obj);
        }

        @Override
        public void decode(JsonObject parent) {
            var obj = parent.getAsJsonObject(name());

            for (var entry : elements.entrySet()) {
                entry.getValue().decode(obj);
            }
        }
    }

    public class NumberValue<T extends Number> extends Value<T> {
        public NumberValue(String name, T defaultValue) {
            super(name, defaultValue);
        }

        public NumberValue<T> withRange(T min, T max) {
            // TODO
            return this;
        }
    }

    public class Value<T> implements Element {
        private final String name;
        protected T value;
        private boolean hasDescription = false;

        public Value(String name, T defaultValue) {
            this.name = name;
            this.value = defaultValue;
        }

        public T get() {
            return value;
        }

        public void set(T value) {
            this.value = value;
        }

        @Override
        public void encode(JsonObject parent) {
            parent.add(name(), gson.toJsonTree(value));
        }

        @SuppressWarnings("unchecked")
        @Override
        public void decode(JsonObject parent) {
            var element = parent.get(name());
            value = (T) gson.fromJson(element, value.getClass());
        }

        @Override
        public String name() {
            return name;
        }

        public Value<T> withDescription() {
            hasDescription = true;
            return this;
        }
    }
}

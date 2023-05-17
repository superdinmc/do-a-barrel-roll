package nl.enjarai.doabarrelroll.util;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class CombinedValue<T> implements Value<T> {
    private final Consumer<T> consumer;
    private final Supplier<T> supplier;

    public CombinedValue(Consumer<T> consumer, Supplier<T> supplier) {
        this.consumer = consumer;
        this.supplier = supplier;
    }

    @Override
    public void accept(T t) {
        consumer.accept(t);
    }

    @Override
    public T get() {
        return supplier.get();
    }
}

package org.nield.concurrency;

public final class LazyObject<T> {

    private volatile T value;
    private final Supplier<T> supplier;

    private LazyObject(Supplier<T> supplier) {
        this.supplier = supplier;
    }
    public T get() {
        if (value == null) {
            synchronized(this) {
                if (value == null) {
                    value = supplier.get();
                }
            }
        }
        return value;
    }
    public void reset() {
        if (value != null) {
            synchronized(this) {
                if (value != null) {
                    this.value = null;
                }
            }
        }
    }
    public static <B> LazyObject<B> forSupplier(Supplier<B> supplier) {
        return new LazyObject<B>(supplier);
    }
}
package org.nield.concurrency;

import java.util.function.Supplier;

/**<html>A simple but powerful concurrency utility that simplifies lazy initialization. <br><br>Client simply provides a
 * supplier for the lazy-initialized value to the static factory method <b>forSupplier()</b><br><br>
 * A LazyObject will be returned by the static factory, and the first time <b>get()</b> is called, the value is initalized and cached in a concurrent/threadsafe manner.
 * </html>
 */

public final class LazyObject<T> {

    private volatile T value;
    private volatile boolean updated;
    private final Supplier<T> supplier;

    private LazyObject(Supplier<T> supplier) {
        this.supplier = supplier;
    }
    public T get() {
        if (!updated) {
            synchronized(this) {
                if (!updated) {
                    value = supplier.get();
                    updated = true;
                }
            }
        }
        return value;
    }
    public void reset() {
        if (updated) {
            synchronized(this) {
                if (updated) {
                    updated = false;
                    value = null;
                }
            }
        }
    }
    public static <B> LazyObject<B> forSupplier(Supplier<B> supplier) {
        return new LazyObject<B>(supplier);
    }
}

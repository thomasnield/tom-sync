package org.nield.concurrency;


import java.util.OptionalLong;
import java.util.function.LongSupplier;

/**<html>A simple but powerful concurrency utility that simplifies lazy initialization. <br><br>Client simply provides a
 * supplier for the lazy-initialized <b>int</b> value to the static factory method <b><i>forSupplier()</i></b><br><br>
 * A LazyObject will be returned by the static factory, and the first time <b>get()</b> is called, the <b>int</b> value is initialized and cached in a concurrent/threadsafe manner.
 * </html>
 */
public final class LazyLong {
    private final LongSupplier longSupplier;
    private volatile OptionalLong value = OptionalLong.empty();

    private LazyLong(LongSupplier longSupplier) {
        this.longSupplier = longSupplier;
    }

    public static LazyLong forSupplier(LongSupplier longSupplier) {
        return new LazyLong(longSupplier);
    }

    public long getAsLong() {
        if (! value.isPresent()) {
            synchronized(this) {
                if (! value.isPresent()) {
                    value = OptionalLong.of(longSupplier.getAsLong());
                }
            }
        }
        return value.getAsLong();
    }
    public void reset() {
        if (!value.isPresent()) {
            synchronized(this) {
                if (!value.isPresent()) {
                    this.value = OptionalLong.empty();
                }
            }
        }
    }
}
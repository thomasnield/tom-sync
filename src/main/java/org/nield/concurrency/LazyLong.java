package org.nield.concurrency;


import java.util.function.LongSupplier;

/**<html>A simple but powerful concurrency utility that simplifies lazy initialization. <br><br>Client simply provides a
 * supplier for the lazy-initialized <b>int</b> value to the static factory method <b><i>forSupplier()</i></b><br><br>
 * A LazyObject will be returned by the static factory, and the first time <b>get()</b> is called, the <b>int</b> value is initialized and cached in a concurrent/threadsafe manner.
 * </html>
 */
public final class LazyLong {
    private final LongSupplier longSupplier;
    private volatile boolean updated;
    private volatile long value;

    private LazyLong(LongSupplier longSupplier) {
        this.longSupplier = longSupplier;
    }

    public static LazyLong forSupplier(LongSupplier longSupplier) {
        return new LazyLong(longSupplier);
    }

    public long getAsLong() {
        if (!updated) {
            synchronized(this) {
                if (updated) {
                    value = longSupplier.getAsLong();
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
                }
            }
        }
    }
}
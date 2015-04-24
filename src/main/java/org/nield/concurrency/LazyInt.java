package org.nield.concurrency;

import java.util.function.IntSupplier;

/**<html>A simple but powerful concurrency utility that simplifies lazy initialization. <br><br>Client simply provides a
 * supplier for the lazy-initialized <b>int</b> value to the static factory method <b><i>forSupplier()</i></b><br><br>
 * A LazyObject will be returned by the static factory, and the first time <b>get()</b> is called, the <b>int</b> value is initialized and cached in a concurrent/threadsafe manner.
 * </html>
 */
public final class LazyInt {
    private final IntSupplier intSupplier;
    private volatile boolean updated;
    private volatile int value;

    private LazyInt(IntSupplier intSupplier) {
        this.intSupplier = intSupplier;
    }

    public static LazyInt forSupplier(IntSupplier intSupplier) {
        return new LazyInt(intSupplier);
    }

    public int getAsInt() {
        if (!updated) {
            synchronized(this) {
                if (!updated) {
                    value = intSupplier.getAsInt();
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

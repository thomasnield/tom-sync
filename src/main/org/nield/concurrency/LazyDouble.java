package org.nield.concurrency;


import java.util.function.DoubleSupplier;

/**<html>A simple but powerful concurrency utility that simplifies lazy initialization. <br><br>Client simply provides a
 * supplier for the lazy-initialized <b>double</b> value to the static factory method <b><i>forSupplier()</i></b><br><br>
 * A LazyObject will be returned by the static factory, and the first time <b>get()</b> is called, the <b>double</b> value is initialized and cached in a concurrent/threadsafe manner.
 * </html>
 */
public final class LazyDouble {
    private final DoubleSupplier doubleSupplier;
    private volatile boolean updated;
    private volatile double value;

    private LazyDouble(DoubleSupplier doubleSupplier) {
        this.doubleSupplier = doubleSupplier;
    }

    public static LazyDouble forSupplier(DoubleSupplier doubleSupplier) {
        return new LazyDouble(doubleSupplier);
    }

    public double getAsDouble() {
        if (! updated) {
            synchronized(this) {
                if (! updated) {
                    value = doubleSupplier.getAsDouble();
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

package org.nield.concurrency;


import java.util.OptionalDouble;
import java.util.function.DoubleSupplier;

/**<html>A simple but powerful concurrency utility that simplifies lazy initialization. <br><br>Client simply provides a
 * supplier for the lazy-initialized <b>double</b> value to the static factory method <b><i>forSupplier()</i></b><br><br>
 * A LazyObject will be returned by the static factory, and the first time <b>get()</b> is called, the <b>double</b> value is initialized and cached in a concurrent/threadsafe manner.
 * </html>
 */
public final class LazyDouble {
    private final DoubleSupplier doubleSupplier;
    private volatile OptionalDouble value = OptionalDouble.empty();

    private LazyDouble(DoubleSupplier doubleSupplier) {
        this.doubleSupplier = doubleSupplier;
    }

    public static LazyDouble forSupplier(DoubleSupplier doubleSupplier) {
        return new LazyDouble(doubleSupplier);
    }

    public double getAsDouble() {
        if (! value.isPresent()) {
            synchronized(this) {
                if (! value.isPresent()) {
                    value = OptionalDouble.of(doubleSupplier.getAsDouble());
                }
            }
        }
        return value.getAsDouble();
    }
    public void reset() {
        if (!value.isPresent()) {
            synchronized(this) {
                if (!value.isPresent()) {
                    this.value = OptionalDouble.empty();
                }
            }
        }
    }
}

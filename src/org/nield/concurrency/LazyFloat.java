package org.nield.concurrency;

/**<html>A simple but powerful concurrency utility that simplifies lazy initialization. <br><br>Client simply provides a
 * supplier for the lazy-initialized <b>float</b> value to the static factory method <b><i>forSupplier()</i></b><br><br>
 * A LazyObject will be returned by the static factory, and the first time <b>getAsFloat()</b> is called, the <b>float</b> value is initialized and cached in a concurrent/threadsafe manner.
 * </html>
 */
public final class LazyFloat {
    private final FloatSupplier floatSupplier;
    private volatile boolean updated = false;
    private volatile float value;

    private LazyFloat(FloatSupplier floatSupplier) {
        this.floatSupplier = floatSupplier;
    }

    public static LazyFloat forSupplier(FloatSupplier floatSupplier) {
        return new LazyFloat(floatSupplier);
    }

    public float getAsFloat() {
        if (! updated) {
            synchronized(this) {
                if (! updated) {
                    value = floatSupplier.getAsFloat();
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

    @FunctionalInterface
    public interface FloatSupplier {
        public float getAsFloat();
    }
}
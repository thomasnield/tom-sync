package org.nield.concurrency;

import java.util.function.BooleanSupplier;

/**<html>A simple but powerful concurrency utility that simplifies lazy initialization. <br><br>Client simply provides a
 * supplier for the lazy-initialized <b>boolean</b> value to the static factory method <b><i>forSupplier()</i></b><br><br>
 * A LazyObject will be returned by the static factory, and the first time <b>get()</b> is called, the <b>boolean</b> value is initialized and cached in a concurrent/threadsafe manner.
 * </html>
 */
public final class LazyBoolean {
    private final BooleanSupplier boolSupplier;
    private volatile boolean updated = false;
    private volatile boolean value;

    private LazyBoolean(BooleanSupplier booleanSupplier) {
        this.boolSupplier = booleanSupplier;
    }
    public boolean getAsBoolean() {
        if (! updated) {
            synchronized(this) {
                if (! updated) {
                    value = boolSupplier.getAsBoolean();
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

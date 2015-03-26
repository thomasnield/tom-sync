package org.nield.concurrency;

import com.sun.javafx.scene.control.behavior.OptionalBoolean;

import java.util.OptionalInt;
import java.util.function.BooleanSupplier;
import java.util.function.IntSupplier;

/**<html>A simple but powerful concurrency utility that simplifies lazy initialization. <br><br>Client simply provides a
 * supplier for the lazy-initialized <b>int</b> value to the static factory method <b><i>forSupplier()</i></b><br><br>
 * A LazyObject will be returned by the static factory, and the first time <b>get()</b> is called, the <b>int</b> value is initialized and cached in a concurrent/threadsafe manner.
 * </html>
 */
public final class LazyBoolean {
    private final BooleanSupplier boolSupplier;
    private volatile boolean updated = false;
    private volatile boolean value;

    private LazyBoolean(BooleanSupplier booleanSupplier) {
        this.boolSupplier = booleanSupplier;
    }
    public int getAsBoolean() {
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
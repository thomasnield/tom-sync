package org.nield.concurrency;

/**<html>A simple but powerful concurrency utility that simplifies lazy initialization. <br><br>Client simply provides a
 * supplier for the lazy-initialized <b>int</b> value to the static factory method <b><i>forSupplier()</i></b><br><br>
 * A LazyObject will be returned by the static factory, and the first time <b>get()</b> is called, the <b>int</b> value is initalized and cached in a concurrent/threadsafe manner.
 * </html>
 */
public final class LazyInt {
    private final IntSupplier intSupplier;
    private volatile OptionalInt value;

    private LazyInt(IntSupplier intSupplier) {
        this.intSupplier = intSupplier;
    }
    public int getAsInt() {
        if (! value.isPresent()) {
            synchronized(this) {
                if (! value.isPresent()) {
                    value = OptionalInt.of(intSupplier.getAsInt());
                }
            }
        }
        return value.getAsInt();
    }
    public void reset() {
        if (!value.isPresent()) {
            synchronized(this) {
                if (!value.isPresent()) {
                    this.value = null;
                }
            }
        }
    }
}

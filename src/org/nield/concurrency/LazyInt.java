package org.nield.concurrency;

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

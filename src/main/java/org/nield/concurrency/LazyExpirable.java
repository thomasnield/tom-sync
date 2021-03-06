package org.nield.concurrency;


import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * LazyExpirable is an augmentation of LazyObject. The only difference is the value is reset and garbage-collected after a specified period of no use
 */
public final class LazyExpirable<T> {
	
	private final LazyObject<T> value; 
	private final ScheduledThreadPoolExecutor executor;
	private final long expirationDelay;
	private final TimeUnit timeUnit;
	private final AtomicReference<ScheduledFuture<?>> scheduledRemoval = new AtomicReference<>();
	
	private LazyExpirable(Supplier<T> supplier, ScheduledThreadPoolExecutor executor, long expirationDelay, TimeUnit timeUnit) { 
		value = LazyObject.forSupplier(() -> supplier.get());
		this.expirationDelay = expirationDelay;
		this.executor = executor;
		this.timeUnit = timeUnit;
	}
	public static <T> LazyExpirable<T> forSupplier(Supplier<T> supplier, ScheduledThreadPoolExecutor executor, long expirationDelay, TimeUnit timeUnit) { 
		return new LazyExpirable<>(supplier, executor, expirationDelay, timeUnit);
	}
	public T get() { 
		
		final ScheduledFuture<?> next = executor.schedule(() -> value.reset(), expirationDelay, timeUnit);
		final ScheduledFuture<?> current = scheduledRemoval.getAndSet( next );
		
	    if (current != null) {
	            current.cancel(true);
	    }

	    T returnVal = value.get();

	    return returnVal;
	}
}

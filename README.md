Welcome to the tom-sync Java library. These are my concurrency tools I found a need for. I hope you find value in them too.

You will need Java 8 to use this library as it requires the functional interfaces and Optionals. 

So far there are only two core features... Lazy Initialization wrappers and the BufferedLatch. 

**BufferedLatch**

The `BufferedLatch` is a synchronizer much like a `CountDownLatch`, but it is used for situations where the "count" is not known until later. It positively increments two `int` counts, the `leadCount` and the `chaseCount`. The `leadCount` is the leader and incremented by calling `incrementLeadCount()`, and the `chaseCount` chases it and incremented by `incrementChaseCount()` When both counts are equal and `setLeaderComplete()` is called, anything waiting on the `BufferedLatch` is notified. 

A typical use of `BufferedLatch` is to iterate a `ResultSet`, process each one asynchronously (be careful to extract the data first to prevent race conditions on the `ResultSet`!), and when the iteration is complete wait for all the tasks to finish. 

Think of it as a fork-join on an unknown number of asynchronous tasks.

```
ResultSet rs = ...;
ExecutorService service = ...;
BufferedLatch bufferedLatch = new BufferedLatch();

while (rs.next()) { 
    bufferedLatch.incrementLeadCount();
    final String reportCode = rs.getString("REPORT_CODE");
    
    service.execute(() -> {
        processReport(reportCode);
        bufferedLatch.incrementChaseCount();
    });
}
bufferedLatch.setLeaderComplete();
bufferedLatch.await();
```

**Lazy Initialization**

The Lazy Initialization wrappers streamline the task of deferring the calculation of a value, and doing so in a threadsafe manner.

`LazyObject<BigDecimal> balance = LazyObject.forSupplier(() -> calculateBalance(financeDate));`

When `balance` has the `get()` method first called, it will calculate and cache the value. After the value is cached, it avoids unnecessary synchronization to improve concurrency. 

`public BigDecimal getBalance() { 
    return balance.get();
}`

There are different primitive flavors of `LazyObject` as well. These will store a lazy primitive instead of an object much like how the `Optional` has as `OptionalInt`, `OptionalLong`, and `OptionalDouble` counterparts.

`LazyInt`

`LazyLong`

`LazyFloat`

`LazyBoolean`

`LazyDouble`



**Lazy Expirable**

There is also a `LazyExpirable` which behaves identically to a `LazyObject`, but the cached value will expire after a specified time period of no use. It uses a `Supplier<T>`, a `ScheduledThreadPoolExecutor` and time interval specified by the client. When `get()` is first called, the object will be created and cached. But if `get()` is not called again for the specified time period, it will expire and clear the cache, causing the next call to `get()` to rebuild the object. However, if `get()` is called before the value expires, the countdown to expiration will restart. 

The static factory signature is

`public static <T> LazyExpirable<T> forSupplier(Supplier<T> supplier, ScheduledThreadPoolExecutor executor, long expirationDelay, TimeUnit timeUnit)`

Therefore, to create a `MarketEngine` type that caches but expires after 5 minutes of no use, call this code. 

`LazyExpirable<MarketEngine> marketEngine = LazyExpirable.forSupplier(() -> MarketEngine.create(), scheduledExecutor, 5L, TimeUnit.MINUTES);`



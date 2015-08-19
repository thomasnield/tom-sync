Welcome to the tom-sync Java library. These are my concurrency tools I personally found a need for, and I hope you find value in them too.

You will need Java 8 to use this library as it requires the functional interfaces and Optionals. 

So far there are only two core features... Lazy Initialization wrappers and the BufferedLatch. 


**Lazy Initialization**

The Lazy Initialization wrappers streamline the task of deferring the calculation of a value, and doing so in a threadsafe manner.

`LazyObject<BigDecimal> balance = LazyObject.forSupplier(() -> calculateBalance(financeDate));`

When `balance` has the `get()` method first called, it will calculate and cache the value. After the value is cached, it avoids unnecessary synchronization to improve concurrency. 

`public BigDecimal getBalance() { 
    return balance.get();
}`

There are also variants for the 

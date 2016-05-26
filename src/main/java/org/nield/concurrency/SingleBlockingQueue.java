import java.util.concurrent.Semaphore;


public final class SingleBlockingQueue<T> {
    private volatile T value;
    private final Semaphore full = new Semaphore(0);
    private final Semaphore empty = new Semaphore(1);
    private volatile boolean isDone = false;

    private volatile boolean hasValue = false;

    public void offer(T value) throws InterruptedException {
        empty.acquire();
        this.value = value;
        hasValue = true;
        full.release();
    }

    public T take() throws InterruptedException {
        full.acquire();
        T returnValue = value;
        value = null; // Should release reference
        hasValue = false;
        empty.release();
        return returnValue;
    }
    public void setDone() {
        isDone = true;
    }
    public boolean isDone() {
        return isDone;
    }
    public boolean hasValue() {
        return hasValue || !isDone;
    }
}

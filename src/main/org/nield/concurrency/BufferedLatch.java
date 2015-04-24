package org.nield.concurrency;

/**<html>A synchronizer simliar to CountDownLatch, except it is used for situations where the countdown number is not known until later.<br><br>
 *This makes it ideal for mulitthreading record-buffering tasks, where you want to kick off a concurrent task for each record but do not know how many records there will be</html>
 */
public final class BufferedLatch {
    private int leadCount;
    private int chaseCount;
    private boolean leaderComplete = false;

    public synchronized void incrementLeadCount() {
        if (leaderComplete) {
            throw new RuntimeException("Cannot increment record count after iteration is flagged complete!");
        }
        else {
            leadCount++;
        }
    }
    public synchronized void incrementChaseCount() {
        chaseCount++;
        if (leaderComplete && leadCount == chaseCount) {
            this.notifyAll();
        }
    }
    public synchronized void setLeaderComplete() {
        leaderComplete = true;
        if (leadCount == chaseCount) {
            this.notifyAll();
        }
    }
    public void await() throws InterruptedException {
        while (! (leaderComplete && leadCount == chaseCount)) {
            this.wait();
        }
    }
}

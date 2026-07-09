package com.eclipse.domain.common;

import com.eclipse.domain.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class ConcurrencyUtils {

    private ConcurrencyUtils() {
    }

    public static List<String> demonstrateLifecycle() throws InterruptedException {
        List<String> log = new ArrayList<>();
        Object monitor = new Object();

        Thread worker = new Thread(() -> {
            synchronized (monitor) {
                try {
                    monitor.wait(60);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "lifecycle-demo-thread");

        log.add("NEW: " + worker.getState());

        synchronized (monitor) {
            worker.start();
            Thread.sleep(20);
            log.add("BLOCKED (waiting to enter synchronized block): " + worker.getState());
        }

        Thread.sleep(20);
        log.add("TIMED_WAITING (inside wait(60)): " + worker.getState());

        worker.join();
        log.add("TERMINATED: " + worker.getState());

        return log;
    }

    public static Thread runAsRunnable(String threadName, Runnable action) {
        Thread thread = new Thread(action, threadName);
        thread.start();
        return thread;
    }

    public static Task loadTaskAsCallable(Callable<Task> callable) throws Exception {
        return callable.call();
    }

    public static Thread namedDaemonThread(String threadName, Runnable action) {
        Thread thread = new Thread(action, threadName);
        thread.setDaemon(true);
        thread.start();
        return thread;
    }

    public static class StopFlag {
        private volatile boolean stopRequested = false;

        public void requestStop() {
            stopRequested = true;
        }

        public boolean isStopRequested() {
            return stopRequested;
        }
    }

    public static Thread runUntilStopped(StopFlag flag, Runnable action) {
        Thread worker = new Thread(() -> {
            while (!flag.isStopRequested()) {
                action.run();
            }
        }, "stoppable-worker");
        worker.start();
        return worker;
    }

    public static class SynchronizedCounter {
        private int count = 0;

        public synchronized void increment() {
            count++;
        }

        public synchronized int get() {
            return count;
        }
    }

    public static class LockedCounter {
        private final ReentrantLock lock = new ReentrantLock();
        private int count = 0;

        public void increment() {
            lock.lock();
            try {
                count++;
            } finally {
                lock.unlock();
            }
        }

        public int get() {
            lock.lock();
            try {
                return count;
            } finally {
                lock.unlock();
            }
        }
    }

    public static class ReadWriteTaskCache {
        private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
        private final Map<UUID, Task> cache = new HashMap<>();

        public void put(Task task) {
            rwLock.writeLock().lock();
            try {
                cache.put(task.getId(), task);
            } finally {
                rwLock.writeLock().unlock();
            }
        }

        public Task get(UUID id) {
            rwLock.readLock().lock();
            try {
                return cache.get(id);
            } finally {
                rwLock.readLock().unlock();
            }
        }

        public int size() {
            rwLock.readLock().lock();
            try {
                return cache.size();
            } finally {
                rwLock.readLock().unlock();
            }
        }
    }

    public static List<String> processWithFixedPool(List<Task> tasks, int poolSize)
            throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(poolSize);
        try {
            List<Future<String>> futures = new ArrayList<>();
            for (Task task : tasks) {
                futures.add(executor.submit(() -> "Processed: " + task.getTitle()));
            }
            List<String> results = new ArrayList<>();
            for (Future<String> future : futures) {
                try {
                    results.add(future.get());
                } catch (ExecutionException e) {
                    results.add("Failed: " + e.getCause().getMessage());
                }
            }
            return results;
        } finally {
            executor.shutdown();
        }
    }

    public static void submitBurstToCachedPool(List<Runnable> jobs) throws InterruptedException {
        ExecutorService executor = Executors.newCachedThreadPool();
        try {
            for (Runnable job : jobs) {
                executor.submit(job);
            }
        } finally {
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    public static ScheduledFuture<?> scheduleRecurring(ScheduledExecutorService scheduler,
                                                         Runnable job,
                                                         long initialDelayMillis,
                                                         long periodMillis) {
        return scheduler.scheduleAtFixedRate(job, initialDelayMillis, periodMillis, TimeUnit.MILLISECONDS);
    }
}
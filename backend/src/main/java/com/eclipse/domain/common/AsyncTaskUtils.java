package com.eclipse.domain.common;

import com.eclipse.domain.task.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.StructuredTaskScope;
import java.util.function.Supplier;

public final class AsyncTaskUtils {

    private AsyncTaskUtils() {
    }

    public static String demonstrateFutureLimitation(ExecutorService executor, Task task)
            throws ExecutionException, InterruptedException {
        Future<String> future = executor.submit(() -> "Loaded: " + task.getTitle());
        return future.get();
    }

    public static CompletableFuture<Task> loadTaskAsync(Task task) {
        return CompletableFuture.supplyAsync(() -> {
            return task;
        });
    }

    public static CompletableFuture<String> loadTaskTitleAsync(Task task) {
        return loadTaskAsync(task).thenApply(Task::getTitle);
    }

    public static CompletableFuture<Void> logTaskAsync(Task task) {
        return loadTaskAsync(task)
                .thenAccept(loaded -> System.out.println("Loaded task: " + loaded.getTitle()));
    }

    public static CompletableFuture<String> loadTaskThenSummarize(Task task) {
        return loadTaskAsync(task)
                .thenCompose(loaded -> CompletableFuture.supplyAsync(
                        () -> loaded.getTitle() + " [" + loaded.getPriority() + "]"));
    }

    public static List<Task> loadAllTasksAsync(List<Task> tasks) {
        List<CompletableFuture<Task>> futures = tasks.stream()
                .map(AsyncTaskUtils::loadTaskAsync)
                .toList();
        CompletableFuture<Void> all =
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        return all.thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .toList())
                .join();
    }

    public static Object firstToRespond(CompletableFuture<?>... futures) {
        return CompletableFuture.anyOf(futures).join();
    }

    public static CompletableFuture<String> loadTitleWithFallback(Task task, String fallback) {
        return CompletableFuture.supplyAsync(() -> {
                    if (task == null) {
                        throw new IllegalArgumentException("task must not be null");
                    }
                    return task.getTitle();
                })
                .exceptionally(ex -> fallback);
    }

    public static Thread runOnVirtualThread(String name, Runnable action) {
        return Thread.ofVirtual().name(name).start(action);
    }

    public static List<String> processWithVirtualThreads(List<Task> tasks) throws InterruptedException {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<String>> futures = tasks.stream()
                    .map(task -> executor.submit(() -> "Processed: " + task.getTitle()))
                    .toList();
            List<String> results = new ArrayList<>();
            for (Future<String> future : futures) {
                try {
                    results.add(future.get());
                } catch (ExecutionException e) {
                    results.add("Failed: " + e.getCause().getMessage());
                }
            }
            return results;
        }
    }

    public static String loadTaskAndAssigneeName(Task task, Supplier<String> assigneeNameLookup)
            throws InterruptedException, ExecutionException {
        try (var scope = StructuredTaskScope.open(
                StructuredTaskScope.Joiner.<String>awaitAllSuccessfulOrThrow())) {

            StructuredTaskScope.Subtask<String> titleTask = scope.fork(task::getTitle);
            StructuredTaskScope.Subtask<String> assigneeTask = scope.fork(assigneeNameLookup::get);

            scope.join();

            return titleTask.get() + " (assignee: " + assigneeTask.get() + ")";
        }
    }
}
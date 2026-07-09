package com.eclipse.domain.common;

import com.eclipse.domain.task.Task;
import com.eclipse.domain.task.TaskPriority;
import com.eclipse.domain.task.TaskStatus;

import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class FunctionalUtils {

    private FunctionalUtils() {
    }

    public static <T, R> R apply(T input, Function<T, R> function) {
        return function.apply(input);
    }

    public static <T> boolean test(T input, Predicate<T> predicate) {
        return predicate.test(input);
    }

    public static <T> void accept(T input, Consumer<T> consumer) {
        consumer.accept(input);
    }

    public static <T> T get(Supplier<T> supplier) {
        return supplier.get();
    }

    public static <T, U, R> R combine(T first, U second, BiFunction<T, U, R> function) {
        return function.apply(first, second);
    }

    public static <T> T applyTwice(T input, UnaryOperator<T> operator) {
        return operator.apply(operator.apply(input));
    }

    public static Function<String, String> capitalizeRef() {
        return StringUtils::capitalize;
    }

    public static Supplier<String> titleSupplierFor(Task task) {
        return task::getTitle;
    }

    public static Function<Task, String> titleExtractor() {
        return Task::getTitle;
    }

    public static Supplier<Task.Builder> newBuilder() {
        return Task.Builder::new;
    }

    public static Predicate<Task> isPriorityAtLeast(TaskPriority minimum) {
        return task -> task.getPriority().getWeight() >= minimum.getWeight();
    }

    @FunctionalInterface
    public interface TaskTransformer {
        Task transform(Task task);
    }

    public static Task transform(Task task, TaskTransformer transformer) {
        return transformer.transform(task);
    }
}
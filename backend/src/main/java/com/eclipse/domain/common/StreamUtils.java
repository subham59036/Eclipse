package com.eclipse.domain.common;

import com.eclipse.domain.task.Task;
import com.eclipse.domain.task.TaskPriority;
import com.eclipse.domain.task.TaskStatus;
import com.eclipse.domain.user.User;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public final class StreamUtils {

    private StreamUtils() {
    }

    public static List<Task> filterByStatus(List<Task> tasks, TaskStatus status) {
        return tasks.stream()
                .filter(task -> task.getStatus() == status)
                .collect(Collectors.toList());
    }

    public static List<Task> filterByMinimumPriority(List<Task> tasks, TaskPriority minimum) {
        return tasks.stream()
                .filter(FunctionalUtils.isPriorityAtLeast(minimum))
                .collect(Collectors.toList());
    }

    public static List<String> mapToTitles(List<Task> tasks) {
        return tasks.stream()
                .map(Task::getTitle)
                .collect(Collectors.toList());
    }

    public static List<Task> flattenProjectTaskLists(List<List<Task>> tasksByProject) {
        return tasksByProject.stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public static List<String> distinctSortedTitlesPage(List<Task> tasks, int skip, int limit) {
        return tasks.stream()
                .map(Task::getTitle)
                .distinct()
                .sorted()
                .skip(skip)
                .limit(limit)
                .collect(Collectors.toList());
    }

    public static int sumPriorityWeights(List<Task> tasks) {
        return tasks.stream()
                .map(task -> task.getPriority().getWeight())
                .reduce(0, Integer::sum);
    }

    public static long countTasks(List<Task> tasks) {
        return tasks.stream().count();
    }

    public static boolean anyOverdue(List<Task> tasks) {
        return tasks.stream().anyMatch(task -> DateUtils.isOverdue(task.getDeadline()));
    }

    public static boolean allAssigned(List<Task> tasks) {
        return tasks.stream().allMatch(task -> task.getAssigneeId() != null);
    }

    public static Optional<Task> lowestPriorityTask(List<Task> tasks) {
        return tasks.stream().min(Comparator.comparing(task -> task.getPriority().getWeight()));
    }

    public static Optional<Task> highestPriorityTask(List<Task> tasks) {
        return tasks.stream().max(Comparator.comparing(task -> task.getPriority().getWeight()));
    }

    public static Map<TaskStatus, List<Task>> groupByStatus(List<Task> tasks) {
        return tasks.stream().collect(Collectors.groupingBy(Task::getStatus));
    }

    public static Map<TaskStatus, Long> countByStatus(List<Task> tasks) {
        return tasks.stream().collect(Collectors.groupingBy(Task::getStatus, Collectors.counting()));
    }

    public static Map<Boolean, List<Task>> partitionByOverdue(List<Task> tasks) {
        return tasks.stream()
                .collect(Collectors.partitioningBy(task -> DateUtils.isOverdue(task.getDeadline())));
    }

    public static String joinTitles(List<Task> tasks) {
        return tasks.stream()
                .map(Task::getTitle)
                .collect(Collectors.joining(", ", "[", "]"));
    }

    public static Set<TaskPriority> distinctPrioritiesUsed(List<Task> tasks) {
        return tasks.stream().map(Task::getPriority).collect(Collectors.toSet());
    }

    public static Optional<Task> findById(List<Task> tasks, UUID id) {
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst();
    }

    public static String titleOrDefault(List<Task> tasks, UUID id, String defaultTitle) {
        return findById(tasks, id)
                .map(Task::getTitle)
                .orElse(defaultTitle);
    }

    public static User resolveAssignee(User possiblyNullUser, Supplier<User> fallbackSupplier) {
        return Optional.ofNullable(possiblyNullUser).orElseGet(fallbackSupplier);
    }

    public static int sumPriorityWeightsParallel(List<Task> tasks) {
        return tasks.parallelStream()
                .map(task -> task.getPriority().getWeight())
                .reduce(0, Integer::sum);
    }

    private interface Supplier<T> extends java.util.function.Supplier<T> {
    }
}
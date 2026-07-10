package com.eclipse.domain.common;

import com.eclipse.domain.task.Task;
import com.eclipse.domain.user.User;
import com.eclipse.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public final class OptionalUtils {

    private OptionalUtils() {
    }


    public static boolean hasAssignee(Task task) {
        return Optional.ofNullable(task.getAssigneeId()).isPresent();
    }

    public static boolean hasNoAssignee(Task task) {
        return Optional.ofNullable(task.getAssigneeId()).isEmpty();
    }

    public static String describeOrElse(Optional<Task> maybeTask) {
        var result = new StringBuilder();
        maybeTask.ifPresentOrElse(
                task -> result.append("Found: ").append(task.getTitle()),
                () -> result.append("No task found"));
        return result.toString();
    }

    public static Task requireTask(Optional<Task> maybeTask, UUID id) {
        return maybeTask.orElseThrow(() -> new ResourceNotFoundException("Task", id.toString()));
    }

    public static Optional<User> resolveAssignee(Optional<Task> maybeTask, List<User> users) {
        return maybeTask.flatMap(task ->
                users.stream()
                        .filter(u -> u.getId().equals(task.getAssigneeId()))
                        .findFirst());
    }

    public static Optional<Task> firstAvailable(Optional<Task> primary, Optional<Task> fallback) {
        return primary.or(() -> fallback);
    }

    public static List<Task> presentTasksOnly(List<Optional<Task>> maybeTasks) {
        return maybeTasks.stream()
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }
}
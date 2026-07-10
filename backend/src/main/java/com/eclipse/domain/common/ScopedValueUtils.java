package com.eclipse.domain.common;

import com.eclipse.domain.task.Task;
import com.eclipse.domain.user.User;
import com.eclipse.security.SecurityContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;

public final class ScopedValueUtils {

    private ScopedValueUtils() {
    }

    public static String simulateTaskCreation(User actor, Task task) {
        var result = new String[1];
        SecurityContext.run(actor, () -> {
            result[0] = describeTaskCreation(task);
        });
        return result[0];
    }

    private static String describeTaskCreation(Task task) {
        User actor = SecurityContext.requireCurrent();
        return "Task \"" + task.getTitle() + "\" created by " + actor.getEmail();
    }

    public static List<String> simulateNestedAdminOverride(User originalActor, User adminOverride, Task task) {
        List<String> log = new ArrayList<>();
        SecurityContext.run(originalActor, () -> {
            log.add("Outer actor: " + SecurityContext.requireCurrent().getEmail());

            SecurityContext.run(adminOverride, () -> {
                log.add("Inner (admin override) actor: " + SecurityContext.requireCurrent().getEmail());
                log.add(describeTaskCreation(task));
            });

            log.add("Outer actor restored: " + SecurityContext.requireCurrent().getEmail());
        });
        return log;
    }

    public static List<String> simulateStructuredConcurrencyInheritance(User actor, List<Task> tasks)
            throws Exception {
        return SecurityContext.call(actor, () -> {
            try (var scope = StructuredTaskScope.open(
                    StructuredTaskScope.Joiner.<String>awaitAllSuccessfulOrThrow())) {

                List<StructuredTaskScope.Subtask<String>> subtasks = new ArrayList<>();
                for (Task task : tasks) {
                    subtasks.add(scope.fork(() -> describeTaskCreation(task)));
                }

                scope.join();

                List<String> results = new ArrayList<>();
                for (var subtask : subtasks) {
                    results.add(subtask.get());
                }
                return results;
            }
        });
    }

    public static String describeWithoutBinding(Task task) {
        return SecurityContext.current()
                .map(user -> "Actor: " + user.getEmail())
                .orElse("No actor bound — likely a background job (task: " + task.getTitle() + ")");
    }
}
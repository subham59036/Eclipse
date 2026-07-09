package com.eclipse.domain.common;

import com.eclipse.domain.project.Project;
import com.eclipse.domain.task.Task;
import com.eclipse.domain.task.TaskPriority;
import com.eclipse.domain.user.User;

public final class PatternMatchingUtils {

    private PatternMatchingUtils() {
    }

    public static String describeWithInstanceof(Object obj) {
        if (obj instanceof Task task) {
            return "Task: " + task.getTitle();
        } else if (obj instanceof User user) {
            return "User: " + user.getEmail();
        } else if (obj instanceof Project project) {
            return "Project: " + project.getName();
        }
        return "Unknown object";
    }

    public static String requireTaskTitle(Object obj) {
        if (!(obj instanceof Task task)) {
            return "not a task";
        }
        return task.getTitle();
    }

    public static String describeEntity(BaseEntity entity) {
        return switch (entity) {
            case User u -> "User: " + u.getEmail();
            case Project p -> "Project: " + p.getName();
            case Task t -> "Task: " + t.getTitle();
            case null -> "No entity provided";
            default -> "Unknown entity type";
        };
    }

    public static String classifyTask(Task task) {
        return switch (task) {
            case null -> "No task";
            case Task t when t.getPriority() == TaskPriority.CRITICAL
                    && DateUtils.isOverdue(t.getDeadline()) -> "URGENT — overdue and critical";
            case Task t when DateUtils.isOverdue(t.getDeadline()) -> "Overdue";
            case Task t when t.getPriority() == TaskPriority.CRITICAL -> "Critical";
            default -> "Normal";
        };
    }

    public static String errorMessage(EclipseError error) {
        return switch (error) {
            case EclipseError.NotFound nf -> "We couldn't find that " + nf.resourceType() + ".";
            case EclipseError.Validation v -> "Please check " + v.field() + ": " + v.reason();
            case EclipseError.Unauthorized u -> "You don't have permission to " + u.action() + ".";
        };
    }

    public static String errorMessageDeconstructed(EclipseError error) {
        return switch (error) {
            case EclipseError.NotFound(String resourceType, String resourceId) ->
                    "We couldn't find that " + resourceType + " (" + resourceId + ").";
            case EclipseError.Validation(String field, String reason) ->
                    "Please check '" + field + "': " + reason;
            case EclipseError.Unauthorized(String action) ->
                    "You don't have permission to " + action + ".";
        };
    }

    public static int errorHttpStatus(EclipseError error) {
        return switch (error) {
            case EclipseError.NotFound nf -> 404;
            case EclipseError.Validation v -> 422;
            case EclipseError.Unauthorized u -> 403;
        };
    }

    public static String classifyWeight(int weight) {
        return switch (weight) {
            case int w when w >= 4 -> "Critical";
            case int w when w == 3 -> "High";
            case int w when w == 2 -> "Medium";
            case int w -> "Low";
        };
    }

    public static String sampleMigrationSql() {
        return """
                CREATE TABLE tasks (
                    id UUID PRIMARY KEY,
                    project_id UUID NOT NULL REFERENCES projects(id),
                    title VARCHAR(255) NOT NULL,
                    status VARCHAR(20) NOT NULL,
                    created_at TIMESTAMP WITH TIME ZONE NOT NULL
                );
                """;
    }

    public static String sampleTaskJson() {
        return """
                {
                  "title": "Fix login bug",
                  "priority": "HIGH",
                  "status": "TODO"
                }
                """;
    }
}
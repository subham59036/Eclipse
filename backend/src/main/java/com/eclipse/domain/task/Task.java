package com.eclipse.domain.task;

import com.eclipse.domain.common.BaseEntity;
import com.eclipse.domain.common.StringUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Task extends BaseEntity {

    private final UUID projectId;
    private String title;
    private String description;
    private UUID assigneeId;
    private TaskPriority priority;
    private TaskStatus status;
    private LocalDate deadline;

    public Task(UUID projectId, String title, TaskPriority priority) {
        if (projectId == null) {
            throw new IllegalArgumentException("projectId must not be null");
        }
        if (StringUtils.isBlank(title)) {
            throw new IllegalArgumentException("title must not be blank");
        }
        if (priority == null) {
            throw new IllegalArgumentException("priority must not be null");
        }
        super();
        this.projectId = projectId;
        this.title = title;
        this.description = null;
        this.assigneeId = null;
        this.priority = priority;
        this.status = TaskStatus.TODO;
        this.deadline = null;
    }

    protected Task(UUID id, Instant createdAt, Instant updatedAt,
                    UUID projectId, String title, String description, UUID assigneeId,
                    TaskPriority priority, TaskStatus status, LocalDate deadline) {
        super(id, createdAt, updatedAt);
        this.projectId = projectId;
        this.title = title;
        this.description = description;
        this.assigneeId = assigneeId;
        this.priority = priority;
        this.status = status;
        this.deadline = deadline;
    }

    public UUID getProjectId() {
        return projectId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public UUID getAssigneeId() {
        return assigneeId;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void assignTo(UUID newAssigneeId) {
        this.assigneeId = newAssigneeId;
        touch();
    }

    public void changeStatus(TaskStatus newStatus) {
        if (newStatus != null) {
            this.status = newStatus;
            touch();
        }
    }

    public void changePriority(TaskPriority newPriority) {
        if (newPriority != null) {
            this.priority = newPriority;
            touch();
        }
    }

    public void setDeadline(LocalDate newDeadline) {
        this.deadline = newDeadline;
        touch();
    }

    @Override
    public String describe() {
        return "Task[" + title + "]";
    }

    @Override
    public Task self() {
        return this;
    }

    @Override
    public String toString() {
        return "Task{" + super.toString() + ", title=" + title
                + ", priority=" + priority + ", status=" + status + "}";
    }

    public static class Builder {
        private UUID projectId;
        private String title;
        private String description;
        private UUID assigneeId;
        private TaskPriority priority = TaskPriority.MEDIUM;
        private LocalDate deadline;

        public Builder projectId(UUID projectId) {
            this.projectId = projectId;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder assigneeId(UUID assigneeId) {
            this.assigneeId = assigneeId;
            return this;
        }

        public Builder priority(TaskPriority priority) {
            this.priority = priority;
            return this;
        }

        public Builder deadline(LocalDate deadline) {
            this.deadline = deadline;
            return this;
        }

        public Task build() {
            Task task = new Task(projectId, title, priority);
            task.changeDescription(description);
            task.assignTo(assigneeId);
            task.setDeadline(deadline);
            return task;
        }
    }

    private void changeDescription(String newDescription) {
        this.description = newDescription;
        touch();
    }
}
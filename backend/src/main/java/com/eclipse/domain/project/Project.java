package com.eclipse.domain.project;

import com.eclipse.domain.common.BaseEntity;
import com.eclipse.domain.common.StringUtils;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Project extends BaseEntity implements Archivable {

    private String name;
    private String description;
    private final UUID ownerId;
    private ProjectStatus status;

    public Project(String name, String description, UUID ownerId) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("name must not be blank");
        }
        if (ownerId == null) {
            throw new IllegalArgumentException("ownerId must not be null");
        }
        super();
        this.name = name;
        this.description = description;
        this.ownerId = ownerId;
        this.status = ProjectStatus.ACTIVE;
    }

    protected Project(UUID id, Instant createdAt, Instant updatedAt,
                       String name, String description, UUID ownerId, ProjectStatus status) {
        super(id, createdAt, updatedAt);
        this.name = name;
        this.description = description;
        this.ownerId = ownerId;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void rename(String newName) {
        if (StringUtils.isNotBlank(newName)) {
            this.name = newName.trim();
            touch();
        }
    }

    public void changeDescription(String newDescription) {
        this.description = newDescription;
        touch();
    }

    // --- Archivable implementation (Part 7 §5.2) ---

    @Override
    public void archive() {
        this.status = ProjectStatus.ARCHIVED;
        touch();
    }

    @Override
    public void restore() {
        this.status = ProjectStatus.ACTIVE;
        touch();
    }

    @Override
    public boolean isArchived() {
        return this.status == ProjectStatus.ARCHIVED;
    }

    @Override
    public String describe() {
        return "Project[" + name + "]";
    }

    @Override
    public Project self() {
        return this;
    }

    @Override
    public String toString() {
        return "Project{" + super.toString() + ", name=" + name
                + ", ownerId=" + ownerId + ", status=" + status + "}";
    }
}
package com.eclipse.domain.common;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public abstract class BaseEntity {

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    protected BaseEntity() {
        this(UUID.randomUUID(), Instant.now(), Instant.now());
    }

    protected BaseEntity(UUID id, Instant createdAt, Instant updatedAt) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        if (createdAt == null || updatedAt == null) {
            throw new IllegalArgumentException("createdAt and updatedAt must not be null");
        }
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public final UUID getId() {
        return id;
    }

    public final Instant getCreatedAt() {
        return createdAt;
    }

    public final Instant getUpdatedAt() {
        return updatedAt;
    }

    protected void touch() {
        this.updatedAt = Instant.now();
    }

    public abstract String describe();

    public BaseEntity self() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BaseEntity other = (BaseEntity) o;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "id=" + id + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt;
    }
}
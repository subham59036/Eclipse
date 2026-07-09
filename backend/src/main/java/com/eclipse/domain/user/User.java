// File: backend/src/main/java/com/eclipse/domain/user/User.java
package com.eclipse.domain.user;

import com.eclipse.domain.common.BaseEntity;
import com.eclipse.domain.common.StringUtils;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class User extends BaseEntity {

    public static final int MIN_PASSWORD_HASH_LENGTH = 8;

    private String email;
    private String passwordHash;
    private String name;
    private UserRole role;
    private String avatarUrl;

    public User(String email, String passwordHash, String name, UserRole role) {
        if (StringUtils.isBlank(email) || !email.contains("@")) {
            throw new IllegalArgumentException("email must be a non-blank address containing '@'");
        }
        if (StringUtils.isBlank(passwordHash) || passwordHash.length() < MIN_PASSWORD_HASH_LENGTH) {
            throw new IllegalArgumentException(
                    "passwordHash must be at least " + MIN_PASSWORD_HASH_LENGTH + " characters");
        }
        if (role == null) {
            throw new IllegalArgumentException("role must not be null");
        }
        super();
        this.email = email;
        this.passwordHash = passwordHash;
        this.name = StringUtils.isBlank(name) ? "Unnamed User" : name;
        this.role = role;
        this.avatarUrl = null;
    }

    public User(String email, String passwordHash, String name) {
        this(email, passwordHash, name, UserRole.MEMBER);
    }

    protected User(UUID id, Instant createdAt, Instant updatedAt,
                    String email, String passwordHash, String name,
                    UserRole role, String avatarUrl) {
        super(id, createdAt, updatedAt);
        this.email = email;
        this.passwordHash = passwordHash;
        this.name = name;
        this.role = role;
        this.avatarUrl = avatarUrl;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getName() {
        return name;
    }

    public UserRole getRole() {
        return role;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void changeName(String newName) {
        if (StringUtils.isNotBlank(newName)) {
            final String trimmed = newName.trim();
            this.name = trimmed;
            touch();
        }
    }

    public void changeAvatarUrl(String newAvatarUrl) {
        this.avatarUrl = newAvatarUrl;
        touch();
    }

    public void promoteToAdmin() {
        this.role = UserRole.ADMIN;
        touch();
    }

    public boolean isAdmin() {
        return this.role == UserRole.ADMIN;
    }

    @Override
    public String describe() {
        return "User[" + email + "]";
    }

    @Override
    public User self() {
        return this;
    }

    @Override
    public String toString() {
        return "User{" + super.toString() + ", email=" + email + ", name=" + name
                + ", role=" + role + "}";
    }
}
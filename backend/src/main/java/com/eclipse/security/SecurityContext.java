package com.eclipse.security;

import com.eclipse.domain.user.User;

import java.util.Optional;

public final class SecurityContext {

    private static final ScopedValue<User> CURRENT_USER = ScopedValue.newInstance();

    private SecurityContext() {
    }

    public static void run(User user, Runnable action) {
        ScopedValue.where(CURRENT_USER, user).run(action);
    }

      public static <T> T call(User user, ScopedValue.CallableOp<T, Exception> action) throws Exception {
      return ScopedValue.where(CURRENT_USER, user).call(action);
      }

    public static Optional<User> current() {
        return CURRENT_USER.isBound() ? Optional.of(CURRENT_USER.get()) : Optional.empty();
    }

    public static User requireCurrent() {
        return current().orElseThrow(
                () -> new IllegalStateException("No authenticated user bound in SecurityContext"));
    }
}
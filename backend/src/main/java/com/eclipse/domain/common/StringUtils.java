package com.eclipse.domain.common;

import java.util.List;
import java.util.StringJoiner;

public final class StringUtils {

    private StringUtils() {
    }

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isNotBlank(String value) {
        return !isBlank(value);
    }

    public static String capitalize(String value) {
        if (isBlank(value)) {
            return value;
        }
        var firstChar = Character.toUpperCase(value.charAt(0));
        var builder = new StringBuilder();
        builder.append(firstChar);
        builder.append(value.substring(1));
        return builder.toString();
    }

    public static String reverse(String value) {
        if (value == null) {
            return null;
        }
        var builder = new StringBuilder();
        for (int i = value.length() - 1; i >= 0; i--) {
            builder.append(value.charAt(i));
        }
        return builder.toString();
    }

    public static String truncateWithEllipsis(String value, int maxLength) {
        if (value == null) {
            return null;
        }
        return (value.length() <= maxLength) ? value : value.substring(0, maxLength) + "...";
    }

    public static String joinBracketed(List<String> values) {
        var joiner = new StringJoiner(", ", "[", "]");
        for (String value : values) {
            joiner.add(value);
        }
        return joiner.toString();
    }

    public static String maskEmail(String email) {
        if (isBlank(email) || !email.contains("@")) {
            return email;
        }
        int atIndex = email.indexOf('@');
        String localPart = email.substring(0, atIndex);
        String domainPart = email.substring(atIndex);

        String maskedLocal = switch (localPart.length()) {
            case 0 -> "";
            case 1, 2 -> localPart.charAt(0) + "**";
            default -> {
                String visible = localPart.substring(0, 2);
                yield visible + "***";
            }
        };
        return maskedLocal + domainPart;
    }

    public static String projectBanner() {
        return """
                ============================================
                  ECLIPSE — Event-Driven Task Management
                ============================================
                """;
    }
}
package com.eclipse.domain.common;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public final class DateUtils {

    private DateUtils() {
    }

    public static boolean isOverdue(LocalDate deadline) {
        if (deadline == null) {
            return false;
        }
        return deadline.isBefore(LocalDate.now());
    }

    public static long daysBetween(Instant earlier, Instant later) {
        if (earlier == null || later == null) {
            return 0L;
        }
        return ChronoUnit.DAYS.between(earlier, later);
    }

    public static String classifyDeadline(LocalDate deadline) {
        if (deadline == null) {
            return "No Deadline";
        }
        long daysUntil = ChronoUnit.DAYS.between(LocalDate.now(), deadline);

        if (daysUntil < 0) {
            return "Overdue";
        } else if (daysUntil == 0) {
            return "Due Today";
        } else if (daysUntil <= 3) {
            return "Due Soon";
        } else {
            return "Upcoming";
        }
    }

    public static String formatElapsed(Instant from, Instant to) {
        if (from == null || to == null) {
            return "unknown";
        }
        long totalMinutes = ChronoUnit.MINUTES.between(from, to);
        if (totalMinutes < 0) {
            totalMinutes = 0;
        }

        long days = totalMinutes / (24 * 60);
        long hours = (totalMinutes % (24 * 60)) / 60;
        long minutes = totalMinutes % 60;

        var builder = new StringBuilder();
        if (days > 0) {
            builder.append(days).append("d ");
        }
        if (hours > 0 || days > 0) {
            builder.append(hours).append("h ");
        }
        builder.append(minutes).append("m");
        return builder.toString();
    }
}
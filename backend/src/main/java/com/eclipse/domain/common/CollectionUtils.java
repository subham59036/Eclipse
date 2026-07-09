package com.eclipse.domain.common;

import com.eclipse.domain.task.Task;
import com.eclipse.domain.task.TaskPriority;
import com.eclipse.domain.task.TaskStatus;
import com.eclipse.domain.user.User;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

public final class CollectionUtils {

    private CollectionUtils() {
    }

    public static <T extends Comparable<T>> T max(List<T> values) {
        if (values == null || values.isEmpty()) {
            throw new IllegalArgumentException("values must not be null or empty");
        }
        T largest = values.get(0);
        for (T value : values) {
            if (value.compareTo(largest) > 0) {
                largest = value;
            }
        }
        return largest;
    }

    public static double sumPriorityWeights(List<? extends Task> tasks) {
        double total = 0;
        for (Task task : tasks) {
            total += task.getPriority().getWeight();
        }
        return total;
    }

    public static void addAllStatuses(List<? super TaskStatus> destination) {
        destination.add(TaskStatus.TODO);
        destination.add(TaskStatus.IN_PROGRESS);
        destination.add(TaskStatus.REVIEW);
        destination.add(TaskStatus.DONE);
    }

    public static Map<UUID, User> indexUsersById(List<User> users) {
        Map<UUID, User> index = new HashMap<>();
        for (User user : users) {
            index.put(user.getId(), user);
        }
        return index;
    }

    public static TreeMap<UUID, User> indexUsersByIdSorted(List<User> users) {
        TreeMap<UUID, User> sorted = new TreeMap<>();
        for (User user : users) {
            sorted.put(user.getId(), user);
        }
        return sorted;
    }

    public static Map<TaskStatus, List<Task>> groupTasksByStatus(List<Task> tasks) {
        Map<TaskStatus, List<Task>> grouped = new HashMap<>();
        for (Task task : tasks) {
            TaskStatus status = task.getStatus();
            List<Task> bucket = grouped.get(status);
            if (bucket == null) {
                bucket = new ArrayList<>();
                grouped.put(status, bucket);
            }
            bucket.add(task);
        }
        return grouped;
    }

    public static Set<UUID> distinctAssigneeIdsInOrder(List<Task> tasks) {
        Set<UUID> seen = new LinkedHashSet<>();
        for (Task task : tasks) {
            if (task.getAssigneeId() != null) {
                seen.add(task.getAssigneeId());
            }
        }
        return seen;
    }

    public static boolean allTasksUnique(List<Task> tasks) {
        Set<Task> uniqueTasks = new HashSet<>(tasks);
        return uniqueTasks.size() == tasks.size();
    }

      public static Task topPriorityTask(List<Task> tasks) {
      if (tasks == null || tasks.isEmpty()) {
            throw new IllegalArgumentException("tasks must not be null or empty");
      }
      PriorityQueue<Task> byPriorityDescending =
                  new PriorityQueue<>((a, b) -> b.getPriority().getWeight() - a.getPriority().getWeight());
      byPriorityDescending.addAll(tasks);
      return byPriorityDescending.poll();
      }

    public static List<Task> reverseUsingStack(List<Task> tasks) {
        Deque<Task> stack = new ArrayDeque<>();
        for (Task task : tasks) {
            stack.push(task);
        }
        List<Task> reversed = new ArrayList<>();
        while (!stack.isEmpty()) {
            reversed.add(stack.pop());
        }
        return reversed;
    }

      public static List<Task> sortedByPriorityDescending(List<Task> tasks) {
      List<Task> copy = new ArrayList<>(tasks);
      Collections.sort(copy, (a, b) -> b.getPriority().getWeight() - a.getPriority().getWeight());
      return Collections.unmodifiableList(copy);
      }
}
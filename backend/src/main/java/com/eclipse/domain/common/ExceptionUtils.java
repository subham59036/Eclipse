package com.eclipse.domain.common;

import com.eclipse.domain.task.Task;
import com.eclipse.exception.EclipseException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public final class ExceptionUtils {

    private ExceptionUtils() {
    }

    public static int finallyAlwaysRuns(List<String> log) {
        try {
            return 1;
        } finally {
            log.add("finally ran");
        }
    }

    public static class SimulatedResource implements AutoCloseable {
        private final String name;
        private final boolean failOnClose;

        public SimulatedResource(String name, boolean failOnClose) {
            this.name = name;
            this.failOnClose = failOnClose;
        }

        public String use() {
            return "Using " + name;
        }

        @Override
        public void close() throws Exception {
            if (failOnClose) {
                throw new Exception("close() failed for " + name);
            }
        }
    }

    public static List<String> demonstrateTryWithResources() throws Exception {
        List<String> log = new ArrayList<>();
        try (var resource = new SimulatedResource("res1", false)) {
            log.add(resource.use());
        }
        log.add("resource closed automatically");
        return log;
    }

    public static List<String> demonstrateSuppressedExceptions() {
        List<String> log = new ArrayList<>();
        try (var resource = new SimulatedResource("res1", true)) {
            throw new RuntimeException("body failed");
        } catch (Exception e) {
            log.add("Primary: " + e.getMessage());
            for (Throwable suppressed : e.getSuppressed()) {
                log.add("Suppressed: " + suppressed.getMessage());
            }
        }
        return log;
    }

    public static String multiCatchDemo(int mode) {
        try {
            if (mode == 1) {
                throw new IllegalArgumentException("bad argument");
            }
            if (mode == 2) {
                throw new IllegalStateException("bad state");
            }
            return "no exception";
        } catch (IllegalArgumentException | IllegalStateException e) {
            return "Caught " + e.getClass().getSimpleName() + ": " + e.getMessage();
        }
    }

    public static Task translateAndLoad(Callable<Task> lowLevelCall) {
        try {
            return lowLevelCall.call();
        } catch (Exception e) {
            throw new EclipseException("Failed to load task", e);
        }
    }
}
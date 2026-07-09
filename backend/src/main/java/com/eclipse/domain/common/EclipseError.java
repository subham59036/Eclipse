package com.eclipse.domain.common;

public sealed interface EclipseError {

    String message();

    record NotFound(String resourceType, String resourceId) implements EclipseError {
        @Override
        public String message() {
            return resourceType + " with id " + resourceId + " was not found";
        }
    }

    record Validation(String field, String reason) implements EclipseError {
        @Override
        public String message() {
            return "Validation failed for '" + field + "': " + reason;
        }
    }

    record Unauthorized(String action) implements EclipseError {
        @Override
        public String message() {
            return "Not authorized to perform: " + action;
        }
    }
}
package com.eclipse.exception;

public final class GlobalExceptionHandler {

    private GlobalExceptionHandler() {
    }

    public record ErrorResponse(int status, String message) {
    }

    public static ErrorResponse handle(EclipseException ex) {
        return switch (ex) {
            case ResourceNotFoundException rnf -> new ErrorResponse(404, rnf.getMessage());
            case AccessDeniedException ade -> new ErrorResponse(403, ade.getMessage());
            default -> new ErrorResponse(500, ex.getMessage());
        };
    }
}
package com.eclipse.exception;

public class EclipseException extends RuntimeException {

    public EclipseException(String message) {
        super(message);
    }

    public EclipseException(String message, Throwable cause) {
        super(message, cause);
    }
}
package com.eclipse.exception;

public class AccessDeniedException extends EclipseException {

    private final String action;

    public AccessDeniedException(String action) {
        super("Not authorized to perform: " + action);
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}
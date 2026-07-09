package com.eclipse.domain.common;

import java.time.Instant;

public final class ApiResponse<T> {

    private final boolean success;
    private final T data;
    private final String message;
    private final Instant timestamp;

    private ApiResponse(boolean success, T data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
        this.timestamp = Instant.now();
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, null, message);
    }

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "ApiResponse{success=" + success + ", data=" + data
                + ", message=" + message + ", timestamp=" + timestamp + "}";
    }
}
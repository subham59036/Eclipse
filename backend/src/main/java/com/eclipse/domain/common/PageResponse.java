package com.eclipse.domain.common;

import java.util.Collections;
import java.util.List;

public record PageResponse<T>(List<T> content, int page, int size, long totalElements,
                               int totalPages, boolean last) {

    public PageResponse {
        if (content == null) {
            throw new IllegalArgumentException("content must not be null");
        }
        if (page < 0) {
            throw new IllegalArgumentException("page must not be negative");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("size must be positive");
        }
        content = Collections.unmodifiableList(content);
    }

    public static <T> PageResponse<T> of(List<T> content, int page, int size, long totalElements) {
        int totalPages = (int) Math.ceil((double) totalElements / size);
        boolean last = (page + 1) >= totalPages;
        return new PageResponse<>(content, page, size, totalElements, totalPages, last);
    }

    public static <T> PageResponse<T> empty(int page, int size) {
        return of(Collections.emptyList(), page, size, 0L);
    }
}
package com.eclipse.domain.common;

import java.util.Collections;
import java.util.List;

public final class PageResponse<T> {

    private final List<T> content;
    private final int page;
    private final int size;
    private final long totalElements;
    private final int totalPages;
    private final boolean last;

    private PageResponse(List<T> content, int page, int size, long totalElements,
                          int totalPages, boolean last) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = last;
    }

    public static <T> PageResponse<T> of(List<T> content, int page, int size, long totalElements) {
        if (content == null) {
            throw new IllegalArgumentException("content must not be null");
        }
        if (page < 0) {
            throw new IllegalArgumentException("page must not be negative");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("size must be positive");
        }
        int totalPages = (int) Math.ceil((double) totalElements / size);
        boolean last = (page + 1) >= totalPages;
        return new PageResponse<>(Collections.unmodifiableList(content), page, size, totalElements, totalPages, last);
    }

    public static <T> PageResponse<T> empty(int page, int size) {
        return of(Collections.emptyList(), page, size, 0L);
    }

    public List<T> getContent() {
        return content;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public boolean isLast() {
        return last;
    }

    @Override
    public String toString() {
        return "PageResponse{page=" + page + ", size=" + size + ", totalElements=" + totalElements
                + ", totalPages=" + totalPages + ", last=" + last + ", content=" + content + "}";
    }
}
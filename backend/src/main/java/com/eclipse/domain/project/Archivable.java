package com.eclipse.domain.project;

public interface Archivable {

    void archive();
    void restore();
    boolean isArchived();

    default String archivalStatusLabel() {
        return isArchived() ? "Archived" : "Active";
    }

    static String describeContract() {
        return "Archivable: supports archive(), restore(), isArchived()";
    }

    private String prefixed(String message) {
        return "[Archivable] " + message;
    }

    default String archivalStatusLabelPrefixed() {
        return prefixed(archivalStatusLabel());
    }
}
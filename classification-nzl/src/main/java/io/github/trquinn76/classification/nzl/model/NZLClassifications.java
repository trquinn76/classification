package io.github.trquinn76.classification.nzl.model;

public enum NZLClassifications {

    // @formatter:off
    UNCLASSIFIED("UNCLASSIFIED"),
    IN_CONFIDENCE("IN-CONFIDENCE"),
    SENSITIVE("SENSITIVE"),
    RESTRICTED("RESTRICTED"),
    CONFIDENTIAL("CONFIDENTIAL"),
    SECRET("SECRET"),
    TOP_SECRET("TOP SECRET");
    // @formatter:on

    private final String text;

    private NZLClassifications(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return this.text;
    }
}

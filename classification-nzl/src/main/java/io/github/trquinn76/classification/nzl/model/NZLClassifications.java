package io.github.trquinn76.classification.nzl.model;

/**
 * The {@link Classification}'s as defined in the Overview of the Classification system.
 * <p>
 * The default configuration will see {@link DevelopmentClassification}'s used. In order for these
 * {@link Classification}'s to be used, it is necessary to set the classification production mode configuration
 * value to true.
 */
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

package io.github.trquinn76.classification.usa.model;

/**
 * The {@link Classification}'s as defined in the DoD Information Security
 * Program: Marking of Information.
 * <p>
 * The default configuration will see {@link DevelopmentClassification}'s used.
 * In order for these {@link Classification}'s to be used, it is necessary to
 * set the classification production mode configuration value to true.
 */
public enum SecurityClassification {

    // @formatter:off
    UNCLASSIFIED("UNCLASSIFIED"),
    RESTRICTED("RESTRICTED"), // only valid when representing Foreign or NATO Classifications. 
    CONFIDENTAL("CONFIDENTIAL"),
    SECRET("SECRET"),
    TOP_SECRET("TOP SECRET");
    // @formatter:on

    private final String text;

    private SecurityClassification(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return this.text;
    }
}

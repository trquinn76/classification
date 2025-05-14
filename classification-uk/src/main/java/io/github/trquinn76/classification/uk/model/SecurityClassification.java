package io.github.trquinn76.classification.uk.model;

/**
 * The {@link Classification}'s as defined in the UK Government Security
 * Classification Policy.
 * <p>
 * The default configuration will see {@link DevelopmentClassification}'s used.
 * In order for these {@link Classification}'s to be used, it is necessary to
 * set the classification production mode configuration value to true.
 */
public enum SecurityClassification {

    // @formatter:off
    OFFICIAL("OFFICIAL"),
    SECRET("SECRET"),
    TOP_SECRET("TOP SECRET");
    // @formatter:on

    private final String text;

    private SecurityClassification(String text) {
        this.text = text;
    }

    /**
     * Overridden to ensure that the {@code toString()} function returns text
     * suitable to display to human Users.
     * 
     * @return String a human readable representation of the {@link Classification}
     */
    @Override
    public String toString() {
        return this.text;
    }
}

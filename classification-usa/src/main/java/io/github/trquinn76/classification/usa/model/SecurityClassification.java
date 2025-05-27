package io.github.trquinn76.classification.usa.model;

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

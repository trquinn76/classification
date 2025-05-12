package io.github.trquinn76.classification.uk.model;

public enum SecurityClassification {

    OFFICIAL("OFFICIAL"),
    SECRET("SECRET"),
    TOP_SECRET("TOP SECRET");
    
    private final String text;
    
    private SecurityClassification(String text) {
        this.text = text;
    }
    
    @Override
    public String toString() {
        return this.text;
    }
}

package io.github.trquinn76.classification.usa.model;

import io.github.trquinn76.classification.usa.ClassificationConfig;

public enum DevelopmentClassification {

    // @formatter:off
    DEVELOPOMENT_UNCLASSIFIED(ClassificationConfig.developmentUnclassifiedName()),
    DEVELOPMENT_REDTRICTED(ClassificationConfig.developmentRestrictedName()),
    DEVELOPMENT_CONFIDENTIAL(ClassificationConfig.developmentConfidentalName()),
    DEVELOPMENT_SECRET(ClassificationConfig.developmentSecretName()),
    DEVELOPMENT_TOP_SECRET(ClassificationConfig.developmentTopSecretName());
    // @formatter:on
    
    private final String text;
    
    private DevelopmentClassification(String text) {
        this.text = text;
    }
    
    @Override
    public String toString() {
        return this.text;
    }
}

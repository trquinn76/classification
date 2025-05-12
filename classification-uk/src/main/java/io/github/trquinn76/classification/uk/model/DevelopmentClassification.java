package io.github.trquinn76.classification.uk.model;

import io.github.trquinn76.classification.uk.ClassificationConfig;

public enum DevelopmentClassification {

    DEVELOPMENT_OFFICIAL(ClassificationConfig.developmentOfficialName()),
    DEVELOPMENT_SECRET(ClassificationConfig.developmentSecretName()),
    DEVELPMENT_TOP_SECRET(ClassificationConfig.developmentTopSecretName());
    
    private final String text;
    
    private DevelopmentClassification(String text) {
        this.text = text;
    }
    
    @Override
    public String toString() {
        return this.text;
    }
}

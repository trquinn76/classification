package io.github.trquinn76.classification.nzl.model;

import io.github.trquinn76.classification.nzl.ClassificationConfig;

public enum DevelopmentClassifications {

    // @formatter:off
    DEVELOPMENT_UNCLASSIFIED(ClassificationConfig.developmentUnclassifiedName()),
    DEVELOPMENT_IN_CONFIDENCE(ClassificationConfig.developmentInConfidenceName()),
    DEVELOPMENT_SENSITIVE(ClassificationConfig.developmentSensitiveName()),
    DEVELOPMENT_RESTRICTED(ClassificationConfig.developmentRestrictedName()),
    DEVELOPMENT_CONFIDENTIAL(ClassificationConfig.developmentConfidentialName()),
    DEVELOPMENT_SECRET(ClassificationConfig.developmentSecretName()),
    DEVELOPMENT_TOP_SECRET(ClassificationConfig.developmentTopSecretName());
    // @formatter:on

    private final String text;

    private DevelopmentClassifications(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return this.text;
    }
}

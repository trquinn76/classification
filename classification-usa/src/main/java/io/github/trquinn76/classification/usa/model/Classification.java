package io.github.trquinn76.classification.usa.model;

import java.util.Objects;

import io.github.trquinn76.classification.usa.ClassificationConfig;

public record Classification(String classificationName) implements Comparable<Classification> {
    
    public Classification {
        Objects.requireNonNull(classificationName);
        if (ClassificationConfig.productionMode()) {
            SecurityClassification.valueOf(classificationName);
        } else {
            DevelopmentClassification.valueOf(classificationName);
        }
    }
    
    public static Classification unclassified() {
        if (ClassificationConfig.productionMode()) {
            return new Classification(SecurityClassification.UNCLASSIFIED.name());
        }
        return new Classification(DevelopmentClassification.DEVELOPOMENT_UNCLASSIFIED.name());
    }
    
    public static Classification restricted() {
        if (ClassificationConfig.productionMode()) {
            return new Classification(SecurityClassification.RESTRICTED.name());
        }
        return new Classification(DevelopmentClassification.DEVELOPMENT_REDTRICTED.name());
    }
    
    public static Classification confidential() {
        if (ClassificationConfig.productionMode()) {
            return new Classification(SecurityClassification.CONFIDENTAL.name());
        }
        return new Classification(DevelopmentClassification.DEVELOPMENT_CONFIDENTIAL.name());
    }
    
    public static Classification secret() {
        if (ClassificationConfig.productionMode()) {
            return new Classification(SecurityClassification.SECRET.name());
        }
        return new Classification(DevelopmentClassification.DEVELOPMENT_SECRET.name());
    }
    
    public static Classification topSecret() {
        if (ClassificationConfig.productionMode()) {
            return new Classification(SecurityClassification.TOP_SECRET.name());
        }
        return new Classification(DevelopmentClassification.DEVELOPMENT_TOP_SECRET.name());
    }

    @Override
    public int compareTo(Classification other) {
        if (ClassificationConfig.productionMode()) {
            return SecurityClassification.valueOf(classificationName())
                    .compareTo(SecurityClassification.valueOf(other.classificationName()));
        }
        return DevelopmentClassification.valueOf(classificationName())
                .compareTo(DevelopmentClassification.valueOf(other.classificationName()));
    }
    
    @Override
    public String toString() {
        if (ClassificationConfig.productionMode()) {
            return SecurityClassification.valueOf(classificationName()).toString();
        }
        return DevelopmentClassification.valueOf(classificationName()).toString();
    }

}

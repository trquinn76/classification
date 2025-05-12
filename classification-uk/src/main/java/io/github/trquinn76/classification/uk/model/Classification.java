package io.github.trquinn76.classification.uk.model;

import java.util.Objects;

import io.github.trquinn76.classification.uk.ClassificationConfig;

public record Classification(String classificationName) implements Comparable<Classification> {

    public Classification {
        Objects.requireNonNull(classificationName);
        if (ClassificationConfig.productionMode()) {
            SecurityClassification.valueOf(classificationName);
        } else {
            DevelopmentClassification.valueOf(classificationName);
        }
    }

    public static Classification official() {
        if (ClassificationConfig.productionMode()) {
            return new Classification(SecurityClassification.OFFICIAL.name());
        } else {
            return new Classification(DevelopmentClassification.DEVELOPMENT_OFFICIAL.name());
        }
    }

    public static Classification secret() {
        if (ClassificationConfig.productionMode()) {
            return new Classification(SecurityClassification.SECRET.name());
        } else {
            return new Classification(DevelopmentClassification.DEVELOPMENT_SECRET.name());
        }
    }

    public static Classification topSecret() {
        if (ClassificationConfig.productionMode()) {
            return new Classification(SecurityClassification.TOP_SECRET.name());
        } else {
            return new Classification(DevelopmentClassification.DEVELPMENT_TOP_SECRET.name());
        }
    }
    
    @Override
    public String toString() {
        if (ClassificationConfig.productionMode()) {
            return SecurityClassification.valueOf(classificationName()).toString();
        }
        else {
            return DevelopmentClassification.valueOf(classificationName()).toString();
        }
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

}

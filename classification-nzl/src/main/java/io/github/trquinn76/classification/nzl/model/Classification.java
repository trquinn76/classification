package io.github.trquinn76.classification.nzl.model;

import java.util.Objects;

import io.github.trquinn76.classification.nzl.ClassificationConfig;

public record Classification(String classificationName) implements Comparable<Classification> {

    public Classification {
        Objects.requireNonNull(classificationName);
        if (ClassificationConfig.productionMode()) {
            NZLClassifications.valueOf(classificationName);
        } else {
            DevelopmentClassifications.valueOf(classificationName);
        }
    }

    @Override
    public String toString() {
        if (ClassificationConfig.productionMode()) {
            return NZLClassifications.valueOf(classificationName()).toString();
        }
        return DevelopmentClassifications.valueOf(classificationName()).toString();
    }

    public static Classification unclassified() {
        if (ClassificationConfig.productionMode()) {
            return new Classification(NZLClassifications.UNCLASSIFIED.name());
        }
        return new Classification(DevelopmentClassifications.DEVELOPMENT_UNCLASSIFIED.name());
    }

    public static Classification inConfidence() {
        if (ClassificationConfig.productionMode()) {
            return new Classification(NZLClassifications.IN_CONFIDENCE.name());
        }
        return new Classification(DevelopmentClassifications.DEVELOPMENT_IN_CONFIDENCE.name());
    }

    public static Classification sensitive() {
        if (ClassificationConfig.productionMode()) {
            return new Classification(NZLClassifications.SENSITIVE.name());
        }
        return new Classification(DevelopmentClassifications.DEVELOPMENT_SENSITIVE.name());
    }

    public static Classification restricted() {
        if (ClassificationConfig.productionMode()) {
            return new Classification(NZLClassifications.RESTRICTED.name());
        }
        return new Classification(DevelopmentClassifications.DEVELOPMENT_RESTRICTED.name());
    }

    public static Classification confidential() {
        if (ClassificationConfig.productionMode()) {
            return new Classification(NZLClassifications.CONFIDENTIAL.name());
        }
        return new Classification(DevelopmentClassifications.DEVELOPMENT_CONFIDENTIAL.name());
    }

    public static Classification secret() {
        if (ClassificationConfig.productionMode()) {
            return new Classification(NZLClassifications.SECRET.name());
        }
        return new Classification(DevelopmentClassifications.DEVELOPMENT_SECRET.name());
    }

    public static Classification topSecret() {
        if (ClassificationConfig.productionMode()) {
            return new Classification(NZLClassifications.TOP_SECRET.name());
        }
        return new Classification(DevelopmentClassifications.DEVELOPMENT_TOP_SECRET.name());
    }

    @Override
    public int compareTo(Classification other) {
        if (ClassificationConfig.productionMode()) {
            return NZLClassifications.valueOf(classificationName())
                    .compareTo(NZLClassifications.valueOf(other.classificationName()));
        }
        return DevelopmentClassifications.valueOf(classificationName())
                .compareTo(DevelopmentClassifications.valueOf(other.classificationName()));
    }

}

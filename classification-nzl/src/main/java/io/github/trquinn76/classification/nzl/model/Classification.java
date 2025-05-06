package io.github.trquinn76.classification.nzl.model;

import java.util.Objects;

import io.github.trquinn76.classification.nzl.ClassificationConfig;

/**
 * Represents a specific Classification such as IN CONFIDENCE or SECRET.
 * <p>
 * There are two enumerations which represent Classifications, {@link NZLClassification} and
 * {@link DevelopmentClassification}.
 * <p>
 * {@link NZLClassification} represents the Classifications defined in the Overview of the Classification system.
 * <p>
 * {@link DevelopmentClassification} represents parallel Classifications, which should obviously map to the
 * real Classifications, and should be used in environments where data, and test data, should not be marked
 * with real Classifications.
 * <p>
 * The {@code classificationName} passed to this record is required to be the name of an entry in one of those
 * enumerations. Any other value will raise exceptions. In addition only values for the appropriate enumeration
 * based on the current configuration of production mode are accepted.
 * 
 * @param classificationName defines the name of an entry in the currently configured Classification enumeration.
 */
public record Classification(String classificationName) implements Comparable<Classification> {

    public Classification {
        Objects.requireNonNull(classificationName);
        if (ClassificationConfig.productionMode()) {
            NZLClassification.valueOf(classificationName);
        } else {
            DevelopmentClassification.valueOf(classificationName);
        }
    }

    @Override
    public String toString() {
        if (ClassificationConfig.productionMode()) {
            return NZLClassification.valueOf(classificationName()).toString();
        }
        return DevelopmentClassification.valueOf(classificationName()).toString();
    }

    public static Classification unclassified() {
        if (ClassificationConfig.productionMode()) {
            return new Classification(NZLClassification.UNCLASSIFIED.name());
        }
        return new Classification(DevelopmentClassification.DEVELOPMENT_UNCLASSIFIED.name());
    }

    public static Classification inConfidence() {
        if (ClassificationConfig.productionMode()) {
            return new Classification(NZLClassification.IN_CONFIDENCE.name());
        }
        return new Classification(DevelopmentClassification.DEVELOPMENT_IN_CONFIDENCE.name());
    }

    public static Classification sensitive() {
        if (ClassificationConfig.productionMode()) {
            return new Classification(NZLClassification.SENSITIVE.name());
        }
        return new Classification(DevelopmentClassification.DEVELOPMENT_SENSITIVE.name());
    }

    public static Classification restricted() {
        if (ClassificationConfig.productionMode()) {
            return new Classification(NZLClassification.RESTRICTED.name());
        }
        return new Classification(DevelopmentClassification.DEVELOPMENT_RESTRICTED.name());
    }

    public static Classification confidential() {
        if (ClassificationConfig.productionMode()) {
            return new Classification(NZLClassification.CONFIDENTIAL.name());
        }
        return new Classification(DevelopmentClassification.DEVELOPMENT_CONFIDENTIAL.name());
    }

    public static Classification secret() {
        if (ClassificationConfig.productionMode()) {
            return new Classification(NZLClassification.SECRET.name());
        }
        return new Classification(DevelopmentClassification.DEVELOPMENT_SECRET.name());
    }

    public static Classification topSecret() {
        if (ClassificationConfig.productionMode()) {
            return new Classification(NZLClassification.TOP_SECRET.name());
        }
        return new Classification(DevelopmentClassification.DEVELOPMENT_TOP_SECRET.name());
    }

    @Override
    public int compareTo(Classification other) {
        if (ClassificationConfig.productionMode()) {
            return NZLClassification.valueOf(classificationName())
                    .compareTo(NZLClassification.valueOf(other.classificationName()));
        }
        return DevelopmentClassification.valueOf(classificationName())
                .compareTo(DevelopmentClassification.valueOf(other.classificationName()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(classificationName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        return compareTo((Classification) obj) == 0;
    }

}

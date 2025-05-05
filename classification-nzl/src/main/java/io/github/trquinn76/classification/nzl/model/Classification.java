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

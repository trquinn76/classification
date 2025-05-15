package io.github.trquinn76.classification.uk.model;

import java.util.Objects;

import io.github.trquinn76.classification.uk.ClassificationConfig;

/**
 * Represents a specific Classification such as OFFICIAL or SECRET.
 * <p>
 * There are two enumerations which represent Classifications,
 * {@link SecurityClassification} and {@link DevelopmentClassification}.
 * <p>
 * {@link SecurityClassification} represents the Classifications defined in the
 * UK Government Security Classification Policy.
 * <p>
 * {@link DevelopmentClassification} represents parallel Classifications, which
 * should obviously map to the real Classifications, and should be used in
 * environments where data, and test data, should not be marked with real
 * Classifications.
 * <p>
 * The {@code classificationName} passed to this record is required to be the
 * name of an entry in one of those enumerations. Any other value will raise
 * exceptions. In addition only values for the appropriate enumeration based on
 * the current configuration of production mode are accepted.
 * 
 * @param classificationName defines the name of an entry in the currently
 *                           configured Classification enumeration.
 */
public record Classification(String classificationName) implements Comparable<Classification> {

    /**
     * Constructor.
     * 
     * @param classificationName defines the name of an entry in the currently
     *                           configured Classification enumeration.
     */
    public Classification {
        Objects.requireNonNull(classificationName);
        if (ClassificationConfig.productionMode()) {
            SecurityClassification.valueOf(classificationName);
        } else {
            DevelopmentClassification.valueOf(classificationName);
        }
    }

    /**
     * Returns the {@link Classification} which represents OFFICIAL based on current
     * configuration.
     * 
     * @return OFFICIAL or DEVELOPMENT_OFFICIAL depending on configuration.
     */
    public static Classification official() {
        if (ClassificationConfig.productionMode()) {
            return new Classification(SecurityClassification.OFFICIAL.name());
        } else {
            return new Classification(DevelopmentClassification.DEVELOPMENT_OFFICIAL.name());
        }
    }

    /**
     * Returns the {@link Classification} which represents SECRET based on current
     * configuration.
     * 
     * @return SECRET or DEVELOPMENT_SECRET depending on configuration.
     */
    public static Classification secret() {
        if (ClassificationConfig.productionMode()) {
            return new Classification(SecurityClassification.SECRET.name());
        } else {
            return new Classification(DevelopmentClassification.DEVELOPMENT_SECRET.name());
        }
    }

    /**
     * Returns the {@link Classification} which represents TOP SECRET based on
     * current configuration.
     * 
     * @return TOP_SECRET or DEVELOPMENT_TOP_SECRET depending no configuration.
     */
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
        } else {
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

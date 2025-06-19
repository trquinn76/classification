package io.github.trquinn76.classification.usa.model;

import java.util.Objects;

import io.github.trquinn76.classification.usa.ClassificationConfig;

/**
 * Represents a specific Classification such as UNCLASSIFIED or SECRET.
 * <p>
 * There are two enumerations which represent Classifications,
 * {@link SecurityClassification} and {@link DevelopmentClassification}.
 * <p>
 * {@link SecurityClassification} represents the Classifications defined in the
 * "DoD Information Security Program: Marking of Information" document.
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
     * Returns the {@link Classification} which represents UNCLASSIFIED based on
     * current configuration.
     * 
     * @return UNCLASSIFIED or DEVELOPMENT_UNCLASSIFIED depending on configuration.
     */
    public static Classification unclassified() {
        if (ClassificationConfig.productionMode()) {
            return new Classification(SecurityClassification.UNCLASSIFIED.name());
        }
        return new Classification(DevelopmentClassification.DEVELOPOMENT_UNCLASSIFIED.name());
    }

    /**
     * Returns the {@link Classification} which represents RESTRICTED based on
     * current configuration.
     * 
     * @return RESTRICTED or DEVELOPMENT_RESTRICTED depending on configuration.
     */
    public static Classification restricted() {
        if (ClassificationConfig.productionMode()) {
            return new Classification(SecurityClassification.RESTRICTED.name());
        }
        return new Classification(DevelopmentClassification.DEVELOPMENT_REDTRICTED.name());
    }

    /**
     * Returns the {@link Classification} which represents CONFIDENTIAL based on
     * current configuration.
     * 
     * @return CONFIDENTIAL or DEVELOPMENT_CONFIDENTIAL depending on configuration.
     */
    public static Classification confidential() {
        if (ClassificationConfig.productionMode()) {
            return new Classification(SecurityClassification.CONFIDENTAL.name());
        }
        return new Classification(DevelopmentClassification.DEVELOPMENT_CONFIDENTIAL.name());
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
        }
        return new Classification(DevelopmentClassification.DEVELOPMENT_SECRET.name());
    }

    /**
     * Returns the {@link Classification} which represents TOP_SECRET based on
     * current configuration.
     * 
     * @return TOP_SECRET or DEVELOPMENT_TOP_SECRET depending on configuration.
     */
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

package io.github.trquinn76.classification.aus.model;

import java.util.Objects;

import io.github.trquinn76.classification.aus.ClassificationConfig;

/**
 * Represents a specific Classification such as OFFICIAL or SECRET.
 * <p>
 * There are two enumerations which represent Classifications, {@link PSPFClassification} and
 * {@link DevelopmentClassification}.
 * <p>
 * {@link PSPFClassification} represents the Classifications defined in the Australian Government Protective
 * Security Policy Framework.
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
	
	/**
	 * Constructor.
	 * 
	 * @param classificationName defines the name of an entry in the currently configured Classification enumeration.
	 */
	public Classification {
		Objects.requireNonNull(classificationName);
		if (ClassificationConfig.productionMode()) {
			PSPFClassification.valueOf(classificationName);
		} else {
			DevelopmentClassification.valueOf(classificationName);
		}
	}
	
	@Override
	public String toString() {
		if (ClassificationConfig.productionMode()) {
			return PSPFClassification.valueOf(classificationName()).toString();
		}
		return DevelopmentClassification.valueOf(classificationName()).toString();
	}

	/**
	 * Returns the {@link Classification} which represents UNOFFICIAL based on current configuration.
	 * 
	 * @return UNOFFICIAL or DEVELOPMENT_UNOFFICIAL depending on configuration.
	 */
	public static Classification unofficial() {
		if (ClassificationConfig.productionMode()) {
			return new Classification(PSPFClassification.UNOFFICIAL.name());
		}
		return new Classification(DevelopmentClassification.DEVELOPMENT_UNOFFICIAL.name());
	}
	
	/**
	 * Returns the {@link Classification} which represents OFFICIAL based on current configuration.
	 * 
	 * @return OFFICIAL or DEVELOPMENT_OFFICIAL depending on configuration.
	 */
	public static Classification official() {
		if (ClassificationConfig.productionMode()) {
			return new Classification(PSPFClassification.OFFICIAL.name());
		}
		return new Classification(DevelopmentClassification.DEVELOPMENT_OFFICIAL.name());
	}
	
	/**
	 * Returns the {@link Classification} which represents OFFICIAL: Sensitive based on current configuration.
	 * 
	 * @return OFFICIAL_SENSITIVE or DEVELOPMENT_OFFICIAL_SENSITIVE depending on configuration.
	 */
	public static Classification officialSensitive() {
		if (ClassificationConfig.productionMode()) {
			return new Classification(PSPFClassification.OFFICIAL_SENSITIVE.name());
		}
		return new Classification(DevelopmentClassification.DEVELOPMENT_OFFICIAL_SENSITIVE.name());
	}
	
	/**
	 * Returns the {@link Classification} which represents PROTECTED based on current configuration.
	 * <p>
	 * Unfortunately, {@code protected} is a reserved work in {@code Java}, so it is not possible to use the obvious
	 * function name here.
	 * 
	 * @return PROTECTED or DEVELOPMENT_PROTECTED depending on configuration.
	 */
	public static Classification protect() {
		if (ClassificationConfig.productionMode()) {
			return new Classification(PSPFClassification.PROTECTED.name());
		}
		return new Classification(DevelopmentClassification.DEVELOPMENT_PROTECTED.name());
	}
	
	/**
	 * Returns the {@link Classification} which represents SECRET based on current configuration.
	 * 
	 * @return SECRET or DEVELOPMENT_SECRET depending on configuration.
	 */
	public static Classification secret() {
		if (ClassificationConfig.productionMode()) {
			return new Classification(PSPFClassification.SECRET.name());
		}
		return new Classification(DevelopmentClassification.DEVELOPMENT_SECRET.name());
	}
	
	/**
	 * Returns the {@link Classification} which represents TOP SECRET based on current configuration.
	 * 
	 * @return TOP_SECRET or DEVELOPMENT_TOP_SECRET depending no configuration.
	 */
	public static Classification topSecret() {
		if (ClassificationConfig.productionMode()) {
			return new Classification(PSPFClassification.TOP_SECRET.name());
		}
		return new Classification(DevelopmentClassification.DEVELOPMENT_TOP_SECRET.name());
	}
	
	/**
	 * Will determine if the given text String will map to a current {@link Classification}.
	 * <p>
	 * May be used before {@code fromString()} to avoid {@code IllegalArgumentException's}.
	 * 
	 * @param text the String to determine if it will map to the {@code toString()} value of a {@link Classification}.
	 * @return true if the text String DOES map to a current {@link Classification}, false otherwise.
	 */
	public static boolean isString(String text) {
		if (ClassificationConfig.productionMode()) {
			for (PSPFClassification classification : PSPFClassification.values()) {
				if (classification.toString().equals(text)) {
					return true;
				}
			}
		}
		else {
			for (DevelopmentClassification classification : DevelopmentClassification.values()) {
				if (classification.toString().equals(text)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public int compareTo(Classification other) {
		if (ClassificationConfig.productionMode()) {
			return PSPFClassification.valueOf(classificationName()).compareTo(PSPFClassification.valueOf(other.classificationName()));
		}
		return DevelopmentClassification.valueOf(classificationName()).compareTo(DevelopmentClassification.valueOf(other.classificationName()));
	}
}

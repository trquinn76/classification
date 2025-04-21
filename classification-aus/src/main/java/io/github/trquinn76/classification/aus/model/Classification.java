package io.github.trquinn76.classification.aus.model;

import java.util.Objects;

import io.github.trquinn76.classification.aus.ClassificationConfig;

/**
 * An wrapper record for either a {@link PSPFClassification} or a {@link DevelopmentClassification} to represent a
 * specific Classification, such as OFFICIAL or SECRET.
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
 */
public record Classification(PSPFClassification pspfClassification, DevelopmentClassification developmentClassification) implements Comparable<Classification> {
	
	public Classification {
		if (ClassificationConfig.productionMode()) {
			if (developmentClassification != null) {
				throw new IllegalArgumentException(
						"Classification may not contain a DevelopmentClassification when production mode is set.");
			}
			Objects.requireNonNull(pspfClassification);
		} else {
			if (pspfClassification != null) {
				throw new IllegalArgumentException(
						"Classification may not contain a PSPFClassification when production mode is not set.");
			}
			Objects.requireNonNull(developmentClassification);
		}
	}
	
	@Override
	public String toString() {
		if (ClassificationConfig.productionMode()) {
			return this.pspfClassification.toString();
		}
		return this.developmentClassification.toString();
	}

	/**
	 * Returns the {@link Classification} which represents UNOFFICIAL based on current configuration.
	 * 
	 * @return UNOFFICIAL or DEVELOPMENT_UNOFFICIAL depending on configuration.
	 */
	public static Classification unofficial() {
		if (ClassificationConfig.productionMode()) {
			return new Classification(PSPFClassification.UNOFFICIAL, null);
		}
		return new Classification(null, DevelopmentClassification.DEVELOPMENT_UNOFFICIAL);
	}
	
	/**
	 * Returns the {@link Classification} which represents OFFICIAL based on current configuration.
	 * 
	 * @return OFFICIAL or DEVELOPMENT_OFFICIAL depending on configuration.
	 */
	public static Classification official() {
		if (ClassificationConfig.productionMode()) {
			return new Classification(PSPFClassification.OFFICIAL, null);
		}
		return new Classification(null, DevelopmentClassification.DEVELOPMENT_OFFICIAL);
	}
	
	/**
	 * Returns the {@link Classification} which represents OFFICIAL: Sensitive based on current configuration.
	 * 
	 * @return OFFICIAL_SENSITIVE or DEVELOPMENT_OFFICIAL_SENSITIVE depending on configuration.
	 */
	public static Classification officialSensitive() {
		if (ClassificationConfig.productionMode()) {
			return new Classification(PSPFClassification.OFFICIAL_SENSITIVE, null);
		}
		return new Classification(null, DevelopmentClassification.DEVELOPMENT_OFFICIAL_SENSITIVE);
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
			return new Classification(PSPFClassification.PROTECTED, null);
		}
		return new Classification(null, DevelopmentClassification.DEVELOPMENT_PROTECTED);
	}
	
	/**
	 * Returns the {@link Classification} which represents SECRET based on current configuration.
	 * 
	 * @return SECRET or DEVELOPMENT_SECRET depending on configuration.
	 */
	public static Classification secret() {
		if (ClassificationConfig.productionMode()) {
			return new Classification(PSPFClassification.SECRET, null);
		}
		return new Classification(null, DevelopmentClassification.DEVELOPMENT_SECRET);
	}
	
	/**
	 * Returns the {@link Classification} which represents TOP SECRET based on current configuration.
	 * 
	 * @return TOP_SECRET or DEVELOPMENT_TOP_SECRET depending no configuration.
	 */
	public static Classification topSecret() {
		if (ClassificationConfig.productionMode()) {
			return new Classification(PSPFClassification.TOP_SECRET, null);
		}
		return new Classification(null, DevelopmentClassification.DEVELOPMENT_TOP_SECRET);
	}
	
	/**
	 * Gets the {@link Classification} for which the given text is the {@code toString()} value.
	 * <p>
	 * Only one of the {@link Classification} enumerations will be searched, depending on current configuration.
	 * 
	 * @param text the String to convert to a {@link Classification}
	 * @return the {@link Classification} which has {@code text} as it's {@code toString()} value.
	 * @throws IllegalArgumentException if unable to map the given text parameter to a {@link Classification}
	 */
	public static Classification fromString(String text) {
		Objects.requireNonNull(text);
		if (ClassificationConfig.productionMode()) {
			for (PSPFClassification classification : PSPFClassification.values()) {
				if (classification.toString().equals(text)) {
					return new Classification(classification, null);
				}
			}
			throw new IllegalArgumentException("Unable to map string \"" + text + "\" to a Classification");
		}
		else {
			for (DevelopmentClassification developmentClassification : DevelopmentClassification.values()) {
				if (developmentClassification.toString().equals(text)) {
					return new Classification(null, developmentClassification);
				}
			}
			throw new IllegalArgumentException("Unable to map string \"" + text + "\" to a DevelopmentClassification");
		}
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
			return this.pspfClassification().compareTo(other.pspfClassification());
		}
		return this.developmentClassification().compareTo(other.developmentClassification());
	}
}

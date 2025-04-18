package trquinn.classification.aus.model;

import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import trquinn.classification.aus.ClassificationConfig;
import trquinn.classification.aus.model.deserializer.ClassificationDeserializer;

/**
 * An interface to represent a specific Classification, such as OFFICIAL or SECRET.
 * <p>
 * There are two enumerations which implement this interface, {@link PSPFClassification} and
 * {@link DevelopmentClassification}.
 * <p>
 * {@link PSPFClassification} represents the Classifications defined in the Australian Government Protective
 * Security Policy Framework.
 * <p>
 * {@link DevelopmentClassification} represents parallel Classifications, which should obviously map to the
 * real Classifications, and should be used in environments where data, and test data, should not be marked
 * with real Classifications.
 */
@JsonDeserialize(using = ClassificationDeserializer.class)
public interface Classification {

	/**
	 * This function makes the under laying enumeration implementations' {@code ordinal()} function available
	 * through the interface.
	 * 
	 * @return the enumeration ordinal number of the {@link Classification}.
	 */
	int ordinal();
	
	/**
	 * This function makes the under laying implementation's {@code toString()} function available through the
	 * interface.
	 * 
	 * @return a String representation of the {@link Classification}.
	 */
	String toString();
	
	/**
	 * Ensures the implementing enumerations {@code hashCode()} function is exposed.
	 * 
	 * @return a hash number
	 */
	int hashCode();
	
	/**
	 * Ensures the implementing enumerations {@code equals()} function is exposed.
	 * 
	 * @param obj the object to compare for equality.
	 * @return true if the objects are deeply equal, false otherwise.
	 */
	boolean equals(Object obj);
	
	/**
	 * Returns the {@link Classification} which represents UNOFFICIAL based on current configuration.
	 * 
	 * @return UNOFFICIAL or DEVELOPMENT_UNOFFICIAL depending on configuration.
	 */
	public static Classification unofficial() {
		if (ClassificationConfig.productionMode()) {
			return PSPFClassification.UNOFFICIAL;
		}
		return DevelopmentClassification.DEVELOPMENT_UNOFFICIAL;
	}
	
	/**
	 * Returns the {@link Classification} which represents OFFICIAL based on current configuration.
	 * 
	 * @return OFFICIAL or DEVELOPMENT_OFFICIAL depending on configuration.
	 */
	public static Classification official() {
		if (ClassificationConfig.productionMode()) {
			return PSPFClassification.OFFICIAL;
		}
		return DevelopmentClassification.DEVELOPMENT_OFFICIAL;
	}
	
	/**
	 * Returns the {@link Classification} which represents OFFICIAL: Sensitive based on current configuration.
	 * 
	 * @return OFFICIAL_SENSITIVE or DEVELOPMENT_OFFICIAL_SENSITIVE depending on configuration.
	 */
	public static Classification officialSensitive() {
		if (ClassificationConfig.productionMode()) {
			return PSPFClassification.OFFICIAL_SENSITIVE;
		}
		return DevelopmentClassification.DEVELOPMENT_OFFICIAL_SENSITIVE;
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
			return PSPFClassification.PROTECTED;
		}
		return DevelopmentClassification.DEVELOPMENT_PROTECTED;
	}
	
	/**
	 * Returns the {@link Classification} which represents SECRET based on current configuration.
	 * 
	 * @return SECRET or DEVELOPMENT_SECRET depending on configuration.
	 */
	public static Classification secret() {
		if (ClassificationConfig.productionMode()) {
			return PSPFClassification.SECRET;
		}
		return DevelopmentClassification.DEVELOPMENT_SECRET;
	}
	
	/**
	 * Returns the {@link Classification} which represents TOP SECRET based on current configuration.
	 * 
	 * @return TOP_SECRET or DEVELOPMENT_TOP_SECRET depending no configuration.
	 */
	public static Classification topSecret() {
		if (ClassificationConfig.productionMode()) {
			return PSPFClassification.TOP_SECRET;
		}
		return DevelopmentClassification.DEVELOPMENT_TOP_SECRET;
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
					return classification;
				}
			}
			throw new IllegalArgumentException("Unable to map string \"" + text + "\" to a Classification");
		}
		else {
			for (DevelopmentClassification classification : DevelopmentClassification.values()) {
				if (classification.toString().equals(text)) {
					return classification;
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
}

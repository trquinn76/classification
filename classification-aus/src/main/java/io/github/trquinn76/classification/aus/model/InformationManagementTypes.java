package io.github.trquinn76.classification.aus.model;

import java.util.Objects;

/**
 * These are a subset of the controlled list of terms for the `Rights Type` property in the National Archives of
 * Australia's Australian Government Record Keeping Metadata Standard.
 * <p>
 * Information management markers are not protective markers or security classifications.
 * <p>
 * See section 9.4 Information Management Markers of the PSPF Guidelines, for a description of these markers. 
 */
public enum InformationManagementTypes {

	LEGAL_PRIVILEGE("Legal Privilege"),
	LEGISLATIVE_SECRECY("Legislative Secrecy"),
	PERSONAL_PRIVACY("Personal Privacy");
	
	private final String text;
	
	private InformationManagementTypes(String text) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return this.text;
	}
	
	/**
	 * Will return the {@link InformationManagementTypes} which has the given text as it's {@code toString()} result.
	 * 
	 * @param text the String for which to find the matching {@link InformationManagementTypes}.
	 * @return the {@link InformationManagementTypes} for the given string.
	 * @throws IllegalArgumentException if the given String does not match any {@link InformationManagementTypes}.
	 */
	public static InformationManagementTypes fromString(String text) {
		Objects.requireNonNull(text);
		for (InformationManagementTypes imt : InformationManagementTypes.values()) {
			if (imt.toString().equals(text)) {
				return imt;
			}
		}
		throw new IllegalArgumentException("Unable to map string \"" + text + "\" to an InformationManagementTypes");
	}
	
	/**
	 * Returns if the given String is the result of the {@code toString()} function of an
	 * {@link InformationManagementTypes}. 
	 * 
	 * @param text the String to check.
	 * @return if the given text equals the result of the {@code toString()} function on any 
	 * 		{@link InformationManagementTypes} will return true, otherwise will return false.
	 */
	public static boolean isString(String text) {
		for (InformationManagementTypes imt : InformationManagementTypes.values()) {
			if (imt.toString().equals(text)) {
				return true;
			}
		}
		return false;
	}
}

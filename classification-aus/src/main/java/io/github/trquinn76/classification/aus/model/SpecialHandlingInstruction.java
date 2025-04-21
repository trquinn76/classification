package io.github.trquinn76.classification.aus.model;

import java.util.Objects;

/**
 * The set of {@link SpecialHandlingInstruction}'s supported.
 * <p>
 * See the Australian Government Email Protective Marking Standard section 7.1, Table 1 for the
 * list of instructions.
 */
public enum SpecialHandlingInstruction {

	DELICATE_SOURCE("DELICATE-SOURCE"),
	ORCON("ORCON"),
	EXCLUSIVE_FOR("EXCLUSIVE-FOR"),
	CABINET("CABINET"),
	NATIONAL_CABINET("NATIONAL-CABINET"); // obsolete!
	
	private final String text;
	
	private SpecialHandlingInstruction(String text) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return this.text;
	}
	
	/**
	 * Maps from a String back to a {@link SpecialHandlingInstruction}.
	 * 
	 * @param text the text to map.
	 * @return the {@link SpecialHandlingInstruction} for which the given text equals to result of the
	 * {@code toString()} function.
	 * @throws IllegalArgumentException if the given String does not map to any {@link SpecialHandlingInstruction}.
	 */
	public static SpecialHandlingInstruction fromString(String text) {
		Objects.requireNonNull(text);
		for (SpecialHandlingInstruction shi : SpecialHandlingInstruction.values()) {
			if (shi.toString().equals(text)) {
				return shi;
			}
		}
		throw new IllegalArgumentException("Unable to map string \"" + text + "\" to a SpecialHandlingInstruction");
	}
	
	/**
	 * Returns true if the given text will map to a {@link SpecialHandlingInstruction}.
	 * @param text the text to check for a matching {@link SpecialHandlingInstruction}.
	 * @return true if the text will map to a {@link SpecialHandlingInstruction}. false otherwise.
	 */
	public static boolean isString(String text) {
		for (SpecialHandlingInstruction shi : SpecialHandlingInstruction.values()) {
			if (shi.toString().equals(text)) {
				return true;
			}
		}
		return false;
	}
}

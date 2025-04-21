package io.github.trquinn76.classification.aus.model;

import java.util.Objects;

/**
 * Represents a Special Handling Instruction applied to a {@link ProtectiveMarker}.
 * 
 * @param instruction the {@link SpecialHandlingInstruction} for this caveat.
 * @param exclusiveFor a String representing an addressee who may receive this data. Should only be set if
 * 		{@code instruction} is EXCLUSIVE_FOR.
 */
public record SpecialHandlingCaveat (SpecialHandlingInstruction instruction, String exclusiveFor) {
	
	/**
	 * Construction ensuring {@code instruction} is not null.
	 * 
	 * @param instruction the {@link SpecialHandlingInstruction} for this caveat.
	 * @param exclusiveFor a String representing an addressee who may receive this data. Should only be set if
	 * 		{@code instruction} is EXCLUSIVE_FOR.
	 */
	public SpecialHandlingCaveat {
		Objects.requireNonNull(instruction);
	}
	
	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append(this.instruction.name());
		if (this.exclusiveFor != null) {
			buf.append(" ").append(this.exclusiveFor);
		}
		return buf.toString();
	}
}

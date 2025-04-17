package trquinn.classification.aus.model;

import java.util.List;
import java.util.Objects;

/**
 * The {@link SecurityCaveats} contains code words, foreign government markings,
 * special handling caveats and releasability.
 * 
 * @param codeWords                 a List of Code Words/Compartments. May be
 *                                  empty, may not be null.
 * @param foreignGovernmentMarkings a List of any foreign government markings.
 *                                  May be empty, may not be null.
 * @param specialHandlingCaveat     any Special Handling Caveat, or null.
 * @param releasabilityCaveat       a releasability caveat, or null.
 */
public record SecurityCaveats(List<String> codeWords, List<String> foreignGovernmentMarkings,
		SpecialHandlingCaveat specialHandlingCaveat, ReleasabilityCaveat releasabilityCaveat) {

	/**
	 * Constructor performs defensive copying of lists.
	 * 
	 * @param codeWords                 a List of Code Words/Compartments. May be
	 *                                  empty, may not be null.
	 * @param foreignGovernmentMarkings a List of any foreign government markings.
	 *                                  May be empty, may not be null.
	 * @param specialHandlingCaveat     any Special Handling Caveat, or null.
	 * @param releasabilityCaveat       a releasability caveat, or null.
	 */
	public SecurityCaveats {
		Objects.requireNonNull(codeWords);
		Objects.requireNonNull(foreignGovernmentMarkings);
		codeWords = List.copyOf(codeWords);
		foreignGovernmentMarkings = List.copyOf(foreignGovernmentMarkings);
	}

	/**
	 * Overridden to produce a String representation of the {@link SecurityCaveats}
	 * which is human readable, and follows the order and pattern described in the
	 * Australian Government Email Protective Marking Standard.
	 */
	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		this.codeWords.forEach(codeWord -> {
			if (buf.length() > 0)
				buf.append(" ");
			buf.append(codeWord);
		});
		this.foreignGovernmentMarkings.forEach(marking -> {
			if (buf.length() > 0)
				buf.append(" ");
			buf.append(marking);
		});
		if (this.specialHandlingCaveat != null) {
			if (buf.length() > 0)
				buf.append(" ");
			buf.append(this.specialHandlingCaveat.toString());
		}
		if (this.releasabilityCaveat != null) {
			if (buf.length() > 0)
				buf.append(" ");
			buf.append(this.releasabilityCaveat.toString());
		}
		return buf.toString();
	}
}

package trquinn.classification.aus.model;

import java.util.List;
import java.util.Objects;

/**
 * Represents the Releasability of Classified data.
 * <p>
 * The {@code releasableToList} should only be populated when the {@link ReleasabilityType} is {@code REL}. It is
 * expected to be populated with country trigraphs, as defined in the {@code ISO 3166-1 alpha-3} list. This is not
 * enforced.
 * <p>
 * When {@code releasableToList}'s exist they are required to have a minimum length of 2, and they must contain the
 * Australian trigraph, 'AUS'.
 * 
 * @param type the {@link ReleasabilityType} defining the releasability of the record.
 * @param releasableToList if the {@code type} is {@code REL}, then a releasable to lists is required.
 */
public record ReleasabilityCaveat (ReleasabilityType type, List<String> releasableToList) {

	/**
	 * Constructor ensuring parameters are not null, and performing a defensive copy of the releasable to list.
	 * 
	 * @param type the {@link ReleasabilityType} defining the releasability of the record.
	 * @param releasableToList if the {@code type} is {@code REL}, then a releasable to lists is required.
	 */
	public ReleasabilityCaveat {
		Objects.requireNonNull(type);
		Objects.requireNonNull(releasableToList);
		releasableToList = List.copyOf(releasableToList);
	}
	
	/**
	 * Overridden to provide a human readable String suitable for use in displaying security releabilities.
	 */
	@Override
	public String toString() {
		StringBuilder buff = new StringBuilder();
		buff.append(type.name());
		if (!releasableToList.isEmpty()) {
			buff.append(" ");
			for (int i = 0; i < releasableToList.size(); i++) {
				if (i > 0) buff.append("/");
				buff.append(releasableToList.get(i));
			}
		}
		return buff.toString();
	}
}

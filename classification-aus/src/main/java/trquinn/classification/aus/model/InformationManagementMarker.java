package trquinn.classification.aus.model;

import java.util.List;
import java.util.Objects;

/**
 * Represents an Information Management Marker, as used in a {@link ProtectiveMarker}.
 * 
 * @param type a {@link InformationManagementTypes} which may not be null.
 * @param legislationSecrecyWarnings a list of warning Strings for the {@code LEGISLATIVE_SECRECY} type. May be empty.
 * 		May not be null.
 */
public record InformationManagementMarker(InformationManagementTypes type, List<String> legislationSecrecyWarnings) {

	/**
	 * Checks that required parameters are not null, and perform defensive copy of list.
	 * 
	 * @param type the {@link InformationManagementTypes} this marker is for.
	 * @param legislationSecrecyWarnings Legislative Secrecy Warning Strings which should only exist the
	 * 		{@code LEGISLATIVE_SECRECY} type. May not be null. May be empty.
	 */
	public InformationManagementMarker {
		Objects.requireNonNull(type);
		Objects.requireNonNull(legislationSecrecyWarnings);
		legislationSecrecyWarnings = List.copyOf(legislationSecrecyWarnings);
	}
	
	/**
	 * Provides a human readable string for the Information Management Marker, which is appropriate for appending to
	 * Protective Marker strings.
	 */
	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append(this.type.name());
		this.legislationSecrecyWarnings.forEach(warning -> {
			buf.append(" \"").append(warning).append("\"");
		});
		return buf.toString();
	}
}

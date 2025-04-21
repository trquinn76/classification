package io.github.trquinn76.classification.aus.model;

import java.util.Objects;
import java.util.List;

/**
 * {@link ProtectiveMarker} represents Protective Markings as defined in the
 * Australian Government Protective Security Policy Framework.
 * <p>
 * A {@link ProtectiveMarker} is made of three parts.
 * <ul>
 * <li>{@link Classification} - Either a {@link PSPFClassification} or a
 * {@link DevelopmentClassification} depending on configuration.</li>
 * <li>Information Management Markers - based on a subset of the controlled list
 * of terms for the `Rights Type` property in the National Archives of
 * Australia's Australian Government Record Keeping Metadata Standard.</li>
 * <li>{@link SecurityCaveats} - other security related markings, including code
 * words, releasability and special handling instructions.</li>
 * </ul>
 * 
 * @param classification the {@link Classification} to set. May not be null.
 * @param informationManagementMarkers the list of {@link InformationManagementMarker}. May be empty. May not be null.
 * @param securityCaveats a {@link SecurityCaveats}, or null.
 */
public record ProtectiveMarker(Classification classification,
		List<InformationManagementMarker> informationManagementMarkers, SecurityCaveats securityCaveats) {

	/**
	 * Constructor ensuring {@code classification} and {@code informationManagementMarkers} are not null.
	 * <p>
	 * Also performs defensive copy of the {@code informationManagementMarkers} list.
	 * 
	 * @param classification the {@link Classification} to set. May not be null.
	 * @param informationManagementMarkers the list of {@link InformationManagementMarker}. May be empty. May not be null.
	 * @param securityCaveats a {@link SecurityCaveats}, or null.
	 */
	public ProtectiveMarker {
		Objects.requireNonNull(classification);
		Objects.requireNonNull(informationManagementMarkers);
		informationManagementMarkers = List.copyOf(informationManagementMarkers);
	}

	/**
	 * Overridden to provide a good human readable String value.
	 */
	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append(this.classification.toString());
		if (this.securityCaveats != null) {
			buf.append(" ").append(this.securityCaveats.toString());
		}
		if (!this.informationManagementMarkers.isEmpty()) {
			this.informationManagementMarkers.forEach(marker -> buf.append(" ").append(marker));
		}
		return buf.toString();
	}
}

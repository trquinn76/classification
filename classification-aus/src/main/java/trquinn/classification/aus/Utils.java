package trquinn.classification.aus;

import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import trquinn.classification.aus.model.ProtectiveMarker;
import trquinn.classification.aus.model.ReleasabilityCaveat;
import trquinn.classification.aus.model.ReleasabilityType;

/**
 * Utility functions and constants for the Classification scheme.
 */
public class Utils {
	
	// Five Eyes Country Codes
	public static final String AUS = "AUS";
	public static final String CAN = "CAN";
	public static final String GBR = "GBR";
	public static final String NZL = "NZL";
	public static final String USA = "USA";
	/** Five Eyes Set */
	public static final Set<String> FIVE_EYES = Set.of(AUS, CAN, GBR, NZL, USA);
	
	/**
	 * A {@link Comparator} which will sort 'AUS' first, and then remaining values in alphabetic order.
	 */
	public static Comparator<String> AUS_FIRST = new Comparator<>() {
		@Override
		public int compare(String alpha, String beta) {
			int retval = alpha.compareTo(beta);
			if (alpha == AUS) {
				retval = -1;
			}
			else if (beta == "AUS") {
				retval = 1;
			}
			return retval;
		}
	};
	
	/**
	 * A {@link Comparator} which will sort with the Five Eyes countries in alphabetical order first, and then
	 * remaining countries in alphabetical order.
	 */
	public static Comparator<String> FIVE_EYES_FIRST = new Comparator<>() {
		@Override
		public int compare(String alpha, String beta) {
			int retval = alpha.compareTo(beta);
			if (FIVE_EYES.contains(alpha) && !FIVE_EYES.contains(beta)) {
				retval = -1;
			}
			else if (!FIVE_EYES.contains(alpha) && FIVE_EYES.contains(beta)) {
				retval = 1;
			}
			return retval;
		}
	};
	
	/**
	 * Will merge the two {@link ProtectiveMarker}'s together.
	 * 
	 * The returned {@link ProtectiveMarkerBuilder} will have the highest
	 * {@link trquinn.classification.aus.model.Classification}, a combined list of Codewords, and Foreign Markings,
	 * and the least restrictive Releasability, which still abides by all releasability markings.
	 * <p>
	 * Information Management Markings, and Special Handling Instructions are NOT merged, and are not included in
	 * the return {@link ProtectiveMarkerBuilder}.
	 * <p>
	 * The returned {@link ProtectiveMarkerBuilder} may be in an invalid state, and may need additional changes to
	 * be able to build a valid {@link ProtectiveMarker}.
	 * 
	 * @param alpha one of the {@link ProtectiveMarker}'s to merge. May not be null.
	 * @param beta the second {@link ProtectiveMarker} to merge. May be null.
	 * @return a {@link ProtectiveMarkerBuilder}.
	 */
	public static ProtectiveMarkerBuilder merge(ProtectiveMarker alpha, ProtectiveMarker beta) {
		Objects.requireNonNull(alpha);
		ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder(alpha);
		builder.clearInformationManagementMarkers().setSpecialHandlingInstruction(null).setSpecialHandlingExclusiveFor(null);
		if (beta != null) {
			builder = merge(builder, beta);
		}
		return builder;
	}
	
	/**
	 * Merges the {@code Collection} of {@link ProtectiveMarker}'s into a single {@link ProtectiveMarkerBuilder}.
	 * 
	 * The returned {@link ProtectiveMarkerBuilder} will have the highest
	 * {@link trquinn.classification.aus.model.Classification}, a combined list of Codewords, and Foreign Markings,
	 * and the least restrictive Releasability, which still abides by all releasability markings.
	 * <p>
	 * Information Management Markings, and Special Handling Instructions are NOT merged, and are not included in
	 * the return {@link ProtectiveMarkerBuilder}.
	 * <p>
	 * The returned {@link ProtectiveMarkerBuilder} may be in an invalid state, and may need additional changes to
	 * be able to build a valid {@link ProtectiveMarker}.
	 * <p>
	 * If the {@code Collection} is empty, this function will return null.
	 * 
	 * @param protectiveMarkings the {@code Collection} of {@link ProtectiveMarker}'s to merge. May not be null. May be empty.
	 * @return a {@link ProtectiveMarkerBuilder} which represents the merging of the given {@link ProtectiveMarker}'s. Will be null if the {@code Collection} is empty.
	 */
	public static ProtectiveMarkerBuilder merge(Collection<ProtectiveMarker> protectiveMarkings) {
		Objects.requireNonNull(protectiveMarkings);
		ProtectiveMarkerBuilder builder = null;
		for (ProtectiveMarker marking : protectiveMarkings) {
			if (builder == null) {
				builder = new ProtectiveMarkerBuilder(marking);
				builder.clearInformationManagementMarkers().setSpecialHandlingInstruction(null).setSpecialHandlingExclusiveFor(null);
			}
			else {
				builder = merge(builder, marking);
			}
		}
		return builder;
	}
	
	/**
	 * This function merges the given {@link ProtectiveMarker} into the existing {@link ProtectiveMarkerBuilder}.
	 * 
	 * The returned {@link ProtectiveMarkerBuilder} will have the highest
	 * {@link trquinn.classification.aus.model.Classification}, a combined list of Codewords, and Foreign Markings,
	 * and the least restrictive Releasability, which still abides by all releasability markings.
	 * <p>
	 * Information Management Markings, and Special Handling Instructions are NOT merged, and are not included in
	 * the return {@link ProtectiveMarkerBuilder}.
	 * <p>
	 * The returned {@link ProtectiveMarkerBuilder} may be in an invalid state, and may need additional changes to
	 * be able to build a valid {@link ProtectiveMarker}.
	 * 
	 * @param builder the {@link ProtectiveMarkerBuilder} to modify with the merging of the {@code marking}.
	 * @param marking the {@link ProtectiveMarker} to merge into the {@code builder}.
	 * @return the {@code builder} modified by merging the given {@link ProtectiveMarker}.
	 */
	protected static ProtectiveMarkerBuilder merge(ProtectiveMarkerBuilder builder, ProtectiveMarker marking) {
		Objects.requireNonNull(builder);
		if (marking != null) {
			if (builder.getClassificiation() == null || builder.getClassificiation().ordinal() < marking.classification().ordinal()) {
				builder.setClassification(marking.classification());
			}
			// no attempt is made to merge Information Management Markers.
			if (marking.securityCaveats() != null) {
				marking.securityCaveats().codeWords().forEach(codeWord -> builder.addCodeword(codeWord));
				marking.securityCaveats().foreignGovernmentMarkings().forEach(fgm -> builder.addForeignGovernmentMarking(fgm));
				// no attempt is made to merge Special Handling Instructions.
				if (marking.securityCaveats().releasabilityCaveat() != null) {
					mergeReleasabilityCaveats(builder, marking.securityCaveats().releasabilityCaveat());
				}
			}
		}
		return builder;
	}
	
	private static void mergeReleasabilityCaveats(ProtectiveMarkerBuilder builder, ReleasabilityCaveat releasabilityCaveat) {
		Objects.requireNonNull(builder);
		Objects.requireNonNull(releasabilityCaveat);
		if (builder.haveReleasabilityCaveat()) {
			if (releasabilityCaveat.type().ordinal() < builder.getReleasabilityType().ordinal()) {
				builder.setReleasability(releasabilityCaveat.type());
				if (releasabilityCaveat.type() != ReleasabilityType.REL) {
					builder.clearReleasableToList();
				}
			}
			if (ReleasabilityType.REL == releasabilityCaveat.type() && ReleasabilityType.REL == builder.getReleasabilityType()) {
				Set<String> existingCountries = builder.getReleasableToList();
				Set<String> newCountries = new TreeSet<>(releasabilityCaveat.releasableToList());
				
				if (existingCountries.retainAll(newCountries)) {
					builder.setReleasableToList(existingCountries);
				}
			}
		}
		else {
			builder.setReleasability(releasabilityCaveat.type());
			releasabilityCaveat.releasableToList().forEach(countryCode -> builder.addReleasableToCountry(countryCode));
		}
	}
	
	private Utils() {}
}

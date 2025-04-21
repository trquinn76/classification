package io.github.trquinn76.classification.aus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import io.github.trquinn76.classification.aus.model.Classification;
import io.github.trquinn76.classification.aus.model.InformationManagementMarker;
import io.github.trquinn76.classification.aus.model.InformationManagementTypes;
import io.github.trquinn76.classification.aus.model.ProtectiveMarker;
import io.github.trquinn76.classification.aus.model.ReleasabilityCaveat;
import io.github.trquinn76.classification.aus.model.ReleasabilityType;
import io.github.trquinn76.classification.aus.model.SecurityCaveats;
import io.github.trquinn76.classification.aus.model.SpecialHandlingCaveat;
import io.github.trquinn76.classification.aus.model.SpecialHandlingInstruction;

/**
 * A builder for {@link ProtectiveMarker}'s.
 * 
 * This class performs 2 primary jobs. One is obviously supporting the Builder Pattern for {@link ProtectiveMarker}'s.
 * The second is being an object which can hold the values of a {@link ProtectiveMarker} in an invalid state,
 * specifically in support of UI components.
 * <p>
 * A number of lists in the {@link ProtectiveMarker}'s data structure are modeled with Sets in this class.
 * This allows sorting and elimination of duplicate values. When the {@link ProtectiveMarkerBuilder} performs the
 * actual build, these Sets are converted to Lists as necessary.
 */
public class ProtectiveMarkerBuilder {
	
	private static final Logger LOGGER = Logger.getLogger(ProtectiveMarkerBuilder.class.getCanonicalName());

	private Classification classification = null;
	private Set<InformationManagementTypes> informationManagementTypes = new TreeSet<>();
	private Set<String> immLegislativeSecrecyWarnings = new TreeSet<>();
	private Set<String> codeWords = new TreeSet<>();
	private Set<String> foreignGovernmentMarkings = new TreeSet<>();
	private SpecialHandlingInstruction specialHandlingInstruction = null;
	private String specialHandlingExclusiveFor = null;
	private ReleasabilityType releasabilityType = null;
	private Set<String> releasableToList = new TreeSet<>(ClassificationConfig.releasableToOrder());

	public ProtectiveMarkerBuilder() {

	}

	/**
	 * A copy constructor.
	 * 
	 * Creates an instance of {@link ProtectiveMarkerBuilder} which is populated with the values from the given
	 * {@code protectiveMarker} parameter.
	 * 
	 * @param protectiveMarker the {@link ProtectiveMarker} with which to populate the builder.
	 */
	public ProtectiveMarkerBuilder(ProtectiveMarker protectiveMarker) {
		this.classification = protectiveMarker.classification();
		protectiveMarker.informationManagementMarkers().forEach(imm -> {
			this.informationManagementTypes.add(imm.type());
			if (imm.type() == InformationManagementTypes.LEGISLATIVE_SECRECY) {
				this.immLegislativeSecrecyWarnings.addAll(imm.legislationSecrecyWarnings());
			}
		});
		SecurityCaveats sc = protectiveMarker.securityCaveats();
		if (sc != null) {
			this.codeWords.addAll(sc.codeWords());
			this.foreignGovernmentMarkings.addAll(sc.foreignGovernmentMarkings());
			SpecialHandlingCaveat shc = sc.specialHandlingCaveat();
			if (shc != null) {
				this.specialHandlingInstruction = shc.instruction();
				this.specialHandlingExclusiveFor = shc.exclusiveFor();
			}
			ReleasabilityCaveat releasability = sc.releasabilityCaveat();
			if (releasability != null) {
				this.releasabilityType = releasability.type();
				this.releasableToList.addAll(releasability.releasableToList());
			}
		}
	}

	/**
	 * Sets the {@link Classification}.
	 * 
	 * @param classification the {@link Classification} to set. May be null.
	 * @return this for function chaining.
	 */
	public ProtectiveMarkerBuilder setClassification(Classification classification) {
		this.classification = classification;
		return this;
	}

	/**
	 * Gets the {@link Classification} set in this builder.
	 * 
	 * @return the currently set {@link Classification}.
	 */
	public Classification getClassificiation() {
		return this.classification;
	}
	
	/**
	 * Sets the {@link Classification} to UNOFFICIAL.
	 * 
	 * @return this for function chaining.
	 */
	public ProtectiveMarkerBuilder unofficial() {
		this.classification = Classification.unofficial();
		return this;
	}
	
	/**
	 * Sets the {@link Classification} to OFFICIAL.
	 * 
	 * @return this for function chaining.
	 */
	public ProtectiveMarkerBuilder official() {
		this.classification = Classification.official();
		return this;
	}
	
	/**
	 * Sets the {@link Classification} to OFFICIAL: Sensitive.
	 * 
	 * @return this for function chaining.
	 */
	public ProtectiveMarkerBuilder officialSensitive() {
		this.classification = Classification.officialSensitive();
		return this;
	}
	
	/**
	 * Sets the {@link Classification} to PROTECTED.
	 * 
	 * As 'protected' is a reserve word in Java, it is not possible to use that name for the function.
	 * 
	 * @return this for function chaining.
	 */
	public ProtectiveMarkerBuilder protect() {
		this.classification = Classification.protect();
		return this;
	}
	
	/**
	 * Sets the {@link Classification} to SECRET.
	 * 
	 * @return this for function chaining.
	 */
	public ProtectiveMarkerBuilder secret() {
		this.classification = Classification.secret();
		return this;
	}
	
	/**
	 * Sets the {@link Classification} to TOP SECRET.
	 * 
	 * @return this for function chaining.
	 */
	public ProtectiveMarkerBuilder topSecret() {
		this.classification = Classification.topSecret();
		return this;
	}

	/**
	 * Adds the LEGAL_PRIVILEGE {@link InformationManagementMarker} to the builder.
	 * 
	 * @return this for function chaining.
	 */
	public ProtectiveMarkerBuilder legalPrivilege() {
		this.informationManagementTypes.add(InformationManagementTypes.LEGAL_PRIVILEGE);
		return this;
	}

	/**
	 * Adds the LEGISLATIVE_SECRECY {@link InformationManagementMarker} to the builder.
	 * 
	 * This function will replace any existing Legislative Secrecy Warning Strings with the new given warning.
	 * 
	 * @param warning the warning string associated with the LEGISLATIVE_SECRECY marker. May not be null.
	 * @return this for function chaining.
	 * @throws IllegalArgumentException if {@code warning} is missing or blank.
	 */
	public ProtectiveMarkerBuilder legislativeSecrecy(String warning) {
		if (warning == null || warning.isBlank()) {
			throw new IllegalArgumentException(
					"A Legislative Secrecy Information Management Marker requires a Warning string");
		}
		this.informationManagementTypes.add(InformationManagementTypes.LEGISLATIVE_SECRECY);
		this.immLegislativeSecrecyWarnings.clear();
		this.immLegislativeSecrecyWarnings.add(warning);
		return this;
	}
	
	/**
	 * Adds the LEGISLATIVE_SECRECY {@link InformationManagementMarker} to the builder.
	 * 
	 * This function will add the given Legislative Secrecy Warning String to the collection of warning Strings. When
	 * a build occurs, the collection of warnings will be combined into a single String.
	 * <p>
	 * The only obvious use for this function is during {@link ProtectiveMarker} merges.
	 * 
	 * @param warning the warning string associated with the LEGISLATIVE_SECRECY marker. May not be null.
	 * @return this for function chaining.
	 * @throws IllegalArgumentException if {@code warning} is missing or blank.
	 */
	public ProtectiveMarkerBuilder addLegislativeSecrecy(String warning) {
		if (warning == null || warning.isBlank()) {
			throw new IllegalArgumentException(
					"A Legislative Secrecy Information Management Marker requires a Warning string");
		}
		this.informationManagementTypes.add(InformationManagementTypes.LEGISLATIVE_SECRECY);
		this.immLegislativeSecrecyWarnings.add(warning);
		return this;
	}

	/**
	 * Adds the PERSONAL_PRIVACY {@link InformationManagementMarker} to the builder.
	 * 
	 * @return this for function chaining.
	 */
	public ProtectiveMarkerBuilder personalPrivacy() {
		this.informationManagementTypes.add(InformationManagementTypes.PERSONAL_PRIVACY);
		return this;
	}

	/**
	 * Returns a List of the, up to three, {@link InformationManagementMarker}.
	 * 
	 * @return a copy of the current List of {@link InformationManagementMarker}. May be empty. Will not be null.
	 */
	public List<InformationManagementMarker> getInformationManagementMarkers() {
		return buildInformationManagementMarkers();
	}

	/**
	 * Returns true if there is at least one {@link InformationManagementMarker} set.
	 * 
	 * @return true if there are {@link InformationManagementMarker}'s, false otherwise.
	 */
	public boolean haveInformationManagementMarkers() {
		return this.informationManagementTypes.size() > 0;
	}

	/**
	 * Clears existing {@link InformationManagementMarker}.
	 * 
	 * @return this for function chaining.
	 */
	public ProtectiveMarkerBuilder clearInformationManagementMarkers() {
		this.informationManagementTypes.clear();
		this.immLegislativeSecrecyWarnings.clear();
		return this;
	}

	/**
	 * Sets the code words to the given Set.
	 * 
	 * @param codes a set of String's representing code words, or compartments. May not be null.
	 * @return this for function chaining.
	 */
	public ProtectiveMarkerBuilder setCodeWords(Set<String> codes) {
		Objects.requireNonNull(codes);
		this.codeWords = new TreeSet<>(codes);
		return this;
	}

	/**
	 * Adds the given code word to the existing list of code words.
	 * 
	 * @param codeWord the code word to add. May not be null.
	 * @return this for function chaining.
	 */
	public ProtectiveMarkerBuilder addCodeword(String codeWord) {
		Objects.requireNonNull(codeWord);
		this.codeWords.add(codeWord);
		return this;
	}

	/**
	 * Gets a copy of the internal code words set.
	 * 
	 * @return a copy of the set of current code words.
	 */
	public Set<String> getCodewords() {
		return new TreeSet<>(this.codeWords);
	}

	/**
	 * Clears the existing code words from the builder.
	 * 
	 * @return this for function chaining.
	 */
	public ProtectiveMarkerBuilder clearCodeWords() {
		this.codeWords.clear();
		return this;
	}

	/**
	 * Sets the foreign government markings to the given Set.
	 * 
	 * @param markings a set of String's representing foreign government markings. May not be null.
	 * @return this for function chaining.
	 */
	public ProtectiveMarkerBuilder setForeignGovernmentMarkings(Set<String> markings) {
		Objects.requireNonNull(markings);
		this.foreignGovernmentMarkings = new TreeSet<>(markings);
		return this;
	}

	/**
	 * Adds the given foreign government markings to the existing list of foreign government markings.
	 * 
	 * @param marking the foreign government marking to add. May not be null.
	 * @return this for function chaining.
	 */
	public ProtectiveMarkerBuilder addForeignGovernmentMarking(String marking) {
		Objects.requireNonNull(marking);
		this.foreignGovernmentMarkings.add(marking);
		return this;
	}

	/**
	 * Gets a copy of the internal foreign government markings set.
	 * 
	 * @return a copy of the set of current foreign government markings.
	 */
	public Set<String> getForeignGovernmentMarkings() {
		return new TreeSet<>(this.foreignGovernmentMarkings);
	}

	/**
	 * Clears the existing foreign government markings from the builder.
	 * 
	 * @return this for function chaining.
	 */
	public ProtectiveMarkerBuilder clearForeignGovernmentMarkers() {
		this.foreignGovernmentMarkings.clear();
		return this;
	}

	/**
	 * Set a {@link SpecialHandlingInstruction}.
	 * 
	 * @param instruction the {@link SpecialHandlingInstruction} to set. May be null.
	 * @return this for function chaining.
	 */
	public ProtectiveMarkerBuilder setSpecialHandlingInstruction(SpecialHandlingInstruction instruction) {
		this.specialHandlingInstruction = instruction;
		return this;
	}

	/**
	 * Gets the {@link SpecialHandlingInstruction} which is currently set.
	 * 
	 * @return the current {@link SpecialHandlingInstruction}. May be null.
	 */
	public SpecialHandlingInstruction getSpecialHandlingInstruction() {
		return this.specialHandlingInstruction;
	}

	/**
	 * Sets the addressee String for the EXCLUSIVE_FOR {@link SpecialHandlingInstruction}.
	 * 
	 * @param exclusiveFor the addressee String for the EXCLUSIVE_FOR {@link SpecialHandlingInstruction}. May be null.
	 * @return this for function chaining.
	 */
	public ProtectiveMarkerBuilder setSpecialHandlingExclusiveFor(String exclusiveFor) {
		this.specialHandlingExclusiveFor = exclusiveFor;
		return this;
	}

	/**
	 * Gets the current EXCLUSIVE_FOR addressee String.
	 * 
	 * @return the current EXCLUSIVE_FOR addressee, or null if it is unset.
	 */
	public String getSpecialHandlingExclusiveFor() {
		return this.specialHandlingExclusiveFor;
	}
	
	/**
	 * Returns true if there is a current {@link SpecialHandlingInstruction}.
	 * 
	 * @return true if there is a current {@link SpecialHandlingInstruction}, false otherwise.
	 */
	public boolean haveSpecialHandlingCaveat() {
		return this.specialHandlingInstruction != null;
	}
	
	/**
	 * Sets the {@link SpecialHandlingInstruction} to DELICATE_SOURCE.
	 * 
	 * @return this for function chaining.
	 */
	public ProtectiveMarkerBuilder delicateSource() {
		this.specialHandlingInstruction = SpecialHandlingInstruction.DELICATE_SOURCE;
		this.specialHandlingExclusiveFor = null;
		return this;
	}
	
	/**
	 * Sets the {@link SpecialHandlingInstruction} to ORCON.
	 * 
	 * @return this for function chaining.
	 */
	public ProtectiveMarkerBuilder orcon() {
		this.specialHandlingInstruction = SpecialHandlingInstruction.ORCON;
		this.specialHandlingExclusiveFor = null;
		return this;
	}
	
	/**
	 * Sets the {@link SpecialHandlingInstruction} to EXCLUSIVE_FOR.
	 * 
	 * @param addressee the target of the exclusive for instruction. May not be null or empty.
	 * 
	 * @return this for function chaining.
	 */
	public ProtectiveMarkerBuilder exclusiveFor(String addressee) {
		if (addressee == null || addressee.isBlank()) {
			throw new IllegalArgumentException(
					"An Exclusive For Special Handling Instruction requires an addressee string");
		}
		this.specialHandlingInstruction = SpecialHandlingInstruction.EXCLUSIVE_FOR;
		this.specialHandlingExclusiveFor = addressee;
		return this;
	}
	
	/**
	 * Sets the {@link SpecialHandlingInstruction} to CABINET.
	 * 
	 * @return this for function chaining.
	 */
	public ProtectiveMarkerBuilder cabinet() {
		this.specialHandlingInstruction = SpecialHandlingInstruction.CABINET;
		this.specialHandlingExclusiveFor = null;
		return this;
	}
	
	/**
	 * Clears existing Special Handling Instructions.
	 * 
	 * @return this for function chaining.
	 */
	public ProtectiveMarkerBuilder clearSpecialHandlingInstruction() {
		this.specialHandlingInstruction = null;
		this.specialHandlingExclusiveFor = null;
		return this;
	}

	/**
	 * Sets the {@link ReleasabilityType}.
	 * 
	 * If the {@link ReleasabilityType} is {@code REL}, will also add 'AUS' to the releasableYToList.
	 * 
	 * @param type the {@link ReleasabilityType} to set. May be null.
	 * @return this for function chaining.
	 */
	public ProtectiveMarkerBuilder setReleasability(ReleasabilityType type) {
		this.releasabilityType = type;
		if (ReleasabilityType.REL == type) {
			// Having set releasability type to REL am ensuring that AUS exists in the Releasable To list.
			this.releasableToList.add("AUS");
		}
		return this;
	}

	/**
	 * Returns the current {@link ReleasabilityType}.
	 * 
	 * @return the current {@link ReleasabilityType}.
	 */
	public ReleasabilityType getReleasabilityType() {
		return this.releasabilityType;
	}

	/**
	 * Sets the releasable to list.
	 * 
	 * The releasable to list is a list of trigraph country codes, as defined in the {@code ISO 3166-1 alpha-3} list.
	 * 
	 * @param releasableToList Set of country trigraphs. May be empty. May not be null.
	 * @return this for function chaining.
	 */
	public ProtectiveMarkerBuilder setReleasableToList(Set<String> releasableToList) {
		Objects.requireNonNull(releasableToList);
		this.releasableToList = new TreeSet<>(ClassificationConfig.releasableToOrder());
		this.releasableToList.addAll(releasableToList);
		return this;
	}

	/**
	 * Adds a trigraph country code, as defined in the {@code ISO 3166-1 alpha-3} list, to the releasable to list.
	 * 
	 * @param countryCode the trigraph country code to add to the releasable to list.
	 * @return this for function chaining.
	 */
	public ProtectiveMarkerBuilder addReleasableToCountry(String countryCode) {
		this.releasableToList.add(countryCode);
		return this;
	}

	/**
	 * Gets a copy of the current releasable to list.
	 * 
	 * @return a Set representing the trigraph country codes in the releasable to list. Iteration order will match the current order of the list of countries.
	 */
	public Set<String> getReleasableToList() {
		Set<String> releasableToList = new TreeSet<>(ClassificationConfig.releasableToOrder());
		releasableToList.addAll(this.releasableToList);
		return releasableToList;
	}
	
	/**
	 * Clears the existing releasable to list.
	 * 
	 * @return this for function chaining.
	 */
	public ProtectiveMarkerBuilder clearReleasableToList() {
		this.releasableToList.clear();
		return this;
	}
	
	/**
	 * Clears existing releasability values.
	 * 
	 * @return this for function chaining.
	 */
	public ProtectiveMarkerBuilder clearReleasability() {
		this.releasabilityType = null;
		return clearReleasableToList();
	}
	
	/**
	 * Returns true if a {@link ReleasabilityCaveat} will be needed to hold a {@link ReleasabilityType}.
	 * 
	 * @return true if there is a {@link ReleasabilityType} set, false otherwise.
	 */
	public boolean haveReleasabilityCaveat() {
		return this.releasabilityType != null;
	}
	
	/**
	 * Sets {@link ReleasabilityType} to AUSTEO.
	 * 
	 * @return this for function chaining.
	 */
	public ProtectiveMarkerBuilder austeo() {
		this.releasabilityType = ReleasabilityType.AUSTEO;
		this.releasableToList.clear();
		return this;
	}
	
	/**
	 * Sets {@link ReleasabilityType} to AGAO.
	 * 
	 * @return this for function chaining.
	 */
	public ProtectiveMarkerBuilder agao() {
		this.releasabilityType = ReleasabilityType.AGAO;
		this.releasableToList.clear();
		return this;
	}
	
	/**
	 * Sets {@link ReleasabilityType} to REL.
	 * 
	 * @param releasableToList an array of country trigraphs.
	 * @return this for function chaining.
	 */
	public ProtectiveMarkerBuilder rel(String... releasableToList) {
		this.releasabilityType = ReleasabilityType.REL;
		this.releasableToList.clear();
		this.releasableToList.addAll(Arrays.asList(releasableToList));
		return this;
	}

	/**
	 * Returns true if any field requiring a {@link SecurityCaveats} is populated.
	 * 
	 * @return true if any field requiring a {@link SecurityCaveats} is populated, false otherwise.
	 */
	public boolean haveSecurityCaveat() {
		return !this.codeWords.isEmpty() || !this.foreignGovernmentMarkings.isEmpty()
				|| this.specialHandlingInstruction != null || this.releasabilityType != null;
	}

	/**
	 * Clears all values from the builder, and makes it ready for creating a new {@link ProtectiveMarker}.
	 * 
	 * @return this for function chaining.
	 */
	public ProtectiveMarkerBuilder clear() {
		classification = null;
		clearInformationManagementMarkers();
		clearCodeWords();
		clearForeignGovernmentMarkers();
		clearSpecialHandlingInstruction();
		clearReleasability();
		
		return this;
	}

	/**
	 * Used to determine if the {@link ProtectiveMarkerBuilder} is in a valid state, and able to build a
	 * {@link ProtectiveMarker}.
	 * 
	 * @return a list of String, which reports on invalid parts of the builder which would need to be fixed in order
	 * 		to perform a build. If there are no problems, and the builder is in a valid state, this list is empty.
	 */
	public List<String> isValid() {
		List<String> report = new ArrayList<>();

		checkClassificationValid(report);
		checkInformationManagementMarkerValid(report);
		checkSecurityCaveatValid(report);
		checkSpecialHandlingInstructionsValid(report);
		checkReleasabilityValid(report);

		return report;
	}

	/**
	 * Builds a new instance of {@link ProtectiveMarker} based on the fields in the builder.
	 * 
	 * @return a new instance of {@link ProtectiveMarker}.
	 * @throws IllegalStateException if the builder is not in a valid state, then this exception is thrown.
	 */
	public ProtectiveMarker build() {
		List<String> report = isValid();

		if (report.size() > 0) {
			LOGGER.severe("Do not have valid values to build a ProtectiveMarking.");
			for (String line: report) {
				LOGGER.severe(line);
			}
			throw new IllegalStateException("Invalid state, cannot build Protective Marking.");
		}

		ReleasabilityCaveat relCaveat = null;
		if (haveReleasabilityCaveat()) {
			List<String> relToList = List.copyOf(this.releasableToList);
			relCaveat = new ReleasabilityCaveat(this.releasabilityType, relToList);
		}

		SpecialHandlingCaveat shCaveat = null;
		if (haveSpecialHandlingCaveat()) {
			shCaveat = new SpecialHandlingCaveat(this.specialHandlingInstruction, this.specialHandlingExclusiveFor);
		}

		SecurityCaveats securityCaveats = null;
		if (haveSecurityCaveat()) {
			List<String> cwList = List.copyOf(this.codeWords);
			List<String> fmList = List.copyOf(this.foreignGovernmentMarkings);
			securityCaveats = new SecurityCaveats(cwList, fmList, shCaveat, relCaveat);
		}
		
		List<InformationManagementMarker> immList = buildInformationManagementMarkers();

		return new ProtectiveMarker(this.classification, immList, securityCaveats);
	}

	private void checkClassificationValid(List<String> report) {
		if (this.classification == null) {
			report.add("Classification must be set.");
		}
	}

	private void checkInformationManagementMarkerValid(List<String> report) {
		if (this.informationManagementTypes.size() > InformationManagementTypes.values().length) {
			report.add("Too many Information Management Markers. Clear existing markers, and repopulate.");
		}
		for (InformationManagementTypes type : this.informationManagementTypes) {
			if (type == InformationManagementTypes.LEGISLATIVE_SECRECY
					&& (this.immLegislativeSecrecyWarnings.isEmpty())) {
				report.add("Missing Legislation Secrecy Warning for Legislative Secrecy Marker");
			}
		}
		if (!this.immLegislativeSecrecyWarnings.isEmpty()
				&& !this.informationManagementTypes.contains(InformationManagementTypes.LEGISLATIVE_SECRECY)) {
			report.add("May not have a Legislative Secrecy Warning without a "
					+ InformationManagementTypes.LEGISLATIVE_SECRECY + " Information Management Marker.");
		}
	}

	private void checkSecurityCaveatValid(List<String> report) {
		if (haveSecurityCaveat()
				&& Set.of(Classification.unofficial(), Classification.official(), Classification.officialSensitive())
						.contains(this.classification)) {
			report.add("Attempting to add a Security Caveat when the Classification \"" + this.classification
					+ "\" is lower than \"" + Classification.protect() + "\"");
		}
	}

	private void checkSpecialHandlingInstructionsValid(List<String> report) {
		if (this.specialHandlingInstruction == SpecialHandlingInstruction.EXCLUSIVE_FOR
				&& (this.specialHandlingExclusiveFor == null || this.specialHandlingExclusiveFor.isBlank())) {
			report.add("No Named recipient for Exclusive For Special Handling Instruction");
		} else if ((this.specialHandlingExclusiveFor != null && !this.specialHandlingExclusiveFor.isBlank())
				&& this.specialHandlingInstruction != SpecialHandlingInstruction.EXCLUSIVE_FOR) {
			report.add("May only have a Named recipient for the Exclusive For Special Handling Instruction. Not for: "
					+ this.specialHandlingInstruction);
		}
	}

	private void checkReleasabilityValid(List<String> report) {
		if (ReleasabilityType.REL == this.releasabilityType) {
			// make sure releasable to list starts with "AUS" and has a length of at least
			// 2.
			if (!this.releasableToList.contains(Utils.AUS)) {
				report.add("Releasable To Lists must contain `AUS`");
			}
			if (this.releasableToList.size() < 2) {
				report.add("Releasable To List must have a minimum size of 2");
			}
		} else {
			// make sure releasable to list is empty.
			if (!this.releasableToList.isEmpty()) {
				report.add("Cannot have a releasable to list with Releasability Type: " + this.releasabilityType);
			}
		}
	}
	
	private List<InformationManagementMarker> buildInformationManagementMarkers() {
		List<InformationManagementMarker> immList = new ArrayList<>();
		this.informationManagementTypes.forEach(type -> {
			switch (type) {
			case LEGISLATIVE_SECRECY: {
				// no check is made here to ensure that Warning Strings exist. This allows getting the current set of
				// Information Management Markers while they are in an invalid state.
				immList.add(new InformationManagementMarker(type, List.copyOf(this.immLegislativeSecrecyWarnings)));
				break;
			}
			case LEGAL_PRIVILEGE:
			case PERSONAL_PRIVACY:
				immList.add(new InformationManagementMarker(type, Collections.emptyList()));
				break;
			}
		});
		return immList;
	}

	@Override
	public int hashCode() {
		return Objects.hash(classification, codeWords, foreignGovernmentMarkings, informationManagementTypes, immLegislativeSecrecyWarnings,
				releasabilityType, releasableToList, specialHandlingExclusiveFor, specialHandlingInstruction);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProtectiveMarkerBuilder other = (ProtectiveMarkerBuilder) obj;
		return Objects.equals(classification, other.classification) && Objects.equals(codeWords, other.codeWords)
				&& Objects.equals(foreignGovernmentMarkings, other.foreignGovernmentMarkings)
				&& Objects.equals(informationManagementTypes, other.informationManagementTypes)
				&& Objects.equals(immLegislativeSecrecyWarnings, other.immLegislativeSecrecyWarnings)
				&& releasabilityType == other.releasabilityType
				&& Objects.equals(releasableToList, other.releasableToList)
				&& Objects.equals(specialHandlingExclusiveFor, other.specialHandlingExclusiveFor)
				&& specialHandlingInstruction == other.specialHandlingInstruction;
	}

}

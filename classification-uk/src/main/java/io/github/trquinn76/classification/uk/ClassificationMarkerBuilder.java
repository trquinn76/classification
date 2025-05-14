package io.github.trquinn76.classification.uk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import io.github.trquinn76.classification.uk.model.Classification;
import io.github.trquinn76.classification.uk.model.ClassificationMarker;

/**
 * A Builder for {@link ClassificationMarker}'s.
 * 
 * This class performs 2 primary jobs. One is obviously supporting the Builder
 * Pattern for {@link ClassificationMarker}'s. The second is being an object
 * which can hold the values of a {@link ClassificationMarker} in an invalid
 * state, specifically in support of UI components.
 * <p>
 * A number of lists in the {@link ClassificationMarker}'s data structure are
 * modeled with Sets in this class. This allows sorting and elimination of
 * duplicate values. When the {@link ClassificationMarkerBuilder} performs the
 * actual build, these Sets are converted to Lists as necessary.
 */
public class ClassificationMarkerBuilder {

    private static final Logger LOGGER = Logger.getLogger(ClassificationMarkerBuilder.class.getCanonicalName());

    private boolean ukPrefix = false;
    private Classification classification = null;
    private boolean sensitive = false;
    private Set<String> handlingInstructions = new TreeSet<>();
    private Set<String> useOnlyOrganisations = new TreeSet<>();
    private Set<String> descriptors = new TreeSet<>();
    private Set<String> codeWords = new TreeSet<>();
    private Set<String> eyesOnly = new TreeSet<>(ClassificationConfig.eyesOnlyOrder());
    private Set<String> additionalInstructions = new TreeSet<>();

    public ClassificationMarkerBuilder() {

    }

    /**
     * A copy constructor.
     * 
     * Creates an instance of {@link ClassificationMarkerBuilder} which is populated
     * with the values of the given {@link ClassificationMarker} parameter.
     * 
     * @param classificationMarker the {@link ClassificationMarker} with which to
     *                             populate the builder. May not be null.
     */
    public ClassificationMarkerBuilder(ClassificationMarker classificationMarker) {
        this.ukPrefix = classificationMarker.ukPrefix();
        this.classification = classificationMarker.classification();
        this.sensitive = classificationMarker.sensitive();
        this.descriptors.addAll(classificationMarker.descriptors());
        this.codeWords.addAll(classificationMarker.codeWords());
        this.eyesOnly.addAll(classificationMarker.eyesOnly());
        this.additionalInstructions.addAll(classificationMarker.additionalInstructions());

        for (String handlingInstruction : classificationMarker.handlingInstructions()) {
            if (Utils.endsInOrganisationUseOnly(handlingInstruction)) {
                String str = handlingInstruction.substring(0, handlingInstruction.length() - Utils.USE_ONLY.length())
                        .trim();
                String[] organisations = str.split(", ");
                this.useOnly(organisations);
            } else {
                this.handlingInstructions.add(handlingInstruction);
            }
        }
    }

    /**
     * Sets the presence or absence of the UK prefix.
     * 
     * Quoting from "Government Security Classification Policy" page 21 'Prefixes
     * and National Caveats':
     * 
     * "ALL assets sent to foreign governments or International Organisations (e.g.
     * theNorthAtlanticTreatyOrganisation (NATO))must be marked with a UK prefix."
     * 
     * @param value true if the UK prefix should be present, false otherwise.
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder setUkPrefix(boolean value) {
        this.ukPrefix = value;
        return this;
    }

    /**
     * Sets the UK Prefix to true.
     * 
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder ukPrefix() {
        return setUkPrefix(true);
    }

    /**
     * Gets the current value of the UK Prefix field.
     * 
     * @return boolean the current value of UK Prefix.
     */
    public boolean getUkPrefix() {
        return this.ukPrefix;
    }

    /**
     * Adds a REL EU mark to the Additional Instructions. This is only valid when UK
     * Prefix is true.
     * 
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder addRelEu() {
        this.additionalInstructions.add(Utils.REL_EU);
        return this;
    }

    /**
     * Will remove the REL EU mark from Additional Instructions, if it is present.
     * 
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder removeRelEu() {
        this.additionalInstructions.remove(Utils.REL_EU);
        return this;
    }

    /**
     * Sets the UK Prefix to true, and adds the REL EU mark to the Additional
     * Instructions.
     * 
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder ukPrefixWithRelEU() {
        this.ukPrefix = true;
        return addRelEu();
    }

    /**
     * Sets UK Prefix to false, and removes any REL EU mark from Additional
     * Instructions.
     * 
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder clearUkPrefix() {
        setUkPrefix(false);
        this.additionalInstructions.remove(Utils.REL_EU);
        return this;
    }

    /**
     * Sets the {@link Classification}. The Classification is required.
     * 
     * @param classification the {@link Classification} to set. May not be null.
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder setClassification(Classification classification) {
        Objects.requireNonNull(classification);
        this.classification = classification;
        return this;
    }

    /**
     * Gets the current {@link Classification} in the builder.
     * 
     * @return the {@link Classification} in the builder.
     */
    public Classification getClassification() {
        return this.classification;
    }

    /**
     * Sets the {@link Classification} to OFFICIAL. The SENSITIVE mark is set false.
     * 
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder official() {
        this.classification = Classification.official();
        this.sensitive = false;
        return this;
    }

    /**
     * Sets the {@link Classification} to OFFICIAL, and the SENSITIVE mark to true.
     * 
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder officialSensitive() {
        this.classification = Classification.official();
        this.sensitive = true;
        return this;
    }

    /**
     * Sets the {@link Classification} to SECRET. The SENSITVE mark is set false.
     * 
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder secret() {
        this.classification = Classification.secret();
        this.sensitive = false;
        return this;
    }

    /**
     * Sets the {@link Classification to TOP SECRET. The SENSITIVE mark is set
     * false.
     * 
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder topSecret() {
        this.classification = Classification.topSecret();
        this.sensitive = false;
        return this;
    }

    /**
     * Sets the SENSITIV mark. This mark is only valid with the
     * {@link Classification} it OFFICIAL.
     * 
     * @param sensitive boolean representing the presence or absence of the
     *                  SENSITIVE mark.
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder setSensitiveMark(boolean sensitive) {
        this.sensitive = sensitive;
        return this;
    }

    /**
     * Gets the current value of the SENSITIVE mark.
     * 
     * @return boolean true if the mark is present, false otherwise.
     */
    public boolean getSensitiveMark() {
        return this.sensitive;
    }

    /**
     * Sets the {@link Classification} to null, and the SENSITIVE mark to false.
     * 
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder clearClassification() {
        this.classification = null;
        this.sensitive = false;
        return this;
    }

    /**
     * Sets the Handling Instructions. Existing Handling Instructions are removed
     * and replaced.
     * 
     * @param handlingInstructions a {@link Collection} of Handling Instruction
     *                             Strings.
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder setHandlingInstructions(Collection<String> handlingInstructions) {
        return setHandlingInstructions(handlingInstructions, null);
    }

    /**
     * Sets the Handling Instructions, and any Use Only Organisations. Existing
     * values are remove and replaced.
     * 
     * The presence of Use Only Organisations implies that the USE_ONLY Handling
     * Instruction is also present. However this is not enforced until calls to
     * {@code isValid()} or {@code build()}.
     * 
     * @param handlingInstructions a {@link Collection} of Handling Instructions.
     *                             May not be null.
     * @param useOnlyOrganisations a {@link Collection} of Use Only Organisations.
     *                             May be null.
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder setHandlingInstructions(Collection<String> handlingInstructions,
            Collection<String> useOnlyOrganisations) {
        Objects.requireNonNull(handlingInstructions);
        clearHandlingInstructions();
        this.handlingInstructions.addAll(handlingInstructions);
        if (useOnlyOrganisations != null) {
            this.useOnlyOrganisations.addAll(useOnlyOrganisations);
        }
        return this;
    }

    /**
     * Adds a single Handling Instruction. Existing Instructions will be left
     * unchanged.
     * 
     * @param instruction the Handling Instruction to add. May be an arbitrary
     *                    String. Should be all caps, but that is not enforced. May
     *                    not be null.
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder addHandlingInstruction(String instruction) {
        Objects.requireNonNull(instruction);
        this.handlingInstructions.add(instruction);
        return this;
    }

    /**
     * Gets a copy of the existing internal Handling Instructions set.
     * 
     * @return a copy of the existing Handling Instructions.
     */
    public Set<String> getHandlingInstructions() {
        return new TreeSet<>(this.handlingInstructions);
    }

    /**
     * Clears existing Handling Instructions, including any Use Only Organisations.
     * 
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder clearHandlingInstructions() {
        this.handlingInstructions.clear();
        return clearUseOnlyOrganisations();
    }

    /**
     * Sets Use Only Organisations. Existing organisations are remove and replaced.
     * 
     * @param organisations a {@link Collection} of organisations. May be empty. May
     *                      not be null.
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder setUseOnlyOrganisations(Collection<String> organisations) {
        Objects.requireNonNull(organisations);
        this.useOnlyOrganisations.clear();
        this.useOnlyOrganisations.addAll(organisations);
        return this;
    }

    /**
     * Adds a single Use Only Organisation.
     * 
     * @param organisation the Organisation to all. Should be all caps, but that is
     *                     not enforced. May not be null.
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder addUseOnlyOrganisation(String organisation) {
        Objects.requireNonNull(organisation);
        this.useOnlyOrganisations.add(organisation);
        return this;
    }

    /**
     * Get a copy of the current set of Use Only Organisations.
     * 
     * @return the Set of Use Only Organisations.
     */
    public Set<String> getUseOnlyOrganisations() {
        return new TreeSet<>(this.useOnlyOrganisations);
    }

    /**
     * Clears all existing Use Only Organisations.
     * 
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder clearUseOnlyOrganisations() {
        this.useOnlyOrganisations.clear();
        return this;
    }

    /**
     * Adds the RECIPIENTS ONLY Handling Instruction to the builder.
     * 
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder recipientsOnly() {
        return addHandlingInstruction(Utils.RECIPIENTS_ONLY);
    }

    /**
     * Adds the FOR PUBLIC RELEASE Handling Instruction to the builder.
     * 
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder forPublicRelease() {
        return addHandlingInstruction(Utils.FOR_PUBLIC_RELEASE);
    }

    /**
     * Adds the ORGANISATION USE ONLY Handling Instruction to the builder, and sets
     * the Use Only Organisations list.
     * 
     * @param organisations the array of Organisations. May not be null. May not be
     *                      empty.
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder useOnly(String... organisations) {
        Objects.requireNonNull(organisations);
        if (organisations.length < 1) {
            throw new IllegalArgumentException(
                    "When using Organisation Use Only Handler, there must be a minimum of one Organisation.");
        }
        addHandlingInstruction(Utils.USE_ONLY);
        return setUseOnlyOrganisations(Arrays.asList(organisations));
    }

    /**
     * Adds the HMG USE ONLY Handling Instruction to the builder.
     * 
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder hmgUseOnly() {
        return addHandlingInstruction(Utils.HMG_USE_ONLY);
    }

    /**
     * Adds the EMBARGOED Handling Instruction to the builder.
     * 
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder embargoed() {
        return addHandlingInstruction(Utils.EMBARGOED);
    }

    /**
     * Sets the Descriptors in the builder, removing and replacing any existing
     * values.
     * 
     * @param descriptors a {@link Collection} of Descriptors. May be empty. May not
     *                    be null.
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder setDescriptors(Collection<String> descriptors) {
        Objects.requireNonNull(descriptors);
        clearDescriptors();
        this.descriptors.addAll(descriptors);
        return this;
    }

    /**
     * Adds a single descriptor to the existing list.
     * 
     * @param descriptor the Descriptor to add. Should be all caps but this is not
     *                   enforced. May not be null.
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder addDescriptor(String descriptor) {
        Objects.requireNonNull(descriptor);
        this.descriptors.add(descriptor);
        return this;
    }

    /**
     * Gets a copy of the current Set of Descriptors.
     * 
     * @return a copy of the current Set of Descriptors.
     */
    public Set<String> getDescriptors() {
        return new TreeSet<>(this.descriptors);
    }

    /**
     * Clears the existing Descriptors.
     * 
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder clearDescriptors() {
        this.descriptors.clear();
        return this;
    }
    
    /**
     * Sets the descriptors to the given array.
     * 
     * Existing descriptors will be removed and replaced.
     * 
     * @param descriptors an array of Descriptors. May be empty. May not be null.
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder descriptors(String... descriptors) {
        return setDescriptors(Arrays.asList(descriptors));
    }

    /**
     * Adds the PERSONAL DATA Descriptor to the builder.
     * 
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder personalData() {
        return addDescriptor(Utils.PERSONAL_DATA);
    }

    /**
     * Adds the LEGAL PROFESSIONAL PRIVILEGE Descriptor to the builder.
     * 
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder legalProfessionalPrivilege() {
        return addDescriptor(Utils.LEGAL_PROFESSIONAL_PRIVILEGE);
    }

    /**
     * Adds the LEGAL Descriptor to the builder.
     * 
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder legal() {
        return addDescriptor(Utils.LEGAL);
    }

    /**
     * Adds the MARKET SENSITIVE Descriptor to the builder.
     * 
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder marketSensitive() {
        return addDescriptor(Utils.MARKET_SENSITIVE);
    }

    /**
     * Adds the COMMERCIAL Descriptor to the builder.
     * 
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder commercial() {
        return addDescriptor(Utils.COMMERCIAL);
    }

    /**
     * Adds the HR/MANAGEMENT Descriptor to the builder.
     * 
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder hrManagement() {
        return addDescriptor(Utils.HR_MANAGEMENT);
    }

    /**
     * Sets the code/compartment words on the builder. Will remove and replace
     * existing values.
     * 
     * @param codeWords a {@link Collection} of code words to set. May be empty. May
     *                  not be null.
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder setCodeWords(Collection<String> codeWords) {
        Objects.requireNonNull(codeWords);
        clearCodeWords();
        this.codeWords.addAll(codeWords);
        return this;
    }

    /**
     * Adds the given code word to the builder.
     * 
     * @param codeWord the code/compartment word to add. May not be null.
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder addCodeWord(String codeWord) {
        Objects.requireNonNull(codeWord);
        this.codeWords.add(codeWord);
        return this;
    }

    /**
     * Gets a copy of the current code words set.
     * 
     * @return a copy of the current code words set.
     */
    public Set<String> getCodeWords() {
        return new TreeSet<>(this.codeWords);
    }

    /**
     * Clears existing code/compartment words.
     * 
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder clearCodeWords() {
        this.codeWords.clear();
        return this;
    }

    /**
     * Sets the code/compartment words to the given array.
     * 
     * @param codeWords an array of code/compartment words. May be empty. May not be
     *                  null.
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder codeWords(String... codeWords) {
        Objects.requireNonNull(codeWords);
        return setCodeWords(Arrays.asList(codeWords));
    }

    /**
     * Sets the National Caveat Eyes Only list to the given {@code Collection} of
     * countries.
     * 
     * Existing values will be remove and replaced.
     * 
     * @param eyesOnlyList the {link Collection} of Eyes Only Countries. May be
     *                     empty. May not be null.
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder setEyesOnly(Collection<String> eyesOnlyList) {
        Objects.requireNonNull(eyesOnlyList);
        clearEyesOnly();
        this.eyesOnly.addAll(eyesOnlyList);
        return this;
    }

    /**
     * Add a National Caveat Eyes Only Country.
     * 
     * @param country a country or organisation (eg: FIVE, NATO). May not be null.
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder addEyesOnlyCountry(String country) {
        Objects.requireNonNull(country);
        this.eyesOnly.add(country);
        return this;
    }

    /**
     * Get a copy of the current Eyes Only country list.
     * 
     * @return Set of current Eyes Only countries.
     */
    public Set<String> getEyesOnlyCountries() {
        Set<String> retSet = new TreeSet<>(ClassificationConfig.eyesOnlyOrder());
        retSet.addAll(this.eyesOnly);
        return retSet;
    }

    /**
     * Clears existing Eyes Only countries from builder.
     * 
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder clearEyesOnly() {
        this.eyesOnly.clear();
        return this;
    }

    /**
     * Takes an array of countries, or organisations, and sets them as the Eyes Only
     * List.
     * 
     * The list of Eyes Only countries/organisations is currently required to
     * contain either UK or FIVE. Values should be in all capitals, but this is not
     * enforced.
     * 
     * @param countries an array of country/organisation Strings. May be empty. May
     *                  not be null.
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder eyesOnly(String... countries) {
        Objects.requireNonNull(countries);
        return setEyesOnly(Arrays.asList(countries));
    }

    /**
     * Sets the Eyes Only list to be UK alone.
     * 
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder ukEyesOnly() {
        return eyesOnly(Utils.UK);
    }

    /**
     * Sets the Eyes Only list to be FIVE alone.
     * 
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder fiveEyesOnly() {
        return eyesOnly(Utils.FIVE);
    }

    /**
     * Sets the Additional Instructions in the builder.
     * 
     * Existing values will be removed and replaced.
     * 
     * @param instructions a {@link Collection} of additional instructions. May be
     *                     empty. May not be null.
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder setAdditionalInstructions(Collection<String> instructions) {
        Objects.requireNonNull(instructions);
        clearAdditionalInstructions();
        this.additionalInstructions.addAll(instructions);
        return this;
    }

    /**
     * Adds an Additional Instruction to the builder.
     * 
     * @param instruction an instruction to add to the builder. May not be null.
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder addAdditionalInstruction(String instruction) {
        Objects.requireNonNull(instruction);
        this.additionalInstructions.add(instruction);
        return this;
    }

    /**
     * Gets a copy of the existing Additional Instructions.
     * 
     * @return a copy of the Additional Instructions.
     */
    public Set<String> getAdditionalInstructions() {
        return new TreeSet<>(this.additionalInstructions);
    }

    /**
     * Clears the existing Additional Instructions from the builder.
     * 
     * @return
     */
    public ClassificationMarkerBuilder clearAdditionalInstructions() {
        this.additionalInstructions.clear();
        return this;
    }

    /**
     * Sets the given array as the Additional Instructions.
     * 
     * @param instructions an array of Additional Instructions. May be empty. May
     *                     not be null.
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder additionalInstructions(String... instructions) {
        return setAdditionalInstructions(Arrays.asList(instructions));
    }

    /**
     * Resets the builder to a state where values are unset, empty or their default
     * value.
     * 
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder clear() {
        clearUkPrefix();
        clearClassification();
        clearHandlingInstructions();
        clearDescriptors();
        clearCodeWords();
        clearEyesOnly();
        clearAdditionalInstructions();
        return this;
    }

    /**
     * Used to determine if the {@link ClassificationMarkerBuilder} is in a valid
     * state, and able to build a {@link ClassificationMarker}.
     * 
     * @return a list of String, which reports on invalid parts of the builder which
     *         would need to be fixed in order to perform a build. If there are no
     *         problems, and the builder is in a valid state, this list is empty.
     */
    public List<String> isValid() {
        List<String> report = new ArrayList<>();

        checkUkPrefix(report);
        checkClassificationValid(report);
        checkHandlingInstructionsValid(report);
        checkDescriptorsValid(report);
        checkNationalCaveats(report);

        return report;
    }

    /**
     * Builds a new instance of {@link ClassificationMarker} based on the fields in
     * the builder.
     * 
     * @return a new instance of {@link ClassificationMarker}.
     * @throws IllegalStateException if the builder is not in a valid state, then
     *                               this exception is thrown.
     */
    public ClassificationMarker build() {
        List<String> report = isValid();

        if (report.size() > 0) {
            LOGGER.severe("Do not have valid values to build a ClassificationMarker.");
            for (String line : report) {
                LOGGER.severe(line);
            }
            throw new IllegalStateException("Invalid state, cannot build Classification Marker.");
        }

        Set<String> handlingInstructions = new TreeSet<>(this.handlingInstructions);
        if (handlingInstructions.contains(Utils.USE_ONLY)) {
            handlingInstructions.remove(Utils.USE_ONLY);
            StringBuilder buf = new StringBuilder();
            for (String organisation : this.useOnlyOrganisations) {
                if (buf.length() > 0) {
                    buf.append(", ");
                }
                buf.append(organisation);
            }
            buf.append(" ").append(Utils.USE_ONLY);
            handlingInstructions.add(buf.toString().trim());
        }

        return new ClassificationMarker(this.ukPrefix, this.classification, this.sensitive,
                List.copyOf(handlingInstructions), List.copyOf(this.descriptors), List.copyOf(this.codeWords),
                List.copyOf(this.eyesOnly), List.copyOf(this.additionalInstructions));
    }

    private void checkUkPrefix(List<String> report) {
        if (this.additionalInstructions.contains(Utils.REL_EU) && !this.ukPrefix) {
            report.add("May not have the " + Utils.REL_EU + " marker if the UK Prefix is not set.");
        }
    }

    private void checkClassificationValid(List<String> report) {
        if (this.classification == null) {
            report.add("Classification must be set.");
        } else if (this.sensitive && !Classification.official().equals(this.classification)) {
            StringBuilder buf = new StringBuilder();
            buf.append("The SENSITIVE mark is only permitted when the Classification is '")
                    .append(Classification.official()).append("'. Current Classification is: '")
                    .append(this.classification).append("'.");
            report.add(buf.toString());
        }
    }

    private void checkHandlingInstructionsValid(List<String> report) {
        if (this.handlingInstructions.contains(Utils.RECIPIENTS_ONLY)
                && Classification.official().equals(this.classification) && !this.sensitive) {
            StringBuilder buf = new StringBuilder();
            buf.append(Utils.RECIPIENTS_ONLY).append(" Handling Instruction may not be used with '")
                    .append(Classification.official()).append("' Classification without the SENSITVE mark");
            report.add(buf.toString());
        }

        if (this.handlingInstructions.contains(Utils.FOR_PUBLIC_RELEASE)) {
            if (!Classification.official().equals(this.classification)) {
                StringBuilder buf = new StringBuilder();
                buf.append("May only use Handling Instruction ").append(Utils.FOR_PUBLIC_RELEASE).append(" with '")
                        .append(Classification.official()).append("' Classification. It may not be used with: '")
                        .append(this.classification.toString()).append("'.");
                report.add(buf.toString());
            } else if (Classification.official().equals(this.classification) && this.sensitive) {
                StringBuilder buf = new StringBuilder();
                buf.append("May only use Handling Instruction ").append(Utils.FOR_PUBLIC_RELEASE).append(" with '")
                        .append(Classification.official()).append("' Classification without the SENSITIVE mark.");
                report.add(buf.toString());
            }
        }

        if (this.handlingInstructions.contains(Utils.USE_ONLY) && this.useOnlyOrganisations.isEmpty()) {
            report.add(
                    "When the [INSERT ORGANISATION(S) NAME] USE ONLY Handling Instruction is used, there must be at least one 'Use Only Organisation' defined.");
        }

        if (this.handlingInstructions.contains(Utils.HMG_USE_ONLY)
                && !Classification.official().equals(this.classification)) {
            StringBuilder buf = new StringBuilder();
            buf.append("May only use ").append(Utils.HMG_USE_ONLY).append(" Handling Instruction with '")
                    .append(Classification.official()).append("' Classification. It may not be used with: '")
                    .append(this.classification.toString()).append("'.");
            report.add(buf.toString());
        }

        if (this.handlingInstructions.contains(Utils.EMBARGOED)
                && Classification.topSecret().equals(this.classification)) {
            report.add("May not use the " + Utils.EMBARGOED + " Handling Instruction with the Classification '"
                    + Classification.topSecret() + "'.");
        }
    }

    private void checkDescriptorsValid(List<String> report) {
        if (this.descriptors.contains(Utils.LEGAL) && !Classification.official().equals(this.classification)) {
            StringBuilder buf = new StringBuilder();
            buf.append("May only use ").append(Utils.LEGAL).append(" Handling Instruction when the Classification is '")
                    .append(Classification.official()).append("'. It may not be used with: '")
                    .append(this.classification).append("'.");
            report.add(buf.toString());
        }
    }

    private void checkNationalCaveats(List<String> report) {
        if (!this.eyesOnly.isEmpty()) {
            if (!Set.of(Classification.secret(), Classification.topSecret()).contains(this.classification)) {
                StringBuilder buf = new StringBuilder();
                buf.append("National/Eyes Only Caveats may only be added to '").append(Classification.secret())
                        .append("' and '").append(Classification.topSecret())
                        .append("' Classifications. They may not be used with '").append(this.classification)
                        .append("'.");
                report.add(buf.toString());
            }
            if (!this.eyesOnly.contains(Utils.UK) && !this.eyesOnly.contains(Utils.FIVE)) {
                StringBuilder buf = new StringBuilder();
                buf.append("When Eyes Only Caveats are present, the list must contain either ").append(Utils.UK)
                        .append(" or ").append(Utils.FIVE).append(".");
                report.add(buf.toString());
            }
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(additionalInstructions, classification, codeWords, descriptors, eyesOnly,
                handlingInstructions, sensitive, ukPrefix, useOnlyOrganisations);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ClassificationMarkerBuilder other = (ClassificationMarkerBuilder) obj;
        return Objects.equals(additionalInstructions, other.additionalInstructions)
                && Objects.equals(classification, other.classification) && Objects.equals(codeWords, other.codeWords)
                && Objects.equals(descriptors, other.descriptors) && Objects.equals(eyesOnly, other.eyesOnly)
                && Objects.equals(handlingInstructions, other.handlingInstructions) && sensitive == other.sensitive
                && ukPrefix == other.ukPrefix && Objects.equals(useOnlyOrganisations, other.useOnlyOrganisations);
    }
}

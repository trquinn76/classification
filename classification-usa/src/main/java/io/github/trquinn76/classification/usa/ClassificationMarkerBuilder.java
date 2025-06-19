package io.github.trquinn76.classification.usa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import io.github.trquinn76.classification.usa.model.Classification;
import io.github.trquinn76.classification.usa.model.ClassificationMarker;
import io.github.trquinn76.classification.usa.model.Disseminations;
import io.github.trquinn76.classification.usa.model.NonUSAndJointType;
import io.github.trquinn76.classification.usa.model.OtherDisseminations;
import io.github.trquinn76.classification.usa.subbuilders.AtomicEnergyActInformationBuilder;
import io.github.trquinn76.classification.usa.subbuilders.ClassificationModifierBuilder;
import io.github.trquinn76.classification.usa.subbuilders.DisseminationsBuilder;
import io.github.trquinn76.classification.usa.subbuilders.FGIBuilder;
import io.github.trquinn76.classification.usa.subbuilders.OtherDisseminationsBuilder;
import io.github.trquinn76.classification.usa.subbuilders.SAPBuilder;
import io.github.trquinn76.classification.usa.subbuilders.SCIBuilder;

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
 * <p>
 * This builder has a number of sub builders. The shear number of functions this
 * builder would have required to allow setting of all values and fields is
 * excessive. Instead there is a set of sub builders, which may be accessed from
 * an instance of {@code ClassificationMarkerBuilder}.
 */
public class ClassificationMarkerBuilder {

    private static final Logger LOGGER = Logger.getLogger(ClassificationMarkerBuilder.class.getCanonicalName());

    /**
     * Allows setting of Classification modifying properties, including Joint and
     * NATO
     */
    public final ClassificationModifierBuilder modifier;
    /** Allows setting of Sensitive Compartmented Information */
    public final SCIBuilder sci;
    /** Allows setting of Special Access Programs */
    public final SAPBuilder sap;
    /** Allows setting of Atomic Energy Agency Information */
    public final AtomicEnergyActInformationBuilder aea;
    /** Allows setting of Foreign Government Information markers */
    public final FGIBuilder fgi;
    /** Allows setting of Disseminations */
    public final DisseminationsBuilder disseminations;
    /** Allows setting of Other Disseminations */
    public final OtherDisseminationsBuilder otherDisseminations;

    // Classification
    private Classification classification = null;
    // Additional Markings
    private Set<String> additionalMarkings = new TreeSet<>();

    public ClassificationMarkerBuilder() {
        this.modifier = new ClassificationModifierBuilder(this);
        this.sci = new SCIBuilder(this);
        this.sap = new SAPBuilder(this);
        this.aea = new AtomicEnergyActInformationBuilder(this);
        this.fgi = new FGIBuilder(this);
        this.disseminations = new DisseminationsBuilder(this);
        this.otherDisseminations = new OtherDisseminationsBuilder(this);
    }

    /**
     * A copy constructor.
     * 
     * Creates an instance of {@link ClassificationMarkerBuilder} which has it's
     * fields populated with the values of the given {@link ClassificationMarker}
     * parameter.
     * 
     * @param marker the {@link ClassificationMarker} with which to populate the
     *               builder.
     */
    public ClassificationMarkerBuilder(ClassificationMarker marker) {
        this();
        classification = marker.classification();
        modifier.populate(marker.classificationModifier());
        sci.populate(marker.sensitiveCompartmentControlSystems());
        sap.populate(marker.specialAccessPrograms());
        aea.populate(marker.atomicEnergyInformationMarker());
        fgi.populate(marker.foreignGovernmentInformationMarker());
        disseminations.populate(marker.disseminations());
        otherDisseminations.populate(marker.otherDisseminations());
        additionalMarkings.addAll(marker.additionalMarkings());
    }

    /**
     * Sets the {@link Classification} to the given value.
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
     * @return the current {@link Classification} in the builder.
     */
    public Classification getClassification() {
        return this.classification;
    }

    /**
     * Sets the {@link Classification} to UNCLASSIFIED.
     * 
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder unclassified() {
        return this.setClassification(Classification.unclassified());
    }

    // No restricted function, as that classification is NOT valid in the USA
    // context. It is only valid in some limited circumstances involving Foreign
    // and NATO markings, but in the USA context it should never be seen.

    /**
     * Sets the {@link Classification} to CONFIDENTIAL.
     * 
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder confidential() {
        return this.setClassification(Classification.confidential());
    }

    /**
     * Sets the {@link Classification} to SECRET.
     * 
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder secret() {
        return this.setClassification(Classification.secret());
    }

    /**
     * Sets the {@link Classification} to TOP SECRET.
     * 
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder topSecret() {
        return this.setClassification(Classification.topSecret());
    }

    /**
     * Returns true iff the {@link Classification} value in the builder is
     * UNCLASSIFIED.
     * 
     * @return true iff the {@link Classification} is UNCLASSIFIED.
     */
    public boolean isUnclassified() {
        return Classification.unclassified().equals(this.classification);
    }

    /**
     * Returns true iff the {@link Classification} value in the builder is
     * RESTRICTED.
     * 
     * @return true iff the {@link Classification} is RESTRICTED.
     */
    public boolean isRestricted() {
        return Classification.restricted().equals(this.classification);
    }

    /**
     * Returns true iff the {@link Classification} value in the builder is one of
     * CONFIDENTIAL, SECRET or TP SECRET.
     * 
     * @return true iff the {@link Classification} is one of CONFIDENTIAL, SECRET or
     *         TOP SECRET.
     */
    public boolean isClassified() {
        return new HashSet<>(Set.of(Classification.confidential(), Classification.secret(), Classification.topSecret()))
                .contains(this.classification);
    }

    /**
     * Returns true iff the {@link Classification} value in the builder is one of
     * SECRET or TOP SECRET.
     * 
     * @return true iff the {@link Classification} is one of SECRET or TOP SECRET.
     */
    public boolean isHighlyClassified() {
        return new HashSet<>(Set.of(Classification.secret(), Classification.topSecret())).contains(this.classification);
    }

    /**
     * Returns true iff the {@link Classification} value in the builder is one of
     * CONFIDENTIAL, SECRET or TOP SECRET, or if the {@link Classification} is
     * UNCLASSIFIED, with the Dissemination CUI.
     * 
     * @return true iff the {@link Classification} is one of CONFIDENTIAL, SECRET or
     *         TOP SECRET, or UNCLASSIFIED with the Dissemination CUI.
     */
    public boolean isClassifiedOrControlUnclassified() {
        boolean retval = isClassified();
        if (!retval) {
            retval = Classification.unclassified().equals(this.classification)
                    && disseminations.hasDissemination(Disseminations.CONTROLLED_UNCLASSIFIED_INFORMATION);
        }
        return retval;
    }

    /**
     * Adds a {@link Collection} of Strings to the list of Additional Markings.
     * 
     * @param additionalMarkings a {@link Collection} of String. May not be null.
     *                           May be empty.
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder addAdditionalMarkings(Collection<String> additionalMarkings) {
        Objects.requireNonNull(additionalMarkings);
        this.additionalMarkings.addAll(additionalMarkings);
        return this;
    }

    /**
     * Adds a single String to Additional Markings.
     * 
     * @param str the String to add to the Additional Markings list. May not be
     *            null.
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder addAdditionMarking(String str) {
        Objects.requireNonNull(str);
        this.additionalMarkings.add(str);
        return this;
    }

    /**
     * Gets a copy of the current Set of Additional Markings.
     * 
     * @return a copy of the current Set of Additional Markings.
     */
    public Set<String> getAdditionalMarkings() {
        return new TreeSet<>(this.additionalMarkings);
    }

    /**
     * Removes the given String from the Additional Markings list.
     * 
     * @param str the String to remove from Additional Markings. May not be null.
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder removeAdditionalMarking(String str) {
        Objects.requireNonNull(str);
        this.additionalMarkings.remove(str);
        return this;
    }

    /**
     * Clears the existing Additional Markings, leaving the list empty.
     * 
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder clearAdditionMarkings() {
        this.additionalMarkings.clear();
        return this;
    }

    /**
     * Clears the builder of all values.
     * 
     * @return this for function chaining.
     */
    public ClassificationMarkerBuilder clear() {
        this.classification = null;
        this.clearAdditionMarkings();

        this.modifier.clear();
        this.sci.clear();
        this.sap.clear();
        this.aea.clear();
        this.fgi.clear();
        this.disseminations.clear();
        this.otherDisseminations.clear();

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

        report.addAll(this.modifier.isValid());
        checkModifiers(report);
        checkClassification(report);
        report.addAll(this.sci.isValid());
        checkSci(report);
        report.addAll(this.sap.isValid());
        checkSap(report);
        report.addAll(this.aea.isValid());
        checkAea(report);
        report.addAll(this.fgi.isValid());
        checkFgi(report);
        report.addAll(this.disseminations.isValid());
        checkDisseminations(report);
        report.addAll(this.otherDisseminations.isValid());
        checkOtherDisseminations(report);

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

        return new ClassificationMarker(modifier.build(), classification, sci.build(), sap.build(), aea.build(),
                fgi.build(), disseminations.build(), otherDisseminations.build(), List.copyOf(additionalMarkings));
    }

    private void checkModifiers(List<String> report) {
        if (modifier.isPopulated()) {
            if (NonUSAndJointType.COSMIC.equals(modifier.getNonUsAndJointType())
                    && !Classification.topSecret().equals(this.classification)) {
                StringBuilder buf = new StringBuilder();
                buf.append("The NATO mark '").append(NonUSAndJointType.COSMIC.name())
                        .append("' can only be used with classification '")
                        .append(Classification.topSecret().toString()).append("'. It is not valid for classification '")
                        .append(this.classification.toString());
                report.add(buf.toString());
            }
            if (NonUSAndJointType.NATO.equals(modifier.getNonUsAndJointType())
                    && Classification.topSecret().equals(this.classification)) {
                StringBuilder buf = new StringBuilder();
                buf.append("The NATO mark '").append(NonUSAndJointType.NATO.name())
                        .append("' may not be used with classification '").append(Classification.topSecret().toString())
                        .append("'. It may only be used with lower Classifications. Use '")
                        .append(NonUSAndJointType.COSMIC.name()).append(" ")
                        .append(Classification.topSecret().toString()).append("' for a notional 'NATO ")
                        .append(Classification.topSecret().toString()).append("'.");
                report.add(buf.toString());
            }
            if (Utils.BOHEMIA.equals(modifier.getNatoSpecialMark())
                    && !Classification.topSecret().equals(this.classification)) {
                StringBuilder buf = new StringBuilder();
                buf.append("NATO mark ").append(Utils.BOHEMIA).append(" may only be used with classification '")
                        .append(Classification.topSecret().toString()).append("' It may not be used with: '")
                        .append(classification.toString()).append("'.");
                report.add(buf.toString());
            }
            if (Utils.ATOMAL.equals(modifier.getNatoSpecialMark()) && !isClassified()) {
                StringBuilder buf = new StringBuilder();
                buf.append("NATO mark ").append(Utils.ATOMAL).append(" may only be used with classifications '")
                        .append(Classification.confidential().toString()).append("', '")
                        .append(Classification.secret().toString()).append("' and ")
                        .append(Classification.topSecret().toString())
                        .append("'. It may not be used with classification: '").append(this.classification.toString());
                report.add(buf.toString());
            }
            if (this.disseminations.hasDissemination(Disseminations.NOFORN)) {
                StringBuilder buf = new StringBuilder();
                buf.append("May not have the ").append(Disseminations.NOFORN.toString())
                        .append(" dissemination control with the Classification Modifier: ")
                        .append(modifier.getNonUsAndJointType());
                report.add(buf.toString());
            }
        }
    }

    private void checkClassification(List<String> report) {
        if (this.classification == null) {
            report.add("Classification must be set.");
        }
        if (isRestricted()) {
            // Restricted is not usually valid in the USA context. Check that the context
            // has been appropriately modified.
            if (modifier.isPopulated() && (modifier.isForeign() || modifier.isNato())) {
                StringBuilder buf = new StringBuilder();
                buf.append("Allowing classification '").append(Classification.restricted())
                        .append("' which is not usually valid in the ").append(Utils.USA)
                        .append(" context. It is permitted as the context is: ");
                if (modifier.isNato()) {
                    buf.append("NATO - which may bring this classification in from foreign sources.");
                } else {
                    buf.append("Foreign Classification - which may use '")
                            .append(Classification.restricted().toString()).append("'.");
                }
                LOGGER.warning(buf.toString());
            } else {
                if (modifier.isJoint()) {
                    StringBuilder buf = new StringBuilder();
                    buf.append("The '").append(Classification.restricted().toString())
                            .append("' classification is not valid for ").append(NonUSAndJointType.JOINT.name())
                            .append(" classifications where the ").append(Utils.USA).append(" is one of the owners.");
                    report.add(buf.toString());
                } else {
                    StringBuilder buf = new StringBuilder();
                    buf.append("The '").append(Classification.restricted().toString())
                            .append("' classification is not valid for the ").append(Utils.USA)
                            .append(". It is only valid in cases involving foreign or NATO data sharing.");
                    report.add(buf.toString());
                }
            }
        }
    }

    private void checkSci(List<String> report) {
        if (sci.hasControlSystem(Utils.HCS) || sci.getCompartments(Utils.TALENT_KEYHOLE).contains(Utils.GEOCAP)) {
            // must have dissem NOFORN
            if (!disseminations.hasDissemination(Disseminations.NOFORN)) {
                StringBuilder buf = new StringBuilder();
                buf.append("The dissemination '").append(Disseminations.NOFORN.toString())
                        .append("' is required for SCI Control System '").append(Utils.HCS)
                        .append("' and Compartment '").append(Utils.TALENT_KEYHOLE).append("-").append(Utils.GEOCAP)
                        .append("'.");
                report.add(buf.toString());
            }
        }
    }

    private void checkSap(List<String> report) {
        // nothing additional to check.
    }

    private void checkAea(List<String> report) {
        if (aea.isPopulated()) {
            if (AtomicEnergyActInformationBuilder.HIGHCLASSIFICATIONSET.contains(aea.getMark())) {
                if (!isHighlyClassified()) {
                    StringBuilder buf = new StringBuilder();
                    buf.append("Atomic Energy Act Information ").append(aea.getMark().toString())
                            .append(" is only valid for classifications '").append(Classification.secret().toString())
                            .append("' and '").append(Classification.topSecret())
                            .append("'. It is not valid for classification: '").append(this.classification.toString())
                            .append("'.");
                    report.add(buf.toString());
                }
            }
            if (AtomicEnergyActInformationBuilder.CLASSIFICATIONSET.contains(aea.getMark())) {
                if (!isClassified()) {
                    StringBuilder buf = new StringBuilder();
                    buf.append("Atomic Energy Act Information ").append(aea.getMark().toString())
                            .append(" is only valid for classifications '")
                            .append(Classification.confidential().toString()).append("', '")
                            .append(Classification.secret().toString()).append("' and '")
                            .append(Classification.topSecret()).append("'. It is not valid for classification: '")
                            .append(this.classification.toString()).append("'.");
                    report.add(buf.toString());
                }
            }
            if (AtomicEnergyActInformationBuilder.UNCLASSIFIEDSET.contains(aea.getMark())) {
                if (!isUnclassified()) {
                    StringBuilder buf = new StringBuilder();
                    buf.append("Atomic Energy Act Information ").append(aea.getMark().toString())
                            .append(" is only valid for classification '")
                            .append(Classification.unclassified().toString()).append("'.");
                    report.add(buf.toString());
                }
            }

            if (AtomicEnergyActInformationBuilder.NOFORNREQUIREDSET.contains(aea.getMark())) {
                if (!disseminations.hasDissemination(Disseminations.NOFORN)) {
                    // According to "Intelligence Community Markings System Register and Manual"
                    // these AEA markings require the Dissemination marking NOFORN, unless there
                    // is a sharing agreement. Meanwhile the "DoD Information Security Program:
                    // Marking of Information" document does not mention this requirement.
                    // So in this library, will detect the lack of NOFORN for these AEA Markings,
                    // and will put out a WARNING rather than an error.
                    StringBuilder buf = new StringBuilder();
                    buf.append("The AEA Mark '").append(aea.getMark().toString())
                            .append("' is expected to be associated with the dissemination mark '")
                            .append(Disseminations.NOFORN.toString()).append("'. However, this is not required.");
                    LOGGER.warning(buf.toString());
                }
            }
        }
    }

    private void checkFgi(List<String> report) {
        if (fgi.isPopulated()) {
            if (!isClassified()) {
                StringBuilder buf = new StringBuilder();
                buf.append("The FGI markings is only valid for classifications '")
                        .append(Classification.confidential().toString()).append("', '")
                        .append(Classification.secret().toString()).append("' and '")
                        .append(Classification.topSecret().toString())
                        .append("'. It is not valid for classification: '").append(this.classification.toString())
                        .append("'.");
                report.add(buf.toString());
            }

            if (disseminations.hasDissemination(Disseminations.RELEASE_TO)) {
                Set<String> intersection = fgi.getCountries();
                intersection.retainAll(disseminations.getCountries(Disseminations.RELEASE_TO));
                if (!intersection.equals(fgi.getCountries())) {
                    StringBuilder buf = new StringBuilder();
                    buf.append("When using the ").append(Disseminations.RELEASE_TO).append(
                            " Dissemination with Foreign Government Information, the Releasable To list needs to contain all Countries in the FGI list. FGI list: ");
                    buf.append(String.join(", ", fgi.getCountries()));
                    buf.append(". Releasable To list: ");
                    buf.append(String.join(", ", disseminations.getCountries(Disseminations.RELEASE_TO)));
                    buf.append(".");
                    report.add(buf.toString());
                }
            }
        }
    }

    private void checkDisseminations(List<String> report) {
        if (disseminations.hasDissemination(Disseminations.WAIVED) && !sap.isPopulated()) {
            StringBuilder buf = new StringBuilder();
            buf.append("If the ").append(Disseminations.WAIVED).append(
                    " Dissemination is used, then there must be at least one SAR Program defined, to be 'waived'.");
            report.add(buf.toString());
        }

        if (disseminations.hasDissemination(Disseminations.FOUO) && !isUnclassified()) {
            StringBuilder buf = new StringBuilder();
            buf.append("The ").append(Disseminations.FOUO).append(" may only be used with '")
                    .append(Classification.unclassified()).append("'. It is not valid for classification: '")
                    .append(this.classification).append("'.");
            report.add(buf.toString());
        }

        if (disseminations.hasDissemination(Disseminations.CONTROLLED_UNCLASSIFIED_INFORMATION) && !isUnclassified()) {
            StringBuilder buf = new StringBuilder();
            buf.append("The ").append(Disseminations.CONTROLLED_UNCLASSIFIED_INFORMATION)
                    .append(" may only be used with '").append(Classification.unclassified())
                    .append("'. It is not valid for classification: '").append(this.classification).append("'.");
            report.add(buf.toString());
        }

        if (disseminations.hasDissemination(Disseminations.ORIGINATOR_CONTROLLED) && !isClassified()) {
            StringBuilder buf = new StringBuilder();
            buf.append("The ").append(Disseminations.ORIGINATOR_CONTROLLED)
                    .append(" Dissemination may only be used with Classifications '")
                    .append(Classification.confidential()).append("', '").append(Classification.secret())
                    .append("' or '").append(Classification.topSecret())
                    .append("'. It is not valid for classification: '").append(this.classification).append("'.");
            report.add(buf.toString());
        }

        if (disseminations.hasDissemination(Disseminations.RELEASE_TO) && !isClassifiedOrControlUnclassified()) {
            StringBuilder buf = new StringBuilder();
            buf.append("The ").append(Disseminations.RELEASE_TO).append(" Dissemination may only be used with '")
                    .append(Classification.confidential()).append("', '").append(Classification.secret())
                    .append("' and '").append(Classification.topSecret()).append("' classifications, or '")
                    .append(Classification.unclassified()).append("' classification with the Dissemination ")
                    .append(Disseminations.CONTROLLED_UNCLASSIFIED_INFORMATION)
                    .append(". It is not valid for classification: '").append(this.classification).append("'.");
            report.add(buf.toString());
        }

        if (disseminations.hasDissemination(Disseminations.DISPLAY_ONLY) && !isClassified()) {
            StringBuilder buf = new StringBuilder();
            buf.append("The ").append(Disseminations.DISPLAY_ONLY)
                    .append(" Dissemination may only be used with Classifications '")
                    .append(Classification.confidential()).append("', '").append(Classification.secret())
                    .append("' or '").append(Classification.topSecret())
                    .append("'. It is not valid for classification: '").append(this.classification).append("'.");
            report.add(buf.toString());
        }

        if (disseminations.hasDissemination(Disseminations.CONTROLLED_IMAGERY) && !isHighlyClassified()) {
            StringBuilder buf = new StringBuilder();
            buf.append("The ").append(Disseminations.CONTROLLED_IMAGERY).append(" Dissemination is only valid for '")
                    .append(Classification.secret()).append("' classification. It is also permitted for '")
                    .append(Classification.topSecret())
                    .append("' classification in this library, as merging data could lead to this out come. This Dissemination is not valid for classification: '")
                    .append(this.classification).append("'.");
            report.add(buf.toString());
        }

        if (disseminations.hasDissemination(Disseminations.NOFORN) && !isClassifiedOrControlUnclassified()) {
            StringBuilder buf = new StringBuilder();
            buf.append("The ").append(Disseminations.NOFORN).append(" Dissemination is only valid for '")
                    .append(Classification.confidential()).append("', ;").append(Classification.secret())
                    .append("' or '").append(Classification.topSecret()).append("' classifications, or the '")
                    .append(Classification.unclassified()).append("' classification with the ")
                    .append(Disseminations.CONTROLLED_UNCLASSIFIED_INFORMATION)
                    .append(" Dissemination. It is not valid for classification: '").append(this.classification)
                    .append("'.");
            report.add(buf.toString());
        }

        if (disseminations.hasDissemination(Disseminations.RELIDO) && !isClassified()) {
            StringBuilder buf = new StringBuilder();
            buf.append("The ").append(Disseminations.RELIDO).append(" Dissemination is only valid for '")
                    .append(Classification.confidential()).append("', '").append(Classification.secret())
                    .append("' and '").append(Classification.topSecret())
                    .append("'. It is not valid for classification: '").append(this.classification).append("'.");
            report.add(buf.toString());
        }
    }

    private void checkOtherDisseminations(List<String> report) {
        if (otherDisseminations.hasOtherDissemination(OtherDisseminations.EXCLUSIVE_DISTRIBUTION)) {
            if (!isClassifiedOrControlUnclassified()) {
                StringBuilder buf = new StringBuilder();
                buf.append("The ").append(OtherDisseminations.EXCLUSIVE_DISTRIBUTION)
                        .append(" Other Dissemination is only valid for '").append(Classification.confidential())
                        .append("', ;").append(Classification.secret()).append("' or '")
                        .append(Classification.topSecret()).append("' classifications, or the '")
                        .append(Classification.unclassified()).append("' classification with the ")
                        .append(Disseminations.CONTROLLED_UNCLASSIFIED_INFORMATION)
                        .append(" Dissemination. It is not valid for classification: '").append(this.classification)
                        .append("'.");
                report.add(buf.toString());
            }
            if (disseminations.hasDissemination(Disseminations.RELEASE_TO)) {
                StringBuilder buf = new StringBuilder();
                buf.append("The ").append(OtherDisseminations.EXCLUSIVE_DISTRIBUTION).append(
                        " Other Dissemination may not be shared with foreign governments or international organisations. As such it is mutually exclusive with the ")
                        .append(Disseminations.RELEASE_TO).append(" Dissemination.");
                report.add(buf.toString());
            }
        }

        if (otherDisseminations.hasOtherDissemination(OtherDisseminations.NO_DISTRIBUTION)) {
            if (!isClassifiedOrControlUnclassified()) {
                StringBuilder buf = new StringBuilder();
                buf.append("The ").append(OtherDisseminations.NO_DISTRIBUTION)
                        .append(" Other Dissemination is only valid for '").append(Classification.confidential())
                        .append("', ;").append(Classification.secret()).append("' or '")
                        .append(Classification.topSecret()).append("' classifications, or the '")
                        .append(Classification.unclassified()).append("' classification with the ")
                        .append(Disseminations.CONTROLLED_UNCLASSIFIED_INFORMATION)
                        .append(" Dissemination. It is not valid for classification: '").append(this.classification)
                        .append("'.");
                report.add(buf.toString());
            }
            if (disseminations.hasDissemination(Disseminations.RELEASE_TO)) {
                StringBuilder buf = new StringBuilder();
                buf.append("The ").append(OtherDisseminations.NO_DISTRIBUTION).append(
                        " Other Dissemination may not be shared with foreign governments or international organisations. As such it is mutually exclusive with the ")
                        .append(Disseminations.RELEASE_TO).append(" Dissemination.");
                report.add(buf.toString());
            }
            if (this.additionalMarkings.isEmpty()) {
                // Additional Markings is empty, indicating that there are no Distribution
                // Instructions provided for this used of NODIS. Of course when there ARE
                // Additional Markings there is no guarantee that they are Distribution
                // Instructions for a NODIS Dissemination mark.
                StringBuilder buf = new StringBuilder();
                buf.append("Have ").append(OtherDisseminations.NO_DISTRIBUTION).append(
                        " Other Dissemination. It is expected that this will have Distribution Instructions associated with it. ")
                        .append("Such Distribution Instructions should appear as Additional Markings in this Builder. ")
                        .append("However Additional Markings is empty, indicating that no such Distribution Instructions have been included.");
                LOGGER.warning(buf.toString());
            }
        }

        if (otherDisseminations.hasOtherDissemination(OtherDisseminations.SENSITIVE_BUT_UNCLASSIFIED)
                && !isUnclassified()) {
            StringBuilder buf = new StringBuilder();
            buf.append("The ").append(OtherDisseminations.SENSITIVE_BUT_UNCLASSIFIED)
                    .append(" Other Dissemination may only be used with the '").append(Classification.unclassified())
                    .append("' classification. It is not valid for classification: '").append(this.classification)
                    .append("'.");
            report.add(buf.toString());
        }

        if (otherDisseminations.hasOtherDissemination(OtherDisseminations.SENSITIVE_BUT_UNCLASSIFIED_NOFORN)
                && !isUnclassified()) {
            StringBuilder buf = new StringBuilder();
            buf.append("The ").append(OtherDisseminations.SENSITIVE_BUT_UNCLASSIFIED_NOFORN)
                    .append(" Other Dissemination may only be used with the '").append(Classification.unclassified())
                    .append("' classification. It is not valid for classification: '").append(this.classification)
                    .append("'.");
            report.add(buf.toString());
        }
    }
}

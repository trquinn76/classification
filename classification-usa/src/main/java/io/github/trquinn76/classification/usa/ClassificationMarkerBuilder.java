package io.github.trquinn76.classification.usa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import io.github.trquinn76.classification.usa.model.AtomicEnergyActMarkings;
import io.github.trquinn76.classification.usa.model.Classification;
import io.github.trquinn76.classification.usa.model.ClassificationMarker;
import io.github.trquinn76.classification.usa.model.DisseminationControls;
import io.github.trquinn76.classification.usa.model.NonUSAndJointType;
import io.github.trquinn76.classification.usa.model.OtherDisseminationControls;
import io.github.trquinn76.classification.usa.subbuilders.AtomicEnergyActInformationBuilder;
import io.github.trquinn76.classification.usa.subbuilders.ClassificationModifierBuilder;
import io.github.trquinn76.classification.usa.subbuilders.DisseminationControlsBuilder;
import io.github.trquinn76.classification.usa.subbuilders.FGIBuilder;
import io.github.trquinn76.classification.usa.subbuilders.OtherDisseminationControlsBuilder;
import io.github.trquinn76.classification.usa.subbuilders.SAPBuilder;
import io.github.trquinn76.classification.usa.subbuilders.SCIBuilder;

public class ClassificationMarkerBuilder {

    private static final Logger LOGGER = Logger.getLogger(ClassificationMarkerBuilder.class.getCanonicalName());

    public final ClassificationModifierBuilder modifier;
    public final SCIBuilder sci;
    public final SAPBuilder sap;
    public final AtomicEnergyActInformationBuilder aea;
    public final FGIBuilder fgi;
    public final DisseminationControlsBuilder disseminations;
    public final OtherDisseminationControlsBuilder otherDisseminations;

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
        this.disseminations = new DisseminationControlsBuilder(this);
        this.otherDisseminations = new OtherDisseminationControlsBuilder(this);
    }

    public ClassificationMarkerBuilder(ClassificationMarker marker) {
        this();
        modifier.populate(marker.classificationModifier());
        sci.populate(marker.sensitiveCompartmentInformationControlSystems());
        sap.populate(marker.specialAccessPrograms());
        aea.populate(marker.atomicEnergyInformationMarker());
        fgi.populate(marker.foreignGovernmentInformationMarker());
        disseminations.populate(marker.disseminations());
        otherDisseminations.populate(marker.otherDisseminations());
        additionalMarkings.addAll(marker.additionalMarkings());
    }

    public ClassificationMarkerBuilder setClassification(Classification classification) {
        Objects.requireNonNull(classification);
        this.classification = classification;
        return this;
    }

    public Classification getClassification() {
        return this.classification;
    }

    public ClassificationMarkerBuilder unclassified() {
        return this.setClassification(Classification.unclassified());
    }

    // No restricted function, as that classification is NOT valid in the USA
    // context. It is only valid in some limited
    // circumstances involving Foreign markings - this library is also allowing
    // Restricted for Joint and NATO markers,
    // but in the USA context they should never be seen.

    public ClassificationMarkerBuilder confidential() {
        return this.setClassification(Classification.confidential());
    }

    public ClassificationMarkerBuilder secret() {
        return this.setClassification(Classification.secret());
    }

    public ClassificationMarkerBuilder topSecret() {
        return this.setClassification(Classification.topSecret());
    }

    public boolean isUnclassified() {
        return Classification.unclassified().equals(this.classification);
    }

    public boolean isRestricted() {
        return Classification.restricted().equals(this.classification);
    }

    public boolean isClassified() {
        return Set.of(Classification.confidential(), Classification.secret(), Classification.topSecret())
                .contains(this.classification);
    }

    public boolean isHighlyClassified() {
        return Set.of(Classification.secret(), Classification.topSecret()).contains(this.classification);
    }

    public boolean isClassifiedOrControlUnclassified() {
        boolean retval = isClassified();
        if (!retval) {
            retval = Classification.unclassified().equals(this.classification)
                    && disseminations.hasDissemination(DisseminationControls.CONTROLLED_UNCLASSIFIED_INFORMATION);
        }
        return retval;
    }

    public ClassificationMarkerBuilder addAdditionalMarkings(Collection<String> additionalMarkings) {
        this.additionalMarkings.addAll(additionalMarkings);
        return this;
    }

    public ClassificationMarkerBuilder addAdditionMarking(String str) {
        this.additionalMarkings.add(str);
        return this;
    }
    
    public Set<String> getAdditionalMarkings() {
        return new TreeSet<>(this.additionalMarkings);
    }

    public ClassificationMarkerBuilder removeAdditionalMarking(String str) {
        this.additionalMarkings.remove(str);
        return this;
    }

    public ClassificationMarkerBuilder clearAdditionMarkings() {
        this.additionalMarkings.clear();
        return this;
    }

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
            if (this.disseminations.hasDissemination(DisseminationControls.NOFORN)) {
                StringBuilder buf = new StringBuilder();
                buf.append("May not have the ").append(DisseminationControls.NOFORN.toString())
                        .append(" dissemination control with the Classification Modifier: ")
                        .append(modifier.getNonUsAndJointType());
                report.add(buf.toString());
            }
        }
    }

    private void checkClassification(List<String> report) {
        if (isRestricted()) {
            // Restricted is not usually valid in the USA context. Check that the context
            // has been appropriately modified.
            if (modifier.isPopulated() && (modifier.isForeign() || modifier.isNato())) {
                StringBuilder buf = new StringBuilder();
                buf.append("Allowing classification '").append(Classification.restricted().toString())
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
        if (sci.getControlSystems().contains(Utils.HCS)
                || sci.getCompartments(Utils.TALENT_KEYHOLE).contains(Utils.GEOCAP)) {
            // must have dissem NOFORN
            if (!disseminations.hasDissemination(DisseminationControls.NOFORN)) {
                StringBuilder buf = new StringBuilder();
                buf.append("The dissemination '").append(DisseminationControls.NOFORN.toString())
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
            Set<AtomicEnergyActMarkings> highClassificationSet = Set.of(AtomicEnergyActMarkings.RESTRICTED_DATA,
                    AtomicEnergyActMarkings.RESTRICTED_DATA_CNWDI, AtomicEnergyActMarkings.RESTRICTED_DATA_SIGMA);
            Set<AtomicEnergyActMarkings> classificationSet = Set.of(AtomicEnergyActMarkings.FORMALLY_RESTRICTED_DATA,
                    AtomicEnergyActMarkings.FORMALLY_RESTRICTED_DATA_SIGMA,
                    AtomicEnergyActMarkings.TRANSCLASSIFIED_FOREIGN_NUCLEAR_INFORMATION);
            Set<AtomicEnergyActMarkings> unclassifiedSet = Set.of(
                    AtomicEnergyActMarkings.DOD_UNCLASSIFIED_CONTROLLED_NUCLEAR_INFORMATION,
                    AtomicEnergyActMarkings.DOE_UNCLASSIFIED_CONTROLLED_NUCLEAR_INFORMATION);
            if (highClassificationSet.contains(aea.getMark())) {
                if (!isHighlyClassified()) {
                    StringBuilder buf = new StringBuilder();
                    buf.append("Atomic Energy Act Information ").append(aea.getMark().toString())
                            .append(" is only valid for classifications '").append(Classification.secret().toString())
                            .append("'and '").append(Classification.topSecret())
                            .append("'. It is not valid for classification: '").append(this.classification.toString())
                            .append("'.");
                    report.add(buf.toString());
                }
            }
            if (classificationSet.contains(aea.getMark())) {
                if (isClassified()) {
                    StringBuilder buf = new StringBuilder();
                    buf.append("Atomic Energy Act Information ").append(aea.getMark().toString())
                            .append(" is only valid for classifications '")
                            .append(Classification.confidential().toString()).append("', ")
                            .append(Classification.secret().toString()).append("'and '")
                            .append(Classification.topSecret()).append("'. It is not valid for classification: '")
                            .append(this.classification.toString()).append("'.");
                    report.add(buf.toString());
                }
            }
            if (unclassifiedSet.contains(aea.getMark())) {
                if (!isUnclassified()) {
                    StringBuilder buf = new StringBuilder();
                    buf.append("Atomic Energy Act Information ").append(aea.getMark().toString())
                            .append(" is only valid for classification '")
                            .append(Classification.unclassified().toString()).append("'.");
                    report.add(buf.toString());
                }
            }

            Set<AtomicEnergyActMarkings> nofornRequiredSet = Set.of(AtomicEnergyActMarkings.RESTRICTED_DATA,
                    AtomicEnergyActMarkings.RESTRICTED_DATA_CNWDI, AtomicEnergyActMarkings.RESTRICTED_DATA_SIGMA,
                    AtomicEnergyActMarkings.FORMALLY_RESTRICTED_DATA,
                    AtomicEnergyActMarkings.FORMALLY_RESTRICTED_DATA_SIGMA);
            if (nofornRequiredSet.contains(aea.getMark())) {
                if (!disseminations.hasDissemination(DisseminationControls.NOFORN)) {
                    // According to "Intelligence Community Markings System Register and Manual"
                    // these AEA markings require the Dissemination marking NOFORN, unless there
                    // is a sharing agreement. Meanwhile the "DoD Information Security Program:
                    // Marking of Information" document does not mention this requirement.
                    // So in this library, will detect the lack of NOFORN for these AEA Markings,
                    // and will put out a WARNING rather than an error.
                    StringBuilder buf = new StringBuilder();
                    buf.append("The AEA Mark '").append(aea.getMark().toString())
                            .append("' is expected to be associated with the dissemination mark '")
                            .append(DisseminationControls.NOFORN.toString())
                            .append("'. However, this is not required.");
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

            if (disseminations.hasDissemination(DisseminationControls.RELEASE_TO)) {
                Set<String> intersection = fgi.getCountries();
                intersection.retainAll(disseminations.getCountries(DisseminationControls.RELEASE_TO));
                if (!intersection.equals(fgi.getCountries())) {
                    StringBuilder buf = new StringBuilder();
                    buf.append("When using the ").append(DisseminationControls.RELEASE_TO).append(
                            " Dissemination with Foreign Government Information, the Releasable To list needs to contain all Countries in the FGI list. FGI list: ");
                    buf.append(String.join(", ", fgi.getCountries()));
                    buf.append(". Releasable To list: ");
                    buf.append(String.join(", ", disseminations.getCountries(DisseminationControls.RELEASE_TO)));
                    buf.append(".");
                    report.add(buf.toString());
                }
            }
        }
    }

    private void checkDisseminations(List<String> report) {
        if (disseminations.hasDissemination(DisseminationControls.WAIVED) && !sap.isPopulated()) {
            StringBuilder buf = new StringBuilder();
            buf.append("If the ").append(DisseminationControls.WAIVED).append(
                    " Dissemination is used, then there must be at least one SAR Program defined, to be 'waived'.");
            report.add(buf.toString());
        }

        if (disseminations.hasDissemination(DisseminationControls.FOUO) && !isUnclassified()) {
            StringBuilder buf = new StringBuilder();
            buf.append("The ").append(DisseminationControls.FOUO).append(" may only be used with '")
                    .append(Classification.unclassified()).append("'. It is not valid for classification: '")
                    .append(this.classification).append("'.");
            report.add(buf.toString());
        }

        if (disseminations.hasDissemination(DisseminationControls.CONTROLLED_UNCLASSIFIED_INFORMATION)
                && !isUnclassified()) {
            StringBuilder buf = new StringBuilder();
            buf.append("The ").append(DisseminationControls.CONTROLLED_UNCLASSIFIED_INFORMATION)
                    .append(" may only be used with '").append(Classification.unclassified())
                    .append("'. It is not valid for classification: '").append(this.classification).append("'.");
            report.add(buf.toString());
        }

        if (disseminations.hasDissemination(DisseminationControls.ORIGINATOR_CONTROLLED) && !isClassified()) {
            StringBuilder buf = new StringBuilder();
            buf.append("The ").append(DisseminationControls.ORIGINATOR_CONTROLLED)
                    .append(" Dissemination may only be used with Classifications '")
                    .append(Classification.confidential()).append("', '").append(Classification.secret())
                    .append("' or '").append(Classification.topSecret())
                    .append("'. It is not valid for classification: '").append(this.classification).append("'.");
            report.add(buf.toString());
        }

        if (disseminations.hasDissemination(DisseminationControls.RELEASE_TO) && !isClassifiedOrControlUnclassified()) {
            StringBuilder buf = new StringBuilder();
            buf.append("The ").append(DisseminationControls.RELEASE_TO).append(" Dissemination may only be used with '")
                    .append(Classification.confidential()).append("', '").append(Classification.secret())
                    .append("' and '").append(Classification.topSecret()).append("' classifications, or '")
                    .append(Classification.unclassified()).append("' classification with the Dissemination ")
                    .append(DisseminationControls.CONTROLLED_UNCLASSIFIED_INFORMATION)
                    .append(". It is not valid for classification: '").append(this.classification).append("'.");
            report.add(buf.toString());
        }

        if (disseminations.hasDissemination(DisseminationControls.DISPLAY_ONLY) && !isClassified()) {
            StringBuilder buf = new StringBuilder();
            buf.append("The ").append(DisseminationControls.DISPLAY_ONLY)
                    .append(" Dissemination may only be used with Classifications '")
                    .append(Classification.confidential()).append("', '").append(Classification.secret())
                    .append("' or '").append(Classification.topSecret())
                    .append("'. It is not valid for classification: '").append(this.classification).append("'.");
            report.add(buf.toString());
        }

        if (disseminations.hasDissemination(DisseminationControls.CONTROLLED_IMAGERY) && !isHighlyClassified()) {
            StringBuilder buf = new StringBuilder();
            buf.append("The ").append(DisseminationControls.CONTROLLED_IMAGERY)
                    .append(" Dissemination is only valid for '").append(Classification.secret())
                    .append("' classification. It is also permitted for '").append(Classification.topSecret())
                    .append("' classification in this library, as merging data could lead to this out come. This Dissemination is not valid for classification: '")
                    .append(this.classification).append("'.");
            report.add(buf.toString());
        }

        if (disseminations.hasDissemination(DisseminationControls.NOFORN) && !isClassifiedOrControlUnclassified()) {
            StringBuilder buf = new StringBuilder();
            buf.append("The ").append(DisseminationControls.NOFORN).append(" Dissemination is only valid for '")
                    .append(Classification.confidential()).append("', ;").append(Classification.secret())
                    .append("' or '").append(Classification.topSecret()).append("' classifications, or the '")
                    .append(Classification.unclassified()).append("' classification with the ")
                    .append(DisseminationControls.CONTROLLED_UNCLASSIFIED_INFORMATION)
                    .append(" Dissemination. It is not valid for classification: '").append(this.classification)
                    .append("'.");
            report.add(buf.toString());
        }

        if (disseminations.hasDissemination(DisseminationControls.RELIDO) && !isClassified()) {
            StringBuilder buf = new StringBuilder();
            buf.append("The ").append(DisseminationControls.RELIDO).append(" Dissemination is only valid for '")
                    .append(Classification.confidential()).append("', '").append(Classification.secret())
                    .append("' and '").append(Classification.topSecret())
                    .append("'. It is not valid for classification: '").append(this.classification).append("'.");
            report.add(buf.toString());
        }
    }

    private void checkOtherDisseminations(List<String> report) {
        if (otherDisseminations.hasOtherDissemination(OtherDisseminationControls.EXCLUSIVE_DISTRIBUTION)) {
            if (!isClassifiedOrControlUnclassified()) {
                StringBuilder buf = new StringBuilder();
                buf.append("The ").append(OtherDisseminationControls.EXCLUSIVE_DISTRIBUTION)
                        .append(" Other Dissemination is only valid for '").append(Classification.confidential())
                        .append("', ;").append(Classification.secret()).append("' or '")
                        .append(Classification.topSecret()).append("' classifications, or the '")
                        .append(Classification.unclassified()).append("' classification with the ")
                        .append(DisseminationControls.CONTROLLED_UNCLASSIFIED_INFORMATION)
                        .append(" Dissemination. It is not valid for classification: '").append(this.classification)
                        .append("'.");
                report.add(buf.toString());
            }
            if (disseminations.hasDissemination(DisseminationControls.RELEASE_TO)) {
                StringBuilder buf = new StringBuilder();
                buf.append("The ").append(OtherDisseminationControls.EXCLUSIVE_DISTRIBUTION).append(
                        " Other Dissemination may not be shared with foreign governments or international organisations. As such it is mutually exclusive with the ")
                        .append(DisseminationControls.RELEASE_TO).append(" Dissemination.");
                report.add(buf.toString());
            }
        }

        if (otherDisseminations.hasOtherDissemination(OtherDisseminationControls.NO_DISTRIBUTION)) {
            if (!isClassifiedOrControlUnclassified()) {
                StringBuilder buf = new StringBuilder();
                buf.append("The ").append(OtherDisseminationControls.NO_DISTRIBUTION)
                        .append(" Other Dissemination is only valid for '").append(Classification.confidential())
                        .append("', ;").append(Classification.secret()).append("' or '")
                        .append(Classification.topSecret()).append("' classifications, or the '")
                        .append(Classification.unclassified()).append("' classification with the ")
                        .append(DisseminationControls.CONTROLLED_UNCLASSIFIED_INFORMATION)
                        .append(" Dissemination. It is not valid for classification: '").append(this.classification)
                        .append("'.");
                report.add(buf.toString());
            }
            if (disseminations.hasDissemination(DisseminationControls.RELEASE_TO)) {
                StringBuilder buf = new StringBuilder();
                buf.append("The ").append(OtherDisseminationControls.NO_DISTRIBUTION).append(
                        " Other Dissemination may not be shared with foreign governments or international organisations. As such it is mutually exclusive with the ")
                        .append(DisseminationControls.RELEASE_TO).append(" Dissemination.");
                report.add(buf.toString());
            }
            if (this.additionalMarkings.isEmpty()) {
                // Additional Markings is empty, indicating that there are no Distribution
                // Instructions provided for
                // this used of NODIS. Of course when there ARE Additional Markings there is no
                // guarantee that they
                // are Distribution Instructions for a NODIS Dissemination mark.
                StringBuilder buf = new StringBuilder();
                buf.append("Have ").append(OtherDisseminationControls.NO_DISTRIBUTION).append(
                        " Other Dissemination. It is expected that this will have Distribution Instructions associated with it. ")
                        .append("Such Distribution Instructions should appear as Additional Markings in this Builder. ")
                        .append("However Additional Markings is empty, indicating that no such Distribution Instructions have been included.");
                LOGGER.warning(buf.toString());
            }
        }

        if (otherDisseminations.hasOtherDissemination(OtherDisseminationControls.SENSITIVE_BUT_UNCLASSIFIED)
                && !isUnclassified()) {
            StringBuilder buf = new StringBuilder();
            buf.append("The ").append(OtherDisseminationControls.SENSITIVE_BUT_UNCLASSIFIED)
                    .append(" Other Dissemination may only be used with the '").append(Classification.unclassified())
                    .append("' classification. It is not valid for classification: '").append(this.classification)
                    .append("'.");
            report.add(buf.toString());
        }

        if (otherDisseminations.hasOtherDissemination(OtherDisseminationControls.SENSITIVE_BUT_UNCLASSIFIED_NOFORN)
                && !isUnclassified()) {
            StringBuilder buf = new StringBuilder();
            buf.append("The ").append(OtherDisseminationControls.SENSITIVE_BUT_UNCLASSIFIED_NOFORN)
                    .append(" Other Dissemination may only be used with the '").append(Classification.unclassified())
                    .append("' classification. It is not valid for classification: '").append(this.classification)
                    .append("'.");
            report.add(buf.toString());
        }
    }
}

package io.github.trquinn76.classification.usa.subbuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import io.github.trquinn76.classification.usa.ClassificationMarkerBuilder;
import io.github.trquinn76.classification.usa.model.AtomicEnergyActInformationMarker;
import io.github.trquinn76.classification.usa.model.AtomicEnergyActMarkings;

/**
 * Builder for {@link AtomicEnergyActInformationMarker}'s.
 * 
 * This is a sub builder for {@link ClassificationMarkerBuilder}, and should not
 * be created outside the context of an instance of
 * {@link ClassificationMarkerBuilder}.
 */
public class AtomicEnergyActInformationBuilder {

    /**
     * The set of {@link AtomicEnergyActMarkings} which require SECRET or TOP SECRET
     * Classification.
     */
    public static Set<AtomicEnergyActMarkings> HIGHCLASSIFICATIONSET = new HashSet<>(
            Set.of(AtomicEnergyActMarkings.RESTRICTED_DATA, AtomicEnergyActMarkings.RESTRICTED_DATA_CNWDI,
                    AtomicEnergyActMarkings.RESTRICTED_DATA_SIGMA));
    /**
     * The set of {@link AtomicEnergyActMarkings} which require CONFIDENTIAL, SECRET
     * or TOP SECRET Classification.
     */
    public static Set<AtomicEnergyActMarkings> CLASSIFICATIONSET = new HashSet<>(Set.of(
            AtomicEnergyActMarkings.FORMALLY_RESTRICTED_DATA, AtomicEnergyActMarkings.FORMALLY_RESTRICTED_DATA_SIGMA,
            AtomicEnergyActMarkings.TRANSCLASSIFIED_FOREIGN_NUCLEAR_INFORMATION));
    /**
     * The set of {@link AtomicEnergyActMarkings} which require UNCLASSIFIED
     * Classification.
     */
    public static Set<AtomicEnergyActMarkings> UNCLASSIFIEDSET = new HashSet<>(
            Set.of(AtomicEnergyActMarkings.DOD_UNCLASSIFIED_CONTROLLED_NUCLEAR_INFORMATION,
                    AtomicEnergyActMarkings.DOE_UNCLASSIFIED_CONTROLLED_NUCLEAR_INFORMATION));
    /**
     * The set of {@link AtomicEnergyActMarkings} which are expected, but not
     * required, to be associated with the NOFORN Dissemination.
     */
    public static Set<AtomicEnergyActMarkings> NOFORNREQUIREDSET = new HashSet<>(
            Set.of(AtomicEnergyActMarkings.RESTRICTED_DATA, AtomicEnergyActMarkings.RESTRICTED_DATA_CNWDI,
                    AtomicEnergyActMarkings.RESTRICTED_DATA_SIGMA, AtomicEnergyActMarkings.FORMALLY_RESTRICTED_DATA,
                    AtomicEnergyActMarkings.FORMALLY_RESTRICTED_DATA_SIGMA));

    private AtomicEnergyActMarkings aeaMark = null;
    private Set<Integer> sigmaNumbers = new TreeSet<>();

    private ClassificationMarkerBuilder parent;

    /**
     * Constructs the builder.
     * 
     * @param parent the parent {@link ClassificationMarkerBuilder}.
     */
    public AtomicEnergyActInformationBuilder(ClassificationMarkerBuilder parent) {
        this.parent = parent;
    }

    /**
     * Populates this builder with the values in the given
     * {@link AtomicEnergyActInformationMarker}.
     * 
     * @param marker the {@link AtomicEnergyActInformationMarker} from which to
     *               populate the builder.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder populate(AtomicEnergyActInformationMarker marker) {
        clear();
        if (marker != null) {
            this.aeaMark = marker.aeaMark();
            this.sigmaNumbers.addAll(marker.sigmaNumbers());
        }
        return parent;
    }

    /**
     * Sets the {@link AtomicEnergyActMarkings} value.
     * 
     * @param mark the {@link AtomicEnergyActMarkings} to set on the builder. May be
     *             null.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder setMark(AtomicEnergyActMarkings mark) {
        this.aeaMark = mark;
        return parent;
    }

    /**
     * Gets the currently set {@link AtomicEnergyActMarkings}.
     * 
     * @return the current {@link AtomicEnergyActMarkings}.
     */
    public AtomicEnergyActMarkings getMark() {
        return this.aeaMark;
    }

    /**
     * Sets the Sigma numbers in the builder.
     * 
     * @param sigmaNumbers the sigma numbers to set. These will replace any existing
     *                     values. May not be null. May be empty.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder setSigmaNumbers(Collection<Integer> sigmaNumbers) {
        Objects.requireNonNull(sigmaNumbers);
        this.sigmaNumbers.clear();
        this.sigmaNumbers.addAll(sigmaNumbers);
        return parent;
    }

    /**
     * Gets the currently set sigma numbers, in an ordered Set.
     * 
     * @return a copy of he sigma numbers in an ordered Set.
     */
    public Set<Integer> getSigmaNumbers() {
        Set<Integer> retSet = new TreeSet<>();
        retSet.addAll(this.sigmaNumbers);
        return retSet;
    }

    /**
     * Clears existing sigma numbers.
     * 
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder clearSigmaNumbers() {
        this.sigmaNumbers.clear();
        return parent;
    }

    /**
     * Clears current {@link AtomicEnergyActMarkings} mark and any sigma numbers.
     * 
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder clear() {
        setMark(null);
        return clearSigmaNumbers();
    }

    /**
     * Sets the {@link AtomicEnergyActMarkings} mark to RESTRICTED_DATA.
     * 
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder restrictedData() {
        return setMark(AtomicEnergyActMarkings.RESTRICTED_DATA);
    }

    /**
     * Sets the {@link AtomicEnergyActMarkings} mark to FORMALLY_RESTRUCTED_DATA.
     * 
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder formallyRestrictedData() {
        return setMark(AtomicEnergyActMarkings.FORMALLY_RESTRICTED_DATA);
    }

    /**
     * Sets the {@link AtomicEnergyActMarkings} mark to RESTRICTED_DATA_SIGMA, with
     * sigma numbers set to the given list.
     * 
     * @param sigmaNumbers an array of Sigma Numbers. May not be null. May be empty.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder restrictedDataSigma(Integer... sigmaNumbers) {
        Objects.requireNonNull(sigmaNumbers);
        setMark(AtomicEnergyActMarkings.RESTRICTED_DATA_SIGMA);
        return setSigmaNumbers(Arrays.asList(sigmaNumbers));
    }

    /**
     * Sets the {@link AtomicEnergyActMarkings} mark to RESTRICTED_DATA_CNWDI.
     * 
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder restrictedDataCnwdi() {
        return setMark(AtomicEnergyActMarkings.RESTRICTED_DATA_CNWDI);
    }

    /**
     * Sets the {@link AtomicEnergyActMarkings} mark to
     * FORMALLY_RESTRICTED_DATA_SIGMA, with sigma numbers set to the given list.
     * 
     * @param sigmaNumbers an array of Sigma Numbers. May not be null. May be empty.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder formallyRestrictedDataSigma(Integer... sigmaNumbers) {
        Objects.requireNonNull(sigmaNumbers);
        setMark(AtomicEnergyActMarkings.FORMALLY_RESTRICTED_DATA_SIGMA);
        return setSigmaNumbers(Arrays.asList(sigmaNumbers));
    }

    /**
     * Sets the current Sigma Numbers to the given array.
     * 
     * Will also update current {@link AtomicEnergyActMarkings} mark to one which
     * includes Sigma, and raise an error if that is not possible.
     * 
     * @param sigmaNumbers an array of Sigma Numbers. May not be null. May be empty.
     * 
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder sigma(Integer... sigmaNumbers) {
        if (this.aeaMark == AtomicEnergyActMarkings.RESTRICTED_DATA) {
            return restrictedDataSigma(sigmaNumbers);
        } else if (this.aeaMark == AtomicEnergyActMarkings.RESTRICTED_DATA_SIGMA) {
            return setSigmaNumbers(Arrays.asList(sigmaNumbers));
        } else if (this.aeaMark == AtomicEnergyActMarkings.FORMALLY_RESTRICTED_DATA) {
            return formallyRestrictedDataSigma(sigmaNumbers);
        } else if (this.aeaMark == AtomicEnergyActMarkings.FORMALLY_RESTRICTED_DATA_SIGMA) {
            return setSigmaNumbers(Arrays.asList(sigmaNumbers));
        } else {
            throw new IllegalStateException("Cannot assign SIGMA to AEA Mark: '" + aeaMark.toString() + "'.");
        }
    }

    /**
     * Returns true iff the builder is populated with values.
     * 
     * @return true iff either the {@link AtomicEnergyActMarkings} mark is not null,
     *         or the sigma number set is not empty.
     */
    public boolean isPopulated() {
        return this.aeaMark != null || !this.sigmaNumbers.isEmpty();
    }

    /**
     * Used to determine if the {@link AtomicEnergyActInformationBuilder} is in a
     * valid state, and able to build a {@link AtomicEnergyActInformationMarker}.
     * 
     * @return a list of String, which reports on invalid parts of the builder which
     *         would need to be fixed in order to perform a build. If there are no
     *         problems, and the builder is in a valid state, this list is empty.
     */
    public List<String> isValid() {
        List<String> report = new ArrayList<>();

        if (isPopulated()) {
            if (this.aeaMark == null) {
                report.add("Must have an Atomic Energy Act Information Marking. Or clear Sigma Numbers.");
            } else {
                switch (this.aeaMark) {
                case RESTRICTED_DATA_SIGMA:
                case FORMALLY_RESTRICTED_DATA_SIGMA:
                    if (this.sigmaNumbers.isEmpty()) {
                        StringBuilder buf = new StringBuilder();
                        buf.append("The AEA Mark '").append(this.aeaMark.toString())
                                .append("' is required to have at least one SIGMA number.");
                        report.add(buf.toString());
                    }
                    break;
                case RESTRICTED_DATA:
                case RESTRICTED_DATA_CNWDI:
                case FORMALLY_RESTRICTED_DATA:
                case TRANSCLASSIFIED_FOREIGN_NUCLEAR_INFORMATION:
                case DOD_UNCLASSIFIED_CONTROLLED_NUCLEAR_INFORMATION:
                case DOE_UNCLASSIFIED_CONTROLLED_NUCLEAR_INFORMATION:
                    if (!this.sigmaNumbers.isEmpty()) {
                        StringBuilder buf = new StringBuilder();
                        buf.append("The AEA Mark '").append(this.aeaMark.toString())
                                .append("' may not have any SIGMA numbers. SIGMA Number count: ")
                                .append(this.sigmaNumbers.size());
                        report.add(buf.toString());
                    }
                    break;
                }
            }
        }

        return report;
    }

    /**
     * Builds a new instance of {@link AtomicEnergyActInformationMarker} based on
     * the fields in the builder.
     * 
     * @return a new instance of {@link AtomicEnergyActInformationMarker}.
     * @throws IllegalStateException if the builder is not in a valid state, then
     *                               this exception is thrown.
     */
    public AtomicEnergyActInformationMarker build() {

        if (!isValid().isEmpty()) {
            // invalid state to build an AtomicEnergyActInformationMarker.
            // throwing an exception. Am not logging, as that should be handled in
            // ClassificationMarkerBuilder before this function is even called.
            throw new IllegalStateException(
                    "Invalid state. Cannot build instance of AtomicEnergyActInformationMarker.");
        }

        if (isPopulated()) {
            return new AtomicEnergyActInformationMarker(this.aeaMark, List.copyOf(this.sigmaNumbers));
        } else {
            return null;
        }
    }

    // excludes reference to parent builder in hashCode calculation.
    @Override
    public int hashCode() {
        return Objects.hash(aeaMark, sigmaNumbers);
    }

    // excludes reference to parent builder in equality comparison.
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AtomicEnergyActInformationBuilder other = (AtomicEnergyActInformationBuilder) obj;
        return aeaMark == other.aeaMark && Objects.equals(sigmaNumbers, other.sigmaNumbers);
    }
}

package io.github.trquinn76.classification.usa.subbuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import io.github.trquinn76.classification.usa.ClassificationMarkerBuilder;
import io.github.trquinn76.classification.usa.Utils;
import io.github.trquinn76.classification.usa.model.NonUSAndJointClassificationModifier;
import io.github.trquinn76.classification.usa.model.NonUSAndJointType;

/**
 * Builder for {@link NonUSAndJointClassificationModifier}'s.
 * 
 * This is a sub builder for {@link ClassificationMarkerBuilder}, and should not
 * be created outside the context of an instance of
 * {@link ClassificationMarkerBuilder}.
 */
public class ClassificationModifierBuilder {

    private NonUSAndJointType nonUsAndJointType = null;
    private Set<String> nonUsAndJointCountryList = new TreeSet<>(Utils.ALPHABETIC);
    private String natoSpecialMark = null;

    private ClassificationMarkerBuilder parent;

    /**
     * Constructs the builder.
     * 
     * @param parent the parent {@link ClassificationMarkerBuilder}.
     */
    public ClassificationModifierBuilder(ClassificationMarkerBuilder parent) {
        Objects.requireNonNull(parent);
        this.parent = parent;
    }

    /**
     * Populates this builder with the values in the given
     * {@link NonUSAndJointClassificationModifier}.
     * 
     * @param modifier the {@link NonUSAndJointClassificationModifier} from which to
     *                 populate the builder.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder populate(NonUSAndJointClassificationModifier modifier) {
        this.clear();
        if (modifier != null) {
            this.nonUsAndJointType = modifier.type();
            this.natoSpecialMark = modifier.natoSpecialMark();
            this.nonUsAndJointCountryList.addAll(modifier.countries());
        }
        return parent;
    }

    /**
     * Sets the {@link NonUSAndJointType} field, which controls the kind of
     * modification of the classification.
     * 
     * @param type the {@link NonUSAndJointType} to set. May be null.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder setNonUsAndJointType(NonUSAndJointType type) {
        this.nonUsAndJointType = type;
        return parent;
    }

    /**
     * Gets the current {@link NonUSAndJointType} value in the builder.
     * 
     * @return the current {@link NonUSAndJointType} value.
     */
    public NonUSAndJointType getNonUsAndJointType() {
        return this.nonUsAndJointType;
    }

    /**
     * Sets a country list, for JOINT or FOREIGN modifications.
     * 
     * Any existing values are removed, and replaced with the given values.
     * 
     * @param countries the collection of countries. May not be null. May be empty.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder setJointOrForeignCountryList(Collection<String> countries) {
        Objects.requireNonNull(countries);
        this.nonUsAndJointCountryList.clear();
        this.nonUsAndJointCountryList.addAll(countries);
        return parent;
    }

    /**
     * Adds an individual country, for JOINT or FOREIGN modifications.
     * 
     * @param country the country to add to the existing list of countries. May not
     *                be null.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder addJointOrForeignCountry(String country) {
        Objects.requireNonNull(country);
        this.nonUsAndJointCountryList.add(country);
        return parent;
    }

    /**
     * Removes the given country from the current JOINT or FOREIGN country list.
     * 
     * @param country the country to remove. May not be null.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder removeJointOrForeignCountry(String country) {
        Objects.requireNonNull(country);
        this.nonUsAndJointCountryList.remove(country);
        return parent;
    }

    /**
     * Gets a copy of the current JOINT or FOREIGN countries list.
     * 
     * @return a copy of the current JOINT or FOREIGN countries list, in Alphabetic
     *         order.
     */
    public Set<String> getJointOrForeignCountries() {
        Set<String> retval = new TreeSet<>(Utils.ALPHABETIC);
        retval.addAll(this.nonUsAndJointCountryList);
        return retval;
    }

    /**
     * Sets the NATO Special Mark. Used for values such as ATOMAL and BOHEMIA.
     * 
     * @param specialMark the mark to set. May be null.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder setNatoSpecialMark(String specialMark) {
        this.natoSpecialMark = specialMark;
        return parent;
    }

    /**
     * Gets the current NATO Special Mark.
     * 
     * @return the current NATO Special Mark.
     */
    public String getNatoSpecialMark() {
        return this.natoSpecialMark;
    }

    /**
     * Sets the {@link NonUSAndJointType} to JOINT, and sets the country list to the
     * given array of countries.
     * 
     * @param countries an array of countries. May not be null. May be empty.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder joint(String... countries) {
        Objects.requireNonNull(countries);
        setNonUsAndJointType(NonUSAndJointType.JOINT);
        return setJointOrForeignCountryList(Arrays.asList(countries));
    }

    /**
     * Sets the {@link NonUSAndJointType} to NATO.
     * 
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder nato() {
        return setNonUsAndJointType(NonUSAndJointType.NATO);
    }

    /**
     * Sets the {@link NonUSAndJointType} to COSMIC.
     * 
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder cosmic() {
        return setNonUsAndJointType(NonUSAndJointType.COSMIC);
    }

    /**
     * Sets the NATO Special Mark to BOHEMIA.
     * 
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder boheamia() {
        return setNatoSpecialMark(Utils.BOHEMIA);
    }

    /**
     * Sets the NATO Special Mark to ATOMAL.
     * 
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder atomal() {
        return setNatoSpecialMark(Utils.ATOMAL);
    }

    /**
     * Sets the {@link NonUSAndJointType} to FOREIGN, and sets the foreign country.
     * 
     * @param country the foreign country this classification is being modified for.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder foreign(String country) {
        Objects.requireNonNull(country);
        setNonUsAndJointType(NonUSAndJointType.FOREIGN);
        return setJointOrForeignCountryList(List.of(country));
    }

    /**
     * Clears the country list.
     * 
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder clearCountryList() {
        this.nonUsAndJointCountryList.clear();
        return parent;
    }

    /**
     * Clears the builder. {@link NonUSAndJointType} and NATO Special Mark are set
     * null, and the country list is cleared.
     * 
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder clear() {
        this.setNonUsAndJointType(null);
        this.clearCountryList();
        return this.setNatoSpecialMark(null);
    }

    /**
     * If any of the {@link NonUSAndJointType}, NATO Special Mark or country list
     * are populated, will return true.
     * 
     * @return true if one of {@link NonUSAndJointType}, NATO Special Mark or
     *         country list are populated.
     */
    public boolean isPopulated() {
        return this.nonUsAndJointType != null || !this.nonUsAndJointCountryList.isEmpty()
                || this.natoSpecialMark != null;
    }

    /**
     * Returns true if the {@link NonUSAndJointType} is NATO or COSMIC.
     * 
     * @return true if the {@link NonUSAndJointType} is NATO or COSMIC.
     */
    public boolean isNato() {
        return List.of(NonUSAndJointType.NATO, NonUSAndJointType.COSMIC).contains(nonUsAndJointType);
    }

    /**
     * Returns true if the {@link NonUSAndJointType} is FOREIGN.
     * 
     * @return true if the {@link NonUSAndJointType} is FOREIGN.
     */
    public boolean isForeign() {
        return NonUSAndJointType.FOREIGN.equals(nonUsAndJointType);
    }

    /**
     * Returns true if the {@link NonUSAndJointType} is JOINT.
     * 
     * @return true if the {@link NonUSAndJointType} is JOINT.
     */
    public boolean isJoint() {
        return NonUSAndJointType.JOINT.equals(nonUsAndJointType);
    }

    /**
     * Used to determine if the {@link ClassificationModifierBuilder} is in a valid
     * state, and able to build a {@link NonUSAndJointClassificationModifier}.
     * 
     * @return a list of String, which reports on invalid parts of the builder which
     *         would need to be fixed in order to perform a build. If there are no
     *         problems, and the builder is in a valid state, this list is empty.
     */
    public List<String> isValid() {
        List<String> report = new ArrayList<>();

        if (isPopulated()) {
            if (this.nonUsAndJointType == null) {
                report.add("A Classification Modifer requires a NonUSAndJointType.");
            } else {
                switch (this.nonUsAndJointType) {
                case NATO:
                case COSMIC:
                    // country list should be empty.
                    if (!this.nonUsAndJointCountryList.isEmpty()) {
                        StringBuilder buf = new StringBuilder();
                        buf.append("May not have a Classification Modifier Country list for Modifier type '")
                                .append(this.nonUsAndJointType.name()).append("'.");
                        report.add(buf.toString());
                    }
                    break;
                case FOREIGN:
                    // country list should be of length one. May not have NATO Special mark.
                    if (this.nonUsAndJointCountryList.size() != 1) {
                        StringBuilder buf = new StringBuilder();
                        buf.append("Must have a Classification Modifier Country list of length one for '")
                                .append(NonUSAndJointType.FOREIGN.name()).append("'. Not: ")
                                .append(this.nonUsAndJointCountryList.size()).append(".");
                        report.add(buf.toString());
                    }
                    if (this.natoSpecialMark != null) {
                        StringBuilder buf = new StringBuilder();
                        buf.append("May not have a Classification Modifier NATO mark for Modifer type '")
                                .append(NonUSAndJointType.FOREIGN.name()).append(". NATO mark: ")
                                .append(this.natoSpecialMark).append(".");
                        report.add(buf.toString());
                    }
                    break;
                case JOINT:
                    // country list should be of minimum length 2. May not have NATO special mark.
                    if (!this.nonUsAndJointCountryList.contains(Utils.USA)) {
                        report.add("A " + NonUSAndJointType.JOINT.name()
                                + " Classification Modifier Country list must contain '" + Utils.USA + "'.");
                    }
                    if (this.nonUsAndJointCountryList.size() < 2) {
                        StringBuilder buf = new StringBuilder();
                        buf.append(
                                "Must have a minimum of two entries in the Classification Modifier Country list for Modifier type '")
                                .append(NonUSAndJointType.JOINT.name()).append("'. Current count is: ")
                                .append(this.nonUsAndJointCountryList.size()).append(".");
                        report.add(buf.toString());
                    }
                    if (this.natoSpecialMark != null) {
                        StringBuilder buf = new StringBuilder();
                        buf.append("May not have a Classification Modifier NATO mark for Modifer type '")
                                .append(NonUSAndJointType.JOINT.name()).append(". NATO mark: ")
                                .append(this.natoSpecialMark).append(".");
                        report.add(buf.toString());
                    }
                    break;
                }
            }
        }

        return report;
    }

    /**
     * Builds a new instance of {@link NonUSAndJointClassificationModifier} based on
     * the fields in the builder.
     * 
     * @return a new instance of {@link NonUSAndJointClassificationModifier}.
     * @throws IllegalStateException if the builder is not in a valid state, then
     *                               this exception is thrown.
     */
    public NonUSAndJointClassificationModifier build() {

        if (!this.isValid().isEmpty()) {
            // invalid state to build a NonUSAndJointClassificationModifier.
            // throwing an exception. Am not logging, as that should be handled in
            // ClassificationMarkerBuilder before
            // this function is even called.
            throw new IllegalStateException(
                    "Invalid state. Cannot build instance of NonUSAndJointClassificationModifier.");
        }

        if (isPopulated()) {
            return new NonUSAndJointClassificationModifier(this.nonUsAndJointType, this.natoSpecialMark,
                    List.copyOf(this.nonUsAndJointCountryList));
        } else {
            return null;
        }
    }
}

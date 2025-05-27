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

public class ClassificationModifierBuilder {

    private NonUSAndJointType nonUsAndJointType = null;
    private Set<String> nonUsAndJointCountryList = new TreeSet<>(Utils.ALPHABETIC);
    private String natoSpecialMark = null;

    private ClassificationMarkerBuilder parent;

    public ClassificationModifierBuilder(ClassificationMarkerBuilder parent) {
        Objects.requireNonNull(parent);
        this.parent = parent;
    }

    public ClassificationMarkerBuilder populate(NonUSAndJointClassificationModifier modifier) {
        this.clear();
        if (modifier != null) {
            this.nonUsAndJointType = modifier.type();
            this.natoSpecialMark = modifier.natoSpecialMark();
            this.nonUsAndJointCountryList.addAll(modifier.countries());
        }
        return parent;
    }

    public ClassificationMarkerBuilder setNonUsAndJointType(NonUSAndJointType type) {
        this.nonUsAndJointType = type;
        return parent;
    }

    public NonUSAndJointType getNonUsAndJointType() {
        return this.nonUsAndJointType;
    }

    public ClassificationMarkerBuilder setJointOrForeignCountryList(Collection<String> countries) {
        Objects.requireNonNull(countries);
        this.nonUsAndJointCountryList.clear();
        this.nonUsAndJointCountryList.addAll(countries);
        return parent;
    }

    public ClassificationMarkerBuilder addJointOrForeignCountry(String country) {
        Objects.requireNonNull(country);
        this.nonUsAndJointCountryList.add(country);
        return parent;
    }

    public ClassificationMarkerBuilder removeJointOrForeignCountry(String country) {
        this.nonUsAndJointCountryList.remove(country);
        return parent;
    }

    public Set<String> getJointOrForeignCountries() {
        Set<String> retval = new TreeSet<>(Utils.ALPHABETIC);
        retval.addAll(this.nonUsAndJointCountryList);
        return retval;
    }

    public ClassificationMarkerBuilder setNatoSpecialMark(String specialMark) {
        this.natoSpecialMark = specialMark;
        return parent;
    }

    public String getNatoSpecialMark() {
        return this.natoSpecialMark;
    }

    public ClassificationMarkerBuilder clearNonUsAndJointModifiers() {
        this.nonUsAndJointType = null;
        this.nonUsAndJointCountryList.clear();
        this.natoSpecialMark = null;
        return parent;
    }

    public ClassificationMarkerBuilder joint(String... countries) {
        Objects.requireNonNull(countries);
        setNonUsAndJointType(NonUSAndJointType.JOINT);
        return setJointOrForeignCountryList(Arrays.asList(countries));
    }

    public ClassificationMarkerBuilder nato() {
        return setNonUsAndJointType(NonUSAndJointType.NATO);
    }

    public ClassificationMarkerBuilder cosmic() {
        return setNonUsAndJointType(NonUSAndJointType.COSMIC);
    }

    public ClassificationMarkerBuilder foreign(String country) {
        Objects.requireNonNull(country);
        setNonUsAndJointType(NonUSAndJointType.FOREIGN);
        return setJointOrForeignCountryList(List.of(country));
    }

    public ClassificationMarkerBuilder clearCountryList() {
        this.nonUsAndJointCountryList.clear();
        return parent;
    }

    public ClassificationMarkerBuilder clear() {
        this.setNonUsAndJointType(null);
        this.clearCountryList();
        return this.setNatoSpecialMark(null);
    }

    public boolean isPopulated() {
        return this.nonUsAndJointType != null || !this.nonUsAndJointCountryList.isEmpty()
                || this.natoSpecialMark != null;
    }

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
                    // NOTE: USA is NOT required here.
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

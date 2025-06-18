package io.github.trquinn76.classification.usa.subbuilders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import io.github.trquinn76.classification.usa.ClassificationMarkerBuilder;
import io.github.trquinn76.classification.usa.Utils;
import io.github.trquinn76.classification.usa.model.ForeignGovernmentInformationMarker;

/**
 * Builder for {@link ForeignGovernmentInformationMarker}'s.
 * 
 * This is a sub builder for {@link ClassificationMarkerBuilder}, and should not
 * be created outside the context of an instance of
 * {@link ClassificationMarkerBuilder}.
 */
public class FGIBuilder {

    private boolean concealed = false;
    private Set<String> countries = new TreeSet<>(Utils.ALPHABETIC);

    private ClassificationMarkerBuilder parent;

    /**
     * Constructs the builder.
     * 
     * @param parent the parent {@link ClassificationMarkerBuilder}.
     */
    public FGIBuilder(ClassificationMarkerBuilder parent) {
        this.parent = parent;
    }

    /**
     * Populates this builder with the values in the given
     * {@link ForeignGovernmentInformationMarker}.
     * 
     * @param disseminationMarks the {@link ForeignGovernmentInformationMarker} from
     *                           which to populate the builder.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder populate(ForeignGovernmentInformationMarker marker) {
        clear();
        if (marker != null) {
            if (marker.countries().isEmpty()) {
                this.concealed();
            } else {
                this.setCountries(marker.countries());
            }
        }
        return parent;
    }

    /**
     * Sets the builder as concealed. This indicates that the foreign countries are
     * not to be included.
     * 
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder concealed() {
        clear();
        this.concealed = true;
        return parent;
    }

    /**
     * Sets the builder's concealed value to the given parameter.
     * 
     * @param concealed boolean indicating if the country list should be included.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder setConcealed(boolean concealed) {
        this.concealed = concealed;
        return parent;
    }

    /**
     * Indicates the current concealed state of this builder.
     * 
     * @return the value of the buidler's {@code concealed} values.
     */
    public boolean isConcealed() {
        return this.concealed;
    }

    /**
     * Sets the list of foreign countries which have contributed to the classified
     * data.
     * 
     * @param countries a collection of countries. May not be null. May be empty.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder setCountries(Collection<String> countries) {
        Objects.requireNonNull(countries);
        clear();
        this.countries.addAll(countries);
        return parent;
    }

    /**
     * Adds a country to the list of countries which have contributed to the
     * classified data.
     * 
     * @param country a country to add to the list of countries. May not be null.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder addCountry(String country) {
        Objects.requireNonNull(country);
        this.countries.add(country);
        return parent;
    }

    /**
     * Gets a copy of the current list of countries.
     * 
     * @return a Set containing a copy of the current list of foreign countries.
     */
    public Set<String> getCountries() {
        Set<String> retSet = new TreeSet<>(Utils.ALPHABETIC);
        retSet.addAll(countries);
        return retSet;
    }

    /**
     * Removes the given country from the list of countries.
     * 
     * @param country the country to remove. May not be null.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder removeCountry(String country) {
        Objects.requireNonNull(country);
        this.countries.remove(country);
        return parent;
    }

    /**
     * Clears existing values from the builder. {@code concealed} is set false, and
     * the countries list if cleared.
     * 
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder clear() {
        this.concealed = false;
        this.countries.clear();
        return parent;
    }

    /**
     * Indicates if there are any values in this builder.
     * 
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public boolean isPopulated() {
        return concealed || !countries.isEmpty();
    }

    /**
     * Used to determine if the {@link FGIBuilder} is in a valid state, and able to
     * build a {@link ForeignGovernmentInformationMarker}.
     * 
     * @return a list of String, which reports on invalid parts of the builder which
     *         would need to be fixed in order to perform a build. If there are no
     *         problems, and the builder is in a valid state, this list is empty.
     */
    public List<String> isValid() {
        List<String> report = new ArrayList<>();

        if (concealed && !this.countries.isEmpty()) {
            report.add(
                    "Cannot have a list of Foreign Government Information countries, while also Concealing Foreign Countries.");
        }

        return report;
    }

    /**
     * Builds a new {@link ForeignGovernmentInformationMarker} based on the fields
     * in the builder.
     * 
     * @return a new {@link ForeignGovernmentInformationMarker}.
     * @throws IllegalStateException if the builder is not in a valid state, then
     *                               this exception is thrown.
     */
    public ForeignGovernmentInformationMarker build() {

        List<String> report = isValid();
        if (!report.isEmpty()) {
            // Invalid state to build a ForeignGovernmentInformationMarker.
            // Throwing an exception. Am not logging, as that should be handled in
            // ClassificationMarkerBuilder before this function is even called.
            throw new IllegalStateException(
                    "Invalid state. Cannot build instance of ForeignGovernmentInformationMarker.");
        }

        if (isPopulated()) {
            return new ForeignGovernmentInformationMarker(List.copyOf(this.countries));
        }
        return null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(concealed, countries);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FGIBuilder other = (FGIBuilder) obj;
        return concealed == other.concealed && Objects.equals(countries, other.countries);
    }
}

package io.github.trquinn76.classification.usa.subbuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import io.github.trquinn76.classification.usa.ClassificationMarkerBuilder;
import io.github.trquinn76.classification.usa.Utils;
import io.github.trquinn76.classification.usa.model.DisseminationMarker;
import io.github.trquinn76.classification.usa.model.Disseminations;

/**
 * Builder for {@link DisseminationMarker}'s.
 * 
 * This is a sub builder for {@link ClassificationMarkerBuilder}, and should not
 * be created outside the context of an instance of
 * {@link ClassificationMarkerBuilder}.
 */
public class DisseminationsBuilder {

    private Map<Disseminations, Set<String>> disseminationMap = new TreeMap<>();

    private ClassificationMarkerBuilder parent;

    /**
     * Constructs the builder.
     * 
     * @param parent the parent {@link ClassificationMarkerBuilder}.
     */
    public DisseminationsBuilder(ClassificationMarkerBuilder parent) {
        this.parent = parent;
    }

    /**
     * Populates this builder with the values in the given list of
     * {@link DisseminationMarker}.
     * 
     * @param disseminationMarks the list of {@link DisseminationMarker} from which
     *                           to populate the builder.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder populate(List<DisseminationMarker> disseminationMarks) {
        for (DisseminationMarker marker : disseminationMarks) {
            setDissemination(marker.type(), marker.countries().toArray(new String[marker.countries().size()]));
        }
        return parent;
    }

    /**
     * Sets the given {@link Disseminations} as one of the dissemination marks to be
     * included.
     * 
     * Any existing country list will be replaced.
     * 
     * @param dissemControl the {@link Disseminations} to add to the builder. May
     *                      not be null.
     * @param countryList   a list of countries related to the
     *                      {@link Disseminations}. May not be null. May be empty.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder setDissemination(Disseminations dissemControl, String... countryList) {
        Objects.requireNonNull(dissemControl);
        Objects.requireNonNull(countryList);

        Set<String> countries = new TreeSet<>(Utils.USA_FIRST);
        countries.addAll(Arrays.asList(countryList));
        this.disseminationMap.put(dissemControl, countries);

        return parent;
    }

    /**
     * Sets the given {@link Disseminations} as one of the dissemination marks to be
     * included.
     * 
     * Any existing country list will be added to, with existing value retained,
     * along with adding the new values.
     * 
     * @param dissemControl the {@link Disseminations} to add to the builder. May
     *                      not be null.
     * @param countryList   a list of countries related to the
     *                      {@link Disseminations}. May not be null. May be empty.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder addDissemination(Disseminations dissemControl, String... countryList) {
        Objects.requireNonNull(dissemControl);
        Objects.requireNonNull(countryList);

        if (this.disseminationMap.keySet().contains(dissemControl)) {
            this.disseminationMap.get(dissemControl).addAll(Arrays.asList(countryList));
        } else {
            Set<String> countries = new TreeSet<>(Utils.USA_FIRST);
            countries.addAll(Arrays.asList(countryList));
            this.disseminationMap.put(dissemControl, countries);
        }
        return parent;
    }

    /**
     * Gets a copy of the current set of {@link Disseminations} in this builder.
     * 
     * @return a copy of the current set of {@link Disseminations} in this builder.
     */
    public Set<Disseminations> getDisseminations() {
        return new TreeSet<>(this.disseminationMap.keySet());
    }

    /**
     * Removes the given {@link Disseminations} from the builder.
     * 
     * @param dissemControl the {@link Disseminations} to remove. May not be null.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder removeDissemination(Disseminations dissemControl) {
        Objects.requireNonNull(dissemControl);
        this.disseminationMap.remove(dissemControl);
        return parent;
    }

    /**
     * Adds the given country to the given {@link Disseminations}. Will also add the
     * {@link Disseminations} if needed.
     * 
     * @param dissemControl the {@link Disseminations} for which to add the country.
     *                      May not be null.
     * @param country       the country String to add to the {@link Disseminations}.
     *                      May not be null.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder addCountry(Disseminations dissemControl, String country) {
        Objects.requireNonNull(dissemControl);
        Objects.requireNonNull(country);

        return addDissemination(dissemControl, country);
    }

    /**
     * Gets the list of countries for the given {@link Disseminations}.
     * 
     * @param dissemControl the {@link Disseminations} for which to get the country
     *                      list. May not be null.
     * @return the set of countries. May be empty.
     */
    public Set<String> getCountries(Disseminations dissemControl) {
        Objects.requireNonNull(dissemControl);
        Set<String> retSet = new TreeSet<>(Utils.USA_FIRST);
        if (hasDissemination(dissemControl)) {
            retSet.addAll(this.disseminationMap.get(dissemControl));
        }
        return retSet;
    }

    /**
     * Removes the given country from the given {@link Disseminations}.
     * 
     * @param dissemControl the {@link Disseminations} from which to remove the
     *                      country. May not be null.
     * @param country       the country to remove from the {@link Disseminations}.
     *                      May not be null.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder removeCountry(Disseminations dissemControl, String country) {
        Objects.requireNonNull(dissemControl);
        Objects.requireNonNull(country);
        if (this.disseminationMap.keySet().contains(dissemControl)) {
            this.disseminationMap.get(dissemControl).remove(country);
        }
        return parent;
    }

    /**
     * Sets the WAIVED {@link Disseminations}. This is only valid if there are also
     * SAP's.
     * 
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder waived() {
        return addDissemination(Disseminations.WAIVED);
    }

    /**
     * Sets the FOUO {@link Disseminations}.
     * 
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder fouo() {
        return addDissemination(Disseminations.FOUO);
    }

    /**
     * Sets the CONTROLLED_UNCLASSIFIED_INFORMATION {@link Disseminations}. Only
     * valid for UNCLASSIFIED classification.
     * 
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder controledUnclassifiedInformation() {
        return addDissemination(Disseminations.CONTROLLED_UNCLASSIFIED_INFORMATION);
    }

    /**
     * Sets the ORCON {@link Disseminations}.
     * 
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder orcon() {
        return addDissemination(Disseminations.ORIGINATOR_CONTROLLED);
    }

    /**
     * Sets the RELEASE_TO {@link Disseminations}.
     * 
     * The list of countries passed to this function can be empty, and can be
     * missing the USA. The list of countries will need to be properly populated
     * before a {@code build()} will be possible.
     * 
     * @param countries a list of countries to which release is allowed. May not be
     *                  null. May be empty.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder releaseTo(String... countries) {
        Objects.requireNonNull(countries);
        return addDissemination(Disseminations.RELEASE_TO, countries);
    }

    /**
     * Sets the DISPLAY_ONLY {@link Disseminations}.
     * 
     * The list of countries passed to this function can be empty. The list of
     * countries will need to be properly populated before a {@code build()} will be
     * possible.
     * 
     * @param countries a list of countries to which the data may be
     *                  displayed/shown, but not released/copied to. May not be
     *                  null. May be empty.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder displayOnly(String... countries) {
        Objects.requireNonNull(countries);
        return addDissemination(Disseminations.DISPLAY_ONLY, countries);
    }

    /**
     * Sets the CONTROLLED_IMAGERY {@link Disseminations}.
     * 
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder controledImagery() {
        return addDissemination(Disseminations.CONTROLLED_IMAGERY);
    }

    /**
     * Sets the NOFORN {@link Disseminations}.
     * 
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder noforn() {
        return addDissemination(Disseminations.NOFORN);
    }

    /**
     * Sets the PROPRIETARY_INFORMATION {@link Disseminatons}.
     * 
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder propin() {
        return addDissemination(Disseminations.PROPRIETARY_INFORMATION);
    }

    /**
     * Sets the RELIDO {@link Disseminations}.
     * 
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder relido() {
        return addDissemination(Disseminations.RELIDO);
    }

    /**
     * Sets the FISA {@link Disseminations}.
     * 
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder fisa() {
        return addDissemination(Disseminations.FISA);
    }

    /**
     * Indicates if the builder has the given {@link Disseminations} set.
     * 
     * @param dissemControl the {@link Disseminations} to test for. May not be null.
     * @return true if the builder currently contains the given
     *         {@link Disseminations}.
     */
    public boolean hasDissemination(Disseminations dissemControl) {
        Objects.requireNonNull(dissemControl);
        return this.disseminationMap.keySet().contains(dissemControl);
    }

    /**
     * Clears the builder of all {@link Disseminations}.
     * 
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder clear() {
        disseminationMap.clear();
        return parent;
    }

    /**
     * Used to determine if the {@link DisseminationsBuilder} is in a valid state,
     * and able to build a list of {@link DisseminationMarker}.
     * 
     * @return a list of String, which reports on invalid parts of the builder which
     *         would need to be fixed in order to perform a build. If there are no
     *         problems, and the builder is in a valid state, this list is empty.
     */
    public List<String> isValid() {
        List<String> report = new ArrayList<>();

        if (hasDissemination(Disseminations.RELEASE_TO)) {
            Set<String> countries = this.disseminationMap.get(Disseminations.RELEASE_TO);
            if (!countries.contains(Utils.USA)) {
                StringBuilder buf = new StringBuilder();
                buf.append("The country list for '").append(Disseminations.RELEASE_TO.toString())
                        .append("' Dissemination Control is required to include ").append(Utils.USA);
                report.add(buf.toString());
            }
            if (countries.size() < 2) {
                StringBuilder buf = new StringBuilder();
                buf.append("The country list for '").append(Disseminations.RELEASE_TO.toString())
                        .append("' Dissemination Control is required to have a minimum of 2 countries. Current count: ")
                        .append(countries.size());
                report.add(buf.toString());
            }
            if (hasDissemination(Disseminations.NOFORN)) {
                StringBuilder buf = new StringBuilder();
                buf.append("May not have '").append(Disseminations.RELEASE_TO.toString()).append("' and '")
                        .append(Disseminations.NOFORN.toString()).append("' Dissemination Controls at the same time.");
                report.add(buf.toString());
            }
        }

        if (hasDissemination(Disseminations.DISPLAY_ONLY)) {
            Set<String> countries = this.disseminationMap.get(Disseminations.DISPLAY_ONLY);
            if (countries.size() < 1) {
                StringBuilder buf = new StringBuilder();
                buf.append("The country list for '").append(Disseminations.DISPLAY_ONLY.toString())
                        .append("' Dissemination Control is required to have a minimum of 1 country. Current count: ")
                        .append(countries.size());
                report.add(buf.toString());
            }
            if (hasDissemination(Disseminations.RELIDO)) {
                StringBuilder buf = new StringBuilder();
                buf.append("May not have '").append(Disseminations.DISPLAY_ONLY.toString()).append("' and '")
                        .append(Disseminations.RELIDO.toString()).append("' Dissemination Controls at the same time.");
                report.add(buf.toString());
            }
            if (hasDissemination(Disseminations.NOFORN)) {
                StringBuilder buf = new StringBuilder();
                buf.append("May not have '").append(Disseminations.DISPLAY_ONLY.toString()).append("' and '")
                        .append(Disseminations.NOFORN.toString()).append("' Dissemination Controls at the same time.");
                report.add(buf.toString());
            }
        }

        if (hasDissemination(Disseminations.NOFORN) && hasDissemination(Disseminations.RELIDO)) {
            StringBuilder buf = new StringBuilder();
            buf.append("May not have '").append(Disseminations.NOFORN.toString()).append("' and '")
                    .append(Disseminations.RELIDO.toString()).append("' Dissemination Controls at the same time.");
            report.add(buf.toString());
        }

        return report;
    }

    /**
     * Builds a new list of {@link DisseminationMarker} based on the fields in the
     * builder.
     * 
     * @return a new list of {@link DisseminationMarker}.
     * @throws IllegalStateException if the builder is not in a valid state, then
     *                               this exception is thrown.
     */
    public List<DisseminationMarker> build() {

        List<String> report = isValid();
        if (!report.isEmpty()) {
            // Invalid state to build a DisseminationControlMarker.
            // Throwing an exception. Am not logging, as that should be handled in
            // ClassificationMarkerBuilder before this function is even called.
            throw new IllegalStateException("Invalid state. Cannot build instance of DisseminationControlMarker.");
        }

        List<DisseminationMarker> retList = new ArrayList<>();
        for (Disseminations dissemControl : this.disseminationMap.keySet()) {
            Set<String> countries = this.disseminationMap.get(dissemControl);
            retList.add(new DisseminationMarker(dissemControl, List.copyOf(countries)));
        }
        return retList;
    }

    @Override
    public int hashCode() {
        return Objects.hash(disseminationMap);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DisseminationsBuilder other = (DisseminationsBuilder) obj;
        return Objects.equals(disseminationMap, other.disseminationMap);
    }
}

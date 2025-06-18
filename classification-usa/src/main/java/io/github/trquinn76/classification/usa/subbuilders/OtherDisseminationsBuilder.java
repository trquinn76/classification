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
import io.github.trquinn76.classification.usa.model.OtherDisseminationMarker;
import io.github.trquinn76.classification.usa.model.OtherDisseminations;

/**
 * Builder for {@link OtherDisseminationMarker}'s.
 * 
 * This is a sub builder for {@link ClassificationMarkerBuilder}, and should not
 * be created outside the context of an instance of
 * {@link ClassificationMarkerBuilder}.
 */
public class OtherDisseminationsBuilder {

    private Map<OtherDisseminations, Set<String>> otherDisseminationControlsMap = new TreeMap<>();

    private ClassificationMarkerBuilder parent;

    /**
     * Constructs the builder.
     * 
     * @param parent the parent {@link ClassificationMarkerBuilder}.
     */
    public OtherDisseminationsBuilder(ClassificationMarkerBuilder parent) {
        this.parent = parent;
    }

    /**
     * Populates this builder with the values in the given list of
     * {@link OtherDisseminationMarker}.
     * 
     * @param disseminationMarks the list of {@link OtherDisseminationMarker} from
     *                           which to populate the builder.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder populate(List<OtherDisseminationMarker> otherDissemMarkers) {
        clear();
        for (OtherDisseminationMarker marker : otherDissemMarkers) {
            setDissemination(marker.type(),
                    marker.programNickNames().toArray(new String[marker.programNickNames().size()]));
        }
        return parent;
    }

    /**
     * Sets the {@link OtherDisseminations} to be added to the builder.
     * 
     * Any existing nickname list for the {@link OtherDisseminations} will be
     * replaced.
     * 
     * @param dissemControl the {@link OtherDisseminations} to set. May not be null.
     * @param nicknames     a list of nicknames for the {@link OtherDisseminations}.
     *                      May not be null. May be empty.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder setDissemination(OtherDisseminations dissemControl, String... nicknames) {
        Objects.requireNonNull(dissemControl);
        Objects.requireNonNull(nicknames);

        Set<String> nicks = new TreeSet<>();
        nicks.addAll(Arrays.asList(nicknames));
        this.otherDisseminationControlsMap.put(dissemControl, nicks);
        return parent;
    }

    /**
     * Get a copy of the current set of {@link OtherDisseminations} in the builder.
     * 
     * @return a copy of the current set of {@link OtherDisseminations} in the
     *         builder.
     */
    public Set<OtherDisseminations> getDisseminations() {
        return new TreeSet<>(this.otherDisseminationControlsMap.keySet());
    }

    /**
     * Removes the given {@link OtherDisseminations} from the builder.
     * 
     * @param dissemControl the {@link OtherDisseminations} to remove. May not be
     *                      null.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder removeDissemination(OtherDisseminations dissemControl) {
        this.otherDisseminationControlsMap.remove(dissemControl);
        return parent;
    }

    /**
     * Adds a nickname for the given {@link OtherDisseminations}.
     * 
     * @param dissemControl the {@link OtherDisseminations} to add the nickname for.
     *                      May not be null.
     * @param nickname      the nickname to add for the {@link OtherDisseminations}.
     *                      May not be null.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder addNickname(OtherDisseminations dissemControl, String nickname) {
        Objects.requireNonNull(dissemControl);
        Objects.requireNonNull(nickname);
        if (!hasOtherDissemination(dissemControl)) {
            this.otherDisseminationControlsMap.put(dissemControl, new TreeSet<>(Set.of(nickname)));
        } else {
            this.otherDisseminationControlsMap.get(dissemControl).add(nickname);
        }
        return parent;
    }

    /**
     * Gets a copy of the nicknames for the given {@link OtherDisseminations}.
     * 
     * @param dissemControl the {@link OtherDisseminations} for which to get the
     *                      nicknames.
     * @return a Set containing the nicknames for the given
     *         {@link OtherDisseminations}. May be empty.
     */
    public Set<String> getNicknames(OtherDisseminations dissemControl) {
        Set<String> retSet = new TreeSet<>();
        if (hasOtherDissemination(dissemControl)) {
            retSet.addAll(this.otherDisseminationControlsMap.get(dissemControl));
        }
        return retSet;
    }

    /**
     * Removes the given nickname from the given {@link OtherDisseminations}.
     * 
     * @param dissemControl the {@link OtherDisseminations} for which to remove the
     *                      nickname. May not be null.
     * @param nickname      the nickname to remove. May not be null.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder removeNickname(OtherDisseminations dissemControl, String nickname) {
        Objects.requireNonNull(dissemControl);
        Objects.requireNonNull(nickname);
        if (hasOtherDissemination(dissemControl)) {
            this.otherDisseminationControlsMap.get(dissemControl).remove(nickname);
        }
        return parent;
    }

    /**
     * Sets the ACCM {@link OtherDisseminations}.
     * 
     * Allows setting nicknames.
     * 
     * @param nicknames the array of nicknames to associated with ACCM. May not be
     *                  null. May be empty.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder alternativeCompensatoryControlMeasures(String... nicknames) {
        Objects.requireNonNull(nicknames);

        return setDissemination(OtherDisseminations.ACCM, nicknames);
    }

    /**
     * Sets the EXCLUSIVE_DISTRIBUTION {@link OtherDisseminations}.
     * 
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder exclusiveDistribution() {
        return setDissemination(OtherDisseminations.EXCLUSIVE_DISTRIBUTION);
    }

    /**
     * Sets the NO_DISTRIBUTION {@link OtherDisseminations}.
     * 
     * Allows setting distribution instructions, which are added to the Addition
     * Markings.
     * 
     * @param distributionInstructions an array of instructions which are added to
     *                                 the Additional Markings. May not be null. May
     *                                 be empty.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder noDistribution(String... distributionInstructions) {
        Objects.requireNonNull(distributionInstructions);
        parent.addAdditionalMarkings(Arrays.asList(distributionInstructions));
        return setDissemination(OtherDisseminations.NO_DISTRIBUTION);
    }

    /**
     * Sets the SENSITIVE_BUT_UNCLASSIFIED {@link OtherDisseminations}.
     * 
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder sensitiveButUnclassified() {
        return setDissemination(OtherDisseminations.SENSITIVE_BUT_UNCLASSIFIED);
    }

    /**
     * Sets the SENSITIVE_BUT_UNCLASSIFIED_NOFORN {@link OtherDisseminations}.
     * 
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder sensitiveButUnclassifiedNoforn() {
        return setDissemination(OtherDisseminations.SENSITIVE_BUT_UNCLASSIFIED_NOFORN);
    }

    /**
     * Clears all {@link OtherDisseminations} from the builder.
     * 
     * Does NOT remove/clear any distribution instructions added to Additional
     * Markings for NO_DISTRIBUTION.
     * 
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder clear() {
        this.otherDisseminationControlsMap.clear();
        return parent;
    }

    /**
     * Returns true if the given {@link OtherDisseminations} is set in the builder.
     * 
     * @param otherDissem the {@link OtherDisseminations} to test for. May not be
     *                    null.
     * @return true if the {@link OtherDisseminations} is in the builder.
     */
    public boolean hasOtherDissemination(OtherDisseminations otherDissem) {
        Objects.requireNonNull(otherDissem);
        return this.otherDisseminationControlsMap.keySet().contains(otherDissem);
    }

    /**
     * Used to determine if the {@link OtherDisseminationsBuilder} is in a valid
     * state, and able to build a list of {@link OtherDisseminationMarker}.
     * 
     * @return a list of String, which reports on invalid parts of the builder which
     *         would need to be fixed in order to perform a build. If there are no
     *         problems, and the builder is in a valid state, this list is empty.
     */
    public List<String> isValid() {
        List<String> report = new ArrayList<>();

        if (hasOtherDissemination(OtherDisseminations.EXCLUSIVE_DISTRIBUTION)
                && hasOtherDissemination(OtherDisseminations.NO_DISTRIBUTION)) {
            StringBuilder buf = new StringBuilder();
            buf.append("May not use Other Disseminations '")
                    .append(OtherDisseminations.EXCLUSIVE_DISTRIBUTION.toString()).append("' and '")
                    .append(OtherDisseminations.NO_DISTRIBUTION).append("' together.");
            report.add(buf.toString());
        }
        if (hasOtherDissemination(OtherDisseminations.SENSITIVE_BUT_UNCLASSIFIED)
                && hasOtherDissemination(OtherDisseminations.SENSITIVE_BUT_UNCLASSIFIED_NOFORN)) {
            StringBuilder buf = new StringBuilder();
            buf.append("May not use Other Disseminations '")
                    .append(OtherDisseminations.SENSITIVE_BUT_UNCLASSIFIED.toString()).append("' and '")
                    .append(OtherDisseminations.SENSITIVE_BUT_UNCLASSIFIED_NOFORN).append("' together.");
            report.add(buf.toString());
        }

        for (OtherDisseminations otherDissem : this.otherDisseminationControlsMap.keySet()) {
            switch (otherDissem) {
            case ACCM:
                if (this.otherDisseminationControlsMap.get(otherDissem).isEmpty()) {
                    report.add("Must have at least one Nick Name for Other Dissemination '"
                            + OtherDisseminations.ACCM.name() + "'.");
                }
                break;
            case EXCLUSIVE_DISTRIBUTION:
            case NO_DISTRIBUTION:
            case SENSITIVE_BUT_UNCLASSIFIED:
            case SENSITIVE_BUT_UNCLASSIFIED_NOFORN:
                if (!this.otherDisseminationControlsMap.get(otherDissem).isEmpty()) {
                    StringBuilder buf = new StringBuilder();
                    buf.append("May not have ").append(OtherDisseminations.ACCM.name())
                            .append(" nick names for Other Dissemination: '").append(otherDissem.toString())
                            .append("'.");
                    report.add(buf.toString());
                }
                break;
            }
        }

        return report;
    }

    /**
     * Builds a new list of {@link OtherDisseminationMarker} based on the fields in
     * the builder.
     * 
     * @return a new list of {@link OtherDisseminationMarker}.
     * @throws IllegalStateException if the builder is not in a valid state, then
     *                               this exception is thrown.
     */
    public List<OtherDisseminationMarker> build() {

        List<String> report = isValid();
        if (!report.isEmpty()) {
            // Invalid state to build a OtherDisseminationControlMarker.
            // Throwing an exception. Am not logging, as that should be handled in
            // ClassificationMarkerBuilder before this function is even called.
            throw new IllegalStateException("Invalid state. Cannot build instance of OtherDisseminationControlMarker.");
        }

        List<OtherDisseminationMarker> retList = new ArrayList<>();
        for (OtherDisseminations otherDissem : this.otherDisseminationControlsMap.keySet()) {
            retList.add(new OtherDisseminationMarker(otherDissem,
                    List.copyOf(this.otherDisseminationControlsMap.get(otherDissem))));
        }
        return retList;
    }

    @Override
    public int hashCode() {
        return Objects.hash(otherDisseminationControlsMap);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OtherDisseminationsBuilder other = (OtherDisseminationsBuilder) obj;
        return Objects.equals(otherDisseminationControlsMap, other.otherDisseminationControlsMap);
    }
}

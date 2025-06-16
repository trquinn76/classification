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

public class OtherDisseminationsBuilder {

    private Map<OtherDisseminations, Set<String>> otherDisseminationControlsMap = new TreeMap<>();

    private ClassificationMarkerBuilder parent;

    public OtherDisseminationsBuilder(ClassificationMarkerBuilder parent) {
        this.parent = parent;
    }

    public ClassificationMarkerBuilder populate(List<OtherDisseminationMarker> otherDissemMarkers) {
        clear();
        for (OtherDisseminationMarker marker : otherDissemMarkers) {
            setDissemination(marker.type(),
                    marker.programNickNames().toArray(new String[marker.programNickNames().size()]));
        }
        return parent;
    }

    public ClassificationMarkerBuilder setDissemination(OtherDisseminations dissemControl, String... nicknames) {
        Objects.requireNonNull(dissemControl);
        Objects.requireNonNull(nicknames);

        Set<String> nicks = new TreeSet<>();
        nicks.addAll(Arrays.asList(nicknames));
        this.otherDisseminationControlsMap.put(dissemControl, nicks);
        return parent;
    }
    
    public Set<OtherDisseminations> getDisseminations() {
        return new TreeSet<>(this.otherDisseminationControlsMap.keySet());
    }

    public ClassificationMarkerBuilder removeDissemination(OtherDisseminations dissemControl) {
        this.otherDisseminationControlsMap.remove(dissemControl);
        return parent;
    }
    
    public ClassificationMarkerBuilder addNickname(OtherDisseminations dissemControl, String nickname) {
        Objects.requireNonNull(dissemControl);
        Objects.requireNonNull(nickname);
        if (!hasOtherDissemination(dissemControl)) {
            this.otherDisseminationControlsMap.put(dissemControl, new TreeSet<>(Set.of(nickname)));
        }
        else {
            this.otherDisseminationControlsMap.get(dissemControl).add(nickname);
        }
        return parent;
    }
    
    public Set<String> getNicknames(OtherDisseminations dissemControl) {
        Set<String> retSet = new TreeSet<>();
        if (hasOtherDissemination(dissemControl)) {
            retSet.addAll(this.otherDisseminationControlsMap.get(dissemControl));
        }
        return retSet;
    }
    
    public ClassificationMarkerBuilder removeNickname(OtherDisseminations dissemControl, String nickname) {
        Objects.requireNonNull(dissemControl);
        Objects.requireNonNull(nickname);
        if (hasOtherDissemination(dissemControl)) {
            this.otherDisseminationControlsMap.get(dissemControl).remove(nickname);
        }
        return parent;
    }

    public ClassificationMarkerBuilder alternativeCompensatoryControlMeasures(String... nicknames) {
        Objects.requireNonNull(nicknames);

        return setDissemination(OtherDisseminations.ACCM, nicknames);
    }

    public ClassificationMarkerBuilder exclusiveDistribution() {
        return setDissemination(OtherDisseminations.EXCLUSIVE_DISTRIBUTION);
    }

    public ClassificationMarkerBuilder noDistribution(String... distributionInstructions) {
        Objects.requireNonNull(distributionInstructions);
        parent.addAdditionalMarkings(Arrays.asList(distributionInstructions));
        return setDissemination(OtherDisseminations.NO_DISTRIBUTION);
    }

    public ClassificationMarkerBuilder sensitiveButUnclassified() {
        return setDissemination(OtherDisseminations.SENSITIVE_BUT_UNCLASSIFIED);
    }

    public ClassificationMarkerBuilder sensitiveButUnclassifiedNoforn() {
        return setDissemination(OtherDisseminations.SENSITIVE_BUT_UNCLASSIFIED_NOFORN);
    }

    public ClassificationMarkerBuilder clear() {
        this.otherDisseminationControlsMap.clear();
        return parent;
    }

    public boolean hasOtherDissemination(OtherDisseminations otherDissem) {
        Objects.requireNonNull(otherDissem);
        return this.otherDisseminationControlsMap.keySet().contains(otherDissem);
    }

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

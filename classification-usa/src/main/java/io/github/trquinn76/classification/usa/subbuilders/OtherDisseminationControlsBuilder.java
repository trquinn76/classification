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
import io.github.trquinn76.classification.usa.model.OtherDisseminationControlMarker;
import io.github.trquinn76.classification.usa.model.OtherDisseminationControls;

public class OtherDisseminationControlsBuilder {

    private Map<OtherDisseminationControls, Set<String>> otherDisseminationControlsMap = new TreeMap<>();

    private ClassificationMarkerBuilder parent;

    public OtherDisseminationControlsBuilder(ClassificationMarkerBuilder parent) {
        this.parent = parent;
    }

    public ClassificationMarkerBuilder populate(List<OtherDisseminationControlMarker> otherDissemMarkers) {
        clear();
        for (OtherDisseminationControlMarker marker : otherDissemMarkers) {
            setDissemination(marker.type(),
                    marker.programNickNames().toArray(new String[marker.programNickNames().size()]));
        }
        return parent;
    }

    public ClassificationMarkerBuilder setDissemination(OtherDisseminationControls dissemControl, String... nicknames) {
        Objects.requireNonNull(dissemControl);
        Objects.requireNonNull(nicknames);

        Set<String> nicks = new TreeSet<>();
        nicks.addAll(Arrays.asList(nicknames));
        this.otherDisseminationControlsMap.put(dissemControl, nicks);
        return parent;
    }
    
    public Set<OtherDisseminationControls> getDisseminations() {
        return new TreeSet<>(this.otherDisseminationControlsMap.keySet());
    }

    public ClassificationMarkerBuilder removeDissemination(OtherDisseminationControls dissemControl) {
        this.otherDisseminationControlsMap.remove(dissemControl);
        return parent;
    }
    
    public ClassificationMarkerBuilder addNickname(OtherDisseminationControls dissemControl, String nickname) {
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
    
    public Set<String> getNicknames(OtherDisseminationControls dissemControl) {
        Set<String> retSet = new TreeSet<>();
        if (hasOtherDissemination(dissemControl)) {
            retSet.addAll(this.otherDisseminationControlsMap.get(dissemControl));
        }
        return retSet;
    }
    
    public ClassificationMarkerBuilder removeNickname(OtherDisseminationControls dissemControl, String nickname) {
        Objects.requireNonNull(dissemControl);
        Objects.requireNonNull(nickname);
        if (hasOtherDissemination(dissemControl)) {
            this.otherDisseminationControlsMap.get(dissemControl).remove(nickname);
        }
        return parent;
    }

    public ClassificationMarkerBuilder alternativeCompensatoryControlMeasures(String... nicknames) {
        Objects.requireNonNull(nicknames);

        return setDissemination(OtherDisseminationControls.ACCM, nicknames);
    }

    public ClassificationMarkerBuilder exclusiveDistribution() {
        return setDissemination(OtherDisseminationControls.EXCLUSIVE_DISTRIBUTION);
    }

    public ClassificationMarkerBuilder noDistribution(String... distributionInstructions) {
        Objects.requireNonNull(distributionInstructions);
        parent.addAdditionalMarkings(Arrays.asList(distributionInstructions));
        return setDissemination(OtherDisseminationControls.NO_DISTRIBUTION);
    }

    public ClassificationMarkerBuilder sensitiveButUnclassified() {
        return setDissemination(OtherDisseminationControls.SENSITIVE_BUT_UNCLASSIFIED);
    }

    public ClassificationMarkerBuilder sensitiveButUnclassifiedNoforn() {
        return setDissemination(OtherDisseminationControls.SENSITIVE_BUT_UNCLASSIFIED_NOFORN);
    }

    public ClassificationMarkerBuilder clear() {
        this.otherDisseminationControlsMap.clear();
        return parent;
    }

    public boolean hasOtherDissemination(OtherDisseminationControls otherDissem) {
        Objects.requireNonNull(otherDissem);
        return this.otherDisseminationControlsMap.keySet().contains(otherDissem);
    }

    public List<String> isValid() {
        List<String> report = new ArrayList<>();

        if (hasOtherDissemination(OtherDisseminationControls.EXCLUSIVE_DISTRIBUTION)
                && hasOtherDissemination(OtherDisseminationControls.NO_DISTRIBUTION)) {
            StringBuilder buf = new StringBuilder();
            buf.append("May not use Other Disseminations '")
                    .append(OtherDisseminationControls.EXCLUSIVE_DISTRIBUTION.toString()).append("' and '")
                    .append(OtherDisseminationControls.NO_DISTRIBUTION).append("' together.");
            report.add(buf.toString());
        }
        if (hasOtherDissemination(OtherDisseminationControls.SENSITIVE_BUT_UNCLASSIFIED)
                && hasOtherDissemination(OtherDisseminationControls.SENSITIVE_BUT_UNCLASSIFIED_NOFORN)) {
            StringBuilder buf = new StringBuilder();
            buf.append("May not use Other Disseminations '")
                    .append(OtherDisseminationControls.SENSITIVE_BUT_UNCLASSIFIED.toString()).append("' and '")
                    .append(OtherDisseminationControls.SENSITIVE_BUT_UNCLASSIFIED_NOFORN).append("' together.");
            report.add(buf.toString());
        }

        for (OtherDisseminationControls otherDissem : this.otherDisseminationControlsMap.keySet()) {
            switch (otherDissem) {
            case ACCM:
                if (this.otherDisseminationControlsMap.get(otherDissem).isEmpty()) {
                    report.add("Must have at least one Nick Name for Other Dissemination '"
                            + OtherDisseminationControls.ACCM.name() + "'.");
                }
                break;
            case EXCLUSIVE_DISTRIBUTION:
            case NO_DISTRIBUTION:
            case SENSITIVE_BUT_UNCLASSIFIED:
            case SENSITIVE_BUT_UNCLASSIFIED_NOFORN:
                if (!this.otherDisseminationControlsMap.get(otherDissem).isEmpty()) {
                    StringBuilder buf = new StringBuilder();
                    buf.append("May not have ").append(OtherDisseminationControls.ACCM.name())
                            .append(" nick names for Other Dissemination: '").append(otherDissem.toString())
                            .append("'.");
                    report.add(buf.toString());
                }
                break;
            }
        }

        return report;
    }

    public List<OtherDisseminationControlMarker> build() {

        List<String> report = isValid();
        if (!report.isEmpty()) {
            // Invalid state to build a OtherDisseminationControlMarker.
            // Throwing an exception. Am not logging, as that should be handled in
            // ClassificationMarkerBuilder before this function is even called.
            throw new IllegalStateException("Invalid state. Cannot build instance of OtherDisseminationControlMarker.");
        }

        List<OtherDisseminationControlMarker> retList = new ArrayList<>();
        for (OtherDisseminationControls otherDissem : this.otherDisseminationControlsMap.keySet()) {
            retList.add(new OtherDisseminationControlMarker(otherDissem,
                    List.copyOf(this.otherDisseminationControlsMap.get(otherDissem))));
        }
        return retList;
    }
}

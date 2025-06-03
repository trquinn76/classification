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
import io.github.trquinn76.classification.usa.model.DisseminationControlMarker;
import io.github.trquinn76.classification.usa.model.DisseminationControls;

public class DisseminationControlsBuilder {
    
    private Map<DisseminationControls, Set<String>> disseminationMap = new TreeMap<>();
    
    private ClassificationMarkerBuilder parent;
    
    public DisseminationControlsBuilder(ClassificationMarkerBuilder parent) {
        this.parent = parent;
    }
    
    public ClassificationMarkerBuilder populate(List<DisseminationControlMarker> disseminationMarks) {
        for (DisseminationControlMarker marker : disseminationMarks) {
            setDissemination(marker.type(), marker.countries().toArray(new String[marker.countries().size()]));
        }
        return parent;
    }
    
    public ClassificationMarkerBuilder setDissemination(DisseminationControls dissemControl, String... countryList) {
        Objects.requireNonNull(dissemControl);
        Objects.requireNonNull(countryList);
        
        Set<String> countries = new TreeSet<>(Utils.USA_FIRST);
        countries.addAll(Arrays.asList(countryList));
        this.disseminationMap.put(dissemControl, countries);
        
        return parent;
    }
    
    public ClassificationMarkerBuilder addDissemination(DisseminationControls dissemControl, String... countryList) {
        Objects.requireNonNull(dissemControl);
        Objects.requireNonNull(countryList);
        
        if (this.disseminationMap.keySet().contains(dissemControl)) {
            this.disseminationMap.get(dissemControl).addAll(Arrays.asList(countryList));
        }
        else {
            Set<String> countries = new TreeSet<>(Utils.USA_FIRST);
            countries.addAll(Arrays.asList(countryList));
            this.disseminationMap.put(dissemControl, countries);
        }
        return parent;
    }
    
    public Set<DisseminationControls> getDisseminations() {
        return new TreeSet<>(this.disseminationMap.keySet());
    }
    
    public ClassificationMarkerBuilder removeDissemination(DisseminationControls dissemControl) {
        this.disseminationMap.remove(dissemControl);
        return parent;
    }
    
    public ClassificationMarkerBuilder addCountry(DisseminationControls dissemControl, String country) {
        Objects.requireNonNull(dissemControl);
        Objects.requireNonNull(country);
        
        return addDissemination(dissemControl, country);
    }
    
    public Set<String> getCountries(DisseminationControls dissemControl) {
        Set<String> retSet = new TreeSet<>(Utils.USA_FIRST);
        if (hasDissemination(dissemControl)) {
            retSet.addAll(this.disseminationMap.get(dissemControl));
        }
        return retSet;
    }
    
    public ClassificationMarkerBuilder removeCountry(DisseminationControls dissemControl, String country) {
        Objects.requireNonNull(dissemControl);
        if (this.disseminationMap.keySet().contains(dissemControl)) {
            this.disseminationMap.get(dissemControl).remove(country);
        }
        return parent;
    }
    
    public ClassificationMarkerBuilder waived() {
        return addDissemination(DisseminationControls.WAIVED);
    }
    
    public ClassificationMarkerBuilder fouo() {
        return addDissemination(DisseminationControls.FOUO);
    }
    
    public ClassificationMarkerBuilder countroledUnclassifiedInformation() {
        return addDissemination(DisseminationControls.CONTROLLED_UNCLASSIFIED_INFORMATION);
    }
    
    public ClassificationMarkerBuilder orcon() {
        return addDissemination(DisseminationControls.ORIGINATOR_CONTROLLED);
    }
    
    public ClassificationMarkerBuilder releaseTo(String... countries) {
        Objects.requireNonNull(countries);
        return addDissemination(DisseminationControls.RELEASE_TO, countries);
    }
    
    public ClassificationMarkerBuilder displayOnly(String... countries) {
        Objects.requireNonNull(countries);
        return addDissemination(DisseminationControls.DISPLAY_ONLY, countries);
    }
    
    public ClassificationMarkerBuilder controledImagery() {
        return addDissemination(DisseminationControls.CONTROLLED_IMAGERY);
    }
    
    public ClassificationMarkerBuilder noforn() {
        return addDissemination(DisseminationControls.NOFORN);
    }
    
    public ClassificationMarkerBuilder propin() {
        return addDissemination(DisseminationControls.PROPRIETARY_INFORMATION);
    }
    
    public ClassificationMarkerBuilder relido() {
        return addDissemination(DisseminationControls.RELIDO);
    }
    
    public ClassificationMarkerBuilder fisa() {
        return addDissemination(DisseminationControls.FISA);
    }
    
    public boolean hasDissemination(DisseminationControls dissemControl) {
        return this.disseminationMap.keySet().contains(dissemControl);
    }
    
    public ClassificationMarkerBuilder clear() {
        disseminationMap.clear();
        return parent;
    }
    
    public List<String> isValid() {
        List<String> report = new ArrayList<>();
        
        if (hasDissemination(DisseminationControls.RELEASE_TO)) {
            Set<String> countries = this.disseminationMap.get(DisseminationControls.RELEASE_TO);
            if (!countries.contains(Utils.USA)) {
                StringBuilder buf = new StringBuilder();
                buf.append("The country list for '").append(DisseminationControls.RELEASE_TO.toString())
                        .append("' Dissemination Control is required to include ").append(Utils.USA);
                report.add(buf.toString());
            }
            if (countries.size() < 2) {
                StringBuilder buf = new StringBuilder();
                buf.append("The country list for '").append(DisseminationControls.RELEASE_TO.toString())
                        .append("' Dissemination Control is required to have a minimum of 2 countries. Current count: ")
                        .append(countries.size());
                report.add(buf.toString());
            }
            if (hasDissemination(DisseminationControls.NOFORN)) {
                StringBuilder buf = new StringBuilder();
                buf.append("May not have '").append(DisseminationControls.RELEASE_TO.toString()).append("' and '")
                        .append(DisseminationControls.NOFORN.toString())
                        .append("' Dissemination Controls at the same time.");
                report.add(buf.toString());
            }
        }
        
        if (hasDissemination(DisseminationControls.DISPLAY_ONLY)) {
            Set<String> countries = this.disseminationMap.get(DisseminationControls.DISPLAY_ONLY);
            if (countries.size() < 2) {
                StringBuilder buf = new StringBuilder();
                buf.append("The country list for '").append(DisseminationControls.DISPLAY_ONLY.toString())
                        .append("' Dissemination Control is required to have a minimum of 1 country. Current count: ")
                        .append(countries.size());
                report.add(buf.toString());
            }
            if (hasDissemination(DisseminationControls.RELIDO)) {
                StringBuilder buf = new StringBuilder();
                buf.append("May not have '").append(DisseminationControls.DISPLAY_ONLY.toString()).append("' and '")
                        .append(DisseminationControls.RELIDO.toString())
                        .append("' Dissemination Controls at the same time.");
                report.add(buf.toString());
            }
            if (hasDissemination(DisseminationControls.NOFORN)) {
                StringBuilder buf = new StringBuilder();
                buf.append("May not have '").append(DisseminationControls.DISPLAY_ONLY.toString()).append("' and '")
                        .append(DisseminationControls.NOFORN.toString())
                        .append("' Dissemination Controls at the same time.");
                report.add(buf.toString());
            }
        }
        
        if (hasDissemination(DisseminationControls.NOFORN) && hasDissemination(DisseminationControls.RELIDO)) {
            StringBuilder buf = new StringBuilder();
            buf.append("May not have '").append(DisseminationControls.NOFORN.toString()).append("' and '")
                    .append(DisseminationControls.RELIDO.toString())
                    .append("' Dissemination Controls at the same time.");
            report.add(buf.toString());
        }
        
        return report;
    }

    public List<DisseminationControlMarker> build() {
        
        List<String> report = isValid();
        if (!report.isEmpty()) {
            // Invalid state to build a DisseminationControlMarker.
            // Throwing an exception. Am not logging, as that should be handled in
            // ClassificationMarkerBuilder before this function is even called.
            throw new IllegalStateException(
                    "Invalid state. Cannot build instance of DisseminationControlMarker.");
        }
        
        List<DisseminationControlMarker> retList = new ArrayList<>();
        for (DisseminationControls dissemControl : this.disseminationMap.keySet()) {
            Set<String> countries = this.disseminationMap.get(dissemControl);            retList.add(new DisseminationControlMarker(dissemControl, List.copyOf(countries)));
        }
        return retList;
    }
}

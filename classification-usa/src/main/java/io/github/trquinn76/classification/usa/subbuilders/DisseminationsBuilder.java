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

public class DisseminationsBuilder {
    
    private Map<Disseminations, Set<String>> disseminationMap = new TreeMap<>();
    
    private ClassificationMarkerBuilder parent;
    
    public DisseminationsBuilder(ClassificationMarkerBuilder parent) {
        this.parent = parent;
    }
    
    public ClassificationMarkerBuilder populate(List<DisseminationMarker> disseminationMarks) {
        for (DisseminationMarker marker : disseminationMarks) {
            setDissemination(marker.type(), marker.countries().toArray(new String[marker.countries().size()]));
        }
        return parent;
    }
    
    public ClassificationMarkerBuilder setDissemination(Disseminations dissemControl, String... countryList) {
        Objects.requireNonNull(dissemControl);
        Objects.requireNonNull(countryList);
        
        Set<String> countries = new TreeSet<>(Utils.USA_FIRST);
        countries.addAll(Arrays.asList(countryList));
        this.disseminationMap.put(dissemControl, countries);
        
        return parent;
    }
    
    public ClassificationMarkerBuilder addDissemination(Disseminations dissemControl, String... countryList) {
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
    
    public Set<Disseminations> getDisseminations() {
        return new TreeSet<>(this.disseminationMap.keySet());
    }
    
    public ClassificationMarkerBuilder removeDissemination(Disseminations dissemControl) {
        this.disseminationMap.remove(dissemControl);
        return parent;
    }
    
    public ClassificationMarkerBuilder addCountry(Disseminations dissemControl, String country) {
        Objects.requireNonNull(dissemControl);
        Objects.requireNonNull(country);
        
        return addDissemination(dissemControl, country);
    }
    
    public Set<String> getCountries(Disseminations dissemControl) {
        Set<String> retSet = new TreeSet<>(Utils.USA_FIRST);
        if (hasDissemination(dissemControl)) {
            retSet.addAll(this.disseminationMap.get(dissemControl));
        }
        return retSet;
    }
    
    public ClassificationMarkerBuilder removeCountry(Disseminations dissemControl, String country) {
        Objects.requireNonNull(dissemControl);
        if (this.disseminationMap.keySet().contains(dissemControl)) {
            this.disseminationMap.get(dissemControl).remove(country);
        }
        return parent;
    }
    
    public ClassificationMarkerBuilder waived() {
        return addDissemination(Disseminations.WAIVED);
    }
    
    public ClassificationMarkerBuilder fouo() {
        return addDissemination(Disseminations.FOUO);
    }
    
    public ClassificationMarkerBuilder controledUnclassifiedInformation() {
        return addDissemination(Disseminations.CONTROLLED_UNCLASSIFIED_INFORMATION);
    }
    
    public ClassificationMarkerBuilder orcon() {
        return addDissemination(Disseminations.ORIGINATOR_CONTROLLED);
    }
    
    public ClassificationMarkerBuilder releaseTo(String... countries) {
        Objects.requireNonNull(countries);
        return addDissemination(Disseminations.RELEASE_TO, countries);
    }
    
    public ClassificationMarkerBuilder displayOnly(String... countries) {
        Objects.requireNonNull(countries);
        return addDissemination(Disseminations.DISPLAY_ONLY, countries);
    }
    
    public ClassificationMarkerBuilder controledImagery() {
        return addDissemination(Disseminations.CONTROLLED_IMAGERY);
    }
    
    public ClassificationMarkerBuilder noforn() {
        return addDissemination(Disseminations.NOFORN);
    }
    
    public ClassificationMarkerBuilder propin() {
        return addDissemination(Disseminations.PROPRIETARY_INFORMATION);
    }
    
    public ClassificationMarkerBuilder relido() {
        return addDissemination(Disseminations.RELIDO);
    }
    
    public ClassificationMarkerBuilder fisa() {
        return addDissemination(Disseminations.FISA);
    }
    
    public boolean hasDissemination(Disseminations dissemControl) {
        return this.disseminationMap.keySet().contains(dissemControl);
    }
    
    public ClassificationMarkerBuilder clear() {
        disseminationMap.clear();
        return parent;
    }
    
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
                        .append(Disseminations.NOFORN.toString())
                        .append("' Dissemination Controls at the same time.");
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
                        .append(Disseminations.RELIDO.toString())
                        .append("' Dissemination Controls at the same time.");
                report.add(buf.toString());
            }
            if (hasDissemination(Disseminations.NOFORN)) {
                StringBuilder buf = new StringBuilder();
                buf.append("May not have '").append(Disseminations.DISPLAY_ONLY.toString()).append("' and '")
                        .append(Disseminations.NOFORN.toString())
                        .append("' Dissemination Controls at the same time.");
                report.add(buf.toString());
            }
        }
        
        if (hasDissemination(Disseminations.NOFORN) && hasDissemination(Disseminations.RELIDO)) {
            StringBuilder buf = new StringBuilder();
            buf.append("May not have '").append(Disseminations.NOFORN.toString()).append("' and '")
                    .append(Disseminations.RELIDO.toString())
                    .append("' Dissemination Controls at the same time.");
            report.add(buf.toString());
        }
        
        return report;
    }

    public List<DisseminationMarker> build() {
        
        List<String> report = isValid();
        if (!report.isEmpty()) {
            // Invalid state to build a DisseminationControlMarker.
            // Throwing an exception. Am not logging, as that should be handled in
            // ClassificationMarkerBuilder before this function is even called.
            throw new IllegalStateException(
                    "Invalid state. Cannot build instance of DisseminationControlMarker.");
        }
        
        List<DisseminationMarker> retList = new ArrayList<>();
        for (Disseminations dissemControl : this.disseminationMap.keySet()) {
            Set<String> countries = this.disseminationMap.get(dissemControl);            retList.add(new DisseminationMarker(dissemControl, List.copyOf(countries)));
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

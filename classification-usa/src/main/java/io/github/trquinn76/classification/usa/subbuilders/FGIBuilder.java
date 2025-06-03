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

public class FGIBuilder {

    private boolean concealed = false;
    private Set<String> countries = new TreeSet<>(Utils.ALPHABETIC);

    private ClassificationMarkerBuilder parent;

    public FGIBuilder(ClassificationMarkerBuilder parent) {
        this.parent = parent;
    }

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

    public ClassificationMarkerBuilder concealed() {
        clear();
        this.concealed = true;
        return parent;
    }

    public ClassificationMarkerBuilder setConcealed(boolean concealed) {
        this.concealed = concealed;
        return parent;
    }
    
    public boolean isConcealed() {
        return this.concealed;
    }

    public ClassificationMarkerBuilder setCountries(Collection<String> countries) {
        Objects.requireNonNull(countries);
        clear();
        this.countries.addAll(countries);
        return parent;
    }

    public ClassificationMarkerBuilder addCountry(String country) {
        Objects.requireNonNull(country);
        this.countries.add(country);
        return parent;
    }
    
    public Set<String> getCountries() {
        Set<String> retSet = new TreeSet<>(Utils.ALPHABETIC);
        retSet.addAll(countries);
        return retSet;
    }

    public ClassificationMarkerBuilder removeCountry(String country) {
        this.countries.remove(country);
        return parent;
    }

    public ClassificationMarkerBuilder clear() {
        this.concealed = false;
        this.countries.clear();
        return parent;
    }

    public boolean isPopulated() {
        return concealed || !countries.isEmpty();
    }

    public List<String> isValid() {
        List<String> report = new ArrayList<>();

        if (concealed && !this.countries.isEmpty()) {
            report.add(
                    "Cannot have a list of Foreign Government Information countries, while also Concealing Foreign Countries.");
        }

        return report;
    }

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
}

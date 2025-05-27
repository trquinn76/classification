package io.github.trquinn76.classification.usa.subbuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import io.github.trquinn76.classification.usa.ClassificationMarkerBuilder;
import io.github.trquinn76.classification.usa.model.AtomicEnergyActInformationMarker;
import io.github.trquinn76.classification.usa.model.AtomicEnergyActMarkings;

public class AtomicEnergyActInformationBuilder {

    private AtomicEnergyActMarkings aeaMark = null;
    private Set<Integer> sigmaNumbers = new TreeSet<>();
    
    private ClassificationMarkerBuilder parent;
    
    public AtomicEnergyActInformationBuilder(ClassificationMarkerBuilder parent) {
        this.parent = parent;
    }
    
    public ClassificationMarkerBuilder populate(AtomicEnergyActInformationMarker marker) {
        clear();
        if (marker != null) {
            this.aeaMark = marker.aeaMark();
            this.sigmaNumbers.addAll(marker.sigmaNumbers());
        }
        return parent;
    }
    
    public ClassificationMarkerBuilder setMark(AtomicEnergyActMarkings mark) {
        this.aeaMark = mark;
        return parent;
    }
    
    public AtomicEnergyActMarkings getMark() {
        return this.aeaMark;
    }
    
    public ClassificationMarkerBuilder setSigmaNumbers(Collection<Integer> sigmaNumbers) {
        this.sigmaNumbers.clear();
        this.sigmaNumbers.addAll(sigmaNumbers);
        return parent;
    }
    
    public Set<Integer> getSigmaNumbers() {
        Set<Integer> retSet = new TreeSet<>();
        retSet.addAll(this.sigmaNumbers);
        return retSet;
    }
    
    public ClassificationMarkerBuilder clearSigmaNumbers() {
        this.sigmaNumbers.clear();
        return parent;
    }
    
    public ClassificationMarkerBuilder clear() {
        setMark(null);
        return clearSigmaNumbers();
    }
    
    public ClassificationMarkerBuilder restrictedData() {
        return setMark(AtomicEnergyActMarkings.RESTRICTED_DATA);
    }
    
    public ClassificationMarkerBuilder formallyRestrictedData() {
        return setMark(AtomicEnergyActMarkings.FORMALLY_RESTRICTED_DATA);
    }
    
    public ClassificationMarkerBuilder restrictedDataSigma(Integer... sigmaNumbers) {
        Objects.requireNonNull(sigmaNumbers);
        setMark(AtomicEnergyActMarkings.RESTRICTED_DATA_SIGMA);
        return setSigmaNumbers(Arrays.asList(sigmaNumbers));
    }
    
    public ClassificationMarkerBuilder restrictedDataCnwdi() {
        return setMark(AtomicEnergyActMarkings.RESTRICTED_DATA_CNWDI);
    }
    
    public ClassificationMarkerBuilder formallyRestrictedDataSigma(Integer... sigmaNumbers) {
        Objects.requireNonNull(sigmaNumbers);
        setMark(AtomicEnergyActMarkings.FORMALLY_RESTRICTED_DATA_SIGMA);
        return setSigmaNumbers(Arrays.asList(sigmaNumbers));
    }
    
    public ClassificationMarkerBuilder sigma(Integer... sigmaNumbers) {
        if (this.aeaMark == AtomicEnergyActMarkings.RESTRICTED_DATA) {
            return restrictedDataSigma(sigmaNumbers);
        }
        else if (this.aeaMark == AtomicEnergyActMarkings.RESTRICTED_DATA_SIGMA) {
            return setSigmaNumbers(Arrays.asList(sigmaNumbers));
        }
        else if (this.aeaMark == AtomicEnergyActMarkings.FORMALLY_RESTRICTED_DATA) {
            return formallyRestrictedDataSigma(sigmaNumbers);
        }
        else if (this.aeaMark == AtomicEnergyActMarkings.FORMALLY_RESTRICTED_DATA_SIGMA) {
            return setSigmaNumbers(Arrays.asList(sigmaNumbers));
        }
        else {
            throw new IllegalStateException("Cannot assign SIGMA to AEA Mark: '" + aeaMark.toString() + "'.");
        }
    }
    
    public boolean isPopulated() {
        return this.aeaMark != null || !this.sigmaNumbers.isEmpty();
    }
    
    public List<String> isValid() {
        List<String> report = new ArrayList<>();
        
        if (isPopulated()) {
            if (this.aeaMark == null) {
                report.add("Must have an Atomic Energy Act Information Marking. Or clear Sigma Numbers.");
            }
            else {
                switch (this.aeaMark) {
                case RESTRICTED_DATA_SIGMA:
                case FORMALLY_RESTRICTED_DATA_SIGMA:
                    if (this.sigmaNumbers.isEmpty()) {
                        StringBuilder buf = new StringBuilder();
                        buf.append("The AEA Mark '").append(this.aeaMark.toString())
                                .append("' is required to at least one SIGMA number.");
                        report.add(buf.toString());
                    }
                    break;
                case RESTRICTED_DATA:
                case RESTRICTED_DATA_CNWDI:
                case FORMALLY_RESTRICTED_DATA:
                case TRANSCLASSIFIED_FOREIGN_NUCLEAR_INFORMATION:
                case DOD_UNCLASSIFIED_CONTROLLED_NUCLEAR_INFORMATION:
                case DOE_UNCLASSIFIED_CONTROLLED_NUCLEAR_INFORMATION:
                    if (!this.sigmaNumbers.isEmpty()) {
                        StringBuilder buf = new StringBuilder();
                        buf.append("The AEA Mark '").append(this.aeaMark.toString())
                                .append("' may not have any SIGMA numbers. SIGMA Number count: ")
                                .append(this.sigmaNumbers.size());
                        report.add(buf.toString());
                    }
                    break;
                }
            }
        }
        
        return report;
    }
    
    public AtomicEnergyActInformationMarker build() {
        
        if (!isValid().isEmpty()) {
            // invalid state to build an AtomicEnergyActInformationMarker.
            // throwing an exception. Am not logging, as that should be handled in
            // ClassificationMarkerBuilder before this function is even called.
            throw new IllegalStateException(
                    "Invalid state. Cannot build instance of AtomicEnergyActInformationMarker.");
        }
        
        if (isPopulated()) {
            return new AtomicEnergyActInformationMarker(this.aeaMark, List.copyOf(this.sigmaNumbers));
        }
        else {
            return null;
        }
    }
}

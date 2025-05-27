package io.github.trquinn76.classification.usa.subbuilders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import io.github.trquinn76.classification.usa.ClassificationConfig;
import io.github.trquinn76.classification.usa.ClassificationMarkerBuilder;
import io.github.trquinn76.classification.usa.Utils;
import io.github.trquinn76.classification.usa.model.Compartment;
import io.github.trquinn76.classification.usa.model.SCIControlSystem;

public class SCIBuilder {
    
    private Map<String, Map<String, Set<String>>> sciControlSystems = new TreeMap<>(Utils.ALPHABETIC);
    
    private ClassificationMarkerBuilder parent;
    
    public SCIBuilder(ClassificationMarkerBuilder parent) {
        this.parent = parent;
    }
    
    public ClassificationMarkerBuilder populate(Collection<SCIControlSystem> sciControlSystems) {
        clear();
        for (SCIControlSystem controlSystem : sciControlSystems) {
            addControlSystem(controlSystem.name());
            for (Compartment compartment : controlSystem.compartments()) {
                addCompartment(controlSystem.name(), compartment.name());
                for (String subCompartment : compartment.subCompartments()) {
                    addSubCompartment(controlSystem.name(), compartment.name(), subCompartment);
                }
            }
        }
        return parent;
    }
    
    public ClassificationMarkerBuilder addControlSystem(String controlSystem) {
        Objects.requireNonNull(controlSystem);
        if (!this.sciControlSystems.containsKey(controlSystem)) {
            // Compartments are sorted AlphaNumerically.
            this.sciControlSystems.put(controlSystem, new TreeMap<>(Utils.ALPHANUMERIC));
        }
        return parent;
    }
    
    public ClassificationMarkerBuilder removeControlSystem(String controlSystem) {
        this.sciControlSystems.remove(controlSystem);
        return parent;
    }
    
    public ClassificationMarkerBuilder addCompartment(String controlSystem, String compartment) {
        if (!this.sciControlSystems.containsKey(controlSystem)) {
            addControlSystem(controlSystem);
        }
        Map<String, Set<String>> compartments = this.sciControlSystems.get(controlSystem);
        if (!compartments.containsKey(compartment)) {
            // Sub Compartments are sorted AlphaNumerically.
            compartments.put(compartment, new TreeSet<>(Utils.ALPHANUMERIC));
        }
        return parent;
    }
    
    public ClassificationMarkerBuilder removeCompartment(String controlSystem, String compartment) {
        Map<String, Set<String>> compartments = this.sciControlSystems.get(controlSystem);
        if (compartments != null) {
            compartments.remove(compartment);
        }
        return parent;
    }
    
    public ClassificationMarkerBuilder addSubCompartment(String controlSystem, String compartment, String subCompartment) {
        if (!this.sciControlSystems.containsKey(controlSystem)) {
            addControlSystem(controlSystem);
        }
        Map<String, Set<String>> compartments = this.sciControlSystems.get(controlSystem);
        if (!compartments.containsKey(compartment)) {
            addCompartment(controlSystem, compartment);
        }
        compartments.get(compartment).add(subCompartment);
        return parent;
    }
    
    public ClassificationMarkerBuilder removeSubCompartment(String controlSystem, String compartment, String subCompartment) {
        Map<String, Set<String>> compartments = this.sciControlSystems.get(controlSystem);
        if (compartments != null) {
            Set<String> subCompartments = compartments.get(compartment);
            if (subCompartments != null) {
                subCompartments.remove(subCompartment);
            }
        }
        return parent;
    }
    
    public ClassificationMarkerBuilder humintCS(String... compartments) {
        Objects.requireNonNull(compartments);
        addControlSystem(Utils.HCS);
        for (String compartment : compartments) {
            addCompartment(Utils.HCS, compartment);
        }
        return parent;
    }
    
    public ClassificationMarkerBuilder specialIntelligence(String... compartments) {
        Objects.requireNonNull(compartments);
        addControlSystem(Utils.SPECIAL_INTELLIGENCE);
        for (String compartment : compartments) {
            addCompartment(Utils.SPECIAL_INTELLIGENCE, compartment);
        }
        return parent;
    }
    
    public ClassificationMarkerBuilder talentKeyhole(String... compartments) {
        Objects.requireNonNull(compartments);
        addControlSystem(Utils.TALENT_KEYHOLE);
        for (String compartment : compartments) {
            addCompartment(Utils.TALENT_KEYHOLE, compartment);
        }
        return parent;
    }
    
    public ClassificationMarkerBuilder clear() {
        this.sciControlSystems.clear();
        return parent;
    }
    
    public List<String> isValid() {
        return Collections.emptyList();
    }

    public List<SCIControlSystem> build() {
        List<SCIControlSystem> controlSystems = new ArrayList<>();
        for (Entry<String, Map<String, Set<String>>> csEntry : this.sciControlSystems.entrySet()) {
            String sciCiName = prefixDevelStrIfNeeded(csEntry.getKey());
            List<Compartment> compartments = new ArrayList<>();
            for (Entry<String, Set<String>> compartmentEntry : csEntry.getValue().entrySet()) {
                Compartment compartment = new Compartment(compartmentEntry.getKey(), List.copyOf(compartmentEntry.getValue()));
                compartments.add(compartment);
            }
            controlSystems.add(new SCIControlSystem(sciCiName, compartments));
        }
        return controlSystems;
    }
    
    /**
     * On page 74, paragraph 6g, of "DoD Information Security Program: Marking of Information", it is stated that:
     * 
     * <p>"SCI, regardless of classification level, must be processed only on an information system accredited for SCI
     * processing"
     * 
     * <p>In order to reassure users and developers that 'SCI's in a development environment do not represent a spill,
     * SCI Control System names in a development environment are given a configurable prefix value, which will clearly
     * indicate that the SCI Control System, and associated Compartments are not Real.
     * 
     * <p>This will NOT magically protect from using unpublished SCI Control Systems and Compartments in an unsuitable
     * lower classification development environment. That will still represent a Spill/Breach. Don't do that. 
     * 
     * @param sciCiName
     * @return
     */
    private String prefixDevelStrIfNeeded(String sciCiName) {
        if (!ClassificationConfig.productionMode()) {
            if (!sciCiName.startsWith(ClassificationConfig.developmentSciPrefix())) {
                return ClassificationConfig.developmentSciPrefix() + sciCiName;
            }
        }
        return sciCiName;
    }
}

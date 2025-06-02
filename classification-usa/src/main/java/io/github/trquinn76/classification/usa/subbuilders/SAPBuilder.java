package io.github.trquinn76.classification.usa.subbuilders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

import io.github.trquinn76.classification.usa.ClassificationConfig;
import io.github.trquinn76.classification.usa.ClassificationMarkerBuilder;
import io.github.trquinn76.classification.usa.Utils;
import io.github.trquinn76.classification.usa.model.Compartment;
import io.github.trquinn76.classification.usa.model.SARProgram;

public class SAPBuilder {
    
    private Map<String, Map<String, Set<String>>> sapControlSystems = new TreeMap<>(Utils.ALPHABETIC);
    
    private ClassificationMarkerBuilder parent;
    
    public SAPBuilder(ClassificationMarkerBuilder parent) {
        this.parent = parent;
    }
    
    public ClassificationMarkerBuilder populate(List<SARProgram> sapPrograms) {
        clear();
        for (SARProgram controlSystem : sapPrograms) {
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
        if (!this.sapControlSystems.containsKey(controlSystem)) {
            // Compartments are sorted AlphaNumerically.
            this.sapControlSystems.put(controlSystem, new TreeMap<>(Utils.ALPHANUMERIC));
        }
        return parent;
    }
    
    public Map<String, Map<String, Set<String>>> getControlSystems() {
        Map<String, Map<String, Set<String>>> retMap = new TreeMap<>(Utils.ALPHABETIC);
        for (String controlSystem : this.sapControlSystems.keySet()) {
            retMap.put(controlSystem, getCompartments(controlSystem));
        }
        return retMap;
    }
    
    public ClassificationMarkerBuilder removeControlSystem(String controlSystem) {
        this.sapControlSystems.remove(controlSystem);
        return parent;
    }
    
    public ClassificationMarkerBuilder addCompartment(String controlSystem, String compartment) {
        if (!this.sapControlSystems.containsKey(controlSystem)) {
            addControlSystem(controlSystem);
        }
        Map<String, Set<String>> compartments = this.sapControlSystems.get(controlSystem);
        if (!compartments.containsKey(compartment)) {
            // Sub Compartments are sorted AlphaNumerically.
            compartments.put(compartment, new TreeSet<>(Utils.ALPHANUMERIC));
        }
        return parent;
    }
    
    public Map<String, Set<String>> getCompartments(String controlSystem) {
        Map<String, Set<String>> retMap = new TreeMap<>(Utils.ALPHANUMERIC);
        if (this.sapControlSystems.containsKey(controlSystem)) {
            Map<String, Set<String>> compartments = this.sapControlSystems.get(controlSystem);
            for (String compartment : compartments.keySet()) {
                retMap.put(compartment, getSubCompartments(controlSystem, compartment));
            }
        }
        return retMap;
    }
    
    public ClassificationMarkerBuilder removeCompartment(String controlSystem, String compartment) {
        Map<String, Set<String>> compartments = this.sapControlSystems.get(controlSystem);
        if (compartments != null) {
            compartments.remove(compartment);
        }
        return parent;
    }
    
    public ClassificationMarkerBuilder addSubCompartment(String controlSystem, String compartment, String subCompartment) {
        if (!this.sapControlSystems.containsKey(controlSystem)) {
            addControlSystem(controlSystem);
        }
        Map<String, Set<String>> compartments = this.sapControlSystems.get(controlSystem);
        if (!compartments.containsKey(compartment)) {
            addCompartment(controlSystem, compartment);
        }
        compartments.get(compartment).add(subCompartment);
        return parent;
    }
    
    public Set<String> getSubCompartments(String controlSystem, String compartment) {
        Set<String> retSet = new TreeSet<>(Utils.ALPHANUMERIC);
        if (this.sapControlSystems.containsKey(controlSystem)) {
            Map<String, Set<String>> compartments = this.sapControlSystems.get(controlSystem);
            if (compartments.containsKey(compartment)) {
                retSet.addAll(compartments.get(compartment));
            }
        }
        return retSet;
    }
    
    public ClassificationMarkerBuilder removeSubCompartment(String controlSystem, String compartment, String subCompartment) {
        Map<String, Set<String>> compartments = this.sapControlSystems.get(controlSystem);
        if (compartments != null) {
            Set<String> subCompartments = compartments.get(compartment);
            if (subCompartments != null) {
                subCompartments.remove(subCompartment);
            }
        }
        return parent;
    }
    
    public boolean isPopulated() {
        return !this.sapControlSystems.isEmpty();
    }
    
    public ClassificationMarkerBuilder waived() {
        parent.disseminations.waived();
        return parent;
    }
    
    public ClassificationMarkerBuilder hvsaco() {
        parent.addAdditionMarking(Utils.HVSACO);
        return parent;
    }
    
    public ClassificationMarkerBuilder clear() {
        this.sapControlSystems.clear();
        return parent;
    }
    
    public List<String> isValid() {
        return Collections.emptyList();
    }
    
    public List<SARProgram> build() {
        List<SARProgram> controlSystems = new ArrayList<>();
        for (Entry<String, Map<String, Set<String>>> programEntry : this.sapControlSystems.entrySet()) {
            String sapName = prefixDevelStrIfNeeded(programEntry.getKey());
            List<Compartment> compartments = new ArrayList<>();
            for (Entry<String, Set<String>> compartmentEntry : programEntry.getValue().entrySet()) {
                Compartment compartment = new Compartment(compartmentEntry.getKey(), List.copyOf(compartmentEntry.getValue()));
                compartments.add(compartment);
            }
            controlSystems.add(new SARProgram(sapName, compartments));
        }
        return controlSystems;
    }
    
    private String prefixDevelStrIfNeeded(String sapName) {
        if (!ClassificationConfig.productionMode()) {
            if (!sapName.startsWith(ClassificationConfig.developmentSapPrefix())) {
                return ClassificationConfig.developmentSapPrefix() + sapName;
            }
        }
        return sapName;
    }
}

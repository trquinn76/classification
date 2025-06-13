package io.github.trquinn76.classification.usa.subbuilders;

import java.util.ArrayList;
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
        controlSystem = removeDevelStrPrefixIfNeeded(controlSystem);
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
        if (controlSystem != null) {
            controlSystem = removeDevelStrPrefixIfNeeded(controlSystem);
            this.sapControlSystems.remove(controlSystem);
        }
        return parent;
    }
    
    public ClassificationMarkerBuilder addCompartment(String controlSystem, String compartment) {
        Objects.requireNonNull(controlSystem);
        Objects.requireNonNull(compartment);
        controlSystem = removeDevelStrPrefixIfNeeded(controlSystem);
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
        Objects.requireNonNull(controlSystem);
        controlSystem = removeDevelStrPrefixIfNeeded(controlSystem);
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
        if (controlSystem != null) {
            controlSystem = removeDevelStrPrefixIfNeeded(controlSystem);
            Map<String, Set<String>> compartments = this.sapControlSystems.get(controlSystem);
            if (compartments != null) {
                compartments.remove(compartment);
            }
        }
        return parent;
    }
    
    public ClassificationMarkerBuilder addSubCompartment(String controlSystem, String compartment, String subCompartment) {
        Objects.requireNonNull(controlSystem);
        Objects.requireNonNull(compartment);
        Objects.requireNonNull(subCompartment);
        controlSystem = removeDevelStrPrefixIfNeeded(controlSystem);
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
        Objects.requireNonNull(controlSystem);
        Objects.requireNonNull(compartment);
        controlSystem = removeDevelStrPrefixIfNeeded(controlSystem);
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
        if (controlSystem != null) {
            controlSystem = removeDevelStrPrefixIfNeeded(controlSystem);
            Map<String, Set<String>> compartments = this.sapControlSystems.get(controlSystem);
            if (compartments != null) {
                Set<String> subCompartments = compartments.get(compartment);
                if (subCompartments != null) {
                    subCompartments.remove(subCompartment);
                }
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
        clear();
        parent.addAdditionMarking(Utils.HVSACO);
        return parent;
    }
    
    public ClassificationMarkerBuilder clear() {
        this.sapControlSystems.clear();
        parent.removeAdditionalMarking(Utils.HVSACO);
        return parent;
    }
    
    public List<String> isValid() {
        List<String> report = new ArrayList<>();
        if (!this.sapControlSystems.isEmpty() && parent.getAdditionalMarkings().contains(Utils.HVSACO)) {
            StringBuilder buf = new StringBuilder();
            buf.append("The '").append(Utils.HVSACO).append("' mark is added to classified or unclassified material ")
                    .append("which exists in a SAP environment, or which needs to be handled in a SAP ")
                    .append("environment. As such the '").append(Utils.HVSACO)
                    .append("' mark may not be used with SAP markings.");
            report.add(buf.toString());
        }
        return report;
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
    
    private String removeDevelStrPrefixIfNeeded(String sciCiName) {
        if (!ClassificationConfig.productionMode()) {
            if (sciCiName.startsWith(ClassificationConfig.developmentSapPrefix())) {
                return sciCiName.replaceFirst(ClassificationConfig.developmentSapPrefix(), "");
            }
        }
        return sciCiName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sapControlSystems);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SAPBuilder other = (SAPBuilder) obj;
        return Objects.equals(sapControlSystems, other.sapControlSystems);
    }
}

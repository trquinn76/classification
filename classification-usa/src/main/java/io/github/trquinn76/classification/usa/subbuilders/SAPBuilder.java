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
import io.github.trquinn76.classification.usa.model.Disseminations;
import io.github.trquinn76.classification.usa.model.SARProgram;

/**
 * Builder for a list of {@link SARProgram}'s.
 * 
 * This is a sub builder for {@link ClassificationMarkerBuilder}, and should not
 * be created outside the context of an instance of
 * {@link ClassificationMarkerBuilder}.
 */
public class SAPBuilder {

    private Map<String, Map<String, Set<String>>> sarPrograms = new TreeMap<>(Utils.ALPHABETIC);

    private ClassificationMarkerBuilder parent;

    /**
     * Constructs the builder.
     * 
     * @param parent the parent {@link ClassificationMarkerBuilder}.
     */
    public SAPBuilder(ClassificationMarkerBuilder parent) {
        this.parent = parent;
    }

    /**
     * Populates this builder with the values in the given list of
     * {@link SARProgram}.
     * 
     * @param sarPrograms the list of {@link SARProgram} from which to populate the
     *                    builder.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder populate(List<SARProgram> sarPrograms) {
        clear();
        for (SARProgram controlSystem : sarPrograms) {
            addSARProgram(controlSystem.name());
            for (Compartment compartment : controlSystem.compartments()) {
                addCompartment(controlSystem.name(), compartment.name());
                for (String subCompartment : compartment.subCompartments()) {
                    addSubCompartment(controlSystem.name(), compartment.name(), subCompartment);
                }
            }
        }
        return parent;
    }

    /**
     * Adds a SAR Program to the list of Programs.
     * 
     * @param sarProgram the SAR Program to add. May not be null.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder addSARProgram(String sarProgram) {
        Objects.requireNonNull(sarProgram);
        sarProgram = removeDevelStrPrefixIfNeeded(sarProgram);
        if (!this.sarPrograms.containsKey(sarProgram)) {
            // Compartments are sorted AlphaNumerically.
            this.sarPrograms.put(sarProgram, new TreeMap<>(Utils.ALPHANUMERIC));
        }
        return parent;
    }

    /**
     * Gets a copy of the current SAR Programs, including Compartments and Sub
     * Compartments.
     * 
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public Map<String, Map<String, Set<String>>> getSARPrograms() {
        Map<String, Map<String, Set<String>>> retMap = new TreeMap<>(Utils.ALPHABETIC);
        for (String controlSystem : this.sarPrograms.keySet()) {
            retMap.put(controlSystem, getCompartments(controlSystem));
        }
        return retMap;
    }

    /**
     * Removes the given SAR Program.
     * 
     * @param sarProgram the SAR Program to remove. May be null.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder removeSARProgram(String sarProgram) {
        if (sarProgram != null) {
            sarProgram = removeDevelStrPrefixIfNeeded(sarProgram);
            this.sarPrograms.remove(sarProgram);
        }
        return parent;
    }

    /**
     * Add the given Compartment to the given SAR Program.
     * 
     * If the SAR Program does not currently exist, it will also be added to the
     * builder.
     * 
     * @param sarProgram  the SAR Program to add the Compartment to. May not be
     *                    null.
     * @param compartment the Compartment to add to the SAR Program. May not be
     *                    null.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder addCompartment(String sarProgram, String compartment) {
        Objects.requireNonNull(sarProgram);
        Objects.requireNonNull(compartment);
        sarProgram = removeDevelStrPrefixIfNeeded(sarProgram);
        if (!this.sarPrograms.containsKey(sarProgram)) {
            addSARProgram(sarProgram);
        }
        Map<String, Set<String>> compartments = this.sarPrograms.get(sarProgram);
        if (!compartments.containsKey(compartment)) {
            // Sub Compartments are sorted AlphaNumerically.
            compartments.put(compartment, new TreeSet<>(Utils.ALPHANUMERIC));
        }
        return parent;
    }

    /**
     * Gets a copy of the Compartment to Sub Compartment Map for the given SAR
     * Program.
     * 
     * @param sarProgram the SAR Program to get the Compartments and Sub
     *                   Compartments for. May not be null.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public Map<String, Set<String>> getCompartments(String sarProgram) {
        Objects.requireNonNull(sarProgram);
        sarProgram = removeDevelStrPrefixIfNeeded(sarProgram);
        Map<String, Set<String>> retMap = new TreeMap<>(Utils.ALPHANUMERIC);
        if (this.sarPrograms.containsKey(sarProgram)) {
            Map<String, Set<String>> compartments = this.sarPrograms.get(sarProgram);
            for (String compartment : compartments.keySet()) {
                retMap.put(compartment, getSubCompartments(sarProgram, compartment));
            }
        }
        return retMap;
    }

    /**
     * Removes the given Compartment from the given SAR Program.
     * 
     * If the Compartment or SAR Program does not exist, no changes will be made.
     * 
     * @param sarProgram  the SAR Program to remove the Compartment from. May be
     *                    null.
     * @param compartment the Compartment to remove. May be null.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder removeCompartment(String sarProgram, String compartment) {
        if (sarProgram != null) {
            sarProgram = removeDevelStrPrefixIfNeeded(sarProgram);
            Map<String, Set<String>> compartments = this.sarPrograms.get(sarProgram);
            if (compartments != null) {
                compartments.remove(compartment);
            }
        }
        return parent;
    }

    /**
     * Adds the given Sub Compartment to the given SAR Program and Compartment.
     * 
     * @param sarProgram     the SAR Program to add the Sub Compartment to. May not
     *                       be null.
     * @param compartment    the Compartment to add the Sub Compartment to. May not
     *                       be null.
     * @param subCompartment the Sub Compartment to add. May not be null.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder addSubCompartment(String sarProgram, String compartment, String subCompartment) {
        Objects.requireNonNull(sarProgram);
        Objects.requireNonNull(compartment);
        Objects.requireNonNull(subCompartment);
        sarProgram = removeDevelStrPrefixIfNeeded(sarProgram);
        if (!this.sarPrograms.containsKey(sarProgram)) {
            addSARProgram(sarProgram);
        }
        Map<String, Set<String>> compartments = this.sarPrograms.get(sarProgram);
        if (!compartments.containsKey(compartment)) {
            addCompartment(sarProgram, compartment);
        }
        compartments.get(compartment).add(subCompartment);
        return parent;
    }

    /**
     * Gets the list of Sub Compartments for the given SAR Program and Compartment.
     * 
     * @param sarProgram  the SAR Program to get the Sub Compartments for. May not
     *                    be null.
     * @param compartment the Compartment to get the Sub Compartments for. May not
     *                    be null.
     * @return a copy of the Sub Compartments for the given SAR Program and
     *         Compartment. May be empty if there are no values.
     */
    public Set<String> getSubCompartments(String sarProgram, String compartment) {
        Objects.requireNonNull(sarProgram);
        Objects.requireNonNull(compartment);
        sarProgram = removeDevelStrPrefixIfNeeded(sarProgram);
        Set<String> retSet = new TreeSet<>(Utils.ALPHANUMERIC);
        if (this.sarPrograms.containsKey(sarProgram)) {
            Map<String, Set<String>> compartments = this.sarPrograms.get(sarProgram);
            if (compartments.containsKey(compartment)) {
                retSet.addAll(compartments.get(compartment));
            }
        }
        return retSet;
    }

    /**
     * Removes the given Sub Compartment from the given SAR Program and Compartment.
     * 
     * @param sarProgram     the SAR Program to remove the Sub Compartment from. May
     *                       be null.
     * @param compartment    the Compartment to remove the Sub Compartment from. May
     *                       be null.
     * @param subCompartment the Sub Compartment to remove. May not be null.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder removeSubCompartment(String sarProgram, String compartment,
            String subCompartment) {
        if (sarProgram != null) {
            sarProgram = removeDevelStrPrefixIfNeeded(sarProgram);
            Map<String, Set<String>> compartments = this.sarPrograms.get(sarProgram);
            if (compartments != null) {
                Set<String> subCompartments = compartments.get(compartment);
                if (subCompartments != null) {
                    subCompartments.remove(subCompartment);
                }
            }
        }
        return parent;
    }

    /**
     * Indicate if there are any SAR Programs in this builder.
     * 
     * @return true if there are any SAR Programs in this builder.
     */
    public boolean isPopulated() {
        return !this.sarPrograms.isEmpty();
    }

    /**
     * Used to indicate that there are WAIVED SAR Programs. WAIVED is stored as a
     * Dissemination.
     * 
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder waived() {
        parent.disseminations.waived();
        return parent;
    }

    /**
     * Sets the HVSACO mark in Additional Markings.
     * 
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder hvsaco() {
        clear();
        parent.addAdditionMarking(Utils.HVSACO);
        return parent;
    }

    /**
     * Clears the builder.
     * 
     * Also removes a WAIVED Dissemination, and any HVSACO mark from Additional
     * Markings.
     * 
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder clear() {
        this.sarPrograms.clear();
        parent.disseminations.removeDissemination(Disseminations.WAIVED);
        parent.removeAdditionalMarking(Utils.HVSACO);
        return parent;
    }

    /**
     * Used to determine if the {@link SAPBuilder} is in a valid state, and able to
     * build a list of {@link SARProgram}.
     * 
     * @return a list of String, which reports on invalid parts of the builder which
     *         would need to be fixed in order to perform a build. If there are no
     *         problems, and the builder is in a valid state, this list is empty.
     */
    public List<String> isValid() {
        List<String> report = new ArrayList<>();
        if (!this.sarPrograms.isEmpty() && parent.getAdditionalMarkings().contains(Utils.HVSACO)) {
            StringBuilder buf = new StringBuilder();
            buf.append("The '").append(Utils.HVSACO).append("' mark is added to classified or unclassified material ")
                    .append("which exists in a SAP environment, or which needs to be handled in a SAP ")
                    .append("environment. As such the '").append(Utils.HVSACO)
                    .append("' mark may not be used with SAP markings.");
            report.add(buf.toString());
        }
        return report;
    }

    /**
     * Builds a new list of {@link SARProgram} based on the fields in the builder.
     * 
     * @return a new list of {@link SARProgram}.
     * @throws IllegalStateException if the builder is not in a valid state, then
     *                               this exception is thrown.
     */
    public List<SARProgram> build() {
        List<SARProgram> controlSystems = new ArrayList<>();
        for (Entry<String, Map<String, Set<String>>> programEntry : this.sarPrograms.entrySet()) {
            String sapName = prefixDevelStrIfNeeded(programEntry.getKey());
            List<Compartment> compartments = new ArrayList<>();
            for (Entry<String, Set<String>> compartmentEntry : programEntry.getValue().entrySet()) {
                Compartment compartment = new Compartment(compartmentEntry.getKey(),
                        List.copyOf(compartmentEntry.getValue()));
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
        return Objects.hash(sarPrograms);
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
        return Objects.equals(sarPrograms, other.sarPrograms);
    }
}

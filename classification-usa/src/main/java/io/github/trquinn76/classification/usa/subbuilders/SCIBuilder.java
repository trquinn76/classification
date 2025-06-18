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

/**
 * Builder for a list of {@link SCIControlSystem}'s.
 * 
 * This is a sub builder for {@link ClassificationMarkerBuilder}, and should not
 * be created outside the context of an instance of
 * {@link ClassificationMarkerBuilder}.
 */
public class SCIBuilder {

    private Map<String, Map<String, Set<String>>> sciControlSystems = new TreeMap<>();

    private ClassificationMarkerBuilder parent;

    /**
     * Constructs the builder.
     * 
     * @param parent the parent {@link ClassificationMarkerBuilder}.
     */
    public SCIBuilder(ClassificationMarkerBuilder parent) {
        this.parent = parent;
    }

    /**
     * Populates this builder with the values in the given list of
     * {@link SCIControlSystem}.
     * 
     * @param sciControlSystems the list of {@link SCIControlSystem} from which to
     *                          populate the builder.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
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

    /**
     * Adds a SCI Control System to the list of Programs.
     * 
     * @param controlSystem the SCI Control System to add. May not be null.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder addControlSystem(String controlSystem) {
        Objects.requireNonNull(controlSystem);
        controlSystem = prefixDevelStrIfNeeded(controlSystem);
        if (!this.sciControlSystems.containsKey(controlSystem)) {
            // Compartments are sorted AlphaNumerically.
            this.sciControlSystems.put(controlSystem, new TreeMap<>(Utils.ALPHANUMERIC));
        }
        return parent;
    }

    /**
     * Gets a copy of the current SCI Control Systems, including Compartments and
     * Sub Compartments.
     * 
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public Set<String> getControlSystems() {
        return new TreeSet<>(this.sciControlSystems.keySet());
    }

    /**
     * Indicates if the builder contains the given SCI Control System
     * 
     * @param controlSystem the SCI Control System to test for. May be null.
     * @return true if the builder has the given SCI Control System.
     */
    public boolean hasControlSystem(String controlSystem) {
        if (controlSystem == null)
            return false;
        controlSystem = prefixDevelStrIfNeeded(controlSystem);
        return this.sciControlSystems.keySet().contains(controlSystem);
    }

    /**
     * Removes the given SCI Control System.
     * 
     * @param controlSystem the SCI Control System to remove. May be null.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder removeControlSystem(String controlSystem) {
        if (controlSystem != null) {
            controlSystem = prefixDevelStrIfNeeded(controlSystem);
            this.sciControlSystems.remove(controlSystem);
        }
        return parent;
    }

    /**
     * Adds a Compartment to the given SCI Control System.
     * 
     * If the given SCI Control System does not exist, it will be created.
     * 
     * @param controlSystem the SCI Control System to add the Compartment to. May
     *                      not be null.
     * @param compartment   the Conpartment to add.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder addCompartment(String controlSystem, String compartment) {
        Objects.requireNonNull(controlSystem);
        Objects.requireNonNull(compartment);
        controlSystem = prefixDevelStrIfNeeded(controlSystem);
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

    /**
     * Gets a copy of the Compartments for the given SCI Control System.
     * 
     * @param controlSystem the SCI Control System to get the Compartments for. May
     *                      not be null.
     * @return the Set of Compartments in the given SCI Control System. May be
     *         empty.
     */
    public Set<String> getCompartments(String controlSystem) {
        Objects.requireNonNull(controlSystem);
        controlSystem = prefixDevelStrIfNeeded(controlSystem);
        Map<String, Set<String>> compartments = this.sciControlSystems.get(controlSystem);
        if (compartments != null) {
            return Collections.unmodifiableSet(compartments.keySet());
        }
        return Collections.emptySet();
    }

    /**
     * Removes the given Compartment from the given SCI Control System.
     * 
     * @param controlSystem The SCI Control System from which to remove the
     *                      Compartment. May be null.
     * @param compartment   the Compartment to remove. May be null.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder removeCompartment(String controlSystem, String compartment) {
        if (controlSystem != null) {
            controlSystem = prefixDevelStrIfNeeded(controlSystem);
            Map<String, Set<String>> compartments = this.sciControlSystems.get(controlSystem);
            if (compartments != null) {
                compartments.remove(compartment);
            }
        }
        return parent;
    }

    /**
     * Adds a Sub Compartment to the given SCI Control System and Compartment.
     * 
     * Both the SCI Control System, and the Compartment, will be created if they do
     * not exist.
     * 
     * @param controlSystem  the SCI Control System to add the Sub Compartment to.
     *                       May not be null.
     * @param compartment    the Compartment to add the Sub Compartment to. May not
     *                       be null.
     * @param subCompartment the Sub Compartment to add. May not be null.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder addSubCompartment(String controlSystem, String compartment,
            String subCompartment) {
        Objects.requireNonNull(controlSystem);
        Objects.requireNonNull(compartment);
        Objects.requireNonNull(subCompartment);
        controlSystem = prefixDevelStrIfNeeded(controlSystem);
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

    /**
     * Gets a copy of the Sub Compartments for the given SCI Control Program and
     * Compartment.
     * 
     * @param controlSystem the SCI Control Program to get the Sub Compartments for.
     *                      May not be null.
     * @param compartment   the Compartment to get the Sub Compartments for. May not
     *                      be null.
     * @return a copy of the Sub Compartments in the given SCI Control Program and
     *         Compartment. May be empty.
     */
    public Set<String> getSubCompartments(String controlSystem, String compartment) {
        Objects.requireNonNull(controlSystem);
        Objects.requireNonNull(compartment);
        controlSystem = prefixDevelStrIfNeeded(controlSystem);
        Map<String, Set<String>> compartments = this.sciControlSystems.get(controlSystem);
        if (compartments != null) {
            Set<String> subCompartments = compartments.get(compartment);
            if (subCompartments != null) {
                return Collections.unmodifiableSet(subCompartments);
            }
        }
        return Collections.emptySet();
    }

    /**
     * Remove the given Sub Compartment from the given SCI Control System and
     * Compartment.
     * 
     * @param controlSystem  the SCI Control System to remove the Sub Compartment
     *                       from. May be null.
     * @param compartment    the Compartment to remove the Sub Compartment from. May
     *                       be null.
     * @param subCompartment the Sub Compartment to remove. May be null.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder removeSubCompartment(String controlSystem, String compartment,
            String subCompartment) {
        if (controlSystem != null) {
            controlSystem = prefixDevelStrIfNeeded(controlSystem);
            Map<String, Set<String>> compartments = this.sciControlSystems.get(controlSystem);
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
     * Adds the Published SCI Control System, HCS.
     * 
     * Also allows adding Compartments for the SCI Control System. Does not allow
     * populating Sub Compartments.
     * 
     * @param compartments an array of Compartments. May not be null. May be empty.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder humintCS(String... compartments) {
        Objects.requireNonNull(compartments);
        addControlSystem(Utils.HCS);
        for (String compartment : compartments) {
            addCompartment(Utils.HCS, compartment);
        }
        return parent;
    }

    /**
     * Adds the Published SCI Control System, SI.
     * 
     * Also allows adding Compartments for the SCI Control Program. Does not allow
     * populating Sub Compartments.
     * 
     * @param compartments an array of Compartments. May not be null. May be empty.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder specialIntelligence(String... compartments) {
        Objects.requireNonNull(compartments);
        addControlSystem(Utils.SPECIAL_INTELLIGENCE);
        for (String compartment : compartments) {
            addCompartment(Utils.SPECIAL_INTELLIGENCE, compartment);
        }
        return parent;
    }

    /**
     * Adds the Published SCI Control System, TK.
     * 
     * Also allows added Compartments for the SCI Control Program. Does not allow
     * populating Sub Compartments.
     * 
     * @param compartments an array of Compartments. May not be null. May be empty.
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder talentKeyhole(String... compartments) {
        Objects.requireNonNull(compartments);
        addControlSystem(Utils.TALENT_KEYHOLE);
        for (String compartment : compartments) {
            addCompartment(Utils.TALENT_KEYHOLE, compartment);
        }
        return parent;
    }

    /**
     * Clears the SCI Control Systems from the builder, including Compartments and
     * Sub Compartments.
     * 
     * @return parent {@link ClassificationMarkerBuilder} for function chaining.
     */
    public ClassificationMarkerBuilder clear() {
        this.sciControlSystems.clear();
        return parent;
    }

    /**
     * Used to determine if the {@link SCIBuilder} is in a valid state, and able to
     * build a list of {@link SCIControlSystem}.
     * 
     * @return a list of String, which reports on invalid parts of the builder which
     *         would need to be fixed in order to perform a build. If there are no
     *         problems, and the builder is in a valid state, this list is empty.
     */
    public List<String> isValid() {
        return Collections.emptyList();
    }

    /**
     * Builds a new list of {@link SCIControlSystem} based on the fields in the
     * builder.
     * 
     * @return a new list of {@link SCIControlSystem}.
     * @throws IllegalStateException if the builder is not in a valid state, then
     *                               this exception is thrown.
     */
    public List<SCIControlSystem> build() {
        List<SCIControlSystem> controlSystems = new ArrayList<>();
        for (Entry<String, Map<String, Set<String>>> csEntry : this.sciControlSystems.entrySet()) {
            String sciCiName = csEntry.getKey();
            List<Compartment> compartments = new ArrayList<>();
            for (Entry<String, Set<String>> compartmentEntry : csEntry.getValue().entrySet()) {
                Compartment compartment = new Compartment(compartmentEntry.getKey(),
                        List.copyOf(compartmentEntry.getValue()));
                compartments.add(compartment);
            }
            controlSystems.add(new SCIControlSystem(sciCiName, compartments));
        }
        return controlSystems;
    }

    /**
     * On page 74, paragraph 6g, of "DoD Information Security Program: Marking of
     * Information", it is stated that:
     * 
     * <p>
     * "SCI, regardless of classification level, must be processed only on an
     * information system accredited for SCI processing"
     * 
     * <p>
     * In order to reassure users and developers that 'SCI's in a development
     * environment do not represent a spill, SCI Control System names in a
     * development environment are given a configurable prefix value, which will
     * clearly indicate that the SCI Control System, and associated Compartments are
     * not Real.
     * 
     * <p>
     * This will NOT magically protect from using unpublished SCI Control Systems
     * and Compartments in an unsuitable lower classification development
     * environment. That will still represent a Spill/Breach. Don't do that.
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

    @Override
    public int hashCode() {
        return Objects.hash(sciControlSystems);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SCIBuilder other = (SCIBuilder) obj;
        return Objects.equals(sciControlSystems, other.sciControlSystems);
    }
}

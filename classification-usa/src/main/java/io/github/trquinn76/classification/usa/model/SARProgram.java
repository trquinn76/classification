package io.github.trquinn76.classification.usa.model;

import java.util.List;
import java.util.Objects;

/**
 * Defines a SAR Program, which {@link ClassificationMarker}'s may have 0 to
 * many of.
 * 
 * @param name         the name of the SAP. May not be null.
 * @param compartments {@link Compartment}'s in the SAR Program. May not be
 *                     null. May be empty.
 */
public record SARProgram(String name, List<Compartment> compartments) {

    /**
     * Constructor. Defensively copies lists to ensure record immutability.
     * 
     * @param name         the name of the SAP. May not be null.
     * @param compartments {@link Compartment}'s in the SAR Program. May not be
     *                     null. May be empty.
     */
    public SARProgram {
        Objects.requireNonNull(name);
        Objects.requireNonNull(compartments);
        if (name.isBlank()) {
            throw new IllegalArgumentException("A SAR Program name is required.");
        }
        compartments = List.copyOf(compartments);
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(name());
        for (Compartment compartment : compartments()) {
            buf.append("-").append(compartment.toString());
        }
        return buf.toString();
    }
}

package io.github.trquinn76.classification.usa.model;

import java.util.List;
import java.util.Objects;

/**
 * Represents a Compartment in either a SCI or SAP structure.
 * 
 * @param name            the name of the Compartment. May not be null.
 * @param subCompartments the names of any sub compartments. May not be null.
 *                        May be empty.
 */
public record Compartment(String name, List<String> subCompartments) {

    /**
     * Constructor. Defensively copies Lists, to ensure immutability.
     * 
     * @param name            the name of the Compartment. May not be null.
     * @param subCompartments the names of any sub compartments. May not be null.
     *                        May be empty.
     */
    public Compartment {
        Objects.requireNonNull(name);
        Objects.requireNonNull(subCompartments);
        if (name.isBlank()) {
            throw new IllegalArgumentException("A Compartment name is required.");
        }
        subCompartments = List.copyOf(subCompartments);
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(name());
        for (String subCompartment : subCompartments()) {
            buf.append(" ").append(subCompartment);
        }
        return buf.toString();
    }
}

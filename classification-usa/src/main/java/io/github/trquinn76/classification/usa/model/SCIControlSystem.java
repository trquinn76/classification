package io.github.trquinn76.classification.usa.model;

import java.util.List;
import java.util.Objects;

/**
 * Defines a Sensitive Compartmented Information Control System. A
 * {@link ClassificationMarker} may have 0 to many of these.
 * 
 * @param name         the name of the SCI Control System. May not be null. May
 *                     not be Blank.
 * @param compartments the {@link Compartment}'s of the SCI Control System. May
 *                     not be null. May be empty.
 */
public record SCIControlSystem(String name, List<Compartment> compartments) {

    /**
     * Constructor. Defensively copies lists to ensure record immutability.
     * 
     * @param name         the name of the SCI Control System. May not be null. May
     *                     not be Blank.
     * @param compartments the {@link Compartment}'s of the SCI Control System. May
     *                     not be null. May be empty.
     */
    public SCIControlSystem {
        Objects.requireNonNull(name);
        Objects.requireNonNull(compartments);
        if (name.isBlank()) {
            throw new IllegalArgumentException("A SCI Control System name is required.");
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

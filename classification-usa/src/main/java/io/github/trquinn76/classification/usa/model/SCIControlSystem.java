package io.github.trquinn76.classification.usa.model;

import java.util.List;
import java.util.Objects;

public record SCIControlSystem(String name, List<Compartment> compartments) {

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

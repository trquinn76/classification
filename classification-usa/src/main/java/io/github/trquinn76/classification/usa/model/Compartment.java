package io.github.trquinn76.classification.usa.model;

import java.util.List;
import java.util.Objects;

public record Compartment(String name, List<String> subCompartments) {

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

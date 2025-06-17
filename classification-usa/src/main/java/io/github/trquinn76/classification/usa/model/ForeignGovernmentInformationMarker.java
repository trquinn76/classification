package io.github.trquinn76.classification.usa.model;

import java.util.List;
import java.util.Objects;

/**
 * Defines the Foreign Government Information Marker.
 */
public record ForeignGovernmentInformationMarker(List<String> countries) {

    /**
     * Constructor. Defensively copies lists to ensure record immutability.
     * 
     * @param countries a list of foreign countries. May not be null. May be empty.
     */
    public ForeignGovernmentInformationMarker {
        Objects.requireNonNull(countries);
        countries = List.copyOf(countries);
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder("FGI");
        for (String country : countries()) {
            buf.append(" ").append(country);
        }
        return buf.toString();
    }
}

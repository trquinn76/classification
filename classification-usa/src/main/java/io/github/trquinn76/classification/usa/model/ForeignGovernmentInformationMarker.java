package io.github.trquinn76.classification.usa.model;

import java.util.List;
import java.util.Objects;

public record ForeignGovernmentInformationMarker(List<String> countries) {

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

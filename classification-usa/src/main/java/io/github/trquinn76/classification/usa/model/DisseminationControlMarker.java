package io.github.trquinn76.classification.usa.model;

import java.util.List;
import java.util.Objects;

import io.github.trquinn76.classification.usa.Utils;

public record DisseminationControlMarker(DisseminationControls type, List<String> countries) {

    public DisseminationControlMarker {
        Objects.requireNonNull(type);
        Objects.requireNonNull(countries);
        if (!countries.isEmpty() && !List.of(DisseminationControls.RELEASE_TO, DisseminationControls.DISPLAY_ONLY).contains(type)) {
            StringBuilder buf = new StringBuilder("The countries list should only be populated for '");
            buf.append(DisseminationControls.RELEASE_TO.toString()).append("' and '").append(DisseminationControls.DISPLAY_ONLY.toString());
            buf.append("'. It should be empty for: '").append(type.toString()).append("'.");
            throw new IllegalArgumentException(buf.toString());
        }
        else if (type == DisseminationControls.DISPLAY_ONLY && countries.isEmpty()) {
            throw new IllegalArgumentException("The '" + DisseminationControls.DISPLAY_ONLY.toString() + "' mark requires at least 1 country.");
        }
        else if (type == DisseminationControls.RELEASE_TO) {
            if (!countries.contains(Utils.USA)) {
                throw new IllegalArgumentException("For the '" + DisseminationControls.RELEASE_TO.toString() + "' mark, the countries list must include " + Utils.USA + ".");
            }
            else if (countries.size() < 2) {
                throw new IllegalArgumentException("For the '" + DisseminationControls.RELEASE_TO.toString() + "' mark, the countries list must have at least 2 entries.");
            }
        }
        countries = List.copyOf(countries);
    }
    
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(type().toString());
        if (!countries().isEmpty()) {
            buf.append(" ");
            for (int i = 0; i < countries().size(); i++) {
                if (i > 0) buf.append(", ");
                buf.append(countries().get(i));
            }
        }
        return buf.toString();
    }
}

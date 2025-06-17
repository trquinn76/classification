package io.github.trquinn76.classification.usa.model;

import java.util.List;
import java.util.Objects;

import io.github.trquinn76.classification.usa.Utils;

/**
 * Defines the Dissemination Mark. Each {@link ClassificationMarker} may have 0
 * to many of these markers.
 * 
 * @param type      the {@link Disseminations} type. May not be null.
 * @param countries a list of countries associated with the Dissemination. May
 *                  not be null. May be empty.
 */
public record DisseminationMarker(Disseminations type, List<String> countries) {

    /**
     * Constructor. Defensively copies lists to ensure record immutability.
     * 
     * @param type      the {@link Disseminations} type. May not be null.
     * @param countries a list of countries associated with the Dissemination. May
     *                  not be null. May be empty.
     */
    public DisseminationMarker {
        Objects.requireNonNull(type);
        Objects.requireNonNull(countries);
        if (!countries.isEmpty() && !List.of(Disseminations.RELEASE_TO, Disseminations.DISPLAY_ONLY).contains(type)) {
            StringBuilder buf = new StringBuilder("The countries list should only be populated for '");
            buf.append(Disseminations.RELEASE_TO.toString()).append("' and '")
                    .append(Disseminations.DISPLAY_ONLY.toString());
            buf.append("'. It should be empty for: '").append(type.toString()).append("'.");
            throw new IllegalArgumentException(buf.toString());
        } else if (type == Disseminations.DISPLAY_ONLY && countries.isEmpty()) {
            throw new IllegalArgumentException(
                    "The '" + Disseminations.DISPLAY_ONLY.toString() + "' mark requires at least 1 country.");
        } else if (type == Disseminations.RELEASE_TO) {
            if (!countries.contains(Utils.USA)) {
                throw new IllegalArgumentException("For the '" + Disseminations.RELEASE_TO.toString()
                        + "' mark, the countries list must include " + Utils.USA + ".");
            } else if (countries.size() < 2) {
                throw new IllegalArgumentException("For the '" + Disseminations.RELEASE_TO.toString()
                        + "' mark, the countries list must have at least 2 entries.");
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
                if (i > 0)
                    buf.append(", ");
                buf.append(countries().get(i));
            }
        }
        return buf.toString();
    }
}

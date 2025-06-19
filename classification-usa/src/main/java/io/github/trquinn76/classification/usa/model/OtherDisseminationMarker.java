package io.github.trquinn76.classification.usa.model;

import java.util.List;
import java.util.Objects;

/**
 * Defines an Other Dissemination Mark, which {@link ClassificationMarker}'s may
 * have 0 to many of.
 * 
 * @param type             {@link OtherDisseminations} which is the type of
 *                         Other Dissemination. May not be null.
 * @param programNickNames a set of Program Nick Names, which are only valid for
 *                         {@link OtherDisseminations}.ACCM. May not be null. May
 *                         be empty.
 */
public record OtherDisseminationMarker(OtherDisseminations type, List<String> programNickNames) {

    /**
     * Constructor. Defensively copies lists to ensure record immutability.
     * 
     * @param type             {@link OtherDisseminations} which is the type of
     *                         Other Dissemination. May not be null.
     * @param programNickNames a set of Program Nick Names, which are only valid for
     *                         {@link OtherDisseminations}.ACCM. May not be null. May
     *                         be empty.
     */
    public OtherDisseminationMarker {
        Objects.requireNonNull(type);
        Objects.requireNonNull(programNickNames);
        if (type != OtherDisseminations.ACCM && !programNickNames.isEmpty()) {
            throw new IllegalArgumentException(
                    "May not have Program Nick Names for Other Dissemination mark: " + type.toString());
        } else if (type == OtherDisseminations.ACCM && programNickNames.isEmpty()) {
            throw new IllegalArgumentException("When using '" + OtherDisseminations.ACCM.toString()
                    + "' there must be a minimum of 1 Programe Nick Name.");
        }
        programNickNames = List.copyOf(programNickNames);
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(type().toString());
        if (type() == OtherDisseminations.ACCM && !programNickNames().isEmpty()) {
            buf.append("-");
            for (int i = 0; i < programNickNames().size(); i++) {
                if (i > 0)
                    buf.append("/");
                buf.append(programNickNames().get(i));
            }
        }
        return buf.toString();
    }
}

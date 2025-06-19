package io.github.trquinn76.classification.usa.model;

import java.util.List;
import java.util.Objects;

/**
 * Defines non-USA modifiers to the Classification.
 * 
 * @param type            the {@link NonUSAndJointType} of the modifier. May not
 *                        be null.
 * @param natoSpecialMark an arbitrary String representing special NATO marks
 *                        (eg: ATOMAL, BOHEMIA). May be null.
 * @param countries       a list of countries related to the
 *                        {@link NonUSAndJointType}. May not be null. May be
 *                        empty.
 */
public record NonUSAndJointClassificationModifier(NonUSAndJointType type, String natoSpecialMark,
        List<String> countries) {

    /**
     * Constructor. Defensively copies lists to ensure record immutability.
     * 
     * @param type            the {@link NonUSAndJointType} of the modifier. May not
     *                        be null.
     * @param natoSpecialMark an arbitrary String representing special NATO marks
     *                        (eg: ATOMAL, BOHEMIA). May be null.
     * @param countries       a list of countries related to the
     *                        {@link NonUSAndJointType}. May not be null. May be
     *                        empty.
     */
    public NonUSAndJointClassificationModifier {
        Objects.requireNonNull(type);
        Objects.requireNonNull(countries);
        if (natoSpecialMark != null && !List.of(NonUSAndJointType.NATO, NonUSAndJointType.COSMIC).contains(type)) {
            StringBuilder buf = new StringBuilder();
            buf.append("A NATO special mark (").append(natoSpecialMark).append(") is only valid for a type of '");
            buf.append(NonUSAndJointType.NATO.name()).append("' or '").append(NonUSAndJointType.COSMIC.name());
            buf.append("'. It is not valid for: ").append(type.name()).append(".");
            throw new IllegalArgumentException(buf.toString());
        }
        if (!countries.isEmpty()) {
            if (!List.of(NonUSAndJointType.JOINT, NonUSAndJointType.FOREIGN).contains(type)) {
                throw new IllegalArgumentException("When country list is populated, the Type must be one of "
                        + NonUSAndJointType.JOINT.name() + " or " + NonUSAndJointType.FOREIGN.name() + ".");
            } else if (type == NonUSAndJointType.JOINT && !countries.contains("USA")) {
                throw new IllegalArgumentException("When using the " + NonUSAndJointType.JOINT.name()
                        + " marker, in the context of the USA, the country list must include the USA");
            } else if (type == NonUSAndJointType.FOREIGN && countries.size() != 1) {
                throw new IllegalArgumentException(
                        "When using Non US Markings, there should be exactly one country listed.");
            }
        }
        countries = List.copyOf(countries);
    }

    /**
     * Indicates if the modifier is for a JOINT classification.
     * 
     * @return true if the modifier is for a JOINT classification. False otherwise.
     */
    public boolean isJoint() {
        return NonUSAndJointType.JOINT == this.type;
    }

    /**
     * Indicates if the modifier is for a NATO classification.
     * 
     * @return true if the modifier is for a NATO or COSMIC classification. False
     *         otherwise.
     */
    public boolean isNATO() {
        return NonUSAndJointType.NATO == this.type || NonUSAndJointType.COSMIC == this.type;
    }

    /**
     * Indicates if the modifier is for a Foreign classification.
     * 
     * @return true if the modifier is for a Foreign classification. False
     *         otherwise.
     */
    public boolean isForeign() {
        return NonUSAndJointType.FOREIGN == this.type;
    }
}

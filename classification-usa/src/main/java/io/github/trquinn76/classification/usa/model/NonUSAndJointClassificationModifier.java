package io.github.trquinn76.classification.usa.model;

import java.util.List;
import java.util.Objects;

public record NonUSAndJointClassificationModifier(NonUSAndJointType type, String natoSpecialMark, List<String> countries) {

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
            }
            else if (type == NonUSAndJointType.JOINT && !countries.contains("USA")) {
                throw new IllegalArgumentException("When using the " + NonUSAndJointType.JOINT.name() + " marker, in the context of the USA, the country list must include the USA");
            }
            else if (type == NonUSAndJointType.FOREIGN && countries.size() != 1) {
                throw new IllegalArgumentException("When using Non US Markings, there should be exactly one country listed.");
            }
        }
        countries = List.copyOf(countries);
    }
    
    public boolean isJoint() {
        return NonUSAndJointType.JOINT == this.type;
    }
    
    public boolean isNATO() {
        return NonUSAndJointType.NATO == this.type || NonUSAndJointType.COSMIC == this.type;
    }
    
    public boolean isForeign() {
        return NonUSAndJointType.FOREIGN == this.type;
    }
}

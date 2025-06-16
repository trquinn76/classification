package io.github.trquinn76.classification.usa.model;

import java.util.List;
import java.util.Objects;

public record OtherDisseminationMarker(OtherDisseminations type, List<String> programNickNames) {

    public OtherDisseminationMarker {
        Objects.requireNonNull(type);
        Objects.requireNonNull(programNickNames);
        if (type != OtherDisseminations.ACCM && !programNickNames.isEmpty()) {
            throw new IllegalArgumentException("May not have Program Nick Names for Other Dissemination mark: " + type.toString());
        }
        else if (type == OtherDisseminations.ACCM && programNickNames.isEmpty()) {
            throw new IllegalArgumentException("When using '" + OtherDisseminations.ACCM.toString() + "' there must be a minimum of 1 Programe Nick Name.");
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
                if (i > 0) buf.append("/");
                buf.append(programNickNames().get(i));
            }
        }
        return buf.toString();
    }
}

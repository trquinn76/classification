package io.github.trquinn76.classification.nzl.model;

import java.util.List;
import java.util.Objects;

public record ReleasabilityMarking(ReleasabilityTypes type, List<String> releasableToList) {

    public ReleasabilityMarking {
        Objects.requireNonNull(type);
        Objects.requireNonNull(releasableToList);
        releasableToList = List.copyOf(releasableToList);
    }

    @Override
    public String toString() {
        StringBuilder buff = new StringBuilder(type().toString());
        if (!releasableToList().isEmpty()) {
            buff.append(" ");
            for (int i = 0; i < releasableToList.size(); i++) {
                if (i > 0)
                    buff.append(", ");
                buff.append(releasableToList.get(i));
            }
        }
        return buff.toString();
    }
}

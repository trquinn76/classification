package io.github.trquinn76.classification.nzl.model;

import java.util.List;
import java.util.Objects;

/**
 * Represents the Releasability of Classified data.
 * <p>
 * The {@code releasableToList} should only be populated when the
 * {@link ReleasabilityType} is {@code RELTO}. It is expected to be populated
 * with country trigraphs, as defined in the {@code ISO 3166-1 Codes} list. This
 * is not enforced.
 * <p>
 * When {@code releasableToList}'s exist they are required to have a minimum
 * length of 2, and they must contain the New Zealand trigraph, 'NZL'.
 * 
 * @param type             the {@link ReleasabilityTypes} representing the
 *                         releasability.
 * @param releasableToList a releasable to list, which should only be populated
 *                         when {@link ReleasabilityTypes} is {@code RELTO}. May
 *                         be empty. May not be null.
 */
public record ReleasabilityMarking(ReleasabilityTypes type, List<String> releasableToList) {

    /**
     * Constructor allows defensive copying of lists.
     * 
     * @param type             the {@link ReleasabilityTypes} representing the
     *                         releasability.
     * @param releasableToList a releasable to list, which should only be populated
     *                         when {@link ReleasabilityTypes} is {@code RELTO}. May
     *                         be empty. May not be null.
     */
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

package io.github.trquinn76.classification.usa.model;

import java.util.List;
import java.util.Objects;

/**
 * A Marker for Atomic Energy Act Information.
 * 
 * @param aeaMark      the {@link AtomicEnergyActMakings}. May not be null.
 * @param sigmaNumbers a list of sigma numbers associated with the
 *                     {@link AtomicEnergyActMakings}. May not be null. May be
 *                     empty.
 */
public record AtomicEnergyActInformationMarker(AtomicEnergyActMarkings aeaMark, List<Integer> sigmaNumbers) {

    /**
     * Constructor. Lists are defensively copied to ensure immutability of the
     * record.
     * 
     * @param aeaMark      the {@link AtomicEnergyActMakings}. May not be null.
     * @param sigmaNumbers a list of sigma numbers associated with the
     *                     {@link AtomicEnergyActMakings}. May not be null. May be
     *                     empty.
     */
    public AtomicEnergyActInformationMarker {
        Objects.requireNonNull(aeaMark);
        Objects.requireNonNull(sigmaNumbers);
        if (List.of(AtomicEnergyActMarkings.RESTRICTED_DATA_SIGMA,
                AtomicEnergyActMarkings.FORMALLY_RESTRICTED_DATA_SIGMA).contains(aeaMark)) {
            if (sigmaNumbers.isEmpty()) {
                throw new IllegalArgumentException(
                        "There MUST be at least one SIGMA number for AEA Mark: " + aeaMark.name());
            }
        } else {
            // sigmaNumbers must be empty.
            if (!sigmaNumbers.isEmpty()) {
                throw new IllegalArgumentException("There may not be SIGMA numbers for AEA Mark: " + aeaMark.name());
            }
        }
        sigmaNumbers = List.copyOf(sigmaNumbers);
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(aeaMark().toString());
        for (Integer sigmaNumber : sigmaNumbers()) {
            buf.append(sigmaNumber);
        }
        return buf.toString();
    }
}

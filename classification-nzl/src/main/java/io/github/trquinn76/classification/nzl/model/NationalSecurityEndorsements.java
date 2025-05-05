package io.github.trquinn76.classification.nzl.model;

import java.util.List;

/**
 * The {@link NationalSecurityEndorsements} contains various national security
 * related endorsements, including Accountable Material, Sensitive Compartments,
 * dissemination marks, and Releasability.
 * 
 * @param accountableMaterial   boolean indicating Accountable Material, which
 *                              requires tighter handling.
 * @param sensitiveCompartments a List of Compartments or Code words. May be
 *                              empty, may not be null.
 * @param disseminationMarks    a List of additional dissemination marks. May be
 *                              empty, may not be null.
 * @param releasability         a releasability marker, or null.
 */
public record NationalSecurityEndorsements(boolean accountableMaterial, List<String> sensitiveCompartments,
        List<String> disseminationMarks, ReleasabilityMarking releasability) {

    /**
     * Constructor performs defensive copying of lists.
     * 
     * @param accountableMaterial   boolean indicating Accountable Material, which
     *                              requires tighter handling.
     * @param sensitiveCompartments a List of Compartments or Code words. May be
     *                              empty, may not be null.
     * @param disseminationMarks    a List of additional dissemination marks. May be
     *                              empty, may not be null.
     * @param releasability         a releasability marker, or null.
     */
    public NationalSecurityEndorsements {
        sensitiveCompartments = List.copyOf(sensitiveCompartments);
        disseminationMarks = List.copyOf(disseminationMarks);
    }

    /**
     * Convenience function to check the presence of control endorsements.
     * 
     * @return true iff there are control endorsements present.
     */
    public boolean hasControls() {
        return accountableMaterial() || !sensitiveCompartments().isEmpty();
    }

    /**
     * Convenience function to check the presence of dissemination endorsements.
     * 
     * @return true iff there are dissemination endorsements present.
     */
    public boolean hasDissemination() {
        return !disseminationMarks().isEmpty() || releasability() != null;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder("");

        if (accountableMaterial() || !sensitiveCompartments().isEmpty() || !disseminationMarks().isEmpty()
                || releasability() != null) {
            // Control Section
            if (hasControls()) {
                buf.append("/");
                if (accountableMaterial()) {
                    buf.append("/ACCOUNTABLE MATERIAL");
                }
                for (String compartment : sensitiveCompartments()) {
                    buf.append("/").append(compartment);
                }
            }

            // Dissemination Section
            if (hasDissemination()) {
                buf.append("/");
                for (String disseminationMark : disseminationMarks()) {
                    buf.append("/").append(disseminationMark);
                }
                if (releasability() != null) {
                    buf.append("/").append(releasability().toString());
                }
            }
        }

        return buf.toString();
    }

    /**
     * Provides a String representation of the {@link NationalSecurityEndorsements} which is suitable for use with
     * TOP SECRET classification.
     * 
     * The main distinction between this function and {@code toString()} is that this function does NOT include
     * an Accountable Material mark. All TOP SECRET data is considered Accountable Material by default, making the
     * mark unnecessary.
     * 
     * @return a String representation of the endorsements.
     */
    public String toStringTopSecret() {
        StringBuilder buf = new StringBuilder("");

        if (accountableMaterial() || !sensitiveCompartments().isEmpty() || !disseminationMarks().isEmpty()
                || releasability() != null) {
            // Control Section
            if (!sensitiveCompartments().isEmpty()) {
                // no ACCOUNTABLE MATERIAL, as all TS is considered Accountable Material by
                // default.
                buf.append("/");
                for (String compartment : sensitiveCompartments()) {
                    buf.append("/").append(compartment);
                }
            }

            // Dissemination Section
            if (hasDissemination()) {
                buf.append("/");
                for (String disseminationMark : disseminationMarks()) {
                    buf.append("/").append(disseminationMark);
                }
                if (releasability() != null) {
                    buf.append("/").append(releasability().toString());
                }
            }
        }

        return buf.toString();
    }
}

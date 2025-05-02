package io.github.trquinn76.classification.nzl.model;

import java.util.List;
import java.util.Objects;

public record ProtectiveMarker(Classification classification,
        List<PolicyAndPrivacyEndorsementMarking> policyAndPrivacyEndorsements, boolean accountableMaterial,
        List<String> sensitiveCompartments, ReleasabilityMarking releasabilityMarker) {

    public ProtectiveMarker {
        Objects.requireNonNull(classification);
        Objects.requireNonNull(policyAndPrivacyEndorsements);
        Objects.requireNonNull(sensitiveCompartments);

        if (Classification.topSecret().equals(classification) && !accountableMaterial) {
            throw new IllegalArgumentException("When Classification is '" + Classification.topSecret().toString()
                    + "', Accountable Material must be set true.");
        }

        policyAndPrivacyEndorsements = List.copyOf(policyAndPrivacyEndorsements);
        sensitiveCompartments = List.copyOf(sensitiveCompartments);
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();

        // Policy and Privacy Section
        for (int i = 0; i < policyAndPrivacyEndorsements().size(); i++) {
            PolicyAndPrivacyEndorsementMarking endorsement = policyAndPrivacyEndorsements().get(i);
            if (i > 0)
                buf.append(" ");
            buf.append(endorsement.toString());
        }
        if (!policyAndPrivacyEndorsements().isEmpty()) {
            buf.append(" ");
        }

        // Classification
        buf.append(classification().toString());

        // Control Section
        if (accountableMaterial() || !sensitiveCompartments().isEmpty()) {
            StringBuilder controlBuf = new StringBuilder();
            if (accountableMaterial() && !classification().equals(Classification.topSecret())) {
                // TOP SECRET is implicitly Accountable Material, so no need to print the
                // endorsement for TOP SECRET.
                controlBuf.append("/ACCOUNTABLE MATERIAL");
            }
            if (!sensitiveCompartments().isEmpty()) {
                for (String compartment : sensitiveCompartments()) {
                    controlBuf.append("/").append(compartment);
                }
            }
            if (!controlBuf.isEmpty()) {
                buf.append("/").append(controlBuf.toString());
            }
        }

        // Dissemination Section
        if (releasabilityMarker() != null) {
            buf.append("//").append(releasabilityMarker());
        }

        return buf.toString();
    }
}

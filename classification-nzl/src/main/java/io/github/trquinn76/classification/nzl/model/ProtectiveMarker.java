package io.github.trquinn76.classification.nzl.model;

import java.util.List;
import java.util.Objects;

/**
 * {@link ProtectiveMarker} represents Protective Markings as defined in the
 * Overview of the Classification system document.
 * <p>
 * A {@link ProtectiveMarker} is made of three parts.
 * <ul>
 * <li>{@link Classification} - Either a {@link NZLClassification} or a
 * {@link DevelopmentClassification} depending on configuration.</li>
 * <li>Policy And Privacy Endorsements - derived from the Overview of the Classification system document.</li>
 * <li>{@link NationalSecurityEndorsements} - other security related markings, including code
 * words, releasability and other dissemination marks.</li>
 * </ul>
 * 
 * @param classification               the {@link Classification}. May not be
 *                                     null.
 * @param policyAndPrivacyEndorsements a List of
 *                                     {@link PolicyAndPrivacyEndorsementMarking},
 *                                     which may be empty, but may not be null.
 * @param nationalSecurityEndorsements a {@link NationalSecurityEndorsements}
 *                                     which contains National Security
 *                                     Endorsements. May be null.
 */
public record ProtectiveMarker(Classification classification,
        List<PolicyAndPrivacyEndorsementMarking> policyAndPrivacyEndorsements, 
        NationalSecurityEndorsements nationalSecurityEndorsements) {

    /**
     * Constructor allowng defencive copying of Lists.
     * 
     * @param classification               the {@link Classification}. May not be
     *                                     null.
     * @param policyAndPrivacyEndorsements a List of
     *                                     {@link PolicyAndPrivacyEndorsementMarking},
     *                                     which may be empty, but may not be null.
     * @param nationalSecurityEndorsements a {@link NationalSecurityEndorsements}
     *                                     which contains National Security
     *                                     Endorsements. May be null.
     */
    public ProtectiveMarker {
        Objects.requireNonNull(classification);
        Objects.requireNonNull(policyAndPrivacyEndorsements);

        policyAndPrivacyEndorsements = List.copyOf(policyAndPrivacyEndorsements);
    }
    
    /**
     * Convenience function which indicates if the {@link ProtectiveMarker} contains Policy and Privacy Endorsements.
     * 
     * @return true iff the {@link ProtectiveMarker} contains Policy and Privacy Endorsements.
     */
    public boolean hasPolicyAndPrivacyEndorsements() {
        return !policyAndPrivacyEndorsements().isEmpty();
    }
    
    /**
     * Convenience function which indicates if the {@link ProtectiveMarker} contains National Security Endorsements.
     * 
     * @return true iff the {@link ProtectiveMarker} contains National Security Endorsements.
     */
    public boolean hasNationalSecurityEndorsements() {
        return nationalSecurityEndorsements() != null;
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

        // National Security Section
        if (nationalSecurityEndorsements() != null) {
            if (Classification.topSecret().compareTo(classification()) == 0) {
                buf.append(nationalSecurityEndorsements().toStringTopSecret());
            }
            else {
                buf.append(nationalSecurityEndorsements().toString());
            }
        }

        return buf.toString();
    }
}

package io.github.trquinn76.classification.nzl.model;

import java.util.Objects;

/**
 * Represents a Policy and Privacy Endorsement, with a String which is required
 * for some endorsements, but not others.
 * 
 * @param endorsement        the {@link PolicyAndPrivacyEndorsements}.
 * @param timeOrUseOnlyValue a String required for some Endorsements, or null.
 */
public record PolicyAndPrivacyEndorsementMarking(PolicyAndPrivacyEndorsements endorsement, String timeOrUseOnlyValue) {

    /**
     * Constructor allows ensuring the String is only populated when necessary.
     * 
     * @param endorsement        the {@link PolicyAndPrivacyEndorsements}.
     * @param timeOrUseOnlyValue a String required for some Endorsements, or null.
     */
    public PolicyAndPrivacyEndorsementMarking {
        Objects.requireNonNull(endorsement);
        switch (endorsement) {
        case TO_BE_REVIEWED_ON:
        case DEPARTMENT_USE_ONLY:
        case EMBARGOED_FOR_RELEASE: {
            Objects.requireNonNull(timeOrUseOnlyValue);
            if (timeOrUseOnlyValue.isBlank()) {
                throw new IllegalArgumentException(
                        "Blank value for 'reviewOrUseOnlyValue' is not permitted with this Endorsement: "
                                + endorsement.toString());
            }
            break;
        }
        case APPOINTMENTS:
        case BUDGET:
        case CABINET:
        case COMMERCIAL:
        case EVALUATE:
        case HONOURS:
        case LEGAL_PRIVILEGE:
        case MEDICAL:
        case POLICY:
        case STAFF: {
            if (timeOrUseOnlyValue != null) {
                throw new IllegalArgumentException(
                        "No value for 'reviewOrUseOnlyValue' is required with this Endorsement: "
                                + endorsement.toString());
            }
            break;
        }

        }
    }

    @Override
    public String toString() {
        switch (endorsement()) {
        case DEPARTMENT_USE_ONLY: {
            // put the list of departments before the string "USE ONLY".
            return new StringBuilder(timeOrUseOnlyValue()).append(" ")
                    .append(PolicyAndPrivacyEndorsements.DEPARTMENT_USE_ONLY).toString();
        }
        case TO_BE_REVIEWED_ON:
        case EMBARGOED_FOR_RELEASE: {
            // place the date after the endorsement string "TO BE REVIEWED ON" or "EMBARGOED
            // FOR RELEASE".
            return new StringBuilder(endorsement().toString()).append(" ").append(timeOrUseOnlyValue()).toString();
        }
        case APPOINTMENTS:
        case BUDGET:
        case CABINET:
        case COMMERCIAL:
        case EVALUATE:
        case HONOURS:
        case LEGAL_PRIVILEGE:
        case MEDICAL:
        case POLICY:
        case STAFF:
        default: {
            return endorsement().toString();
        }
        }
    }
}

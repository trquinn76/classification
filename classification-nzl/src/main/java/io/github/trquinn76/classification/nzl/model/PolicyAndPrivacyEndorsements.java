package io.github.trquinn76.classification.nzl.model;

/**
 * Defines the set of Policy and Privacy Endorsements used in the library.
 * 
 * Taken from the "Overview of the Classification system" document.
 */
public enum PolicyAndPrivacyEndorsements {

    // @formatter:off
    APPOINTMENTS("APPOINTMENTS"),
    BUDGET("BUDGET"),
    CABINET("CABINET"),
    COMMERCIAL("COMMERCIAL"),
    DEPARTMENT_USE_ONLY("USE ONLY"),
    EMBARGOED_FOR_RELEASE("EMBARGOED FOR RELEASE"),
    EVALUATE("EVALUATE"),
    HONOURS("HONOURS"),
    LEGAL_PRIVILEGE("LEGAL PRIVILEGE"),
    MEDICAL("MEDICAL"),
    STAFF("STAFF"),
    POLICY("POLICY"),
    TO_BE_REVIEWED_ON("TO BE REVIEWED ON");
    // @formatter:on

    private String text;

    private PolicyAndPrivacyEndorsements(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return this.text;
    }
}

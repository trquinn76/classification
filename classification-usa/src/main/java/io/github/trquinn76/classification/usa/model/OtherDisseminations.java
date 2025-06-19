package io.github.trquinn76.classification.usa.model;

/**
 * Defines the set of Other Disseminations supported by this library.
 */
public enum OtherDisseminations {

    // @formatter:off
    //Alternative Compensatory Control Measures
    ACCM("ACCM"),
    // DOS DISSEMINATION CONTROL MARKINGS
    EXCLUSIVE_DISTRIBUTION("EXDIS"),
    NO_DISTRIBUTION("NODIS"),
    SENSITIVE_BUT_UNCLASSIFIED("SBU"),
    SENSITIVE_BUT_UNCLASSIFIED_NOFORN("SBU-NF");
    // @formatter:on

    private final String text;

    private OtherDisseminations(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return this.text;
    }
}

package io.github.trquinn76.classification.nzl.model;

public enum ReleasabilityTypes {

    // @formatter:off
    NZEO("NZEO"),
    RELTO("REL TO");
    // @formatter:on

    private String text;

    private ReleasabilityTypes(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return this.text;
    }
}

package io.github.trquinn76.classification.nzl.model;

/**
 * The set of Releasability types defined in the Overview of the Classification system.
 * 
 * <ul>
 * <li>NZEO - New Zealand Eyes Only</li>
 * <li>RELTO - Releasable To</li>
 * </ul>
 */
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

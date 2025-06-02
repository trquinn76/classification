package io.github.trquinn76.classification.usa.model;

public enum AtomicEnergyActMarkings {
    // @formatter:off
    RESTRICTED_DATA("RD"),
    RESTRICTED_DATA_CNWDI("RD-N"),
    RESTRICTED_DATA_SIGMA("RD-SIGMA"),
    FORMALLY_RESTRICTED_DATA("FRD"),
    FORMALLY_RESTRICTED_DATA_SIGMA("FRD-SIGMA"),
    DOD_UNCLASSIFIED_CONTROLLED_NUCLEAR_INFORMATION("DOD UCNI"), // may no longer be in use.
    DOE_UNCLASSIFIED_CONTROLLED_NUCLEAR_INFORMATION("DOE UCNI"), // may no longer be in use.
    TRANSCLASSIFIED_FOREIGN_NUCLEAR_INFORMATION("TFNI");
    // @formatter:on
    
    private final String text;
    
    private AtomicEnergyActMarkings(String text) {
        this.text = text;
    }
    
    @Override
    public String toString() {
        return this.text;
    }
}

package io.github.trquinn76.classification.usa.model;

public enum Disseminations {

    // @formatter:off
    // SAP WAIVED mark
    WAIVED("WAIVED"),
    // Dissemination Control Markings
    FOUO("FOUO"),
    CONTROLLED_UNCLASSIFIED_INFORMATION("CUI"),
    ORIGINATOR_CONTROLLED("ORCON"),
    RELEASE_TO("REL TO"),
    DISPLAY_ONLY("DISPLAY ONLY"),
    // Dissemination Control Markings for Intelligence Information
    CONTROLLED_IMAGERY("IMCON"),
    NOFORN("NOFORN"), // Not Releasable to Foreign Nationals.
    PROPRIETARY_INFORMATION("PROPIN"),
    RELIDO("RELIDO"), // Releasable by Information Disclosure Official.
    FISA("FISA"); // Foreign Intelligence Surveillance Act.
    // @formatter:on
    
    private final String text;
    
    private Disseminations(String text) {
        this.text = text;
    }
    
    @Override
    public String toString() {
        return this.text;
    }
}

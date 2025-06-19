package io.github.trquinn76.classification.usa.model;

/**
 * Defines the set of non-USA classification modifiers.
 */
public enum NonUSAndJointType {

    // @formatter:off
    JOINT, // Restricted NOT Allowed.
    NATO, // Restricted Allowed. TOP SECRET NOT Allowed - use COSMIC for what would be NATO TOP SECRET (ie: COSMIC TOP SECRET).
    COSMIC, // like NATO but for TOP SECRET only. COSMIC TOP SECRET is the correct marking for the notional NATO TOP SECRET.
    FOREIGN; // Restricted Allowed. Part of modifying the Classification. Distinct from FGI marks.
    // @formatter:on
}

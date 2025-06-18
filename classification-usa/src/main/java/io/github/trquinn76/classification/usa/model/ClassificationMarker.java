package io.github.trquinn76.classification.usa.model;

import java.util.List;
import java.util.Objects;

/**
 * {@link ClassificationMarker} represents Classifications as defined in the DoD
 * Information Security Program: Marking of Information.
 * 
 * A {@link ClassificationMarker} is made up of several parts.
 * <ul>
 * <li>Modifier - used to indicate that the standard USA Classification is not
 * in use, and that one of JOINT, NATO or Foreign Classifications are in use.
 * May be null.</li>
 * <li>{@link Classification} - the classification, either a
 * {@link SecurityClassification} or a {@link DevelopmentClassification}
 * depending on configuration. May not be null.</li>
 * <li>Sensitive Compartment Information - a list of {@link SCIControlSystem}'s.
 * May not be null. May be empty.</li>
 * <li>Special Access Programs - a list of {@link SARProgram}'s. May not be
 * null. May be empty.</li>
 * <li>Atomic Energy Act Information - marks Atomic Energy Act Information. May
 * be null.</li>
 * <li>Foreign Government Information - marks the presence of Foreign Government
 * Information. May be null.</li>
 * <li>Disseminations - a list of {@link DisseminationMarker}. May not be null.
 * May be empty.</li>
 * <li>Other Disseminatins - a list of {@link OtherDisseminationMarker}. May not
 * be null. May be empty.</li>
 * <li>Addition Markings - a list of arbitrary Strings. May not be null. May be
 * empty.</li>
 * </ul>
 * 
 * @param classificationModifier             a
 *                                           {@link NonUSAndJointClassificationModifier}
 *                                           describing a modification, or null
 *                                           if there is no modification.
 * @param classification                     the {@link Classification} to set.
 *                                           May not be null.
 * @param sensitiveCompartmentControlSystems a list of
 *                                           {@link SCIControlSystem}'s. May not
 *                                           be null. May be empty.
 * @param specialAccessPrograms              a list of {@link SARProgram}'s. May
 *                                           not be null. May be empty.
 * @param atomicEnergyInformationMarker      an
 *                                           {@link AtomicEnergyActInformationMarker}.
 *                                           May be null.
 * @param foreignGovernmentInformationMarker a
 *                                           {@link ForeignGovernmentInformationMarker}.
 *                                           May be null.
 * @param disseminations                     a list of
 *                                           {@link DisseminationMarker}'s. May
 *                                           not be null. May be empty.
 * @param otherDisseminations                a list of
 *                                           {@link OtherDisseminationMarker}'s.
 *                                           May not be null. May be empty.
 * @param additionalMarkings                 a list of String. May not be null.
 *                                           May be empty.
 */
public record ClassificationMarker(NonUSAndJointClassificationModifier classificationModifier,
        Classification classification, List<SCIControlSystem> sensitiveCompartmentControlSystems,
        List<SARProgram> specialAccessPrograms, AtomicEnergyActInformationMarker atomicEnergyInformationMarker,
        ForeignGovernmentInformationMarker foreignGovernmentInformationMarker, List<DisseminationMarker> disseminations,
        List<OtherDisseminationMarker> otherDisseminations, List<String> additionalMarkings) {

    /**
     * A Constructor which ensures parameters are not null, and performs defensive
     * list copying.
     * 
     * @param classificationModifier             a
     *                                           {@link NonUSAndJointClassificationModifier}
     *                                           describing a modification, or null
     *                                           if there is no modification.
     * @param classification                     the {@link Classification} to set.
     *                                           May not be null.
     * @param sensitiveCompartmentControlSystems a list of
     *                                           {@link SCIControlSystem}'s. May not
     *                                           be null. May be empty.
     * @param specialAccessPrograms              a list of {@link SARProgram}'s. May
     *                                           not be null. May be empty.
     * @param atomicEnergyInformationMarker      an
     *                                           {@link AtomicEnergyActInformationMarker}.
     *                                           May be null.
     * @param foreignGovernmentInformationMarker a
     *                                           {@link ForeignGovernmentInformationMarker}.
     *                                           May be null.
     * @param disseminations                     a list of
     *                                           {@link DisseminationMarker}'s. May
     *                                           not be null. May be empty.
     * @param otherDisseminations                a list of
     *                                           {@link OtherDisseminationMarker}'s.
     *                                           May not be null. May be empty.
     * @param additionalMarkings                 a list of String. May not be null.
     *                                           May be empty.
     */
    public ClassificationMarker {
        Objects.requireNonNull(classification);
        Objects.requireNonNull(sensitiveCompartmentControlSystems);
        Objects.requireNonNull(specialAccessPrograms);
        Objects.requireNonNull(disseminations);
        Objects.requireNonNull(otherDisseminations);
        Objects.requireNonNull(additionalMarkings);

        sensitiveCompartmentControlSystems = List.copyOf(sensitiveCompartmentControlSystems);
        specialAccessPrograms = List.copyOf(specialAccessPrograms);
        disseminations = List.copyOf(disseminations);
        otherDisseminations = List.copyOf(otherDisseminations);
        additionalMarkings = List.copyOf(additionalMarkings);
    }

    /**
     * Produces a String which mostly conforms to the requirements for document banners.
     * 
     * CLASSIFICATION//SCI//SAP//AEA//FGI//DISSEM//OTHER DISSEM
     * <p>
     * There is two ways in which this String does not conform to the defined format for a document banner.
     * <ol>
     * <li>If there are 3 or more SAP values, they are all included in the String. The defined format expects them to
     * be replaced with <STRONG>SAR-MULTIPLE PROGRAMS</STRONG> with a list of SAR Programs displayed elsewhere.
     * Obviously that is not possible in this library.</li>
     * <li>Additional Markings are added at the end, which is not part of the defined format at all.</li>
     * </ol>
     */
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();

        // Classification Modifier
        if (classificationModifier() != null) {
            buf.append("//");
            switch (classificationModifier().type()) {
            case COSMIC:
                buf.append("COSMIC ");
                break;
            case NATO:
                // if there is a special NATO mark (ie: BOHEMIA, ATOMAL, etc) the NATO part is
                // excluded.
                if (classificationModifier().natoSpecialMark() == null) {
                    buf.append("NATO ");
                }
                break;
            case FOREIGN:
                buf.append(classificationModifier().countries().getFirst()).append(" ");
                break;
            case JOINT:
                buf.append("JOINT ");
                break;
            }
        }

        // Classification
        buf.append(classification().toString());

        // Classification Modifer - JOINT country list
        if (classificationModifier() != null && classificationModifier().isJoint()) {
            for (String country : classificationModifier().countries()) {
                buf.append(" ").append(country);
            }
        }

        // Classification Modifier - NATO marks (eg: BOHEMIA, ATOMAL)
        if (classificationModifier() != null && classificationModifier().natoSpecialMark() != null) {
            buf.append(" ").append(classificationModifier().natoSpecialMark());
        }

        // SCI
        if (!sensitiveCompartmentControlSystems().isEmpty()) {
            buf.append("//");
            for (int i = 0; i < sensitiveCompartmentControlSystems().size(); i++) {
                if (i > 0)
                    buf.append("/");
                buf.append(sensitiveCompartmentControlSystems().get(i).toString());
            }
        }

        // SAP
        // Does not display "MULTIPLE PROGRAMS" when there is 3 or more SAP's. Always
        // includes all SAP's.
        // If a SAP is Waived, add "WAIVED" to the Additional Markings section.
        if (!specialAccessPrograms().isEmpty()) {
            buf.append("//SAR-");
            for (int i = 0; i < specialAccessPrograms().size(); i++) {
                if (i > 0)
                    buf.append("/");
                buf.append(specialAccessPrograms().get(i).toString());
            }
        }

        // AEA
        if (atomicEnergyInformationMarker() != null) {
            buf.append("//").append(atomicEnergyInformationMarker().toString());
        }

        // FGI
        if (foreignGovernmentInformationMarker() != null) {
            buf.append("//").append(foreignGovernmentInformationMarker().toString());
        }

        // DISSEM
        if (!disseminations().isEmpty()) {
            buf.append("//");
            for (int i = 0; i < disseminations().size(); i++) {
                if (i > 0)
                    buf.append("/");
                buf.append(disseminations().get(i).toString());
            }
        }

        // OTHER DISSEM
        if (!otherDisseminations().isEmpty()) {
            buf.append("//");
            for (int i = 0; i < otherDisseminations().size(); i++) {
                if (i > 0)
                    buf.append("/");
                buf.append(otherDisseminations().get(i).toString());
            }
        }

        // ADDITIONAL MARKINGS
        // Not a part of the USA Classification markings. Provided to support
        // unpublished marks, and unusual marks that
        // don't fit well - eg: the WAIVED mark available for SAP's.
        if (!additionalMarkings().isEmpty()) {
            buf.append("//");
            for (int i = 0; i < additionalMarkings().size(); i++) {
                if (i > 0)
                    buf.append("/");
                buf.append(additionalMarkings().get(i));
            }
        }

        return buf.toString();
    }
}

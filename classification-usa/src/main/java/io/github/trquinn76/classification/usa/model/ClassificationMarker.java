package io.github.trquinn76.classification.usa.model;

import java.util.List;
import java.util.Objects;

// Classification//SCI//SAP//AEA//FGI//DISSEM//OTHER DISSEM
public record ClassificationMarker(NonUSAndJointClassificationModifier classificationModifier,
        Classification classification,
        List<SCIControlSystem> sensitiveCompartmentInformationControlSystems,
        List<SARProgram> specialAccessPrograms,
        AtomicEnergyActInformationMarker atomicEnergyInformationMarker,
        ForeignGovernmentInformationMarker foreignGovernmentInformationMarker,
        List<DisseminationControlMarker> disseminations,
        List<OtherDisseminationControlMarker> otherDisseminations,
        List<String> additionalMarkings) {
    
    public ClassificationMarker {
        Objects.requireNonNull(classification);
        Objects.requireNonNull(sensitiveCompartmentInformationControlSystems);
        Objects.requireNonNull(specialAccessPrograms);
        Objects.requireNonNull(disseminations);
        Objects.requireNonNull(otherDisseminations);
        Objects.requireNonNull(additionalMarkings);
        
        sensitiveCompartmentInformationControlSystems = List.copyOf(sensitiveCompartmentInformationControlSystems);
        specialAccessPrograms = List.copyOf(specialAccessPrograms);
        disseminations = List.copyOf(disseminations);
        otherDisseminations = List.copyOf(otherDisseminations);
        additionalMarkings = List.copyOf(additionalMarkings);
    }
    
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
                // if there is a special NATO mark (ie: BOHEMIA, ATOMAL, etc) the NATO part is excluded.
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
        if (!sensitiveCompartmentInformationControlSystems().isEmpty()) {
            buf.append("//");
            for (int i = 0; i < sensitiveCompartmentInformationControlSystems().size(); i++) {
                if (i > 0) buf.append("/");
                buf.append(sensitiveCompartmentInformationControlSystems().get(i).toString());
            }
        }
        
        // SAP
        // Does not display "MULTIPLE PROGRAMS" when there is 3 or more SAP's. Always includes all SAP's.
        // If a SAP is Waived, add "WAIVED" to the Additional Markings section.
        if (!specialAccessPrograms().isEmpty()) {
            buf.append("//");
            for (int i = 0; i < specialAccessPrograms().size(); i++) {
                if (i > 0) buf.append("/");
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
                if (i > 0) buf.append("/");
                buf.append(disseminations().get(i).toString());
            }
        }
        
        // OTHER DISSEM
        if (!otherDisseminations().isEmpty()) {
            buf.append("//");
            for (int i = 0; i < otherDisseminations().size(); i++) {
                if (i > 0) buf.append("/");
                buf.append(otherDisseminations().get(i).toString());
            }
        }
        
        // ADDITIONAL MARKINGS
        // Not a part of the USA Classification markings. Provided to support unpublished marks, and unusual marks that
        // don't fit well - eg: the WAIVED mark available for SAP's.
        if (!additionalMarkings().isEmpty()) {
            buf.append("//");
            for (int i = 0; i < additionalMarkings().size(); i++) {
                if (i > 0) buf.append("/");
                buf.append(additionalMarkings().get(i));
            }
        }
        
        return buf.toString();
    }
}

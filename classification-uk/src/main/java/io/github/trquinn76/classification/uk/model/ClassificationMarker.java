package io.github.trquinn76.classification.uk.model;

import java.util.List;
import java.util.Objects;

import io.github.trquinn76.classification.uk.ClassificationConfig;
import io.github.trquinn76.classification.uk.Utils;

public record ClassificationMarker(boolean ukPrefix, Classification classification, boolean sensitive,
        List<String> handlingInstructions, List<String> descriptors, List<String> codeWords, List<String> eyesOnly,
        List<String> additionalInstructions) {

    public ClassificationMarker {
        Objects.requireNonNull(classification);
        Objects.requireNonNull(handlingInstructions);
        Objects.requireNonNull(descriptors);
        Objects.requireNonNull(codeWords);
        Objects.requireNonNull(eyesOnly);
        Objects.requireNonNull(additionalInstructions);
        
        handlingInstructions = List.copyOf(handlingInstructions);
        descriptors = List.copyOf(descriptors);
        codeWords = List.copyOf(codeWords);
        eyesOnly = List.copyOf(eyesOnly);
        additionalInstructions = List.copyOf(additionalInstructions);
    }
    
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        if (ukPrefix()) {
            buf.append("UK ");
        }
        
        buf.append(classification().toString());
        
        if (sensitive()) {
            buf.append(ClassificationConfig.sensitiveMark());
        }
        
        if (!handlingInstructions().isEmpty()) {
            buf.append(" -");
            for (String instruction : handlingInstructions()) {
                if (Utils.endsInOrganisationUseOnly(instruction)) {
                    instruction = instruction.replace(",", "");
                }
                buf.append(" ").append(instruction);
            }
        }
        
        if (!descriptors().isEmpty()) {
            buf.append(" -");
            for (String descriptor : descriptors()) {
                buf.append(" ").append(descriptor);
            }
        }
        
        if (!codeWords().isEmpty()) {
            buf.append(" -");
            for (String codeWord : codeWords()) {
                buf.append(" ").append(codeWord);
            }
        }
        
        if (!eyesOnly().isEmpty()) {
            buf.append(" - ");
            StringBuilder eyesOnlyBuf = new StringBuilder();
            for (String country : eyesOnly()) {
                if (!eyesOnlyBuf.isEmpty()) {
                    eyesOnlyBuf.append("/");
                }
                eyesOnlyBuf.append(country);
            }
            buf.append(eyesOnlyBuf.toString()).append(" EYES ONLY");
        }
        
        if (!additionalInstructions().isEmpty()) {
            for (String instruction : additionalInstructions()) {
                buf.append("\n").append(instruction);
            }
        }
        
        return buf.toString();
    }
}

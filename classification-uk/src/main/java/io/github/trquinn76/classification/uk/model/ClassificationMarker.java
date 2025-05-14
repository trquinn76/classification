package io.github.trquinn76.classification.uk.model;

import java.util.List;
import java.util.Objects;

import io.github.trquinn76.classification.uk.ClassificationConfig;
import io.github.trquinn76.classification.uk.Utils;

/**
 * {@link ClassificationMarker} represents Classifications as defined in the UK
 * Government Security Classification Policy.
 * 
 * A {@link ClassificationMarker} is made of several parts.
 * <ul>
 * <li>UK Prefix - a boolean used to indicate if the UK Prefix should be
 * included.</li>
 * <li>{@link Classification} - the classification, either a
 * {@link SecurityClassification} or a {@link DevelopmentClassification}
 * depending on configuration.</li>
 * <li>Sensitive Marker - a boolean which when combined with an OFFICIAL
 * Classification indicates an OFFICIAL-SENSITIVE mark.</li>
 * <li>Handling Instructions - a list of handling instruction Strings.</li>
 * <li>Descriptors - a list of descriptor Strings.</li>
 * <li>Code Words - a list of code word/compartment Strings.</li>
 * <li>Eyes Only - a list of counties/organisations to which the marked data may
 * be shown.</li>
 * <li>Additional Instructions - a list of additional instructions which do not
 * fit in existing marks.</li>
 * </ul>
 * 
 * @param ukPrefix               a boolean used to indicate if the UK Prefix
 *                               should be included.
 * @param classification         the {@link Classification} to set. May not be
 *                               null.
 * @param sensitive              a boolean used to indicate that the
 *                               Classification is SENSITIVE.
 * @param handlingInstructions   a list of Handling Instructions. May be empty.
 *                               May not be null.
 * @param descriptors            a list of Descriptors. May be empty. May not be
 *                               null.
 * @param codeWords              a list of code words/compartments. May be
 *                               empty. May not be null.
 * @param eyesOnly               a list of countries/organisations to which the
 *                               marked data may be shown. May be empty. May not
 *                               be null.
 * @param additionalInstructions a list of additional instructions which do not
 *                               fit in exiting marking.
 */
public record ClassificationMarker(boolean ukPrefix, Classification classification, boolean sensitive,
        List<String> handlingInstructions, List<String> descriptors, List<String> codeWords, List<String> eyesOnly,
        List<String> additionalInstructions) {

    /**
     * A Constructor which ensures parameters are not null, and performs defensive
     * list copying.
     * 
     * @param ukPrefix               a boolean used to indicate if the UK Prefix
     *                               should be included.
     * @param classification         the {@link Classification} to set. May not be
     *                               null.
     * @param sensitive              a boolean used to indicate that the
     *                               Classification is SENSITIVE.
     * @param handlingInstructions   a list of Handling Instructions. May be empty.
     *                               May not be null.
     * @param descriptors            a list of Descriptors. May be empty. May not be
     *                               null.
     * @param codeWords              a list of code words/compartments. May be
     *                               empty. May not be null.
     * @param eyesOnly               a list of countries/organisations to which the
     *                               marked data may be shown. May be empty. May not
     *                               be null.
     * @param additionalInstructions a list of additional instructions which do not
     *                               fit in exiting marking.
     */
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

    /**
     * Overridden to provide a good human readable String value.
     */
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

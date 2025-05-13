package io.github.trquinn76.classification.uk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import io.github.trquinn76.classification.uk.model.Classification;
import io.github.trquinn76.classification.uk.model.ClassificationMarker;

public class ClassificationMarkerBuilder {
    
    private static final Logger LOGGER = Logger.getLogger(ClassificationMarkerBuilder.class.getCanonicalName());

    private boolean ukPrefix = false;
    private Classification classification = null;
    private boolean sensitive = false;
    private Set<String> handlingInstructions = new TreeSet<>();
    private Set<String> useOnlyOrganisations = new TreeSet<>();
    private Set<String> descriptors = new TreeSet<>();
    private Set<String> codeWords = new TreeSet<>();
    private Set<String> eyesOnly = new TreeSet<>(ClassificationConfig.eyesOnlyOrder());
    private Set<String> additionalInstructions = new TreeSet<>();
    
    public ClassificationMarkerBuilder() {
        
    }
    
    public ClassificationMarkerBuilder(ClassificationMarker classificationMarker) {
        this.ukPrefix = classificationMarker.ukPrefix();
        this.classification = classificationMarker.classification();
        this.sensitive = classificationMarker.sensitive();
        this.descriptors.addAll(classificationMarker.descriptors());
        this.codeWords.addAll(classificationMarker.codeWords());
        this.eyesOnly.addAll(classificationMarker.eyesOnly());
        this.additionalInstructions.addAll(classificationMarker.additionalInstructions());
        
        for (String handlingInstruction : classificationMarker.handlingInstructions()) {
            if (Utils.endsInOrganisationUseOnly(handlingInstruction)) {
                String str = handlingInstruction.substring(0, handlingInstruction.length() - Utils.USE_ONLY.length()).trim();
                String[] organisations = str.split(", ");
                this.useOnly(organisations);
            }
            else {
                this.handlingInstructions.add(handlingInstruction);
            }
        }
    }
    
    public ClassificationMarkerBuilder setUkPrefix(boolean value) {
        this.ukPrefix = value;
        return this;
    }
    
    public ClassificationMarkerBuilder ukPrefix() {
        return setUkPrefix(true);
    }
    
    public boolean getUkPrefix() {
        return this.ukPrefix;
    }
    
    public ClassificationMarkerBuilder addRelEu() {
        this.additionalInstructions.add(Utils.REL_EU);
        return this;
    }
    
    public ClassificationMarkerBuilder removeRelEu() {
        this.additionalInstructions.remove(Utils.REL_EU);
        return this;
    }
    
    public ClassificationMarkerBuilder ukPrefixWithRelEU() {
        this.ukPrefix = true;
        return addRelEu();
    }
    
    public ClassificationMarkerBuilder clearUkPrefix() {
        setUkPrefix(false);
        this.additionalInstructions.remove(Utils.REL_EU);
        return this;
    }
    
    public ClassificationMarkerBuilder setClassification(Classification classification) {
        Objects.requireNonNull(classification);
        this.classification = classification;
        return this;
    }
    
    public Classification getClassification() {
        return this.classification;
    }
    
    public ClassificationMarkerBuilder official() {
        this.classification = Classification.official();
        this.sensitive = false;
        return this;
    }
    
    public ClassificationMarkerBuilder officialSensitive() {
        this.classification = Classification.official();
        this.sensitive = true;
        return this;
    }
    
    public ClassificationMarkerBuilder secret() {
        this.classification = Classification.secret();
        this.sensitive = false;
        return this;
    }
    
    public ClassificationMarkerBuilder topSecret() {
        this.classification = Classification.topSecret();
        this.sensitive = false;
        return this;
    }
    
    public ClassificationMarkerBuilder setSensitiveMark(boolean sensitive) {
        this.sensitive = sensitive;
        return this;
    }
    
    public boolean getSensitiveMark() {
        return this.sensitive;
    }
    
    public ClassificationMarkerBuilder clearClassification() {
        this.classification = null;
        this.sensitive = false;
        return this;
    }
    
    public ClassificationMarkerBuilder setHandlingInstructions(Collection<String> handlingInstructions) {
        return setHandlingInstructions(handlingInstructions, null);
    }
    
    public ClassificationMarkerBuilder setHandlingInstructions(Collection<String> handlingInstructions, Collection<String> useOnlyOrganisations) {
        Objects.requireNonNull(handlingInstructions);
        clearHandlingInstructions();
        this.handlingInstructions.addAll(handlingInstructions);
        if (useOnlyOrganisations != null) {
            this.useOnlyOrganisations.addAll(useOnlyOrganisations);
        }
        return this;
    }
    
    public ClassificationMarkerBuilder addHandlingInstruction(String instruction) {
        this.handlingInstructions.add(instruction);
        return this;
    }
    
    public Set<String> getHandlingInstructions() {
        return new TreeSet<>(this.handlingInstructions);
    }
    
    public ClassificationMarkerBuilder clearHandlingInstructions() {
        this.handlingInstructions.clear();
        return clearUseOnlyOrganisations();
    }
    
    public ClassificationMarkerBuilder setUseOnlyOrganisations(Collection<String> organisations) {
        Objects.requireNonNull(organisations);
        this.useOnlyOrganisations.clear();
        this.useOnlyOrganisations.addAll(organisations);
        return this;
    }
    
    public ClassificationMarkerBuilder addUseOnlyOrganisation(String organisation) {
        this.useOnlyOrganisations.add(organisation);
        return this;
    }
    
    public Set<String> getUseOnlyOrganisations() {
        return new TreeSet<>(this.useOnlyOrganisations);
    }
    
    public ClassificationMarkerBuilder clearUseOnlyOrganisations() {
        this.useOnlyOrganisations.clear();
        return this;
    }
    
    public ClassificationMarkerBuilder recipientsOnly() {
        return addHandlingInstruction(Utils.RECIPIENTS_ONLY);
    }
    
    public ClassificationMarkerBuilder forPublicRelease() {
        return addHandlingInstruction(Utils.FOR_PUBLIC_RELEASE);
    }
    
    public ClassificationMarkerBuilder useOnly(String... organisations) {
        Objects.requireNonNull(organisations);
        if (organisations.length < 1) {
            throw new IllegalArgumentException("When using Organisation Use Only Handler, there must be a minimum of one Organisation.");
        }
        addHandlingInstruction(Utils.USE_ONLY);
        return setUseOnlyOrganisations(Arrays.asList(organisations));
    }
    
    public ClassificationMarkerBuilder hmgUseOnly() {
        return addHandlingInstruction(Utils.HMG_USE_ONLY);
    }
    
    public ClassificationMarkerBuilder embargoed() {
        return addHandlingInstruction(Utils.EMBARGOED);
    }
    
    public ClassificationMarkerBuilder setDescriptors(Collection<String> descriptors) {
        clearDescriptors();
        this.descriptors.addAll(descriptors);
        return this;
    }
    
    public ClassificationMarkerBuilder addDescriptor(String descriptor) {
        this.descriptors.add(descriptor);
        return this;
    }
    
    public Set<String> getDescriptors() {
        return new TreeSet<>(this.descriptors);
    }
    
    public ClassificationMarkerBuilder clearDescriptors() {
        this.descriptors.clear();
        return this;
    }
    
    public ClassificationMarkerBuilder personalData() {
        return addDescriptor(Utils.PERSONAL_DATA);
    }
    
    public ClassificationMarkerBuilder legalProfessionalPrivilege() {
        return addDescriptor(Utils.LEGAL_PROFESSIONAL_PRIVILEGE);
    }
    
    public ClassificationMarkerBuilder legal() {
        return addDescriptor(Utils.LEGAL);
    }
    
    public ClassificationMarkerBuilder marketSensitive() {
        return addDescriptor(Utils.MARKET_SENSITIVE);
    }
    
    public ClassificationMarkerBuilder commercial() {
        return addDescriptor(Utils.COMMERCIAL);
    }
    
    public ClassificationMarkerBuilder hrManagement() {
        return addDescriptor(Utils.HR_MANAGEMENT);
    }
    
    public ClassificationMarkerBuilder setCodeWords(Collection<String> codeWords) {
        clearCodeWords();
        this.codeWords.addAll(codeWords);
        return this;
    }
    
    public ClassificationMarkerBuilder addCodeWord(String codeWord) {
        this.codeWords.add(codeWord);
        return this;
    }
    
    public Set<String> getCodeWords() {
        return new TreeSet<>(this.codeWords);
    }
    
    public ClassificationMarkerBuilder clearCodeWords() {
        this.codeWords.clear();
        return this;
    }
    
    public ClassificationMarkerBuilder codeWords(String... codeWords) {
        Objects.requireNonNull(codeWords);
        return setCodeWords(Arrays.asList(codeWords));
    }
    
    public ClassificationMarkerBuilder setEyesOnly(Collection<String> eyesOnlyList) {
        clearEyesOnly();
        this.eyesOnly.addAll(eyesOnlyList);
        return this;
    }
    
    public ClassificationMarkerBuilder addEyesOnlyCountry(String country) {
        this.eyesOnly.add(country);
        return this;
    }
    
    public Set<String> getEyesOnlyCountries() {
        Set<String> retSet = new TreeSet<>(ClassificationConfig.eyesOnlyOrder());
        retSet.addAll(this.eyesOnly);
        return retSet;
    }
    
    public ClassificationMarkerBuilder clearEyesOnly() {
        this.eyesOnly.clear();
        return this;
    }
    
    public ClassificationMarkerBuilder eyesOnly(String... countries) {
        Objects.requireNonNull(countries);
        return setEyesOnly(Arrays.asList(countries));
    }
    
    public ClassificationMarkerBuilder ukEyesOnly() {
        return eyesOnly(Utils.UK);
    }
    
    public ClassificationMarkerBuilder fiveEyesOnly() {
        return eyesOnly(Utils.FIVE);
    }
    
    public ClassificationMarkerBuilder setAdditionalInstructions(Collection<String> instructions) {
        Objects.requireNonNull(instructions);
        clearAdditionalInstructions();
        this.additionalInstructions.addAll(instructions);
        return this;
    }
    
    public ClassificationMarkerBuilder addAdditionalInstruction(String instruction) {
        this.additionalInstructions.add(instruction);
        return this;
    }
    
    public Set<String> getAdditionalInstructions() {
        return new TreeSet<>(this.additionalInstructions);
    }
    
    public ClassificationMarkerBuilder clearAdditionalInstructions() {
        this.additionalInstructions.clear();
        return this;
    }
    
    public ClassificationMarkerBuilder additionalInstructions(String... instructions) {
        return setAdditionalInstructions(Arrays.asList(instructions));
    }
    
    public ClassificationMarkerBuilder clear() {
        clearUkPrefix();
        clearClassification();
        clearHandlingInstructions();
        clearDescriptors();
        clearCodeWords();
        clearEyesOnly();
        clearAdditionalInstructions();
        return this;
    }
    
    public List<String> isValid() {
        List<String> report = new ArrayList<>();
        
        checkUkPrefix(report);
        checkClassificationValid(report);
        checkHandlingInstructionsValid(report);
        checkDescriptorsValid(report);
        checkNationalCaveats(report);
        
        return report;
    }
    
    public ClassificationMarker build() {
        List<String> report = isValid();

        if (report.size() > 0) {
            LOGGER.severe("Do not have valid values to build a ClassificationMarker.");
            for (String line: report) {
                LOGGER.severe(line);
            }
            throw new IllegalStateException("Invalid state, cannot build Classification Marker.");
        }
        
        Set<String> handlingInstructions = new TreeSet<>(this.handlingInstructions);
        if (handlingInstructions.contains(Utils.USE_ONLY)) {
            handlingInstructions.remove(Utils.USE_ONLY);
            StringBuilder buf = new StringBuilder();
            for (String organisation : this.useOnlyOrganisations) {
                if (buf.length() > 0) {
                    buf.append(", ");
                }
                buf.append(organisation);
            }
            buf.append(" ").append(Utils.USE_ONLY);
            handlingInstructions.add(buf.toString().trim());
        }
        
        return new ClassificationMarker(this.ukPrefix, this.classification, this.sensitive,
                List.copyOf(handlingInstructions), List.copyOf(this.descriptors), List.copyOf(this.codeWords),
                List.copyOf(this.eyesOnly), List.copyOf(this.additionalInstructions));
    }
    
    private void checkUkPrefix(List<String> report) {
        if (this.additionalInstructions.contains(Utils.REL_EU) && !this.ukPrefix) {
            report.add("May not have the " + Utils.REL_EU + " marker if the UK Prefix is not set.");
        }
    }
    
    private void checkClassificationValid(List<String> report) {
        if (this.classification == null) {
            report.add("Classification must be set.");
        } else if (this.sensitive && !Classification.official().equals(this.classification)) {
            StringBuilder buf = new StringBuilder();
            buf.append("The SENSITIVE mark is only permitted when the Classification is '")
                    .append(Classification.official()).append("'. Current Classification is: '")
                    .append(this.classification).append("'.");
            report.add(buf.toString());
        }
    }
    
    private void checkHandlingInstructionsValid(List<String> report) {
        if (this.handlingInstructions.contains(Utils.RECIPIENTS_ONLY) && Classification.official().equals(this.classification) && !this.sensitive) {
            StringBuilder buf = new StringBuilder();
            buf.append(Utils.RECIPIENTS_ONLY).append(" Handling Instruction may not be used with '")
                .append(Classification.official())
                .append("' Classification without the SENSITVE mark");
            report.add(buf.toString());
        }
        
        if (this.handlingInstructions.contains(Utils.FOR_PUBLIC_RELEASE)) {
            if (!Classification.official().equals(this.classification)) {
                StringBuilder buf = new StringBuilder();
                buf.append("May only use Handling Instruction ").append(Utils.FOR_PUBLIC_RELEASE).append(" with '")
                    .append(Classification.official()).append("' Classification. It may not be used with: '")
                    .append(this.classification.toString()).append("'.");
                report.add(buf.toString());
            }
            else if (Classification.official().equals(this.classification) && this.sensitive) {
                StringBuilder buf = new StringBuilder();
                buf.append("May only use Handling Instruction ").append(Utils.FOR_PUBLIC_RELEASE).append(" with '")
                    .append(Classification.official()).append("' Classification without the SENSITIVE mark.");
                report.add(buf.toString());
            }
        }
        
        if (this.handlingInstructions.contains(Utils.USE_ONLY) && this.useOnlyOrganisations.isEmpty()) {
            report.add("When the [INSERT ORGANISATION(S) NAME] USE ONLY Handling Instruction is used, there must be at least one 'Use Only Organisation' defined.");
        }
        
        if (this.handlingInstructions.contains(Utils.HMG_USE_ONLY) && !Classification.official().equals(this.classification)) {
            StringBuilder buf = new StringBuilder();
            buf.append("May only use ").append(Utils.HMG_USE_ONLY).append(" Handling Instruction with '")
                .append(Classification.official()).append("' Classification. It may not be used with: '")
                .append(this.classification.toString()).append("'.");
            report.add(buf.toString());
        }
        
        if (this.handlingInstructions.contains(Utils.EMBARGOED) && Classification.topSecret().equals(this.classification)) {
            report.add("May not use the " + Utils.EMBARGOED + " Handling Instruction with the Classification '" + Classification.topSecret() + "'.");
        }
    }
    
    private void checkDescriptorsValid(List<String> report) {
        if (this.descriptors.contains(Utils.LEGAL) && !Classification.official().equals(this.classification)) {
            StringBuilder buf = new StringBuilder();
            buf.append("May only use ").append(Utils.LEGAL).append(" Handling Instruction when the Classification is '")
                .append(Classification.official()).append("'. It may not be used with: '")
                .append(this.classification).append("'.");
            report.add(buf.toString());
        }
    }
    
    private void checkNationalCaveats(List<String> report) {
        if (!this.eyesOnly.isEmpty()) {
            if (!Set.of(Classification.secret(), Classification.topSecret()).contains(this.classification)) {
                StringBuilder buf = new StringBuilder();
                buf.append("National/Eyes Only Caveats may only be added to '").append(Classification.secret())
                    .append("' and '").append(Classification.topSecret())
                    .append("' Classifications. They may not be used with '").append(this.classification).append("'.");
                report.add(buf.toString());
            }
            if (!this.eyesOnly.contains(Utils.UK) && !this.eyesOnly.contains(Utils.FIVE)) {
                StringBuilder buf = new StringBuilder();
                buf.append("When Eyes Only Caveats are present, the list must contain either ")
                    .append(Utils.UK).append(" or ").append(Utils.FIVE).append(".");
                report.add(buf.toString());
            }
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(additionalInstructions, classification, codeWords, descriptors, eyesOnly,
                handlingInstructions, sensitive, ukPrefix, useOnlyOrganisations);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ClassificationMarkerBuilder other = (ClassificationMarkerBuilder) obj;
        return Objects.equals(additionalInstructions, other.additionalInstructions)
                && Objects.equals(classification, other.classification) && Objects.equals(codeWords, other.codeWords)
                && Objects.equals(descriptors, other.descriptors) && Objects.equals(eyesOnly, other.eyesOnly)
                && Objects.equals(handlingInstructions, other.handlingInstructions) && sensitive == other.sensitive
                && ukPrefix == other.ukPrefix && Objects.equals(useOnlyOrganisations, other.useOnlyOrganisations);
    }
}

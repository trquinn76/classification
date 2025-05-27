package io.github.trquinn76.classification.usa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import io.github.trquinn76.classification.usa.model.Classification;
import io.github.trquinn76.classification.usa.model.ClassificationMarker;
import io.github.trquinn76.classification.usa.subbuilders.AtomicEnergyActInformationBuilder;
import io.github.trquinn76.classification.usa.subbuilders.ClassificationModifierBuilder;
import io.github.trquinn76.classification.usa.subbuilders.DisseminationControlsBuilder;
import io.github.trquinn76.classification.usa.subbuilders.FGIBuilder;
import io.github.trquinn76.classification.usa.subbuilders.OtherDisseminationControlsBuilder;
import io.github.trquinn76.classification.usa.subbuilders.SAPBuilder;
import io.github.trquinn76.classification.usa.subbuilders.SCIBuilder;

public class ClassificationMarkerBuilder {

    public final ClassificationModifierBuilder modifier;
    public final SCIBuilder sci;
    public final SAPBuilder sap;
    public final AtomicEnergyActInformationBuilder aea;
    public final FGIBuilder fgi;
    public final DisseminationControlsBuilder disseminations;
    public final OtherDisseminationControlsBuilder otherDisseminations;
    
    // Classification
    private Classification classification = null;
    // Additional Markings
    private Set<String> additionMarkings = new TreeSet<>();
    
    public ClassificationMarkerBuilder() {
        this.modifier = new ClassificationModifierBuilder(this);
        this.sci = new SCIBuilder(this);
        this.sap = new SAPBuilder(this);
        this.aea = new AtomicEnergyActInformationBuilder(this);
        this.fgi = new FGIBuilder(this);
        this.disseminations = new DisseminationControlsBuilder(this);
        this.otherDisseminations = new OtherDisseminationControlsBuilder(this);
    }
    
    public ClassificationMarkerBuilder(ClassificationMarker marker) {
        this();
        modifier.populate(marker.classificationModifier());
        sci.populate(marker.sensitiveCompartmentInformationControlSystems());
        sap.populate(marker.specialAccessPrograms());
        aea.populate(marker.atomicEnergyInformationMarker());
        fgi.populate(marker.foreignGovernmentInformationMarker());
        disseminations.populate(marker.disseminations());
        otherDisseminations.populate(marker.otherDisseminations());
    }
    
    public ClassificationMarkerBuilder setClassification(Classification classification) {
        Objects.requireNonNull(classification);
        this.classification = classification;
        return this;
    }
    
    public Classification getClassification() {
        return this.classification;
    }
    
    public ClassificationMarkerBuilder unclassified() {
        return this.setClassification(Classification.unclassified());
    }
    
    public ClassificationMarkerBuilder restricted() {
        return this.setClassification(Classification.restricted());
    }
    
    public ClassificationMarkerBuilder confidential() {
        return this.setClassification(Classification.confidential());
    }
    
    public ClassificationMarkerBuilder secret() {
        return this.setClassification(Classification.secret());
    }
    
    public ClassificationMarkerBuilder topSecret() {
        return this.setClassification(Classification.topSecret());
    }
    
    public ClassificationMarkerBuilder setAdditionalMarkings(Collection<String> additionalMarkings) {
        this.additionMarkings.addAll(additionalMarkings);
        return this;
    }
    
    public ClassificationMarkerBuilder addAdditionMarking(String str) {
        this.additionMarkings.add(str);
        return this;
    }
    
    public ClassificationMarkerBuilder removeAdditionalMarking(String str) {
        this.additionMarkings.remove(str);
        return this;
    }
    
    public ClassificationMarkerBuilder clearAdditionMarkings() {
        this.additionMarkings.clear();
        return this;
    }
    
    public ClassificationMarkerBuilder clear() {
        this.classification = null;
        this.clearAdditionMarkings();
        
        this.modifier.clear();
        this.sci.clear();
        this.sap.clear();
        this.aea.clear();
        this.fgi.clear();
        this.disseminations.clear();
        this.otherDisseminations.clear();
        
        return this;
    }
    
    public List<String> isValid() {
        List<String> report = new ArrayList<>();
        
        report.addAll(this.modifier.isValid());
        report.addAll(this.sci.isValid());
        report.addAll(this.sap.isValid());
        report.addAll(this.aea.isValid());
        report.addAll(this.fgi.isValid());
        report.addAll(this.disseminations.isValid());
        report.addAll(this.otherDisseminations.isValid());
        
        return report;
    }
}

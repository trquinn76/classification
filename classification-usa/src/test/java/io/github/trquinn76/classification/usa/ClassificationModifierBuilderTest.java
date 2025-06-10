package io.github.trquinn76.classification.usa;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import io.github.trquinn76.classification.usa.model.Classification;
import io.github.trquinn76.classification.usa.model.NonUSAndJointType;

class ClassificationModifierBuilderTest {

    @Test
    void cosmicMustBeTopSecret() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.topSecret().modifier.cosmic();
        assertTrue(builder.isValid().isEmpty());
        
        builder.secret();
        assertFalse(builder.isValid().isEmpty());
        
        builder.confidential();
        assertFalse(builder.isValid().isEmpty());
        
        builder.setClassification(Classification.restricted());
        assertFalse(builder.isValid().isEmpty());
        
        builder.unclassified();
        assertFalse(builder.isValid().isEmpty());
    }
    
    @Test
    void natoMayNotBeTopSecret() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.topSecret().modifier.nato();
        assertFalse(builder.isValid().isEmpty());
        
        builder.secret();
        assertTrue(builder.isValid().isEmpty());
        
        builder.confidential();
        assertTrue(builder.isValid().isEmpty());
        
        builder.setClassification(Classification.restricted());
        assertTrue(builder.isValid().isEmpty());
        
        builder.unclassified();
        assertTrue(builder.isValid().isEmpty());
    }
    
    @Test
    void noCountryListWithCosmic() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.topSecret().modifier.cosmic().modifier.addJointOrForeignCountry("AAA");
        assertFalse(builder.isValid().isEmpty());
    }
    
    @Test
    void noCountryListWithNato() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.secret().modifier.nato().modifier.addJointOrForeignCountry("AAA");
        assertFalse(builder.isValid().isEmpty());
    }
    
    @Test
    void boheamiaMustBeCosmicTopSecret() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.topSecret().modifier.cosmic().modifier.boheamia();
        assertTrue(builder.isValid().isEmpty());
        
        builder.secret();
        assertFalse(builder.isValid().isEmpty());
        
        builder.modifier.nato();
        assertFalse(builder.isValid().isEmpty());
    }
    
    @Test
    void atomalMustBeAtLeastConfidential() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.topSecret().modifier.cosmic().modifier.atomal();
        assertTrue(builder.isValid().isEmpty());
        
        builder.secret().modifier.nato();
        assertTrue(builder.isValid().isEmpty());
        
        builder.confidential();
        assertTrue(builder.isValid().isEmpty());
        
        builder.setClassification(Classification.restricted());
        assertFalse(builder.isValid().isEmpty());
        
        builder.unclassified();
        assertFalse(builder.isValid().isEmpty());
    }
    
    @Test
    void cosmicMustNotHaveNoforn() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.topSecret().modifier.cosmic().disseminations.noforn();
        assertFalse(builder.isValid().isEmpty());
        
        builder.disseminations.clear();
        assertTrue(builder.isValid().isEmpty());
    }
    
    @Test
    void natoMustNotHaveNoforn() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.secret().modifier.nato().disseminations.noforn();
        assertFalse(builder.isValid().isEmpty());
        
        builder.disseminations.clear();
        assertTrue(builder.isValid().isEmpty());
    }
    
    @Test
    void foreignAllowsRestricted() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.modifier.foreign("AAA").unclassified();
        assertTrue(builder.isValid().isEmpty());
        
        builder.setClassification(Classification.restricted());
        assertTrue(builder.isValid().isEmpty());
        
        builder.confidential();
        assertTrue(builder.isValid().isEmpty());
        
        builder.secret();
        assertTrue(builder.isValid().isEmpty());
        
        builder.topSecret();
        assertTrue(builder.isValid().isEmpty());
    }
    
    @Test
    void foreignCountryListMustBeLengthOne() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.confidential().modifier.setNonUsAndJointType(NonUSAndJointType.FOREIGN);
        // country list length 0
        assertFalse(builder.isValid().isEmpty());
        
        // country list length 1
        builder.modifier.addJointOrForeignCountry("AAA");
        assertTrue(builder.isValid().isEmpty());
        
        // country list length 2
        builder.modifier.addJointOrForeignCountry("BBB");
        assertFalse(builder.isValid().isEmpty());
    }
    
    @Test
    void foreignMayNoHaveNatoMark() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.secret().modifier.foreign("AAA").modifier.setNatoSpecialMark("NATO Mark");
        assertFalse(builder.isValid().isEmpty());
    }
    
    @Test
    void foreignMayNotHaveNoforn() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.secret().modifier.foreign("AAA").disseminations.noforn();
        assertFalse(builder.isValid().isEmpty());
    }
    
    @Test
    void jointTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.unclassified().modifier.joint("USA", "AAA", "BBB");
        assertTrue(builder.isValid().isEmpty());
    }
    
    @Test
    void jointNotAllowingRestricted() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.setClassification(Classification.restricted()).modifier.joint("USA", "AAA");
        assertFalse(builder.isValid().isEmpty());
    }

    @Test
    void jointMustHaveUsaTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.unclassified().modifier.joint("AAA", "BBB");
        assertFalse(builder.isValid().isEmpty());
    }
    
    @Test
    void jointMustHaveMinimumTwoCountries() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.unclassified().modifier.joint("USA");
        assertFalse(builder.isValid().isEmpty());
        builder.modifier.addJointOrForeignCountry("AAA");
        assertTrue(builder.isValid().isEmpty());
    }
    
    @Test
    void jointMayNotHaveNatoMark() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.secret().modifier.joint("USA", "AAA").modifier.atomal();
        assertFalse(builder.isValid().isEmpty());
        builder.modifier.setNatoSpecialMark(null);
        assertTrue(builder.isValid().isEmpty());
    }
    
    @Test
    void jointMayNotHaveNoforn() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.secret().modifier.joint("USA", "AAA").disseminations.noforn();
        assertFalse(builder.isValid().isEmpty());
    }
}

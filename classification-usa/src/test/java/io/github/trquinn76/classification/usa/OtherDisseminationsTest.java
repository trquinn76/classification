package io.github.trquinn76.classification.usa;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import io.github.trquinn76.classification.usa.model.OtherDisseminationControls;

class OtherDisseminationsTest {

    @Test
    void exclusiveDistributionRequiresClassificationTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.topSecret().otherDisseminations.exclusiveDistribution();
        assertTrue(builder.isValid().isEmpty());
        
        builder.secret();
        assertTrue(builder.isValid().isEmpty());
        
        builder.confidential();
        assertTrue(builder.isValid().isEmpty());
        
        builder.unclassified();
        assertFalse(builder.isValid().isEmpty()); // EXDIS not allowed with UNCLASSIFIED alone.
        
        builder.disseminations.controledUnclassifiedInformation();
        assertTrue(builder.isValid().isEmpty()); // EXDIS allowed with CONTROLLED UNCLASSIFIED INFORMATION.
    }
    
    @Test
    void noDistributionRequiresClassificationTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.topSecret().otherDisseminations.noDistribution("none");
        assertTrue(builder.isValid().isEmpty());
        
        builder.secret();
        assertTrue(builder.isValid().isEmpty());
        
        builder.confidential();
        assertTrue(builder.isValid().isEmpty());
        
        builder.unclassified();
        assertFalse(builder.isValid().isEmpty()); // NODIS not allowed with UNCLASSIFIED alone.
        
        builder.disseminations.controledUnclassifiedInformation();
        assertTrue(builder.isValid().isEmpty()); // NODIS allowed with CONTROLLED UNCLASSIFIED INFORMATION.
    }
    
    @Test
    void exclusiveDistributionMayNotHaveReltoTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.confidential().disseminations.releaseTo("USA", "AAA").otherDisseminations.exclusiveDistribution();
        assertFalse(builder.isValid().isEmpty()); // EXDIS not allowed with RELTO dissemination.
    }
    
    @Test
    void noDistributionMayNotHaveReltoTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.confidential().disseminations.releaseTo("USA", "AAA").otherDisseminations.noDistribution("none");
        assertFalse(builder.isValid().isEmpty()); // NODIS not allowed with RELTO dissemination.
    }
    
    @Test
    void exclusiveAndNoDistributionExclusiveTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.confidential().otherDisseminations.noDistribution("none");
        assertTrue(builder.isValid().isEmpty());
        
        builder.otherDisseminations.exclusiveDistribution();
        assertFalse(builder.isValid().isEmpty());
    }

    @Test
    void sensitiveButUnclassifiedOnlyUnclassifiedTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.confidential().otherDisseminations.sensitiveButUnclassified();
        assertFalse(builder.isValid().isEmpty());
        
        builder.unclassified();
        assertTrue(builder.isValid().isEmpty());
    }
    
    @Test
    void senitiveButUnclassifiedNofornOnlyUnclassified() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.confidential().otherDisseminations.sensitiveButUnclassifiedNoforn();
        assertFalse(builder.isValid().isEmpty());
        
        builder.unclassified();
        assertTrue(builder.isValid().isEmpty());
    }
    
    @Test
    void sensitiveButUnclassifiedPairMutuallyExclusiveTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.confidential().otherDisseminations.sensitiveButUnclassified().otherDisseminations.sensitiveButUnclassifiedNoforn();
        assertFalse(builder.isValid().isEmpty());
    }
    
    @Test
    void accmRequiresNickNameTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.confidential().otherDisseminations.alternativeCompensatoryControlMeasures("nick");
        assertTrue(builder.isValid().isEmpty());
        
        builder.otherDisseminations.removeNickname(OtherDisseminationControls.ACCM, "nick");
        assertFalse(builder.isValid().isEmpty()); // as there are no nicknames now, when at least one is required.
    }
    
    @Test
    void populateTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.confidential().otherDisseminations.exclusiveDistribution();
        
        ClassificationMarkerBuilder otherBuilder = new ClassificationMarkerBuilder(builder.build());
        
        assertEquals(builder.otherDisseminations, otherBuilder.otherDisseminations);
    }
}

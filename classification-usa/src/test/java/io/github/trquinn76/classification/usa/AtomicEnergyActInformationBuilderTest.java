package io.github.trquinn76.classification.usa;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.Test;

import io.github.trquinn76.classification.usa.model.AtomicEnergyActMarkings;

class AtomicEnergyActInformationBuilderTest {

    @Test
    void noSigmaMarksTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.secret().aea.setMark(AtomicEnergyActMarkings.RESTRICTED_DATA);
        assertTrue(builder.isValid().isEmpty());
        
        builder.aea.setMark(AtomicEnergyActMarkings.FORMALLY_RESTRICTED_DATA);
        assertTrue(builder.isValid().isEmpty());
        
        builder.aea.setMark(AtomicEnergyActMarkings.RESTRICTED_DATA_CNWDI);
        assertTrue(builder.isValid().isEmpty());
        
        builder.confidential().aea.setMark(AtomicEnergyActMarkings.TRANSCLASSIFIED_FOREIGN_NUCLEAR_INFORMATION);
        assertTrue(builder.isValid().isEmpty());
        
        builder.unclassified().aea.setMark(AtomicEnergyActMarkings.DOD_UNCLASSIFIED_CONTROLLED_NUCLEAR_INFORMATION);
        assertTrue(builder.isValid().isEmpty());
        
        builder.aea.setMark(AtomicEnergyActMarkings.DOE_UNCLASSIFIED_CONTROLLED_NUCLEAR_INFORMATION);
        assertTrue(builder.isValid().isEmpty());
        
        builder.aea.setSigmaNumbers(Set.of(1, 2));
        assertFalse(builder.isValid().isEmpty());
    }
    
    @Test
    void requiresSigmaValueTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.secret().aea.restrictedDataSigma(1, 2, 3);
        assertTrue(builder.isValid().isEmpty());
        
        builder.aea.formallyRestrictedDataSigma(4, 5, 6);
        assertTrue(builder.isValid().isEmpty());
        
        builder.aea.clearSigmaNumbers();
        assertFalse(builder.isValid().isEmpty());
    }
    
    @Test
    void sigmaNumbersOnly() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.secret().aea.setSigmaNumbers(Set.of(1, 2, 3));
        assertFalse(builder.isValid().isEmpty()); // missing mark.
    }
    
    @Test
    void populateTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.secret().aea.restrictedDataSigma(1, 2, 3);
        
        ClassificationMarkerBuilder otherBuilder = new ClassificationMarkerBuilder(builder.build());
        
        assertEquals(builder.aea, otherBuilder.aea);
    }

}

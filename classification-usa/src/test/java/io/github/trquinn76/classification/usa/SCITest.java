package io.github.trquinn76.classification.usa;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import io.github.trquinn76.classification.usa.model.Classification;

class SCITest {

    @Test
    void passingTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.secret().sci.humintCS("AAA", "BBB").sci.specialIntelligence("CCC", "DDD").sci.talentKeyhole("EEE",
                "FFF").disseminations.noforn();
        assertTrue(builder.isValid().isEmpty());

        String prefix = ClassificationConfig.developmentSciPrefix();
        if (ClassificationConfig.productionMode()) {
            prefix = "";
        }
        String expectedStr = Classification.secret().toString() + "//" + prefix + "HCS-AAA-BBB/" + prefix
                + "SI-CCC-DDD/" + prefix + "TK-EEE-FFF//NOFORN";
        String actualStr = builder.build().toString();

        assertEquals(expectedStr, actualStr);
    }

    @Test
    void hcsRequiresNofornTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.secret().sci.humintCS("AAA");
        assertFalse(builder.isValid().isEmpty());
        
        builder.disseminations.noforn();
        assertTrue(builder.isValid().isEmpty());
    }
    
    @Test
    void tkGeocapRequiresNofornTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.secret().sci.talentKeyhole();
        // TK without GEOCAP or NOFORN passes.
        assertTrue(builder.isValid().isEmpty());
        
        builder.sci.addCompartment(Utils.TALENT_KEYHOLE, Utils.GEOCAP);
        // TK with GEOCAP but without NOFORN fails.
        assertFalse(builder.isValid().isEmpty());
        
        builder.disseminations.noforn();
        // TK with GEOCAP and NOFORN passes.
        assertTrue(builder.isValid().isEmpty());
    }
    
    @Test
    void populateTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.secret().sci.addSubCompartment("AAACS", "BBBCompartment", "CCCSubcompartment").sci.addSubCompartment("AAACS", "BBBCompartment", "DDDSubCompartment").sci.addCompartment("ZZZCS", "YYYCompartment");
        
        ClassificationMarkerBuilder otherBuilder = new ClassificationMarkerBuilder(builder.build());
        
        assertEquals(builder.sci, otherBuilder.sci);
    }
}

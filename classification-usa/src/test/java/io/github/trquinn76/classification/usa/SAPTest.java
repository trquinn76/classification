package io.github.trquinn76.classification.usa;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import io.github.trquinn76.classification.usa.model.Classification;
import io.github.trquinn76.classification.usa.model.Disseminations;

class SAPTest {

    @Test
    void passingTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.secret();
        builder.sap.addControlSystem("CS");
        builder.sap.addCompartment("CS", "AA");
        builder.sap.addCompartment("BA", "BB");
        builder.sap.addSubCompartment("CS", "AA", "SubOne");
        builder.sap.addSubCompartment("CS", "AA", "SubTwo");
        builder.sap.addSubCompartment("DDD", "DCD", "DBD");
        
        assertTrue(builder.isValid().isEmpty());
        
        String prefix = ClassificationConfig.developmentSapPrefix();
        if (ClassificationConfig.productionMode()) {
            prefix = "";
        }
        String expectedStr = Classification.secret().toString() + "//SAR-" + prefix + "BA-BB/" + prefix
                + "CS-AA SubOne SubTwo/" + prefix + "DDD-DCD DBD";
        String actualStr = builder.build().toString();

        assertEquals(expectedStr, actualStr);
    }
    
    @Test
    void waivedFunctionSetsDisseminationTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.secret();
        builder.sap.addSubCompartment("AA", "CC", "S1");
        builder.sap.addSubCompartment("AA", "CC", "S2");
        builder.sap.waived();
        
        assertTrue(builder.disseminations.hasDissemination(Disseminations.WAIVED));
    }
    
    @Test
    void hvsacoFunctionPopulatesAdditionalMarkingsTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.secret();
        builder.sap.hvsaco();
        
        assertTrue(builder.getAdditionalMarkings().contains(Utils.HVSACO));
    }
    
    @Test
    void hvsacoAndSAPMutuallyExclusiveTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.secret().sap.hvsaco();
        builder.sap.addSubCompartment("AA", "BB", "CC");
        builder.sap.addSubCompartment("AA", "BB", "DD");
        builder.sap.addSubCompartment("AA", "EE", "FF");
        builder.sap.addSubCompartment("AA", "GG", "HH");
        
        assertFalse(builder.isValid().isEmpty());
        
        builder.sap.clear();
        builder.sap.hvsaco();
        assertTrue(builder.isValid().isEmpty());
        
        builder.sap.clear();
        builder.sap.addSubCompartment("AA", "BB", "CC");
        builder.sap.addSubCompartment("AA", "BB", "DD");
        builder.sap.addSubCompartment("AA", "EE", "FF");
        builder.sap.addSubCompartment("AA", "GG", "HH");
        assertTrue(builder.isValid().isEmpty());
    }

    @Test
    void populateTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.secret();
        builder.sap.addSubCompartment("AA", "CC", "S1");
        builder.sap.addSubCompartment("AA", "CC", "S2");
        builder.sap.addSubCompartment("AA", "CC", "S3");
        builder.sap.addSubCompartment("AA", "CC", "S4");
        builder.sap.addSubCompartment("AA", "DD", "S5");
        builder.sap.addSubCompartment("AA", "DD", "S6");
        
        ClassificationMarkerBuilder otherBuilder = new ClassificationMarkerBuilder(builder.build());
        
        assertEquals(builder.sap, otherBuilder.sap);
    }
}

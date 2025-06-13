package io.github.trquinn76.classification.usa;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import io.github.trquinn76.classification.usa.model.DisseminationControls;

class DisseminationsTest {

    @Test
    void releaseToTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.topSecret().disseminations.releaseTo("USA", "AAA");
        assertTrue(builder.isValid().isEmpty());
        
        builder.secret();
        assertTrue(builder.isValid().isEmpty());
        
        builder.confidential();
        assertTrue(builder.isValid().isEmpty());
        
        // release to not for use with UNCLASSIFIED
        builder.unclassified();
        assertFalse(builder.isValid().isEmpty());
        
        // release to may be used with UNCLASSIFIED iff Controlled Unclassified Information dissemination is also set.
        builder.disseminations.controledUnclassifiedInformation();
        assertTrue(builder.isValid().isEmpty());
    }
    
    @Test
    void releaseToMinTwoTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.confidential().disseminations.releaseTo("USA");
        assertFalse(builder.isValid().isEmpty()); // fails due to single country...
        
        builder.disseminations.addCountry(DisseminationControls.RELEASE_TO, "AAA");
        assertTrue(builder.isValid().isEmpty()); // succeeds due to 2 countries.
    }
    
    @Test
    void releaseToMustContainUSATest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.confidential().disseminations.releaseTo("AAA", "BBB");
        assertFalse(builder.isValid().isEmpty()); // fails due to lack of USA in country list.
        
        builder.disseminations.addCountry(DisseminationControls.RELEASE_TO, Utils.USA);
        assertTrue(builder.isValid().isEmpty()); // succeeds due to USA being added to country list.
    }
    
    @Test
    void releaseToMutuallyExclusiveWithNofornTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.confidential().disseminations.releaseTo("USA", "AAA", "BBB");
        assertTrue(builder.isValid().isEmpty());
        
        builder.disseminations.noforn();
        assertFalse(builder.isValid().isEmpty()); // as cannot mix noforn and releaseTo.
    }

    @Test
    void displayOnlyMinOneCountryTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.confidential().disseminations.displayOnly();
        assertFalse(builder.isValid().isEmpty()); // no countries, so not valid.
        
        builder.disseminations.addCountry(DisseminationControls.DISPLAY_ONLY, "AAA");
        assertTrue(builder.isValid().isEmpty()); // as there is now 1 country.
        
        builder.disseminations.addCountry(DisseminationControls.DISPLAY_ONLY, "BBB");
        assertTrue(builder.isValid().isEmpty()); // as there are now 2 countries.
    }
    
    @Test
    void displayOnlyMutuallExclusiveWithRelidoTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.confidential().disseminations.displayOnly("AAA").disseminations.relido();
        assertFalse(builder.isValid().isEmpty()); // as display only and relido mutually exclusive.
        
        builder.disseminations.clear().disseminations.relido();
        assertTrue(builder.isValid().isEmpty()); // relido only
        
        builder.disseminations.clear().disseminations.displayOnly("BBB");
        assertTrue(builder.isValid().isEmpty()); // display only... erm, only.
    }
    
    @Test
    void displayOnlyMutuallyExclusiveWithNofornTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.confidential().disseminations.displayOnly("AAA").disseminations.noforn();
        assertFalse(builder.isValid().isEmpty()); // as display only and noforn mutually exclusive.
        
        builder.disseminations.clear().disseminations.noforn();
        assertTrue(builder.isValid().isEmpty()); // noforn only
        
        builder.disseminations.clear().disseminations.displayOnly("BBB");
        assertTrue(builder.isValid().isEmpty()); // display only... erm, only.
    }
    
    @Test
    void relidoMutuallyExclusiveWithNoforn() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.confidential().disseminations.relido().disseminations.noforn();
        assertFalse(builder.isValid().isEmpty()); // as relido and noforn mutually exclusive.
        
        builder.disseminations.clear().disseminations.noforn();
        assertTrue(builder.isValid().isEmpty()); // noforn only
        
        builder.disseminations.clear().disseminations.relido();
        assertTrue(builder.isValid().isEmpty()); // relido only.
    }
    
    @Test
    void fouoUnclassifiedOnlyTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.confidential().disseminations.fouo();
        assertFalse(builder.isValid().isEmpty()); // FOUO requires UNCLASSIFIED.
        
        builder.unclassified();
        assertTrue(builder.isValid().isEmpty());
    }
    
    @Test
    void controledUnclassifiedMayOnlyBeUsedWithUnclassifiedTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.confidential().disseminations.controledUnclassifiedInformation();
        assertFalse(builder.isValid().isEmpty()); // CUI requires UNCLASSIFIED.

        builder.unclassified();
        assertTrue(builder.isValid().isEmpty());
    }

    @Test
    void orconRequiresClassificationTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.unclassified().disseminations.orcon();
        assertFalse(builder.isValid().isEmpty()); // ORCON requires CONFIDENTIAL or higher.

        builder.confidential();
        assertTrue(builder.isValid().isEmpty());
    }
    
    @Test
    void controlledImageryIsHighlyClassifiedTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.confidential().disseminations.controledImagery();
        assertFalse(builder.isValid().isEmpty()); // CONTROLLED IMAGERY requires SECRET or higher.
        
        builder.secret();
        assertTrue(builder.isValid().isEmpty());
    }
    
    @Test
    void nofornRequiresClassificationOrCUITest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.secret().disseminations.noforn();
        assertTrue(builder.isValid().isEmpty());
        
        builder.confidential();
        assertTrue(builder.isValid().isEmpty());
        
        builder.unclassified();
        assertFalse(builder.isValid().isEmpty()); // NOFORN requires CONFIDENTIAL or higher, or CUI.
        
        builder.disseminations.controledUnclassifiedInformation();
        assertTrue(builder.isValid().isEmpty());
    }
    
    @Test
    void relidoRequiresClassificationTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.unclassified().disseminations.relido();
        assertFalse(builder.isValid().isEmpty()); // RELIDO requires CONFIDENTIAL or higher.
        
        builder.confidential();
        assertTrue(builder.isValid().isEmpty());
    }
    
    @Test
    void populateTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.confidential().disseminations.fisa().disseminations.noforn();
        
        ClassificationMarkerBuilder otherBuilder = new ClassificationMarkerBuilder(builder.build());
        
        assertEquals(builder.disseminations, otherBuilder.disseminations);
    }
}

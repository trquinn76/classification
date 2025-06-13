package io.github.trquinn76.classification.usa;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.Test;

class FGITest {

    @Test
    void isPopulatedTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        assertFalse(builder.fgi.isPopulated());
        
        builder.fgi.setCountries(Set.of("AAA", "BBB", "CCC"));
        assertTrue(builder.fgi.isPopulated());
        
        builder.fgi.clear().fgi.concealed();
        assertTrue(builder.fgi.isPopulated());
        
        builder.fgi.clear();
        assertFalse(builder.fgi.isPopulated());
    }
    
    @Test
    void concealedAndCountryListNotAllowedTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.confidential().fgi.concealed().fgi.addCountry("AAA");
        assertFalse(builder.isValid().isEmpty());
    }
    
    @Test
    void populateTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.confidential().fgi.concealed();
        
        ClassificationMarkerBuilder otherBuilder = new ClassificationMarkerBuilder(builder.build());
        
        assertEquals(builder.fgi, otherBuilder.fgi);
    }

}

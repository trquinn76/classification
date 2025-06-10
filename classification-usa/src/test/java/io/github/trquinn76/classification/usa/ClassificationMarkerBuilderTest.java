package io.github.trquinn76.classification.usa;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.github.trquinn76.classification.usa.model.Classification;
import io.github.trquinn76.classification.usa.model.ClassificationMarker;
import io.github.trquinn76.classification.usa.model.SecurityClassification;

class ClassificationMarkerBuilderTest {
    
    final static List<String> EMPTY = Collections.emptyList();

    @Test
    void noValueSetTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        List<String> report = builder.isValid();
        assertEquals(List.of("Classification must be set."), report);
        assertThrows(IllegalStateException.class, () -> {
            builder.build();
        });
    }
    
    @Test
    void invalidClassificationStringTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Classification("I'm not a Classification.");
        });
        // currently configured for DevelopmentClassifications, so test that
        // SecurityClassification fails
        assertThrows(IllegalArgumentException.class, () -> {
            new Classification(SecurityClassification.UNCLASSIFIED.name());
        });
    }
    
    @Test
    void classificationSetTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.unclassified();
        assertTrue(builder.isValid().isEmpty());
        ClassificationMarker expectedMarker = new ClassificationMarker(null, Classification.unclassified(),
                Collections.emptyList(), Collections.emptyList(), null, null, Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList());
        ClassificationMarker actualMarker = builder.build();
        assertEquals(expectedMarker, actualMarker);

        builder.setClassification(Classification.restricted());
        assertFalse(builder.isValid().isEmpty());
        assertThrows(IllegalStateException.class, () -> { builder.build(); });

        builder.confidential();
        assertTrue(builder.isValid().isEmpty());
        expectedMarker = new ClassificationMarker(null, Classification.confidential(), Collections.emptyList(),
                Collections.emptyList(), null, null, Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList());
        actualMarker = builder.build();
        assertEquals(expectedMarker, actualMarker);
        
        builder.secret();
        assertTrue(builder.isValid().isEmpty());
        expectedMarker = new ClassificationMarker(null, Classification.secret(), Collections.emptyList(),
                Collections.emptyList(), null, null, Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList());
        actualMarker = builder.build();
        assertEquals(expectedMarker, actualMarker);
        
        builder.topSecret();
        assertTrue(builder.isValid().isEmpty());
        expectedMarker = new ClassificationMarker(null, Classification.topSecret(), Collections.emptyList(),
                Collections.emptyList(), null, null, Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList());
        actualMarker = builder.build();
        assertEquals(expectedMarker, actualMarker);
    }

}

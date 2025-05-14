package io.github.trquinn76.classification.uk;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.trquinn76.classification.uk.model.ClassificationMarker;

class ParsingTest {

    @AfterEach
    void afterEach() {
        // ensure modified config is reverted even if test fails.
        ClassificationConfig.reset();
        System.clearProperty(ClassificationConfig.PRODUCTIONMODECONFIGKEYS.cmdLineProperty());
    }

    @Test
    void parsingTest() throws JsonProcessingException {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        ClassificationMarker marker = builder.secret().ukEyesOnly().addEyesOnlyCountry("US").codeWords("AAA", "BBB")
                .useOnly("ALPHA", "BETA").build();

        ObjectMapper mapper = new ObjectMapper();

        String jsonString = mapper.writeValueAsString(marker);

        ClassificationMarker parsedMarker = mapper.readValue(jsonString, ClassificationMarker.class);

        assertEquals(marker, parsedMarker);
    }

    @Test
    void simpleParsingTest() throws JsonProcessingException {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        ClassificationMarker marker = builder.secret().fiveEyesOnly().build();

        ObjectMapper mapper = new ObjectMapper();

        String jsonString = mapper.writeValueAsString(marker);

        ClassificationMarker parsedMarker = mapper.readValue(jsonString, ClassificationMarker.class);

        assertEquals(marker, parsedMarker);
    }

    @Test
    void parsingSecurityClassificationsTest() throws JsonProcessingException {
        ClassificationConfigTest
                .setAndWaitForSystemProperty(ClassificationConfig.PRODUCTIONMODECONFIGKEYS.cmdLineProperty(), "true");
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        ClassificationMarker marker = builder.secret().codeWords("AAA", "BBB").descriptors("ORCON")
                .eyesOnly("NZ", "UK", "US").build();

        ObjectMapper mapper = new ObjectMapper();

        String jsonString = mapper.writeValueAsString(marker);

        ClassificationMarker parsedMarker = mapper.readValue(jsonString, ClassificationMarker.class);

        assertEquals(marker, parsedMarker);
    }

}

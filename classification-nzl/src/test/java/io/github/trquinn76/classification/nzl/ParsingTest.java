package io.github.trquinn76.classification.nzl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.trquinn76.classification.nzl.model.ProtectiveMarker;

class ParsingTest {

    @AfterEach
    void afterEach() {
        // ensure modified config is reverted even if test fails.
        ClassificationConfig.reset();
        System.clearProperty(ClassificationConfig.PRODUCTIONMODECONFIGKEYS.cmdLineProperty());
    }

    @Test
    void parsingTest() throws JsonProcessingException {
        ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
        ProtectiveMarker marker = builder.restricted().sensitiveCompartments("AAA", "BBB").relTo("CAN", "NZL", "GBR")
                .build();

        ObjectMapper mapper = new ObjectMapper();

        String jsonString = mapper.writeValueAsString(marker);

        ProtectiveMarker parsedMarker = mapper.readValue(jsonString, ProtectiveMarker.class);

        assertEquals(marker, parsedMarker);
    }

    @Test
    void simpleParsingTest() throws JsonProcessingException {
        ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
        ProtectiveMarker marker = builder.secret().relTo("NZL", "AUS", "CAN").build();

        ObjectMapper mapper = new ObjectMapper();

        String jsonString = mapper.writeValueAsString(marker);

        ProtectiveMarker parsedMarker = mapper.readValue(jsonString, ProtectiveMarker.class);

        assertEquals(marker, parsedMarker);
    }

    @Test
    void parsingNZLClassificationsTest() throws JsonProcessingException {
        ClassificationConfigTest
                .setAndWaitForSystemProperty(ClassificationConfig.PRODUCTIONMODECONFIGKEYS.cmdLineProperty(), "true");
        ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
        ProtectiveMarker marker = builder.secret().sensitiveCompartments("AAA", "BBB").disseminationMarks("ORCON")
                .relTo("NZL", "GBR", "USA").build();

        ObjectMapper mapper = new ObjectMapper();

        String jsonString = mapper.writeValueAsString(marker);

        ProtectiveMarker parsedMarker = mapper.readValue(jsonString, ProtectiveMarker.class);

        assertEquals(marker, parsedMarker);
    }

}

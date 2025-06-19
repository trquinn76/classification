package io.github.trquinn76.classification.usa;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.trquinn76.classification.usa.model.ClassificationMarker;

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
        ClassificationMarker marker = builder.secret().disseminations.noforn().sci.addCompartment("MadeUp", "AAA").sci
                .addCompartment("MadeUp", "BBB").disseminations.orcon().build();

        ObjectMapper mapper = new ObjectMapper();

        String jsonString = mapper.writeValueAsString(marker);

        ClassificationMarker parsedMarker = mapper.readValue(jsonString, ClassificationMarker.class);

        assertEquals(marker, parsedMarker);
    }

    @Test
    void simpleParsingTest() throws JsonProcessingException {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        ClassificationMarker marker = builder.secret().disseminations.releaseTo(Utils.USA, "AUS", "CAN", "NZL", "GBR").build();

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
        ClassificationMarker marker = builder.secret().sap.addSARProgram("AAA").sap.addSARProgram("BBB").disseminations
                .orcon().disseminations.releaseTo(Utils.USA, "GBR", "NZL").build();

        ObjectMapper mapper = new ObjectMapper();

        String jsonString = mapper.writeValueAsString(marker);

        ClassificationMarker parsedMarker = mapper.readValue(jsonString, ClassificationMarker.class);

        assertEquals(marker, parsedMarker);
    }

}

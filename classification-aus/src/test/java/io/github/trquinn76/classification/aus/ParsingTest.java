package io.github.trquinn76.classification.aus;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.trquinn76.classification.aus.ClassificationConfig;
import io.github.trquinn76.classification.aus.ProtectiveMarkerBuilder;
import io.github.trquinn76.classification.aus.model.ProtectiveMarker;

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
		ProtectiveMarker marker = builder.protect().agao().addCodeword("AAA").addForeignGovernmentMarking("BBB")
				.exclusiveFor("CCC").legalPrivilege().build();

		ObjectMapper mapper = new ObjectMapper();

		String jsonString = mapper.writeValueAsString(marker);

		ProtectiveMarker parsedMarker = mapper.readValue(jsonString, ProtectiveMarker.class);

		assertEquals(marker, parsedMarker);
	}

	@Test
	void simpleParsingTest() throws JsonProcessingException {
		ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
		ProtectiveMarker marker = builder.secret().rel("AUS", "CAN", "NZL").build();

		ObjectMapper mapper = new ObjectMapper();

		String jsonString = mapper.writeValueAsString(marker);

		ProtectiveMarker parsedMarker = mapper.readValue(jsonString, ProtectiveMarker.class);

		assertEquals(marker, parsedMarker);
	}

	@Test
	void parsingPSPFClassificationsTest() throws JsonProcessingException {
		ClassificationConfigTest
				.setAndWaitForSystemProperty(ClassificationConfig.PRODUCTIONMODECONFIGKEYS.cmdLineProperty(), "true");
		ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
		ProtectiveMarker marker = builder.protect().cabinet().rel("AUS", "GBR", "USA").build();

		ObjectMapper mapper = new ObjectMapper();

		String jsonString = mapper.writeValueAsString(marker);

		ProtectiveMarker parsedMarker = mapper.readValue(jsonString, ProtectiveMarker.class);

		assertEquals(marker, parsedMarker);
	}

}

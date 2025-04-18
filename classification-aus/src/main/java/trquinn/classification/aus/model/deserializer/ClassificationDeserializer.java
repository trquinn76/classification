package trquinn.classification.aus.model.deserializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import trquinn.classification.aus.ClassificationConfig;
import trquinn.classification.aus.model.Classification;
import trquinn.classification.aus.model.DevelopmentClassification;
import trquinn.classification.aus.model.PSPFClassification;

/**
 * A specialised Jackson Databind Deserialiser for the {@link Classification}
 * interface.
 * 
 * Deserialisation will depend on current Production Mode configuration.
 */
@SuppressWarnings("serial")
public class ClassificationDeserializer extends StdDeserializer<Classification> {

	public ClassificationDeserializer() {
		super(Classification.class);
	}

	@Override
	public Classification deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
		if (ClassificationConfig.productionMode()) {
			return PSPFClassification.valueOf(p.getText());
		}
		return DevelopmentClassification.valueOf(p.getText());
	}
}

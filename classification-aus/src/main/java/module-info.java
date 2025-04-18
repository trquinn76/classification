/**
 * Defines Classification/Protective Marking models for the Australian Classification scheme.
 */
open module trquinn.classification.aus {
	requires java.logging;
	requires com.fasterxml.jackson.databind;
	
	exports trquinn.classification.aus;
	exports trquinn.classification.aus.model;
	exports trquinn.classification.aus.model.deserializer;
}
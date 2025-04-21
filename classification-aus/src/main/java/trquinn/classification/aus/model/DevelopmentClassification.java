package trquinn.classification.aus.model;

import trquinn.classification.aus.ClassificationConfig;

/**
 * A parallel list of {@link Classification}'s, which follow the same structure as {@link PSPFClassification}, but
 * which might be used in non-secure environments.
 * <p>
 * These {@link Classification}'s exist in order to allow development and testing of Classification related logic and
 * components, without needing to mark up test or development data with real Classification values. This prevents
 * technical Breaches in corporate or lower classification networks or test data.
 * <p>
 * The default configuration will use this set of {@link Classification} values. In order to switch to using the
 * {@link PSPFClassification}'s in production it is necessary to set the production mode configuration value to
 * {@code true}.  
 */
public enum DevelopmentClassification {

	DEVELOPMENT_UNOFFICIAL(ClassificationConfig.developmentUnofficialName()),
	DEVELOPMENT_OFFICIAL(ClassificationConfig.developmentOfficialName()),
	DEVELOPMENT_OFFICIAL_SENSITIVE(ClassificationConfig.developmentOfficialSensitiveName()),
	DEVELOPMENT_PROTECTED(ClassificationConfig.developmentProtectedName()),
	DEVELOPMENT_SECRET(ClassificationConfig.developmentSecretName()),
	DEVELOPMENT_TOP_SECRET(ClassificationConfig.developmentTopSecretName());
	
	private final String text;
	
	private DevelopmentClassification(String text) {
		this.text = text;
	}
	
	/**
	 * Overridden to ensure that the returned String is suitable for Users.
	 */
	@Override
	public String toString() {
		return this.text;
	}
}

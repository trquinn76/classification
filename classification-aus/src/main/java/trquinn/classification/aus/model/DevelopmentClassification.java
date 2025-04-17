package trquinn.classification.aus.model;

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
public enum DevelopmentClassification implements Classification {

	FAKE_UNOFFICIAL("Fake UNOFFICIAL"),
	FAKE_OFFICIAL("Fake OFFICIAL"),
	FAKE_OFFICIAL_SENSITIVE("Fake OFFICIAL: Sensitive"),
	FAKE_PROTECTED("Fake PROTECTED"),
	FAKE_SECRET("Fake SECRET"),
	FAKE_TOP_SECRET("Fake TOP SECRET");
	
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

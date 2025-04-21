package trquinn.classification.aus.model;

/**
 * The {@link Classification}'s as defined in the Australian Government Protective Security Policy Framework.
 * <p>
 * The default configuration will see {@link DevelopmentClassification}'s used. In order for these
 * {@link Classification}'s to be used, it is necessary to set the classification production mode configuration
 * value to true.
 */
public enum PSPFClassification {

	UNOFFICIAL("UNOFFICIAL"),
	OFFICIAL("OFFICIAL"),
	OFFICIAL_SENSITIVE("OFFICIAL: Sensitive"),
	PROTECTED("PROTECTED"),
	SECRET("SECRET"),
	TOP_SECRET("TOP SECRET");
	
	private final String text;
	
	private PSPFClassification(String text) {
		this.text = text;
	}
	
	/**
	 * Overridden to ensure that the {@code toString()} function returns text suitable to display to human Users.
	 * 
	 * @return String a human readable representation of the {@link Classification}
	 */
	@Override
	public String toString() {
		return this.text;
	}
}

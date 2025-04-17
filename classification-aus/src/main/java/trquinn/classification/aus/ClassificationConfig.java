package trquinn.classification.aus;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * A Class which provides Classification Configuration information.
 */
public class ClassificationConfig {
	
	/** A command line option to set the configuration file to retrieve configuration from. */
	public static final String CMD_LINE_CONFIG_FILE_PROPERTY = "trquinnClassificationConfigFile";
	
	protected static final String AUSFIRST = "ausfirst";
	protected static final String FIVEEYESFIRST = "fiveeyesfirst";
	public static final List<String> RELTO_ORDER_VALUES = List.of(AUSFIRST, FIVEEYESFIRST);
	
	static final ConfigKeys RELTOORDERCONFIGKEYS = new ConfigKeys("trquinnClassificationAusReltoOrder", "TRQUINN_CLASSIFICATION_AUS_RELTO_ORDER", "trquinn.classification.aus.relto.order");
	static final ConfigKeys PRODUCTIONMODECONFIGKEYS = new ConfigKeys("trquinnClassificationAusProductionMode", "TRQUINN_CLASSIFICATION_AUS_PRODUCTION_MODE", "trquinn.classification.aus.production.mode");
	
	private static Config INSTANCE = null;
	
	private static final List<String> CONFIG_FILES = List.of("application.properties",
			"classification-config.properties", "default-classification-config.properties");
	
	private static final Logger LOGGER = Logger.getLogger(ClassificationConfig.class.getCanonicalName());

	/**
	 * Gets the {@link Comparator} which defines the order in which releasable to lists should be sorted.
	 * 
	 * @return the configured {@link Comparator}.
	 */
	public static Comparator<String> releasableToOrder() {
		Config config = getInstance();
		if (config.trquinnClassificationReltoOrder.equals(FIVEEYESFIRST)) {
			return Utils.FIVE_EYES_FIRST;
		}
		return Utils.AUS_FIRST;
	}
	
	/**
	 * Returns if the Classifications are in production mode.
	 * 
	 * In production mode, the real
	 * {@link trquinn.classification.aus.model.Classification}'s from
	 * {@link trquinn.classification.aus.model.PSPFClassification} will be used, while
	 * the {@link trquinn.classification.aus.model.Classification}'s from
	 * {@link trquinn.classification.aus.model.DevelopmentClassification} will be used
	 * otherwise.
	 * <p>
	 * Defaults to false. Needs to be set by a developer to use real
	 * {@link trquinn.classification.aus.model.Classification}'s.
	 * 
	 * @return true if in Production Mode, false otherwise.
	 */
	public static boolean productionMode() {
		Config config = getInstance();
		return config.trquinnClassificationProductionMode;
	}
	
	/**
	 * for testing... resets config and will force a reload.
	 */
	protected static void reset() {
		INSTANCE = null;
	}
	
	private static Config getInstance() {
		if (INSTANCE == null) {
			List<String> filesToSearch = makeListOfConfigFiles();
			List<Properties> configPropertiesFromFiles = readPropertiesConfigFiles(filesToSearch);
			
			Config config = new Config();
			config.trquinnClassificationReltoOrder = getConfig(RELTOORDERCONFIGKEYS, configPropertiesFromFiles);
			config.trquinnClassificationProductionMode = Boolean.parseBoolean(getConfig(PRODUCTIONMODECONFIGKEYS, configPropertiesFromFiles));
				
			INSTANCE = config;
		}
		return INSTANCE;
	}
	
	/**
	 * Builds a list of possible configuration files, in the order they should be searched for config information.
	 * @return
	 */
	private static List<String> makeListOfConfigFiles() {
		List<String> files = new ArrayList<>();
		String cmdLineConfigFile = System.getProperty(CMD_LINE_CONFIG_FILE_PROPERTY);
		if (cmdLineConfigFile != null) {
			files.add(cmdLineConfigFile);
		}
		files.addAll(CONFIG_FILES);
		return files;
	}
	
	private static List<Properties> readPropertiesConfigFiles(List<String> configFileNameList) {
		List<Properties> configPropertiesFromFiles = new ArrayList<>();
		for (String fileName: configFileNameList) {
			try {
				Properties properties = loadPropertiesFile(fileName);
				if (properties != null) {
					configPropertiesFromFiles.add(properties);
				}
			}
			catch (IOException ioe) {
				LOGGER.config("IOException when attempting to read config file " + fileName + ": " + ioe.getMessage());
			}
		}
		return configPropertiesFromFiles;
	}
	
	private static Properties loadPropertiesFile(String propertiesFileName) throws IOException {
		Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(propertiesFileName);
		if (resources.hasMoreElements()) {
			try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(propertiesFileName)) {
				Properties configProps = new Properties();
				configProps.load(in);
				
				return configProps;
			}
		}
		return null;
	}
	
	private static String getConfig(ConfigKeys keys, List<Properties> propertiesList) {
		String cmdLineValue = System.getProperty(keys.cmdLineProperty());
		if (cmdLineValue != null && !cmdLineValue.isBlank()) {
			return cmdLineValue;
		}
		String envValue = System.getenv(keys.envVariable());
		if (envValue != null && !envValue.isBlank()) {
			return envValue;
		}
		for (Properties properties : propertiesList) {
			String value = properties.getProperty(keys.configFileProperty());
			if (value != null && !value.isBlank()) {
				return value;
			}
		}
		throw new Error("No configuration value found for " + keys.configFileProperty() + "! At least a default value should have been found.");
	}
	
	private static class Config {
		
		public String trquinnClassificationReltoOrder;
		public boolean trquinnClassificationProductionMode;
	}
	
	/**
	 * Defines a set of keys for a single configuration value.
	 * 
	 * Configuration can come from the command line, an environment variable, or a config file. This structure
	 * holds the property keys for each of these different sources for a single configuration value.
	 */
	record ConfigKeys (
		String cmdLineProperty,
		String envVariable,
		String configFileProperty
	) {}
	
	private ClassificationConfig() {}
}

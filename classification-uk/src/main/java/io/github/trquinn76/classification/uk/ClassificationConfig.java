package io.github.trquinn76.classification.uk;

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

    /**
     * A command line option to set the configuration file to retrieve configuration
     * from.
     */
    public static final String CMD_LINE_CONFIG_FILE_PROPERTY = "classificationConfigFile";
    /**
     * An environment variable to allow the setting of the configuration file t
     * retrieve configuration from.
     */
    public static final String ENV_VARIABLE_CONFIG_FILE = "CLASSIFICATION_CONFIG_FILE";

    protected static final String UKFIRST = "ukfirst";
    protected static final String ALPHABETICAL = "alphabetical";

    static final ConfigKeys PRODUCTIONMODECONFIGKEYS = new ConfigKeys("classificationProductionMode",
            "CLASSIFICATION_PRODUCTION_MODE", "io.github.trquinn76.classification.production.mode");

    static final ConfigKeys EYESONLYORDERCONFIGKEYS = new ConfigKeys("classificationUkEyesOnlyOrder",
            "CLASSIFICATION_UK_EYES_ONLY_ORDER", "io.github.trquinn76.classification.uk.eyes.only.order");

    static final ConfigKeys DEVELOFFICIALNAME = new ConfigKeys("classificationUkDevelOfficial",
            "CLASSIFICATION_UK_DEVEL_OFFICIAL", "io.github.trquinn76.classification.uk.development.official.name");
    static final ConfigKeys SENSITIVEMARKNAME = new ConfigKeys("classificationUkSensitiveMark",
            "CLASSIFICATION_UK_SENSITIVE_MARK",
            "io.github.trquinn76.classification.uk.sensitive.mark.name");
    static final ConfigKeys DEVELSECRETNAME = new ConfigKeys("classificationUkDevelSecret",
            "CLASSIFICATION_UK_DEVEL_SECRET", "io.github.trquinn76.classification.uk.development.secret.name");
    static final ConfigKeys DEVELTOPSECRETNAME = new ConfigKeys("classificationUkDevelTopSecret",
            "CLASSIFICATION_UK_DEVEL_TOP_SECRET", "io.github.trquinn76.classification.uk.development.top.secret.name");

    private static Config INSTANCE = null;

    private static final List<String> CONFIG_FILES = List.of("application.properties",
            "classification-config.properties", "uk-default-classification-config.properties");

    private static final Logger LOGGER = Logger.getLogger(ClassificationConfig.class.getCanonicalName());

    /**
     * Gets the {@link Comparator} which defines the order in which eyes only lists
     * should be sorted.
     * 
     * @return the configured {@link Comparator}.
     */
    public static Comparator<String> eyesOnlyOrder() {
        Config config = getInstance();
        if (config.trquinnClassificationUkEyesOnlyOrder.equals(UKFIRST)) {
            return Utils.UK_FIRST;
        }
        return Utils.ALPHABETICAL;
    }

    public static boolean productionMode() {
        Config config = getInstance();
        return config.trquinnClassificationProductionMode;
    }

    public static String developmentOfficialName() {
        return getInstance().develOfficalName;
    }

    public static String sensitiveMark() {
        return getInstance().sensitiveMark;
    }

    public static String developmentSecretName() {
        return getInstance().develSecretName;
    }

    public static String developmentTopSecretName() {
        return getInstance().develTopSecretName;
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
            List<Properties> configProperties = readPropertiesConfigFiles(filesToSearch);

            Config config = new Config();
            config.trquinnClassificationUkEyesOnlyOrder = getConfig(EYESONLYORDERCONFIGKEYS, configProperties);
            config.trquinnClassificationProductionMode = Boolean
                    .parseBoolean(getConfig(PRODUCTIONMODECONFIGKEYS, configProperties));

            config.develOfficalName = getConfig(DEVELOFFICIALNAME, configProperties);
            config.sensitiveMark = getConfig(SENSITIVEMARKNAME, configProperties);
            config.develSecretName = getConfig(DEVELSECRETNAME, configProperties);
            config.develTopSecretName = getConfig(DEVELTOPSECRETNAME, configProperties);

            INSTANCE = config;
        }
        return INSTANCE;
    }

    /**
     * Builds a list of possible configuration files, in the order they should be
     * searched for config information.
     * 
     * @return
     */
    private static List<String> makeListOfConfigFiles() {
        List<String> files = new ArrayList<>();
        String userDefinedConfigFile = System.getProperty(CMD_LINE_CONFIG_FILE_PROPERTY);
        if (userDefinedConfigFile == null) {
            userDefinedConfigFile = System.getenv(ENV_VARIABLE_CONFIG_FILE);
        }
        if (userDefinedConfigFile != null) {
            files.add(userDefinedConfigFile);
        }
        files.addAll(CONFIG_FILES);
        return files;
    }

    private static List<Properties> readPropertiesConfigFiles(List<String> configFileNameList) {
        List<Properties> configPropertiesFromFiles = new ArrayList<>();
        for (String fileName : configFileNameList) {
            try {
                Properties properties = loadPropertiesFile(fileName);
                if (properties != null) {
                    configPropertiesFromFiles.add(properties);
                }
            } catch (IOException ioe) {
                LOGGER.config("IOException when attempting to read config file " + fileName + ": " + ioe.getMessage());
            }
        }
        return configPropertiesFromFiles;
    }

    private static Properties loadPropertiesFile(String propertiesFileName) throws IOException {
        Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(propertiesFileName);
        if (resources.hasMoreElements()) {
            try (InputStream in = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream(propertiesFileName)) {
                Properties configProps = new Properties();
                configProps.load(in);

                return configProps;
            }
        }
        return null;
    }

    private static String getConfig(ConfigKeys keys, List<Properties> propertiesList) {
        String cmdLineValue = System.getProperty(keys.cmdLineProperty());
        LOGGER.config("Attempted to get cmd line property: " + keys.cmdLineProperty() + " : " + cmdLineValue);
        if (cmdLineValue != null && !cmdLineValue.isBlank()) {
            return cmdLineValue;
        }
        String envValue = System.getenv(keys.envVariable());
        LOGGER.config("Attempted to get env variable: " + keys.envVariable() + " : " + envValue);
        if (envValue != null && !envValue.isBlank()) {
            return envValue;
        }
        for (Properties properties : propertiesList) {
            String value = properties.getProperty(keys.configFileProperty());
            LOGGER.config("Attempted to get config property: " + keys.configFileProperty() + " : " + value);
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        throw new Error("No configuration value found for " + keys.configFileProperty()
                + "! At least a default value should have been found.");
    }

    private static class Config {

        public boolean trquinnClassificationProductionMode;
        public String trquinnClassificationUkEyesOnlyOrder;

        public String develOfficalName;
        public String sensitiveMark;
        public String develSecretName;
        public String develTopSecretName;
    }

    /**
     * Defines a set of keys for a single configuration value.
     * 
     * Configuration can come from the command line, an environment variable, or a
     * config file. This structure holds the property keys for each of these
     * different sources for a single configuration value.
     */
    record ConfigKeys(String cmdLineProperty, String envVariable, String configFileProperty) {
    }

    private ClassificationConfig() {
    }
}

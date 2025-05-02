package io.github.trquinn76.classification.nzl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.format.DateTimeFormatter;
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

    protected static final String NZLFIRST = "nzlfirst";
    protected static final String FIVEEYESFIRST = "fiveeyesfirst";
    public static final List<String> RELTO_ORDER_VALUES = List.of(NZLFIRST, FIVEEYESFIRST);

    static final ConfigKeys RELTOORDERCONFIGKEYS = new ConfigKeys("classificationNzlReltoOrder",
            "CLASSIFICATION_NZL_RELTO_ORDER", "io.github.trquinn76.classification.nzl.relto.order");
    static final ConfigKeys PRODUCTIONMODECONFIGKEYS = new ConfigKeys("classificationNzlProductionMode",
            "CLASSIFICATION_NZL_PRODUCTION_MODE", "io.github.trquinn76.classification.nzl.production.mode");

    static final ConfigKeys DEVELUNCLASSIFIEDNAME = new ConfigKeys("classificationNzlDevelUnclassified",
            "CLASSIFICATION_NZL_DEVEL_UNCLASSIFIED",
            "io.github.trquinn76.classification.nzl.development.unclassified.name");
    static final ConfigKeys DEVELINCONFIDENCENAME = new ConfigKeys("classificationNzlDevelInConfidence",
            "CLASSIFICATION_NZL_DEVEL_IN_CONFIDENCE",
            "io.github.trquinn76.classification.nzl.development.in.confidence.name");
    static final ConfigKeys DEVELSENSITIVENAME = new ConfigKeys("classificationNzlDevelSensitive",
            "CLASSIFICATION_NZL_DEVEL_SENSITIVE", "io.github.trquinn76.classification.nzl.development.sensitive.name");
    static final ConfigKeys DEVELRESTRICTEDNAME = new ConfigKeys("classificationNzlDevelRestricted",
            "CLASSIFICATION_NZL_DEVEL_RESTRICTED",
            "io.github.trquinn76.classification.nzl.development.restricted.name");
    static final ConfigKeys DEVELCONFIDENTIALNAME = new ConfigKeys("classificationNzlDevelConfidential",
            "CLASSIFICATION_NZL_DEVEL_CONFIDENTIAL",
            "io.github.trquinn76.classification.nzl.development.confidential.name");
    static final ConfigKeys DEVELSECRETNAME = new ConfigKeys("classificationNzlDevelSecret",
            "CLASSIFICATION_NZL_DEVEL_SECRET", "io.github.trquinn76.classification.nzl.development.secret.name");
    static final ConfigKeys DEVELTOPSECRETNAME = new ConfigKeys("classificationNzlDevelTopSecret",
            "CLASSIFICATION_NZL_DEVEL_TOP_SECRET",
            "io.github.trquinn76.classification.nzl.development.top.secret.name");

    static final ConfigKeys DATETIMEFORMATPATTERN = new ConfigKeys("classificationNzlDateTimeFormat",
            "CLASSIFICATION_NZL_DATE_TIME_FORMAT", "io.github.trquinn76.classification.nzl.date.time.format");

    private static Config INSTANCE = null;

    private static final List<String> CONFIG_FILES = List.of("application.properties",
            "classification-config.properties", "default-classification-config.properties");

    private static final Logger LOGGER = Logger.getLogger(ClassificationConfig.class.getCanonicalName());

    private static DateTimeFormatter dateTimeFormatter = null;

    /**
     * Gets the {@link Comparator} which defines the order in which releasable to
     * lists should be sorted.
     * 
     * @return the configured {@link Comparator}.
     */
    public static Comparator<String> releasableToOrder() {
        Config config = getInstance();
        if (config.trquinnClassificationReltoOrder.equals(FIVEEYESFIRST)) {
            return Utils.FIVE_EYES_FIRST;
        }
        return Utils.NZL_FIRST;
    }

    /**
     * Returns if the Classifications are in production mode.
     * 
     * In production mode, the real
     * {@link io.github.trquinn76.classification.nzl.model.Classification}'s from
     * {@link io.github.trquinn76.classification.nzl.model.NZLClassification} will
     * be used, while the
     * {@link io.github.trquinn76.classification.nzl.model.Classification}'s from
     * {@link io.github.trquinn76.classification.nzl.model.DevelopmentClassification}
     * will be used otherwise.
     * <p>
     * Defaults to false. Needs to be set by a developer to use real
     * {@link io.github.trquinn76.classification.nzl.model.Classification}'s.
     * 
     * @return true if in Production Mode, false otherwise.
     */
    public static boolean productionMode() {
        Config config = getInstance();
        return config.trquinnClassificationProductionMode;
    }

    public static String developmentUnclassifiedName() {
        return getInstance().develUnclassifiedName;
    }

    public static String developmentInConfidenceName() {
        return getInstance().develInConfidenceName;
    }

    public static String developmentSensitiveName() {
        return getInstance().develSensitiveName;
    }

    public static String developmentRestrictedName() {
        return getInstance().develRestrictedName;
    }

    public static String developmentConfidentialName() {
        return getInstance().develConfidentialName;
    }

    public static String developmentSecretName() {
        return getInstance().develSecretName;
    }

    public static String developmentTopSecretName() {
        return getInstance().develTopSecretName;
    }

    public static DateTimeFormatter dateTimeFormatter() {
        if (ClassificationConfig.dateTimeFormatter == null) {
            ClassificationConfig.dateTimeFormatter = DateTimeFormatter.ofPattern(getInstance().dateTimeFormatPattern);
        }
        return ClassificationConfig.dateTimeFormatter;
    }

    /**
     * for testing... resets config and will force a reload.
     */
    protected static void reset() {
        INSTANCE = null;
        dateTimeFormatter = null;
    }

    private static Config getInstance() {
        if (INSTANCE == null) {
            List<String> filesToSearch = makeListOfConfigFiles();
            List<Properties> configProperties = readPropertiesConfigFiles(filesToSearch);

            Config config = new Config();
            config.trquinnClassificationReltoOrder = getConfig(RELTOORDERCONFIGKEYS, configProperties);
            config.trquinnClassificationProductionMode = Boolean
                    .parseBoolean(getConfig(PRODUCTIONMODECONFIGKEYS, configProperties));

            config.develUnclassifiedName = getConfig(DEVELUNCLASSIFIEDNAME, configProperties);
            config.develInConfidenceName = getConfig(DEVELINCONFIDENCENAME, configProperties);
            config.develSensitiveName = getConfig(DEVELSENSITIVENAME, configProperties);
            config.develRestrictedName = getConfig(DEVELRESTRICTEDNAME, configProperties);
            config.develConfidentialName = getConfig(DEVELCONFIDENTIALNAME, configProperties);
            config.develSecretName = getConfig(DEVELSECRETNAME, configProperties);
            config.develTopSecretName = getConfig(DEVELTOPSECRETNAME, configProperties);

            config.dateTimeFormatPattern = getConfig(DATETIMEFORMATPATTERN, configProperties);

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

        public String trquinnClassificationReltoOrder;
        public boolean trquinnClassificationProductionMode;

        public String develUnclassifiedName;
        public String develInConfidenceName;
        public String develSensitiveName;
        public String develRestrictedName;
        public String develConfidentialName;
        public String develSecretName;
        public String develTopSecretName;

        public String dateTimeFormatPattern;
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

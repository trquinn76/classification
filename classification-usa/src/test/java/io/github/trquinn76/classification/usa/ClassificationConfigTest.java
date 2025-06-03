package io.github.trquinn76.classification.usa;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class ClassificationConfigTest {

    @AfterEach
    void afterEach() {
        ClassificationConfig.reset();
        System.clearProperty(ClassificationConfig.PRODUCTIONMODECONFIGKEYS.cmdLineProperty());
        System.clearProperty(ClassificationConfig.CMD_LINE_CONFIG_FILE_PROPERTY);
    }

    @Test
    void defaultTest() {
        assertFalse(ClassificationConfig.productionMode());
    }

    @Test
    void cmdLineTest() {
        setAndWaitForSystemProperty(ClassificationConfig.PRODUCTIONMODECONFIGKEYS.cmdLineProperty(), "true");
        assertTrue(ClassificationConfig.productionMode());
        // ClassificationConfig.PRODUCTIONMODECONFIGKEYS.cmdLineProperty() cleaned up in
        // afterEach()
    }

    @Test
    void cmdLineConfigFileTest() {
        setAndWaitForSystemProperty(ClassificationConfig.CMD_LINE_CONFIG_FILE_PROPERTY, "test-config.properties");
        assertTrue(ClassificationConfig.productionMode());
        // ClassificationConfig.CMD_LINE_CONFIG_FILE_PROPERTY cleaned up in afterEach()
    }

    /**
     * This function helps address intermittent bugs in tests where the properties
     * do not appear to have been set by the time the Config code is attempting to
     * read it.
     * 
     * @param propertyName
     * @param value
     */
    protected static void setAndWaitForSystemProperty(String propertyName, String value) {
        System.setProperty(propertyName, value);
        while (System.getProperty(propertyName) == null) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // ignore... will keep waiting if needed.
            }
        }
    }
}

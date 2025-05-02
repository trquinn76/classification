package io.github.trquinn76.classification.nzl;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Comparator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class ClassificationConfigTest {
	
	@AfterEach
	void afterEach() {
		ClassificationConfig.reset();
		System.clearProperty(ClassificationConfig.RELTOORDERCONFIGKEYS.cmdLineProperty());
		System.clearProperty(ClassificationConfig.CMD_LINE_CONFIG_FILE_PROPERTY);
	}

	@Test
	void defaultTest() {
		Comparator<String> actualComparator = ClassificationConfig.releasableToOrder();
		assertEquals(Utils.NZL_FIRST, actualComparator);
		assertFalse(ClassificationConfig.productionMode());

		LocalDateTime dt = LocalDateTime.of(2025, 5, 15, 14, 34);
		String expectedStr = "2025-05-15 14:34";
		String actualStr = dt.format(ClassificationConfig.dateTimeFormatter());
		assertEquals(expectedStr, actualStr);
	}
	
	@Test
	void cmdLineTest() {
		setAndWaitForSystemProperty(ClassificationConfig.RELTOORDERCONFIGKEYS.cmdLineProperty(), ClassificationConfig.FIVEEYESFIRST);
		Comparator<String> actual = ClassificationConfig.releasableToOrder();
		assertEquals(Utils.FIVE_EYES_FIRST, actual);
		assertFalse(ClassificationConfig.productionMode());
		// ClassificationConfig.RELTOORDERCONFIGKEYS.cmdLineProperty() cleaned up in afterEach()
	}
	
	@Test
	void cmdLineConfigFileTest() {
		setAndWaitForSystemProperty(ClassificationConfig.CMD_LINE_CONFIG_FILE_PROPERTY, "test-config.properties");
		Comparator<String> actual = ClassificationConfig.releasableToOrder();
		assertEquals(Utils.FIVE_EYES_FIRST, actual);
		assertTrue(ClassificationConfig.productionMode());
		// ClassificationConfig.CMD_LINE_CONFIG_FILE_PROPERTY cleaned up in afterEach()
	}
	
	/**
	 * This function helps address intermittent bugs in tests where the properties do not appear to have been set by
	 * the time the Config code is attempting to read it.
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

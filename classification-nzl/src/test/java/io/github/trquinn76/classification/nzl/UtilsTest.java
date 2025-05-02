package io.github.trquinn76.classification.nzl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;

class UtilsTest {

	@Test
	void nzlFirstTest() {
		Set<String> initialSet = Set.of("BBB", "DDD", "CCC", "NZL", "AAA");
		TreeSet<String> orderedSet = new TreeSet<>(Utils.NZL_FIRST);
		orderedSet.addAll(initialSet);
		String[] actualOrder = orderedSet.toArray(new String[5]);
		
		String[] expectedOrder = { "NZL", "AAA", "BBB", "CCC", "DDD" };
		assertArrayEquals(expectedOrder, actualOrder);
	}
	
	@Test
	void fiveEyesFirstTest() {
		Set<String> initialSet = Set.of("BBB", "FFF", "NZL", "DDD", "CCC", "AUS", "AAA", "USA", "EEE", "GBR", "CAN");
		TreeSet<String> orderedSet = new TreeSet<>(Utils.FIVE_EYES_FIRST);
		orderedSet.addAll(initialSet);
		String[] actualOrder = orderedSet.toArray(new String[11]);
		
		String[] expectedOrder = { "NZL", "AUS", "CAN", "GBR", "USA", "AAA", "BBB", "CCC", "DDD", "EEE", "FFF" };
		assertArrayEquals(expectedOrder, actualOrder);
	}

}

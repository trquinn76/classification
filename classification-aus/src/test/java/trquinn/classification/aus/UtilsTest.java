package trquinn.classification.aus;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;

import trquinn.classification.aus.model.ProtectiveMarker;
import trquinn.classification.aus.model.ReleasabilityType;

class UtilsTest {

	@Test
	void ausFirstTest() {
		Set<String> initialSet = Set.of("BBB", "DDD", "CCC", "AUS", "AAA");
		TreeSet<String> orderedSet = new TreeSet<>(Utils.AUS_FIRST);
		orderedSet.addAll(initialSet);
		String[] actualOrder = orderedSet.toArray(new String[5]);
		
		String[] expectedOrder = { "AUS", "AAA", "BBB", "CCC", "DDD" };
		assertArrayEquals(expectedOrder, actualOrder);
	}
	
	@Test
	void fiveEyesFirstTest() {
		Set<String> initialSet = Set.of("BBB", "FFF", "NZL", "DDD", "CCC", "AUS", "AAA", "USA", "EEE", "GBR", "CAN");
		TreeSet<String> orderedSet = new TreeSet<>(Utils.FIVE_EYES_FIRST);
		orderedSet.addAll(initialSet);
		String[] actualOrder = orderedSet.toArray(new String[11]);
		
		String[] expectedOrder = { "AUS", "CAN", "GBR", "NZL", "USA", "AAA", "BBB", "CCC", "DDD", "EEE", "FFF" };
		assertArrayEquals(expectedOrder, actualOrder);
	}
	
	@Test
	void mergeProtectiveMarkingsTest() {
		ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
		builder.protect().agao().addCodeword("AAA");
		ProtectiveMarker alpha = builder.build();
		builder.secret().rel("AUS", "NZL").clearCodeWords().addForeignGovernmentMarking("APPLE");
		ProtectiveMarker beta = builder.build();
		// set builder to expected result.
		builder.clear().secret().agao().addCodeword("AAA").addForeignGovernmentMarking("APPLE");
		
		ProtectiveMarkerBuilder actual = Utils.merge(alpha, beta);
		assertEquals(builder, actual);
	}
	
	@Test
	void mergeMarkingAndBuilderTest() {
		ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
		ProtectiveMarker protectiveMarking = builder.protect().austeo().build();
		builder.secret().rel("AUS", "NZL");
		
		ProtectiveMarkerBuilder expected = new ProtectiveMarkerBuilder();
		expected.secret().austeo();
		
		builder = Utils.merge(builder, protectiveMarking);
		assertEquals(expected, builder);
	}
	
	@Test
	void mergeCollectionTest() {
		List<ProtectiveMarker> markings = new ArrayList<>();
		ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
		markings.add(builder.protect().rel("AUS", "NZL", "GBR").build());
		markings.add(builder.protect().rel("AUS", "NZL", "CAN").build());
		markings.add(builder.secret().rel("AUS", "USA").build());
		markings.add(builder.topSecret().rel("AUS", "CAN", "GBR", "NZL", "USA").build());
		
		builder.clear().topSecret().setReleasability(ReleasabilityType.REL).addReleasableToCountry("AUS");
		
		ProtectiveMarkerBuilder actual = Utils.merge(markings);
		assertEquals(builder, actual);
		assertTrue(actual.isValid().size() > 0);
	}
	
	@Test
	void mergeBuilderNoReleasabilityTest() {
		ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
		ProtectiveMarker marking = builder.secret().rel("AUS", "CAN").build();
		builder.clear().secret();
		
		ProtectiveMarkerBuilder expected = new ProtectiveMarkerBuilder();
		expected.secret().rel("AUS", "CAN");
		
		builder = Utils.merge(builder, marking);
		assertEquals(expected, builder);
	}

}

package io.github.trquinn76.classification.aus;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.github.trquinn76.classification.aus.ProtectiveMarkerBuilder;
import io.github.trquinn76.classification.aus.model.Classification;
import io.github.trquinn76.classification.aus.model.InformationManagementMarker;
import io.github.trquinn76.classification.aus.model.InformationManagementTypes;
import io.github.trquinn76.classification.aus.model.PSPFClassification;
import io.github.trquinn76.classification.aus.model.ProtectiveMarker;
import io.github.trquinn76.classification.aus.model.ReleasabilityCaveat;
import io.github.trquinn76.classification.aus.model.ReleasabilityType;
import io.github.trquinn76.classification.aus.model.SecurityCaveats;
import io.github.trquinn76.classification.aus.model.SpecialHandlingCaveat;
import io.github.trquinn76.classification.aus.model.SpecialHandlingInstruction;

class ProtectiveMarkerBuilderTest {

	@Test
	void noValueSetTest() {
		ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
		List<String> report = builder.isValid();
		assertEquals(List.of("Classification must be set."), report);
		assertThrows(IllegalStateException.class, () -> { builder.build(); });
	}
	
	@Test
	void invalidClassificationStringTest() {
		assertThrows(IllegalArgumentException.class, () -> { new Classification("I'm not a Classification."); });
		// currently configured for DevelopmentClassifications, so test that PSPFClassification fails
		assertThrows(IllegalArgumentException.class, () -> { new Classification(PSPFClassification.PROTECTED.name()); });
	}

	@Test
	void classificationSetTest() {
		ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
		builder.unofficial();
		assertTrue(builder.isValid().isEmpty());
		List<InformationManagementMarker> immList = Collections.emptyList();
		ProtectiveMarker expectedMarking = new ProtectiveMarker(Classification.unofficial(), immList, null);
		ProtectiveMarker actualMarking = builder.build();
		assertEquals(expectedMarking, actualMarking);
		
		builder.official();
		assertTrue(builder.isValid().isEmpty());
		expectedMarking = new ProtectiveMarker(Classification.official(), immList, null);
		actualMarking = builder.build();
		assertEquals(expectedMarking, actualMarking);
		
		builder.officialSensitive();
		assertTrue(builder.isValid().isEmpty());
		expectedMarking = new ProtectiveMarker(Classification.officialSensitive(), immList, null);
		actualMarking = builder.build();
		assertEquals(expectedMarking, actualMarking);
		
		builder.protect();
		assertTrue(builder.isValid().isEmpty());
		expectedMarking = new ProtectiveMarker(Classification.protect(), immList, null);
		actualMarking = builder.build();
		assertEquals(expectedMarking, actualMarking);
		
		builder.secret();
		assertTrue(builder.isValid().isEmpty());
		expectedMarking = new ProtectiveMarker(Classification.secret(), immList, null);
		actualMarking = builder.build();
		assertEquals(expectedMarking, actualMarking);
		
		builder.topSecret();
		assertTrue(builder.isValid().isEmpty());
		expectedMarking = new ProtectiveMarker(Classification.topSecret(), immList, null);
		actualMarking = builder.build();
		assertEquals(expectedMarking, actualMarking);
	}
	
	@Test
	void releasabilityWithLowClassificationTest() {
		ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
		builder.unofficial().agao();
		List<String> report = builder.isValid();
		assertEquals(List.of("Attempting to add a Security Caveat when the Classification \"Development UNOFFICIAL\" is lower than \"Development PROTECTED\""), report);
		assertThrows(IllegalStateException.class, () -> { builder.build(); });
		
		builder.official().austeo();
		report = builder.isValid();
		assertEquals(List.of("Attempting to add a Security Caveat when the Classification \"Development OFFICIAL\" is lower than \"Development PROTECTED\""), report);
		assertThrows(IllegalStateException.class, () -> { builder.build(); });
		
		builder.officialSensitive().rel("AUS", "NZL");
		report = builder.isValid();
		assertEquals(List.of("Attempting to add a Security Caveat when the Classification \"Development OFFICIAL: Sensitive\" is lower than \"Development PROTECTED\""), report);
		assertThrows(IllegalStateException.class, () -> { builder.build(); });
	}
	
	@Test
	void releasabilityWithProtectedTest() {
		ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
		builder.protect().austeo();
		ReleasabilityCaveat expectedRC = new ReleasabilityCaveat(ReleasabilityType.AUSTEO, Collections.emptyList());
		SecurityCaveats expectedSC = new SecurityCaveats(Collections.emptyList(), Collections.emptyList(), null, expectedRC);
		ProtectiveMarker expectedMarking = new ProtectiveMarker(Classification.protect(), Collections.emptyList(), expectedSC);
		ProtectiveMarker actualMarking = builder.build();
		assertEquals(expectedMarking, actualMarking);
		
		builder.agao();
		expectedRC = new ReleasabilityCaveat(ReleasabilityType.AGAO, Collections.emptyList());
		expectedSC = new SecurityCaveats(Collections.emptyList(), Collections.emptyList(), null, expectedRC);
		expectedMarking = new ProtectiveMarker(Classification.protect(), Collections.emptyList(), expectedSC);
		actualMarking = builder.build();
		assertEquals(expectedMarking, actualMarking);
		
		builder.rel("AUS", "NZL");
		expectedRC = new ReleasabilityCaveat(ReleasabilityType.REL, List.of("AUS", "NZL"));
		expectedSC = new SecurityCaveats(Collections.emptyList(), Collections.emptyList(), null, expectedRC);
		expectedMarking = new ProtectiveMarker(Classification.protect(), Collections.emptyList(), expectedSC);
		actualMarking = builder.build();
		assertEquals(expectedMarking, actualMarking);
	}
	
	@Test
	void releasabilityWithSecretTest() {
		ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
		builder.secret().austeo();
		ReleasabilityCaveat expectedRC = new ReleasabilityCaveat(ReleasabilityType.AUSTEO, Collections.emptyList());
		SecurityCaveats expectedSC = new SecurityCaveats(Collections.emptyList(), Collections.emptyList(), null, expectedRC);
		ProtectiveMarker expectedMarking = new ProtectiveMarker(Classification.secret(), Collections.emptyList(), expectedSC);
		ProtectiveMarker actualMarking = builder.build();
		assertEquals(expectedMarking, actualMarking);
		
		builder.agao();
		expectedRC = new ReleasabilityCaveat(ReleasabilityType.AGAO, Collections.emptyList());
		expectedSC = new SecurityCaveats(Collections.emptyList(), Collections.emptyList(), null, expectedRC);
		expectedMarking = new ProtectiveMarker(Classification.secret(), Collections.emptyList(), expectedSC);
		actualMarking = builder.build();
		assertEquals(expectedMarking, actualMarking);
		
		builder.rel("AUS", "NZL");
		expectedRC = new ReleasabilityCaveat(ReleasabilityType.REL, List.of("AUS", "NZL"));
		expectedSC = new SecurityCaveats(Collections.emptyList(), Collections.emptyList(), null, expectedRC);
		expectedMarking = new ProtectiveMarker(Classification.secret(), Collections.emptyList(), expectedSC);
		actualMarking = builder.build();
		assertEquals(expectedMarking, actualMarking);
	}
	
	@Test
	void releasabilityWithTopSecretTest() {
		ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
		builder.topSecret().austeo();
		ReleasabilityCaveat expectedRC = new ReleasabilityCaveat(ReleasabilityType.AUSTEO, Collections.emptyList());
		SecurityCaveats expectedSC = new SecurityCaveats(Collections.emptyList(), Collections.emptyList(), null, expectedRC);
		ProtectiveMarker expectedMarking = new ProtectiveMarker(Classification.topSecret(), Collections.emptyList(), expectedSC);
		ProtectiveMarker actualMarking = builder.build();
		assertEquals(expectedMarking, actualMarking);
		
		builder.agao();
		expectedRC = new ReleasabilityCaveat(ReleasabilityType.AGAO, Collections.emptyList());
		expectedSC = new SecurityCaveats(Collections.emptyList(), Collections.emptyList(), null, expectedRC);
		expectedMarking = new ProtectiveMarker(Classification.topSecret(), Collections.emptyList(), expectedSC);
		actualMarking = builder.build();
		assertEquals(expectedMarking, actualMarking);
		
		builder.rel("AUS", "NZL");
		expectedRC = new ReleasabilityCaveat(ReleasabilityType.REL, List.of("AUS", "NZL"));
		expectedSC = new SecurityCaveats(Collections.emptyList(), Collections.emptyList(), null, expectedRC);
		expectedMarking = new ProtectiveMarker(Classification.topSecret(), Collections.emptyList(), expectedSC);
		actualMarking = builder.build();
		assertEquals(expectedMarking, actualMarking);
	}
	
	@Test
	void releasabilityEmptyRelToListTest() {
		ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
		builder.secret().rel();
		
		List<String> report = builder.isValid();
		assertEquals(List.of("Releasable To Lists must contain `AUS`", "Releasable To List must have a minimum size of 2"), report);
		
		assertThrows(IllegalStateException.class, () -> { builder.build(); });
	}
	
	@Test
	void releasabilityRelToOneEntryListTest() {
		ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
		builder.secret();
		builder.setReleasability(ReleasabilityType.REL);
		builder.addReleasableToCountry("AUS");
		
		List<String> report = builder.isValid();
		assertEquals(List.of("Releasable To List must have a minimum size of 2"), report);
		
		assertThrows(IllegalStateException.class, () -> { builder.build(); });
	}
	
	@Test
	void releasabilityRelToMissingAusTest() {
		ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
		builder.secret().rel("CAN", "NZL");
		
		List<String> report = builder.isValid();
		assertEquals(List.of("Releasable To Lists must contain `AUS`"), report);
		
		assertThrows(IllegalStateException.class, () -> { builder.build(); });
	}
	
	@Test
	void releasabilityRelToFiveEyesSortedTest() {
		ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
		builder.secret().rel("NZL", "USA", "AUS", "CAN", "GBR");
		assertTrue(builder.isValid().isEmpty());
		
		ReleasabilityCaveat expectedRC = new ReleasabilityCaveat(ReleasabilityType.REL, List.of("AUS", "CAN", "GBR", "NZL", "USA"));
		SecurityCaveats expectedSC = new SecurityCaveats(Collections.emptyList(), Collections.emptyList(), null, expectedRC);
		ProtectiveMarker expectedMarking = new ProtectiveMarker(Classification.secret(), Collections.emptyList(), expectedSC);
		ProtectiveMarker actualMarking = builder.build();
		assertEquals(expectedMarking, actualMarking);
	}
	
	@Test
	void releasabilityRelToListWithRelTypeAgaoTest() {
		ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
		builder.secret().rel("AUS", "NZL");
		builder.setReleasability(ReleasabilityType.AGAO);
		
		List<String> report = builder.isValid();
		assertEquals(List.of("Cannot have a releasable to list with Releasability Type: AGAO"), report);
		
		assertThrows(IllegalStateException.class, () -> { builder.build(); });
	}
	
	@Test
	void releasabilityRelToListWithRelTypeAusteoTest() {
		ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
		builder.secret().rel("AUS", "NZL");
		builder.setReleasability(ReleasabilityType.AUSTEO);
		
		List<String> report = builder.isValid();
		assertEquals(List.of("Cannot have a releasable to list with Releasability Type: AUSTEO"), report);
		
		assertThrows(IllegalStateException.class, () -> { builder.build(); });
	}
	
	@Test
	void specialHandlingInstructionsWithLowClassificationTest() {
		ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
		builder.unofficial().delicateSource();
		List<String> report = builder.isValid();
		assertEquals(List.of("Attempting to add a Security Caveat when the Classification \"Development UNOFFICIAL\" is lower than \"Development PROTECTED\""), report);
		assertThrows(IllegalStateException.class, () -> { builder.build(); });
		
		builder.official().orcon();
		report = builder.isValid();
		assertEquals(List.of("Attempting to add a Security Caveat when the Classification \"Development OFFICIAL\" is lower than \"Development PROTECTED\""), report);
		assertThrows(IllegalStateException.class, () -> { builder.build(); });
		
		builder.officialSensitive().cabinet();
		report = builder.isValid();
		assertEquals(List.of("Attempting to add a Security Caveat when the Classification \"Development OFFICIAL: Sensitive\" is lower than \"Development PROTECTED\""), report);
		assertThrows(IllegalStateException.class, () -> { builder.build(); });
	}
	
	@Test
	void specialHandlingInstructionsWithProtectedTest() {
		ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
		builder.protect().delicateSource();
		assertTrue(builder.isValid().isEmpty());
		
		SpecialHandlingCaveat expectedSHC = new SpecialHandlingCaveat(SpecialHandlingInstruction.DELICATE_SOURCE, null);
		SecurityCaveats expectedSC = new SecurityCaveats(Collections.emptyList(), Collections.emptyList(), expectedSHC, null);
		ProtectiveMarker expectedPM = new ProtectiveMarker(Classification.protect(), Collections.emptyList(), expectedSC);
		
		assertEquals(expectedPM, builder.build());
	}
	
	@Test
	void specialHandlingInstructionsWithSecretTest() {
		ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
		builder.secret().orcon();
		assertTrue(builder.isValid().isEmpty());
		
		SpecialHandlingCaveat expectedSHC = new SpecialHandlingCaveat(SpecialHandlingInstruction.ORCON, null);
		SecurityCaveats expectedSC = new SecurityCaveats(Collections.emptyList(), Collections.emptyList(), expectedSHC, null);
		ProtectiveMarker expectedPM = new ProtectiveMarker(Classification.secret(), Collections.emptyList(), expectedSC);
		
		assertEquals(expectedPM, builder.build());
	}
	
	@Test
	void specialHandlingInstructionsWithTopSecretTest() {
		ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
		builder.topSecret().cabinet();
		assertTrue(builder.isValid().isEmpty());
		
		SpecialHandlingCaveat expectedSHC = new SpecialHandlingCaveat(SpecialHandlingInstruction.CABINET, null);
		SecurityCaveats expectedSC = new SecurityCaveats(Collections.emptyList(), Collections.emptyList(), expectedSHC, null);
		ProtectiveMarker expectedPM = new ProtectiveMarker(Classification.topSecret(), Collections.emptyList(), expectedSC);
		
		assertEquals(expectedPM, builder.build());
	}
	
	@Test
	void specialHandlingInstructionsAllTest() {
		ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
		builder.secret().delicateSource();
		assertTrue(builder.isValid().isEmpty());
		SpecialHandlingCaveat expectedSHC = new SpecialHandlingCaveat(SpecialHandlingInstruction.DELICATE_SOURCE, null);
		SecurityCaveats expectedSC = new SecurityCaveats(Collections.emptyList(), Collections.emptyList(), expectedSHC, null);
		ProtectiveMarker expectedPM = new ProtectiveMarker(Classification.secret(), Collections.emptyList(), expectedSC);
		assertEquals(expectedPM, builder.build());
		
		builder.orcon();
		assertTrue(builder.isValid().isEmpty());
		expectedSHC = new SpecialHandlingCaveat(SpecialHandlingInstruction.ORCON, null);
		expectedSC = new SecurityCaveats(Collections.emptyList(), Collections.emptyList(), expectedSHC, null);
		expectedPM = new ProtectiveMarker(Classification.secret(), Collections.emptyList(), expectedSC);
		assertEquals(expectedPM, builder.build());
		
		builder.cabinet();
		assertTrue(builder.isValid().isEmpty());
		expectedSHC = new SpecialHandlingCaveat(SpecialHandlingInstruction.CABINET, null);
		expectedSC = new SecurityCaveats(Collections.emptyList(), Collections.emptyList(), expectedSHC, null);
		expectedPM = new ProtectiveMarker(Classification.secret(), Collections.emptyList(), expectedSC);
		assertEquals(expectedPM, builder.build());
		
		builder.exclusiveFor("addressee");
		assertTrue(builder.isValid().isEmpty());
		expectedSHC = new SpecialHandlingCaveat(SpecialHandlingInstruction.EXCLUSIVE_FOR, "addressee");
		expectedSC = new SecurityCaveats(Collections.emptyList(), Collections.emptyList(), expectedSHC, null);
		expectedPM = new ProtectiveMarker(Classification.secret(), Collections.emptyList(), expectedSC);
		assertEquals(expectedPM, builder.build());
		
		builder.setSpecialHandlingInstruction(SpecialHandlingInstruction.NATIONAL_CABINET);
		builder.setSpecialHandlingExclusiveFor(null);
		assertTrue(builder.isValid().isEmpty());
		expectedSHC = new SpecialHandlingCaveat(SpecialHandlingInstruction.NATIONAL_CABINET, null);
		expectedSC = new SecurityCaveats(Collections.emptyList(), Collections.emptyList(), expectedSHC, null);
		expectedPM = new ProtectiveMarker(Classification.secret(), Collections.emptyList(), expectedSC);
		assertEquals(expectedPM, builder.build());
	}
	
	@Test
	void specialHandlingInstructionsExclusiveForNoAddresseeTest() {
		ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
		builder.secret().setSpecialHandlingInstruction(SpecialHandlingInstruction.EXCLUSIVE_FOR);
		
		assertEquals(List.of("No Named recipient for Exclusive For Special Handling Instruction"), builder.isValid());
		
		assertThrows(IllegalStateException.class, () -> { builder.build(); });
	}
	
	@Test
	void specialHandlingInstructionAddresseeWithoutExclusiveForTest() {
		ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
		builder.secret().setSpecialHandlingExclusiveFor("addressee");
		
		assertEquals(List.of("May only have a Named recipient for the Exclusive For Special Handling Instruction. Not for: null"), builder.isValid());
		
		assertThrows(IllegalStateException.class, () -> { builder.build(); });
	}
	
	@Test
	void specialHandlingInstructionAddresseeWithOrconTest() {
		ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
		builder.secret().orcon().setSpecialHandlingExclusiveFor("addressee");
		
		assertEquals(List.of("May only have a Named recipient for the Exclusive For Special Handling Instruction. Not for: ORCON"), builder.isValid());
		
		assertThrows(IllegalStateException.class, () -> { builder.build(); });
	}
	
	@Test
	void codewordsWithLowClassificationTest() {
		ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
		builder.unofficial().addCodeword("AAA");
		List<String> report = builder.isValid();
		assertEquals(List.of("Attempting to add a Security Caveat when the Classification \"Development UNOFFICIAL\" is lower than \"Development PROTECTED\""), report);
		assertThrows(IllegalStateException.class, () -> { builder.build(); });
		
		builder.official();
		report = builder.isValid();
		assertEquals(List.of("Attempting to add a Security Caveat when the Classification \"Development OFFICIAL\" is lower than \"Development PROTECTED\""), report);
		assertThrows(IllegalStateException.class, () -> { builder.build(); });
		
		builder.officialSensitive();
		report = builder.isValid();
		assertEquals(List.of("Attempting to add a Security Caveat when the Classification \"Development OFFICIAL: Sensitive\" is lower than \"Development PROTECTED\""), report);
		assertThrows(IllegalStateException.class, () -> { builder.build(); });
	}
	
	@Test
	void codewordsWithProtectedTest() {
		ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
		builder.protect().addCodeword("AAA");
		SecurityCaveats expectedSC = new SecurityCaveats(List.of("AAA"), Collections.emptyList(), null, null);
		ProtectiveMarker expectedMarking = new ProtectiveMarker(Classification.protect(), Collections.emptyList(), expectedSC);
		ProtectiveMarker actualMarking = builder.build();
		assertEquals(expectedMarking, actualMarking);
	}
	
	@Test
	void codewordsWithSecretTest() {
		ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
		builder.secret().addCodeword("AAA");
		SecurityCaveats expectedSC = new SecurityCaveats(List.of("AAA"), Collections.emptyList(), null, null);
		ProtectiveMarker expectedMarking = new ProtectiveMarker(Classification.secret(), Collections.emptyList(), expectedSC);
		ProtectiveMarker actualMarking = builder.build();
		assertEquals(expectedMarking, actualMarking);
	}
	
	@Test
	void codewordsWithTopSecretTest() {
		ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
		builder.topSecret().addCodeword("AAA");
		SecurityCaveats expectedSC = new SecurityCaveats(List.of("AAA"), Collections.emptyList(), null, null);
		ProtectiveMarker expectedMarking = new ProtectiveMarker(Classification.topSecret(), Collections.emptyList(), expectedSC);
		ProtectiveMarker actualMarking = builder.build();
		assertEquals(expectedMarking, actualMarking);
	}
	
	@Test
	void foreignMarkingsWithLowClassificationTest() {
		ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
		builder.unofficial().addForeignGovernmentMarking("AAA");
		List<String> report = builder.isValid();
		assertEquals(List.of("Attempting to add a Security Caveat when the Classification \"Development UNOFFICIAL\" is lower than \"Development PROTECTED\""), report);
		assertThrows(IllegalStateException.class, () -> { builder.build(); });
		
		builder.official();
		report = builder.isValid();
		assertEquals(List.of("Attempting to add a Security Caveat when the Classification \"Development OFFICIAL\" is lower than \"Development PROTECTED\""), report);
		assertThrows(IllegalStateException.class, () -> { builder.build(); });
		
		builder.officialSensitive();
		report = builder.isValid();
		assertEquals(List.of("Attempting to add a Security Caveat when the Classification \"Development OFFICIAL: Sensitive\" is lower than \"Development PROTECTED\""), report);
		assertThrows(IllegalStateException.class, () -> { builder.build(); });
	}
	
	@Test
	void foreignMarkingWithProtectedTest() {
		ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
		builder.protect().addForeignGovernmentMarking("AAA");
		SecurityCaveats expectedSC = new SecurityCaveats(Collections.emptyList(), List.of("AAA"), null, null);
		ProtectiveMarker expectedMarking = new ProtectiveMarker(Classification.protect(), Collections.emptyList(), expectedSC);
		ProtectiveMarker actualMarking = builder.build();
		assertEquals(expectedMarking, actualMarking);
	}
	
	@Test
	void foreignMarkingWithSecretTest() {
		ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
		builder.secret().addForeignGovernmentMarking("AAA");
		SecurityCaveats expectedSC = new SecurityCaveats(Collections.emptyList(), List.of("AAA"), null, null);
		ProtectiveMarker expectedMarking = new ProtectiveMarker(Classification.secret(), Collections.emptyList(), expectedSC);
		ProtectiveMarker actualMarking = builder.build();
		assertEquals(expectedMarking, actualMarking);
	}
	
	@Test
	void foreignMarkingWithTopSecretTest() {
		ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
		builder.topSecret().addForeignGovernmentMarking("AAA");
		SecurityCaveats expectedSC = new SecurityCaveats(Collections.emptyList(), List.of("AAA"), null, null);
		ProtectiveMarker expectedMarking = new ProtectiveMarker(Classification.topSecret(), Collections.emptyList(), expectedSC);
		ProtectiveMarker actualMarking = builder.build();
		assertEquals(expectedMarking, actualMarking);
	}
	
	@Test
	void informationManagementMarkersAllClassificationsTest() {
		ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
		builder.unofficial().legalPrivilege();
		ProtectiveMarker expectedMarking = new ProtectiveMarker(Classification.unofficial(), List.of(new InformationManagementMarker(InformationManagementTypes.LEGAL_PRIVILEGE, Collections.emptyList())), null);
		ProtectiveMarker actualMarking = builder.build();
		assertEquals(expectedMarking, actualMarking);
		
		builder.official();
		expectedMarking = new ProtectiveMarker(Classification.official(), List.of(new InformationManagementMarker(InformationManagementTypes.LEGAL_PRIVILEGE, Collections.emptyList())), null);
		actualMarking = builder.build();
		assertEquals(expectedMarking, actualMarking);
		
		builder.officialSensitive();
		expectedMarking = new ProtectiveMarker(Classification.officialSensitive(), List.of(new InformationManagementMarker(InformationManagementTypes.LEGAL_PRIVILEGE, Collections.emptyList())), null);
		actualMarking = builder.build();
		assertEquals(expectedMarking, actualMarking);
		
		builder.protect();
		expectedMarking = new ProtectiveMarker(Classification.protect(), List.of(new InformationManagementMarker(InformationManagementTypes.LEGAL_PRIVILEGE, Collections.emptyList())), null);
		actualMarking = builder.build();
		assertEquals(expectedMarking, actualMarking);
		
		builder.secret();
		expectedMarking = new ProtectiveMarker(Classification.secret(), List.of(new InformationManagementMarker(InformationManagementTypes.LEGAL_PRIVILEGE, Collections.emptyList())), null);
		actualMarking = builder.build();
		assertEquals(expectedMarking, actualMarking);
		
		builder.topSecret();
		expectedMarking = new ProtectiveMarker(Classification.topSecret(), List.of(new InformationManagementMarker(InformationManagementTypes.LEGAL_PRIVILEGE, Collections.emptyList())), null);
		actualMarking = builder.build();
		assertEquals(expectedMarking, actualMarking);
	}
	
	@Test
	void infomrationManagementMarkersLegislativeSecrecy() {
		ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
		builder.secret().addLegislativeSecrecy("AAA");
		ProtectiveMarker expectedMarking = new ProtectiveMarker(Classification.secret(), List.of(new InformationManagementMarker(InformationManagementTypes.LEGISLATIVE_SECRECY, List.of("AAA"))), null);
		ProtectiveMarker actualMarking = builder.build();
		assertEquals(expectedMarking, actualMarking);
	}
	
	@Test
	void infomrationManagementMarkersLegislativeSecrecyMissingWarning() {
		ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
		builder.secret();
		assertThrows(IllegalArgumentException.class, () -> { builder.legislativeSecrecy(null); });
	}
	
	@Test
	void infomrationManagementMarkersAllMarkers() {
		ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
		builder.secret().legalPrivilege().legislativeSecrecy("AAA").personalPrivacy();
		ProtectiveMarker expectedMarking = new ProtectiveMarker(Classification.secret(), List.of(
				new InformationManagementMarker(InformationManagementTypes.LEGAL_PRIVILEGE, Collections.emptyList()),
				new InformationManagementMarker(InformationManagementTypes.LEGISLATIVE_SECRECY, List.of("AAA")),
				new InformationManagementMarker(InformationManagementTypes.PERSONAL_PRIVACY, Collections.emptyList())
		), null);
		ProtectiveMarker actualMarking = builder.build();
		assertEquals(expectedMarking, actualMarking);
	}
}

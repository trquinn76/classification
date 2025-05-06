package io.github.trquinn76.classification.nzl;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import io.github.trquinn76.classification.nzl.model.Classification;
import io.github.trquinn76.classification.nzl.model.NZLClassification;
import io.github.trquinn76.classification.nzl.model.NationalSecurityEndorsements;
import io.github.trquinn76.classification.nzl.model.PolicyAndPrivacyEndorsementMarking;
import io.github.trquinn76.classification.nzl.model.PolicyAndPrivacyEndorsements;
import io.github.trquinn76.classification.nzl.model.ProtectiveMarker;
import io.github.trquinn76.classification.nzl.model.ReleasabilityMarking;
import io.github.trquinn76.classification.nzl.model.ReleasabilityTypes;

class ProtectiveMarkerBuilderTest {

    @Test
    void noValueSetTest() {
        ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
        List<String> report = builder.isValid();
        assertEquals(List.of("Classification must be set."), report);
        assertThrows(IllegalStateException.class, () -> {
            builder.build();
        });
    }

    @Test
    void invalidClassificationStringTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Classification("I'm not a Classification.");
        });
        // currently configured for DevelopmentClassifications, so test that
        // NZLClassifications fails
        assertThrows(IllegalArgumentException.class, () -> {
            new Classification(NZLClassification.RESTRICTED.name());
        });
    }

    @Test
    void classificationSetTest() {
        ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();

        ProtectiveMarker expectedMarker = new ProtectiveMarker(Classification.unclassified(), Collections.emptyList(), null);
        ProtectiveMarker actualMarker = builder.unclassified().build();
        assertEquals(expectedMarker, actualMarker);

        expectedMarker = new ProtectiveMarker(Classification.inConfidence(), Collections.emptyList(), null);
        actualMarker = builder.inConfidence().build();
        assertEquals(expectedMarker, actualMarker);

        expectedMarker = new ProtectiveMarker(Classification.sensitive(), Collections.emptyList(), null);
        actualMarker = builder.sensitive().build();
        assertEquals(expectedMarker, actualMarker);

        expectedMarker = new ProtectiveMarker(Classification.restricted(), Collections.emptyList(), null);
        actualMarker = builder.restricted().build();
        assertEquals(expectedMarker, actualMarker);

        expectedMarker = new ProtectiveMarker(Classification.confidential(), Collections.emptyList(), null);
        actualMarker = builder.confidental().build();
        assertEquals(expectedMarker, actualMarker);

        expectedMarker = new ProtectiveMarker(Classification.secret(), Collections.emptyList(), null);
        actualMarker = builder.secret().build();
        assertEquals(expectedMarker, actualMarker);

        NationalSecurityEndorsements ncEndorsements = new NationalSecurityEndorsements(true, Collections.emptyList(), Collections.emptyList(), null);
        expectedMarker = new ProtectiveMarker(Classification.topSecret(), Collections.emptyList(), ncEndorsements);
        actualMarker = builder.topSecret().build();
        assertEquals(expectedMarker, actualMarker);
    }

    @Test
    void copyConstructorPolicyAndPrivacyTest() {
        ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
        ProtectiveMarker marker = builder.sensitive().medical().toBeReviewedOn(LocalDateTime.of(2025, 5, 2, 10, 5)).build();
        ProtectiveMarkerBuilder copy = new ProtectiveMarkerBuilder(marker);
        assertEquals(builder, copy);
    }
    
    @Test
    void copyConstructorNationalSecurityTest() {
        ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
        ProtectiveMarker marker = builder.confidental().relTo("NZL", "CAN", "GBR").disseminationMarks("ORCON").build();
        ProtectiveMarkerBuilder copy = new ProtectiveMarkerBuilder(marker);
        assertEquals(builder, copy);
    }

    @Test
    void topSecretWithNoAccountableMaterialTest() {
        ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
        // ACCOUNTABLE MATERIAL must be set for Top Secret.
        assertThrows(IllegalStateException.class, () -> builder.setClassification(Classification.topSecret()).build());
    }

    @Test
    void topSecretFunctionImplicitlySetsAccountableMaterialTest() {
        ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
        ProtectiveMarker marker = builder.clear().topSecret().build();
        assertTrue(marker.nationalSecurityEndorsements().accountableMaterial());
    }

    @Test
    void sensitiveCompartmentsTest() {
        ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
        builder.confidental();

        builder.sensitiveCompartments("AAA", "DDD", "BBB");
        ProtectiveMarker marker = builder.build();
        assertEquals(List.of("AAA", "BBB", "DDD"), marker.nationalSecurityEndorsements().sensitiveCompartments());

        builder.addSensitiveCompartment("CCC");
        marker = builder.build();
        assertEquals(List.of("AAA", "BBB", "CCC", "DDD"), marker.nationalSecurityEndorsements().sensitiveCompartments());

        assertEquals(Set.of("AAA", "BBB", "CCC", "DDD"), builder.getSensitiveCompartments());
    }

    @Test
    void releasabilityEmptyRelToListTest() {
        ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
        builder.secret().relTo();

        List<String> report = builder.isValid();
        assertEquals(
                List.of("Releasable To Lists must contain `NZL`", "Releasable To List must have a minimum size of 2"),
                report);

        assertThrows(IllegalStateException.class, () -> {
            builder.build();
        });
    }

    @Test
    void releasabilityWithSensitiveTest() {
        ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
        builder.sensitive().nzeo();
        assertThrows(IllegalStateException.class, () -> { builder.build(); });
    }

    @Test
    void releasabilityWithRestrictedTest() {
        ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
        ProtectiveMarker actualMarker = builder.restricted().nzeo().build();
        ReleasabilityMarking releasabilityMarking = new ReleasabilityMarking(ReleasabilityTypes.NZEO,
                Collections.emptyList());
        NationalSecurityEndorsements ncEndorsements = new NationalSecurityEndorsements(false, Collections.emptyList(),
                Collections.emptyList(), releasabilityMarking);
        ProtectiveMarker expectedMarker = new ProtectiveMarker(Classification.restricted(), Collections.emptyList(),
                ncEndorsements);
        assertEquals(expectedMarker, actualMarker);

        actualMarker = builder.relTo("NZL", "AUS", "CAN").build();
        releasabilityMarking = new ReleasabilityMarking(ReleasabilityTypes.RELTO, List.of("NZL", "AUS", "CAN"));
        ncEndorsements = new NationalSecurityEndorsements(false, Collections.emptyList(), Collections.emptyList(),
                releasabilityMarking);
        expectedMarker = new ProtectiveMarker(Classification.restricted(), Collections.emptyList(), ncEndorsements);
        assertEquals(expectedMarker, actualMarker);
    }

    @Test
    void releasabilityWithSecretTest() {
        ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
        ProtectiveMarker actualMarker = builder.secret().nzeo().build();
        ReleasabilityMarking releasabilityMarking = new ReleasabilityMarking(ReleasabilityTypes.NZEO,
                Collections.emptyList());
        NationalSecurityEndorsements ncEndorsements = new NationalSecurityEndorsements(false, Collections.emptyList(),
                Collections.emptyList(), releasabilityMarking);
        ProtectiveMarker expectedMarker = new ProtectiveMarker(Classification.secret(), Collections.emptyList(), ncEndorsements);
        assertEquals(expectedMarker, actualMarker);

        actualMarker = builder.relTo("NZL", "AUS", "CAN").build();
        releasabilityMarking = new ReleasabilityMarking(ReleasabilityTypes.RELTO, List.of("NZL", "AUS", "CAN"));
        ncEndorsements = new NationalSecurityEndorsements(false, Collections.emptyList(), Collections.emptyList(),
                releasabilityMarking);
        expectedMarker = new ProtectiveMarker(Classification.secret(), Collections.emptyList(), ncEndorsements);
        assertEquals(expectedMarker, actualMarker);
    }

    @Test
    void releasabilityWithTopSecretTest() {
        ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
        ProtectiveMarker actualMarker = builder.topSecret().nzeo().build();
        ReleasabilityMarking releasabilityMarking = new ReleasabilityMarking(ReleasabilityTypes.NZEO,
                Collections.emptyList());
        NationalSecurityEndorsements ncEndorsements = new NationalSecurityEndorsements(true, Collections.emptyList(),
                Collections.emptyList(), releasabilityMarking);
        ProtectiveMarker expectedMarker = new ProtectiveMarker(Classification.topSecret(), Collections.emptyList(),
                ncEndorsements);
        assertEquals(expectedMarker, actualMarker);

        actualMarker = builder.relTo("NZL", "AUS", "CAN").build();
        releasabilityMarking = new ReleasabilityMarking(ReleasabilityTypes.RELTO, List.of("NZL", "AUS", "CAN"));
        ncEndorsements = new NationalSecurityEndorsements(true, Collections.emptyList(), Collections.emptyList(),
                releasabilityMarking);
        expectedMarker = new ProtectiveMarker(Classification.topSecret(), Collections.emptyList(), ncEndorsements);
        assertEquals(expectedMarker, actualMarker);
    }

    @Test
    void appointmentsTest() {
        ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
        ProtectiveMarker marker = builder.inConfidence().appointments().build();
        List<PolicyAndPrivacyEndorsementMarking> expectedEndorsements = List
                .of(new PolicyAndPrivacyEndorsementMarking(PolicyAndPrivacyEndorsements.APPOINTMENTS, null));

        assertEquals(expectedEndorsements, marker.policyAndPrivacyEndorsements());
    }

    @Test
    void budgetTest() {
        ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
        ProtectiveMarker marker = builder.inConfidence().budget().build();
        List<PolicyAndPrivacyEndorsementMarking> expectedEndorsements = List
                .of(new PolicyAndPrivacyEndorsementMarking(PolicyAndPrivacyEndorsements.BUDGET, null));

        assertEquals(expectedEndorsements, marker.policyAndPrivacyEndorsements());
    }

    @Test
    void cabinetTest() {
        ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
        ProtectiveMarker marker = builder.inConfidence().cabinet().build();
        List<PolicyAndPrivacyEndorsementMarking> expectedEndorsements = List
                .of(new PolicyAndPrivacyEndorsementMarking(PolicyAndPrivacyEndorsements.CABINET, null));

        assertEquals(expectedEndorsements, marker.policyAndPrivacyEndorsements());
    }

    @Test
    void commercialTest() {
        ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
        ProtectiveMarker marker = builder.inConfidence().commercial().build();
        List<PolicyAndPrivacyEndorsementMarking> expectedEndorsements = List
                .of(new PolicyAndPrivacyEndorsementMarking(PolicyAndPrivacyEndorsements.COMMERCIAL, null));

        assertEquals(expectedEndorsements, marker.policyAndPrivacyEndorsements());
    }

    @Test
    void departmentUseOnlyTest() {
        ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
        ProtectiveMarker marker = builder.inConfidence().departmentUseOnly("Thingy").build();
        List<PolicyAndPrivacyEndorsementMarking> expectedEndorsements = List
                .of(new PolicyAndPrivacyEndorsementMarking(PolicyAndPrivacyEndorsements.DEPARTMENT_USE_ONLY, "Thingy"));

        assertEquals(expectedEndorsements, marker.policyAndPrivacyEndorsements());
    }

    @Test
    void embargoedForReleaseTest() {
        ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
        ProtectiveMarker marker = builder.inConfidence().embargoedForRelease(LocalDateTime.of(2025, 5, 9, 6, 30))
                .build();
        List<PolicyAndPrivacyEndorsementMarking> expectedEndorsements = List.of(new PolicyAndPrivacyEndorsementMarking(
                PolicyAndPrivacyEndorsements.EMBARGOED_FOR_RELEASE, "2025-05-09 06:30"));

        assertEquals(expectedEndorsements, marker.policyAndPrivacyEndorsements());
    }

    @Test
    void evaluateTest() {
        ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
        ProtectiveMarker marker = builder.inConfidence().evaluate().build();
        List<PolicyAndPrivacyEndorsementMarking> expectedEndorsements = List
                .of(new PolicyAndPrivacyEndorsementMarking(PolicyAndPrivacyEndorsements.EVALUATE, null));

        assertEquals(expectedEndorsements, marker.policyAndPrivacyEndorsements());
    }

    @Test
    void honoursTest() {
        ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
        ProtectiveMarker marker = builder.inConfidence().honours().build();
        List<PolicyAndPrivacyEndorsementMarking> expectedEndorsements = List
                .of(new PolicyAndPrivacyEndorsementMarking(PolicyAndPrivacyEndorsements.HONOURS, null));

        assertEquals(expectedEndorsements, marker.policyAndPrivacyEndorsements());
    }

    @Test
    void legalPrivilegeTest() {
        ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
        ProtectiveMarker marker = builder.inConfidence().legalPrivilege().build();
        List<PolicyAndPrivacyEndorsementMarking> expectedEndorsements = List
                .of(new PolicyAndPrivacyEndorsementMarking(PolicyAndPrivacyEndorsements.LEGAL_PRIVILEGE, null));

        assertEquals(expectedEndorsements, marker.policyAndPrivacyEndorsements());
    }

    @Test
    void medicalTest() {
        ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
        ProtectiveMarker marker = builder.inConfidence().medical().build();
        List<PolicyAndPrivacyEndorsementMarking> expectedEndorsements = List
                .of(new PolicyAndPrivacyEndorsementMarking(PolicyAndPrivacyEndorsements.MEDICAL, null));

        assertEquals(expectedEndorsements, marker.policyAndPrivacyEndorsements());
    }

    @Test
    void staffTest() {
        ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
        ProtectiveMarker marker = builder.inConfidence().staff().build();
        List<PolicyAndPrivacyEndorsementMarking> expectedEndorsements = List
                .of(new PolicyAndPrivacyEndorsementMarking(PolicyAndPrivacyEndorsements.STAFF, null));

        assertEquals(expectedEndorsements, marker.policyAndPrivacyEndorsements());
    }

    @Test
    void policyTest() {
        ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
        ProtectiveMarker marker = builder.inConfidence().policy().build();
        List<PolicyAndPrivacyEndorsementMarking> expectedEndorsements = List
                .of(new PolicyAndPrivacyEndorsementMarking(PolicyAndPrivacyEndorsements.POLICY, null));

        assertEquals(expectedEndorsements, marker.policyAndPrivacyEndorsements());
    }

    @Test
    void toBeReviewedOnTest() {
        ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
        ProtectiveMarker marker = builder.inConfidence().toBeReviewedOn(LocalDateTime.of(2025, 5, 9, 6, 30)).build();
        List<PolicyAndPrivacyEndorsementMarking> expectedEndorsements = List.of(new PolicyAndPrivacyEndorsementMarking(
                PolicyAndPrivacyEndorsements.TO_BE_REVIEWED_ON, "2025-05-09 06:30"));

        assertEquals(expectedEndorsements, marker.policyAndPrivacyEndorsements());
    }

    @Test
    void departmentUseOnlyMissingDepartmentTest() {
        ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
        builder.inConfidence().addPolicyAndPrivacyEndorsement(PolicyAndPrivacyEndorsements.DEPARTMENT_USE_ONLY);

        assertThrows(IllegalStateException.class, () -> {
            builder.build();
        });
    }

    @Test
    void embargoedForReleaseMissingDateTest() {
        ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
        builder.inConfidence().addPolicyAndPrivacyEndorsement(PolicyAndPrivacyEndorsements.EMBARGOED_FOR_RELEASE);

        assertThrows(IllegalStateException.class, () -> {
            builder.build();
        });
    }

    @Test
    void toBeReviewedOnMissingDateTest() {
        ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
        builder.inConfidence().addPolicyAndPrivacyEndorsement(PolicyAndPrivacyEndorsements.TO_BE_REVIEWED_ON);

        assertThrows(IllegalStateException.class, () -> {
            builder.build();
        });
    }

    @Test
    void departmentSetWithNoDepartmentUseOnlyTest() {
        ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
        builder.inConfidence().addDepartmentUseOnly("Thingy");

        assertThrows(IllegalStateException.class, () -> {
            builder.build();
        });
    }

    @Test
    void embargoedForReleaseDateSetWithNoEmbargoedForReleaseTest() {
        ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
        builder.inConfidence().setEmbargoedForReleaseTime(LocalDateTime.now());

        assertThrows(IllegalStateException.class, () -> {
            builder.build();
        });
    }

    @Test
    void toBeReviewedOnDateSetWithNoToBeReviewedOnTest() {
        ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
        builder.inConfidence().setToBeReviewedOnTime(LocalDateTime.now());

        assertThrows(IllegalStateException.class, () -> {
            builder.build();
        });
    }

    @Test
    void multipleDepartmentsTest() {
        ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
        ProtectiveMarker marker = builder.sensitive().departmentUseOnly("CCC", "AAA", "BBB").build();
        assertEquals("AAA, BBB, CCC", marker.policyAndPrivacyEndorsements().get(0).timeOrUseOnlyValue());

        ProtectiveMarkerBuilder otherBuilder = new ProtectiveMarkerBuilder(marker);
        assertEquals(builder, otherBuilder);
    }
    
    @Test
    void unclassifiedAndEndorsementsTest() {
        ProtectiveMarkerBuilder builder = new ProtectiveMarkerBuilder();
        builder.unclassified();
        assertThrows(IllegalStateException.class, () -> { builder.budget().build(); });
        
        builder.clearPolicyAndPrivacyEndorsements();
        assertThrows(IllegalStateException.class, () -> { builder.accountableMaterial().build(); });
        
        builder.clearNationalSecurityEndorsements();
        ProtectiveMarker marker = builder.build();
        
        ProtectiveMarker expected = new ProtectiveMarker(Classification.unclassified(), Collections.emptyList(), null);
        assertEquals(expected, marker);
    }
}

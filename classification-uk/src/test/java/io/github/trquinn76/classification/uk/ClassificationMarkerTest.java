package io.github.trquinn76.classification.uk;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.github.trquinn76.classification.uk.model.Classification;
import io.github.trquinn76.classification.uk.model.ClassificationMarker;
import io.github.trquinn76.classification.uk.model.SecurityClassification;

class ClassificationMarkerTest {

    final static List<String> EMPTY = Collections.emptyList();

    @Test
    void noValueSetTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
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
        // PSPFClassification fails
        assertThrows(IllegalArgumentException.class, () -> {
            new Classification(SecurityClassification.OFFICIAL.name());
        });
    }

    @Test
    void relEuRequiresUkPrefixTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.official().addRelEu();

        assertFalse(builder.isValid().isEmpty());

        builder.ukPrefix();
        assertTrue(builder.isValid().isEmpty());
    }

    @Test
    void classificationSetTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.official();
        assertTrue(builder.isValid().isEmpty());
        ClassificationMarker expectedMarker = new ClassificationMarker(false, Classification.official(), false, EMPTY,
                EMPTY, EMPTY, EMPTY, EMPTY);
        ClassificationMarker actualMarker = builder.build();
        assertEquals(expectedMarker, actualMarker);

        builder.officialSensitive();
        assertTrue(builder.isValid().isEmpty());
        expectedMarker = new ClassificationMarker(false, Classification.official(), true, EMPTY, EMPTY, EMPTY, EMPTY,
                EMPTY);
        actualMarker = builder.build();
        assertEquals(expectedMarker, actualMarker);

        builder.secret();
        assertTrue(builder.isValid().isEmpty());
        expectedMarker = new ClassificationMarker(false, Classification.secret(), false, EMPTY, EMPTY, EMPTY, EMPTY,
                EMPTY);
        actualMarker = builder.build();
        assertEquals(expectedMarker, actualMarker);

        builder.topSecret();
        assertTrue(builder.isValid().isEmpty());
        expectedMarker = new ClassificationMarker(false, Classification.topSecret(), false, EMPTY, EMPTY, EMPTY, EMPTY,
                EMPTY);
        actualMarker = builder.build();
        assertEquals(expectedMarker, actualMarker);

        builder.setSensitiveMark(true);
        assertFalse(builder.isValid().isEmpty());
    }

    @Test
    void handlingInstructionRecipientsOnlyTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.official().recipientsOnly();
        assertFalse(builder.isValid().isEmpty());

        builder.officialSensitive();
        assertTrue(builder.isValid().isEmpty());

        builder.secret();
        assertTrue(builder.isValid().isEmpty());

        builder.topSecret();
        assertTrue(builder.isValid().isEmpty());
    }

    @Test
    void handlingInstructionForPublicReleaseTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.forPublicRelease();

        builder.topSecret();
        assertFalse(builder.isValid().isEmpty());

        builder.secret();
        assertFalse(builder.isValid().isEmpty());

        builder.officialSensitive();
        assertFalse(builder.isValid().isEmpty());

        builder.official();
        assertTrue(builder.isValid().isEmpty());
    }

    @Test
    void handlingInstructionOrganisationUseOnlyTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.official();
        builder.addHandlingInstruction(Utils.USE_ONLY);

        assertFalse(builder.isValid().isEmpty());

        builder.addUseOnlyOrganisation("AAA");
        assertTrue(builder.isValid().isEmpty());

        builder.clear();

        builder.secret().useOnly("AAA", "BBB");
        assertTrue(builder.isValid().isEmpty());
    }

    @Test
    void handlingInstructionHmgUseOnlyTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.official().hmgUseOnly();
        assertTrue(builder.isValid().isEmpty());

        builder.officialSensitive();
        assertTrue(builder.isValid().isEmpty());

        builder.secret();
        assertFalse(builder.isValid().isEmpty());

        builder.topSecret();
        assertFalse(builder.isValid().isEmpty());
    }

    @Test
    void handlingInstructionEmbargoedTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.official().embargoed();
        assertTrue(builder.isValid().isEmpty());

        builder.secret();
        assertTrue(builder.isValid().isEmpty());

        builder.topSecret();
        assertFalse(builder.isValid().isEmpty());
    }

    @Test
    void descriptorLegalTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.official().legal();
        assertTrue(builder.isValid().isEmpty());

        builder.officialSensitive();
        assertTrue(builder.isValid().isEmpty());

        builder.secret();
        assertFalse(builder.isValid().isEmpty());

        builder.topSecret();
        assertFalse(builder.isValid().isEmpty());
    }

    @Test
    void nationalCaveatsWithHiClassificationOnlyTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.eyesOnly(Utils.UK, "AA", "BB");

        builder.topSecret();
        assertTrue(builder.isValid().isEmpty());

        builder.secret();
        assertTrue(builder.isValid().isEmpty());

        builder.officialSensitive();
        assertFalse(builder.isValid().isEmpty());

        builder.official();
        assertFalse(builder.isValid().isEmpty());
    }

    @Test
    void nationalCaveatsMustIncludeUkTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        builder.secret().eyesOnly("AA", "BB");
        assertFalse(builder.isValid().isEmpty());

        builder.addEyesOnlyCountry(Utils.FIVE);
        assertTrue(builder.isValid().isEmpty());

        builder.clearEyesOnly().eyesOnly(Utils.UK);
        assertTrue(builder.isValid().isEmpty());
    }

    @Test
    void toStringTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        ClassificationMarker marker = builder.officialSensitive().addAdditionalInstruction("I'm a Teapot")
                .useOnly("AAA", "BBB").hmgUseOnly().commercial().marketSensitive().codeWords("TINY", "BIG").build();

        String expectedStr = ClassificationConfig.developmentOfficialName() + ClassificationConfig.sensitiveMark()
                + " - AAA BBB USE ONLY HMG USE ONLY - COMMERCIAL MARKET SENSITIVE - BIG TINY\nI'm a Teapot";
        String actualStr = marker.toString();

        assertEquals(expectedStr, actualStr);
    }

    @Test
    void copyConstructorTest() {
        ClassificationMarkerBuilder builder = new ClassificationMarkerBuilder();
        ClassificationMarker marker = builder.secret().eyesOnly(Utils.UK).ukPrefix().addCodeWord("APPLE")
                .marketSensitive().useOnly("AAA", "BBB", "CCC").embargoed().build();

        ClassificationMarkerBuilder other = new ClassificationMarkerBuilder(marker);
        assertEquals(builder, other);
    }
}

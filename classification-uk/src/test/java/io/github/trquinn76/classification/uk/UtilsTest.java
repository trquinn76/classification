package io.github.trquinn76.classification.uk;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;

class UtilsTest {

    @Test
    void ukFirst() {
        Set<String> initialSet = Set.of("BB", "DD", "CC", "UK", "AA");
        TreeSet<String> orderedSet = new TreeSet<>(Utils.UK_FIRST);
        orderedSet.addAll(initialSet);
        String[] actualOrder = orderedSet.toArray(new String[5]);
        
        String[] expectedOrder = { "UK", "AA", "BB", "CC", "DD" };
        assertArrayEquals(expectedOrder, actualOrder);
    }
    
    @Test
    void alphabeticalOrder() {
        Set<String> initialSet = Set.of("BB", "DD", "CC", "UK", "AA", "UU");
        TreeSet<String> orderedSet = new TreeSet<>(Utils.ALPHABETICAL);
        orderedSet.addAll(initialSet);
        String[] actualOrder = orderedSet.toArray(new String[5]);
        
        String[] expectedOrder = { "AA", "BB", "CC", "DD", "UK", "UU" };
        assertArrayEquals(expectedOrder, actualOrder);
    }

    @Test
    void endsInOrganisationUseOnlyTest() {
        String orgUseOnlyStr = "Test " + Utils.USE_ONLY;
        String hmgUseOnlyStr = Utils.HMG_USE_ONLY;
        
        assertTrue(Utils.endsInOrganisationUseOnly(orgUseOnlyStr));
        assertFalse(Utils.endsInOrganisationUseOnly(hmgUseOnlyStr));
    }
}

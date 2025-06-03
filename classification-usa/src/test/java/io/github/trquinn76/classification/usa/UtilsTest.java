package io.github.trquinn76.classification.usa;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;

class UtilsTest {

    @Test
    void usaFirstTest() {
        ArrayList<String> countries = new ArrayList<>(List.of("BBB", "FFF", "DDD", "CCC", "USA", "AAA", "EEEE", "AAAA"));
        countries.sort(Utils.USA_FIRST);
        List<String> expectedOrder = List.of("USA", "AAA", "BBB", "CCC", "DDD", "FFF", "AAAA", "EEEE");
        assertArrayEquals(toStringArray(expectedOrder), toStringArray(countries));
    }
    
    @Test
    void alphabeticTest() {
        ArrayList<String> countries = new ArrayList<>(List.of("EEE", "AAAAA", "ZZZZ", "CCC", "BBB", "DDD", "AAA", "HHHH"));
        countries.sort(Utils.ALPHABETIC);
        List<String> expectedOrder = List.of("AAA", "BBB", "CCC", "DDD", "EEE", "HHHH", "ZZZZ", "AAAAA");
        assertArrayEquals(toStringArray(expectedOrder), toStringArray(countries));
    }
    
    @Test
    void alphanumericTest() {
        ArrayList<String> countries = new ArrayList<>(List.of("AAA", "GGG", "123", "1AA", "10A", "C12", "C1", "C10", "C2"));
        countries.sort(Utils.ALPHANUMERIC);
        List<String> expectedOrder = List.of("1AA", "10A", "123", "AAA", "C1", "C2", "C10", "C12", "GGG");
        assertArrayEquals(toStringArray(expectedOrder), toStringArray(countries));
    }

    private static String[] toStringArray(Collection<String> collection) {
        return collection.toArray(new String[collection.size()]);
    }
}

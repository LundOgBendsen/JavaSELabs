package dk.logb.javase.labs.sayings;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilitiesTest {

    @Test
    void levenshteinDistance() {
        assertEquals(0, Utilities.levenshteinDistance("same", "same"));
        assertEquals(1, Utilities.levenshteinDistance("same", "same1"));
        assertEquals(1, Utilities.levenshteinDistance("sme", "same"));
        assertEquals(1, Utilities.levenshteinDistance("same", "sam"));
        assertEquals(2, Utilities.levenshteinDistance("sm", "same"));
        assertEquals(3, Utilities.levenshteinDistance("1234", "345"));
        assertEquals(5, Utilities.levenshteinDistance("01234", "56789"));

    }
}
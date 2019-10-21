package com.teamn.crypto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Simple test to ensure the retriever returns random lines.
 */
class PhraseRetrieverTest {

    private static PhraseRetriever pr;
    private final int MIN_LENGTH = 40;
    private final int MAX_LENGTH = 100;

    @BeforeAll
    static void setup(){
        pr = new PhraseRetriever("sentences");
    }

    @Test
    void testSuccessfulReturn() {
        String str = pr.getRandomPhrase();
        assertNotNull(str);
        assertTrue(str.length()>0);
        assertTrue(str.length()>MIN_LENGTH);
        assertTrue(str.length()<MAX_LENGTH);
    }

    /**
     * Test it does not produce the same phrase all the time
     */
    @Test
    void testPhrasesAreRelativelyRandom() {
        final int RUNS = 100;
        int numberOfTimesEqual = 0;

        String str1, str2;
        for(int i = 0; i < RUNS; i++) {
            str1 = pr.getRandomPhrase();
            str2 = pr.getRandomPhrase();
            if(str1.equals(str2))
                numberOfTimesEqual++;
        }

        assertTrue(numberOfTimesEqual < RUNS);
    }
}
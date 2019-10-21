package com.teamn.crypto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class FrequencyManagerTest {

    private final static String A_CHAR_FREQUENCY = "8.12";
    private final static String B_CHAR_FREQUENCY = "1.49";
    private final static String C_CHAR_FREQUENCY = "2.71";

    @Test
    public void testInternalStringFrequency() {
        String testString = "aabbccd";
        Map<String,String> frequencies = FrequencyManager.getInternalFrequenciesForCharactersString(testString);
        Assertions.assertEquals(frequencies.get("a"), "28.57");
        Assertions.assertEquals(frequencies.get("b"), "28.57");
        Assertions.assertEquals(frequencies.get("c"), "28.57");
        Assertions.assertEquals(frequencies.get("d"), "14.29");
    }

    @Test
    public void testEnglishStringFrequency() {
        String testString = "abc";
        Map<String,String> frequencies = FrequencyManager.getEnglishFrequencies();
        Assertions.assertEquals(frequencies.get("a"), A_CHAR_FREQUENCY);
        Assertions.assertEquals(frequencies.get("b"), B_CHAR_FREQUENCY);
        Assertions.assertEquals(frequencies.get("c"), C_CHAR_FREQUENCY);
    }
}

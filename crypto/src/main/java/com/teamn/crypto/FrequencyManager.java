package com.teamn.crypto;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Supplies frequency information about characters in phrases
 * or in the english language
 */
public class FrequencyManager {

    private final static int ASCII_START_LOWERCASE = 0x61;
    private final static int ENGLISH_CHARS_N = 26;
    private final static double[] ENGLISH_C_FREQUENCIES = {8.12,1.49,2.71,4.32,12.02,2.30,2.03,5.92,7.31,0.10,0.69,3.98,2.61,6.95,7.68,1.82,0.11,6.02,6.28,9.10,2.88,1.11,2.09,0.17,2.11,0.07};

    /**
     * Counts the number of occurrences of each character in a string
     * @param string
     * @return A map from character - number of occurrences
     */
    private static Map<Character, Integer> countCharacterOccurrences(String string) {
        if (string == null) return null;

        Map<Character, Integer> map = new HashMap<>();
        for(Character c: string.toCharArray()) {
            int previousVal = map.getOrDefault(c, 0);
            map.put(c, previousVal+1);
        }

        return map;
    }

    /**
     * Returns the frequency for a given character in the english language
     * @param c the character
     * @return the frequency - between 0 and 100
     */
    private static double frequencyForCharacter(Character c) {
        Character cl = Character.toLowerCase(c);
        int index = cl-ASCII_START_LOWERCASE;
        if (index > ENGLISH_C_FREQUENCIES.length || index < 0) return -1;
        return ENGLISH_C_FREQUENCIES[index];
    }


    /**
     * For every character in a string, get its frequency as a percentage within that string
     * @param string
     * @return a map from each character to its frequency within that string (percentage formatted as a string)
     */
    public static Map<String,String> getInternalFrequenciesForCharactersString(String string) {
        if (string == null) return null;

        Map<String,String> frequencies = new HashMap<>();
        Set<Character> characterSet = string.chars().mapToObj(c -> (char)c).collect(Collectors.toSet());
        int totalChars = string.replace(",", "").length();

        Map<Character, Integer> occurrences = FrequencyManager.countCharacterOccurrences(string);

        for (Character c: characterSet) {
            if (c.equals(' ')) continue;
            frequencies.put(c.toString(),String.format("%.2f",((double)occurrences.get(c) / (double)totalChars)*100));
        }

        return frequencies;
    }

    /**
     * For every character in a string, get its frequency as a percentage within that string
     * @param string
     * @return a map from each character to its frequency within that string (percentage formatted as a string)
     */
    public static Map<String,String> getInternalFrequenciesForNumbersString(String string) {
        if (string == null) return null;

        Map<String,String> frequencies = new HashMap<>();
        List<String> characterList = Arrays.asList(string.split(","));

        Set<String> characterSet = new HashSet<>(characterList);

        int totalChars = characterList.size();

        Map<String, Integer> occurrences = new HashMap<>();
        for (String c: characterList) {
            int currentN = occurrences.getOrDefault(c, 0);
                occurrences.put(c, currentN+1);
        }

        for (String c: characterSet) {
            frequencies.put(c,String.format("%.2f",((double)occurrences.get(c) / (double)totalChars)*100));
        }

        return frequencies;
    }

    /**
     * For each character in a string, get its frequency as a percentage in the english language
     * @return a map from each character to its frequency with the language (percentage formatted as a string)
     */
    public static Map<String, String> getEnglishFrequencies() {

        Map<String,String> frequencies = new HashMap<>();

        for (int i = ASCII_START_LOWERCASE+ENGLISH_CHARS_N-1; i >= ASCII_START_LOWERCASE; i--) {
            Character c = (char) i;
            frequencies.put(c.toString(), String.format("%.2f",frequencyForCharacter(c)));
        }

        return frequencies;
    }

}

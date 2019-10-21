package com.teamn.crypto;

import java.io.Serializable;
import java.util.*;
import java.util.stream.IntStream;

/**
 * Represents a cryptogram state
 */
import static java.util.stream.Collectors.toList;

public abstract class Cryptogram implements Serializable{

    protected final static int ASCII_BASE_ALPHABET = 65;

    protected String phrase;
    protected List<String> cypherText;
    protected Map<String, String> correctness;

    public Cryptogram(String sentencesFile) {
        PhraseRetriever pr = new PhraseRetriever(sentencesFile);
        phrase = pr.getRandomPhrase();
        this.cypherText = this.encryptString();
    }

    /**
     * Generates a list the size of the alphabet with a new mapping between a given character
     * (represented by the index) to a new character (represented by the value at the index)
     * @param alphabetSize Te number of elements in the alphabet
     * @return List with randomised allocations
     */
    List<Integer> generateRandomCharacterAllocations(int alphabetSize) {
        List<Integer> allocations = IntStream.range(0, alphabetSize).boxed().collect(toList());
        Collections.shuffle(allocations);
        return allocations;
    }

    /**
     * Get the current plaintext phrase
     * @return the plaintext phrase
     */
    public String getPhrase()
    {
        return phrase;
    }

    /**
     * Get the frequencies of the cypher characters.
     * @return a map of cypher numbers (as strings) to frequencies.
     */
    public Map<String, Integer> getCypherTextFrequencies() {
        Map<String, Integer> frequencies = new HashMap<>();
        for (String s: this.cypherText) {
            if(!frequencies.containsKey(s))
                frequencies.put(s,Collections.frequency(this.cypherText,s));
        } return frequencies;
    }

    /**
     * Gets the current cypher text
     * @return the cypher text
     */
    public List<String> getCypherText(){
        return cypherText;
    }

    /**
     * Gets the correct mappings of cyphercharacter and plaintext character
     * @return correct mappings
     */
    public Map<String, String> getCorrectMappings() {
        return correctness;
    }

    @Override
    public String toString() {
        StringBuilder ctext = new StringBuilder();
        for(String s: cypherText)
            ctext.append(s).append(" ");

        return ctext.toString();
    }

    /**
     * Gets the alphabet of symbols used in the instance of Cryptogram
     * @return the alphabet
     */
    public abstract List<String> getAlphabet();

    /**
     * Encrypts the given string with a randomly generated character allocation.
     * @return the encrypted string
     */
    protected abstract  List<String> encryptString();

    /**
     *
     * @param c The character to check for
     * @return True if the Ciphertext contains that character
     */
    public boolean cyphertextContainsChar(String c) {
        return this.cypherText.contains(c);
    }



    /**
     * Checks if the guesses supplied complete the cryptogram
     *
     * @return True if all guesses are correct and complete the cryptogram
     */
    public boolean isCorrect(Map<String, Character> guesses){
        Character userguess;
        Character actualChar;
        for(int i = 0; i < phrase.length(); i++){
            if(phrase.charAt(i) == ' ') {
                continue;
            }
            userguess = guesses.get(cypherText.get(i));
            actualChar = phrase.charAt(i);
            if(userguess != actualChar){
                return false;
            }
        }

        return true;
    }

}

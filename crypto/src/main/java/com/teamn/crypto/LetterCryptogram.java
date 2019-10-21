package com.teamn.crypto;

import java.util.*;

/**
 * Represents a cryptogram where plaintext characters are mapped to letters
 */
public class LetterCryptogram extends Cryptogram{

    public LetterCryptogram(String sentencesFile) {
        super(sentencesFile);
    }

    /**
     * Encrypts the given string with a randomly generated character allocation.
     * @return the encrypted string
     */
    protected List<String> encryptString() {
        cypherText = new ArrayList<>();
        List<Integer> characterAllocations = this.generateRandomCharacterAllocations(this.getAlphabet().size());

        correctness = new HashMap<>();   //get correct mappings

        for (char c: phrase.toCharArray()) {
            if (c == ' ') {
                cypherText.add(""+c);
            } else {
                cypherText.add(this.getAlphabet().get(characterAllocations.get(c-Cryptogram.ASCII_BASE_ALPHABET)));

                String cypher = this.getAlphabet().get(characterAllocations.get(c-Cryptogram.ASCII_BASE_ALPHABET));
                if(!correctness.containsKey(Character.toUpperCase(c))) {
                    correctness.put(cypher, c+"");
                }
            }
        }

//        System.out.println(correctness.keySet() + "\n" + correctness.values());

        return cypherText;
    }

    /**
     * Gets the current alphabet used
     * @return the current alphabet
     */
    @Override
    public List<String> getAlphabet() {
        List<String> alphabet = new ArrayList<>();
        for (Character c: "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()) {
            alphabet.add(c.toString());
        } return alphabet;
    }

}

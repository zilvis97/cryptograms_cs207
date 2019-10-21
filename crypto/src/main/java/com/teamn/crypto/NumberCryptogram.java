package com.teamn.crypto;

import java.util.*;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

/**
 * Represents a cryptogram where plaintext characters are mapped to numbers
 */
public class NumberCryptogram extends Cryptogram{



    public NumberCryptogram(String sentencesFile) {
        super(sentencesFile);
    }

    /**
     * Encrypts the given string with a randomly generated number allocation.
     * @return the encrypted string
     */
    protected List<String> encryptString() {
        List<Integer> characterAllocations = this.generateRandomCharacterAllocations(this.getAlphabet().size());
        List<String> cypherText = new ArrayList<>();

        correctness = new HashMap<>();

        for (Character c: this.phrase.toCharArray()) {
            if (!c.equals(' ')) {
                cypherText.add(characterAllocations.get(c-ASCII_BASE_ALPHABET).toString());

                //fill correct mappings map
                String cypher = characterAllocations.get(c-ASCII_BASE_ALPHABET).toString();
                if(!correctness.containsKey(c)) {
                    correctness.put(cypher, c+"");
                }
            }
            else
                cypherText.add(" ");
        }

        return cypherText;
    }

    /**
     * Gets the current alphabet used
     * @return the current alphabet
     */
    @Override
    public List<String> getAlphabet() {
        List<String> alphabet = new ArrayList<>();
        for (Integer i : IntStream.range(0, 26).boxed().collect(toList())) {
            alphabet.add(i.toString());
        } return alphabet;
    }

}

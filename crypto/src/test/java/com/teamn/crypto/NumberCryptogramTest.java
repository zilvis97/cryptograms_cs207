package com.teamn.crypto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test the NumberCryptogram class
 */
class NumberCryptogramTest {

    NumberCryptogram cg;

    @BeforeEach
    void setup(){
        cg = new NumberCryptogram("sentences");
    }

    @Test
    void cypherTextCorrectLength() {
        List<String> cypherNumbers = cg.getCypherText();
        String phrase = cg.getPhrase();
        assertTrue(cypherNumbers.size() >= phrase.length() && cypherNumbers.size() <= 2*phrase.length());
    }

    @Test
    void cypherTextBoundaryChecks() {
        List<String> cypherNumbers;
        for(int i = 0; i < 100; i++){
            cypherNumbers= cg.getCypherText();
            assertFalse(cypherNumbers.contains("-1"));
            assertFalse(cypherNumbers.contains("26"));
        }
    }

    /**
     * Boundary testing the alphabet
     */
    @Test
    void getAlphabet() {
        List<String> alphabet = cg.getAlphabet();
        assertFalse(alphabet.contains("27"));
        assertFalse(alphabet.contains("26"));
        assertTrue(alphabet.contains("25"));
        assertTrue(alphabet.contains("1"));
        assertTrue(alphabet.contains("0"));
        assertFalse(alphabet.contains("-1"));
    }
}
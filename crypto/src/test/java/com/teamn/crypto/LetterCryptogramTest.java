package com.teamn.crypto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test the LetterCryptogram class
 */
class LetterCryptogramTest {
    LetterCryptogram cg;

    @BeforeEach
    void setup(){
        cg = new LetterCryptogram("sentences");
    }

    @Test
    void cypherTextCorrectLength() {
        List<String> cypherText = cg.getCypherText();
        String phrase = cg.getPhrase();
        assertEquals(cypherText.size(), phrase.length());
    }

    @Test
    void alphabetElementsAreSingleCharacters(){
        List<String> alphabet = cg.getAlphabet();

        for(String s: alphabet){
            assertTrue(s.length()==1);
        }
    }

    // boundary testing the characters
    @Test
    void getAlphabet() {
        List<String> alphabet = cg.getAlphabet();
        for(String s: alphabet){
            assertTrue("ABCDEFGHIJKLMNOPQRSTUVWXYZ".contains(s));
        }
    }



}
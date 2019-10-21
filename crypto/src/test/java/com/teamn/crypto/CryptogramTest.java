package com.teamn.crypto;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CryptogramTest {

    @Test
    void isCorrectLetterShouldBeCorrect() {
        Cryptogram cr = new LetterCryptogram("testsentences");
        Map<String, Character> guesses = new HashMap<String, Character>(){{
            this.put(cr.getCypherText().get(0), 'T');
            this.put(cr.getCypherText().get(1), 'E');
            this.put(cr.getCypherText().get(2), 'S');
        }};
        assertTrue(cr.isCorrect(guesses));
    }

    @Test
    void isCorrectLetterShouldNotBeCorrect() {
        Cryptogram cr = new NumberCryptogram("testsentences");
        Map<String, Character> guesses = new HashMap<String, Character>(){{
            this.put(cr.getCypherText().get(0), 'T');
            this.put(cr.getCypherText().get(1), 'E');
            this.put(cr.getCypherText().get(2), 'S');
        }};
        assertTrue(cr.isCorrect(guesses));
    }

    @Test
    void isCorrectNumberShouldBeCorrect() {
        Cryptogram cr = new NumberCryptogram("testsentences");
        Map<String, Character> guesses = new HashMap<String, Character>(){{
            this.put(cr.getCypherText().get(0), 'T');
            this.put(cr.getCypherText().get(1), 'E');
            this.put(cr.getCypherText().get(2), 'U');
        }};
        assertFalse(cr.isCorrect(guesses));

    }

    @Test
    void isCorrectNumberShouldNotBeCorrect() {
        Cryptogram cr = new LetterCryptogram("testsentences");
        Map<String, Character> guesses = new HashMap<String, Character>(){{
            this.put(cr.getCypherText().get(0), 'T');
            this.put(cr.getCypherText().get(1), 'E');
            this.put(cr.getCypherText().get(2), 'U');
        }};
        assertFalse(cr.isCorrect(guesses));
    }
}
package com.teamn.crypto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game game;

    private final String VALID_LETTER_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final String INVALID_LETTER_ALPHABET = "0123456789!\"£$%^&*()_+}{][~@:#';?></.,|\\`¬";
    private final List<Integer> VALID_NUMBER_ALPHABET = IntStream.range(0, 26).boxed().collect(toList());
    private final String INVALID_NUMBER_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ!\"£$%^&*()_+}{][~@:#';?></.,|\\`¬";

    @BeforeEach
    void setUp() {
        game = new Game(new Player("name"), "testsentences");
    }

    @Test
    void generateLetterCryptogram() {
        game.generateCryptogram(Game.GameType.LETTER);
        assertTrue(game.cryptogramType(LetterCryptogram.class));
    }

    @Test
    void generateNumberCryptogram() {
        game.generateCryptogram(Game.GameType.NUMBER);
        assertTrue(game.cryptogramType(NumberCryptogram.class));
    }

    @Test
    void isValidCypherCharacterLetterCryptogram() {
        game.generateCryptogram(Game.GameType.LETTER);

        for (Character c: VALID_LETTER_ALPHABET.toCharArray()) {
            assertTrue(game.isValidCypherCharacter(c.toString()));
        }
        for (Character c: INVALID_LETTER_ALPHABET.toCharArray()) {
            assertFalse(game.isValidCypherCharacter(c.toString()));
        }
    }

    @Test
    void isValidCypherCharacterNumberCryptogram() {
        game.generateCryptogram(Game.GameType.NUMBER);

        for (Integer i: VALID_NUMBER_ALPHABET) {
            assertTrue(game.isValidCypherCharacter(i.toString()));
        }
        for (Character c: INVALID_NUMBER_ALPHABET.toCharArray()) {
            assertFalse(game.isValidCypherCharacter(c.toString()));
        }
    }

    @Test
    void enterValidCypherCharacterLetterCryptogram() {
        game.generateCryptogram(Game.GameType.LETTER);

        char plaintextcharacter = 'A'; // doesn't matter what this is for this test

        for (Character c: VALID_LETTER_ALPHABET.toCharArray()) {
            if (game.getCryptogram().getCypherText().contains(c.toString())) {
                assertTrue(game.enterLetter(c.toString(), plaintextcharacter));
                assertNotNull(game.getGuessValue(c.toString()));
            }
        }
    }

    @Test
    void enterInvalidCypherCharacterLetterCryptogram() {
        Random random = new Random();
        game.generateCryptogram(Game.GameType.LETTER);

        char plaintextcharacter = 'A'; // doesn't matter what this is for this test

        for (Character c: INVALID_LETTER_ALPHABET.toCharArray()) {
            assertFalse(game.enterLetter(c.toString(), plaintextcharacter));
            assertNull(game.getGuessValue(c.toString()));
        }
    }

    @Test
    void enterValidCypherCharacterNumberCryptogram() {
        game.generateCryptogram(Game.GameType.NUMBER);
        assertNotNull(game);

        char plaintextcharacter = game.getCryptogram().getPhrase().charAt(0);

        for (Integer i: VALID_NUMBER_ALPHABET) {
            if (game.getCryptogram().getCypherText().contains(i.toString())) {
                assertTrue(game.enterLetter(i.toString(), plaintextcharacter));
                assertNotNull(game.getGuessValue(i.toString()));
            }
        }
    }

    @Test
    void enterInvalidCypherCharacterNumberCryptogram() {
        Random random = new Random();
        game.generateCryptogram(Game.GameType.NUMBER);

        char plaintextcharacter = game.getCryptogram().getPhrase().charAt(0); // doesn't matter what this is for this test

        for (Character c: INVALID_NUMBER_ALPHABET.toCharArray()) {
            assertFalse(game.enterLetter(c.toString(), plaintextcharacter));
            assertNull(game.getGuessValue(c.toString()));
        }
    }


    @Test
    void undoLetterLetterCryptogram() {
        game.generateCryptogram(Game.GameType.LETTER);

        for (Character c: VALID_LETTER_ALPHABET.toCharArray()) {
            game.enterLetter(c.toString(), 'X');
        }

        for(Character c: VALID_LETTER_ALPHABET.toCharArray()){
            game.undoLetter(c.toString());
        }

        for(Character c: VALID_LETTER_ALPHABET.toCharArray()){
            assertNull(game.getGuessValue(c.toString()));
        }

    }

    @Test
    void undoLetterNumberCryptogram() {
        game.generateCryptogram(Game.GameType.NUMBER);

        for(Integer i: VALID_NUMBER_ALPHABET){
            game.enterLetter(i.toString(), 'X');
        }

        for(Integer i: VALID_NUMBER_ALPHABET){
            game.undoLetter(i.toString());
        }

        for(Integer i: VALID_NUMBER_ALPHABET){
            assertNull(game.getGuessValue(i.toString()));
        }
    }

    @Test
    @DisplayName("Players are instantiated with appropriate values for cryptograms played and solved")
    void testPlayerStatsOnInstantiation(){
        Player player = new Player("name");
        assertEquals(0, player.cryptogramsPlayed);
        assertEquals(0, player.cryptogramsSolved);
    }

    @Test
    @DisplayName("The player stats are updated appropriately on generating a cryptogram")
    void testPlayerStatsOnGenerate(){
        Player player = new Player("name");
        game = new Game(player, "testsentences");
        game.generateCryptogram(Game.GameType.LETTER);
        assertEquals(1, player.getNumCryptogramsPlayed());
        assertEquals(0, player.getNumCryptogramsSolved());
    }

    @Test
    @DisplayName("The player stats are updated appropriately when the players guesses are complete and correct")
    void testPlayerStatsOnChecking(){
        Player player = new Player("name");
        game = new Game(player, "testsentences");
        game.generateCryptogram(Game.GameType.LETTER);

        List<String> cyphertext = game.getCryptogram().getCypherText();

        game.enterLetter(cyphertext.get(0), 'T');
        game.enterLetter(cyphertext.get(1), 'E');
        game.enterLetter(cyphertext.get(2), 'S');

        game.userGuessesAreCorrect();

        assertEquals(1, player.getNumCryptogramsSolved());
        assertEquals(1, player.getNumCryptogramsPlayed());
    }

    @Test
    @DisplayName("The player stats are updated appropriately when the players guesses are complete and incorrect")
    void testPlayerStatsOnCheckingButWrong(){
        Player player = new Player("name");
        game = new Game(player, "testsentences");
        game.generateCryptogram(Game.GameType.LETTER);

        List<String> cyphertext = game.getCryptogram().getCypherText();

        game.enterLetter(cyphertext.get(0), 'T');
        game.enterLetter(cyphertext.get(1), 'E');
        game.enterLetter(cyphertext.get(2), 'U');

        game.userGuessesAreCorrect();

        assertEquals(0, player.getNumCryptogramsSolved());
        assertEquals(1, player.getNumCryptogramsPlayed());
    }
}
package com.teamn.crypto;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static java.lang.System.out;

/**
 * Represents a given game. Manipulates Players and Cryptograms to facilitate the state of a
 * game being played.
 */
public class Game implements Serializable {

    private Player currentPlayer;
    private Cryptogram cryptogram;
    private Map<String, Character> guesses;
    private String sentencesFile;

    public Game(Player player, String sentencesFile) {
        this.currentPlayer = player;
        this.sentencesFile = sentencesFile;
    }

    /**
     * Generates a new cryptogram for the game as either a LetterCryptogram
     * or a NumberCryptogram depending on GameType
     * @param gameType the type of game to generate
     */
    public void generateCryptogram(GameType gameType) {
        currentPlayer.incrementCryptogramsPlayed();
        Cryptogram newCryptogram;

        if(cryptogram == null){
            if(gameType==GameType.LETTER)
                newCryptogram = new LetterCryptogram(sentencesFile);
            else
                newCryptogram = new NumberCryptogram(sentencesFile);
        } else { // ensure we don't regenerate the same cryptogram
            do {
                if(gameType==GameType.LETTER) {
                    newCryptogram = new LetterCryptogram(sentencesFile);
                } else {
                    newCryptogram = new NumberCryptogram(sentencesFile);
                }
            } while (newCryptogram.getPhrase().equals(cryptogram.getPhrase()));
        }

        cryptogram = newCryptogram;
        guesses = new HashMap<>(); // reset the user guesses
    }

    /**
     * Guess a plaintext character for a chosen cyphertext character
     * @param cypherchar the cyphertext character
     * @param plaintextchar the plaintext character
     * @return if the guess was made successfully (a guess for cypherchar doesn't already exist
     */
    public boolean enterLetter(String cypherchar, char plaintextchar) {
        if (cryptogram.getAlphabet().contains(cypherchar) && !guesses.containsKey(cypherchar)) {
            guesses.put(cypherchar, Character.toUpperCase(plaintextchar));
            //check for correctness & update currentPlayer accuracy
            Map<String, String> mappings = cryptogram.getCorrectMappings();

            String mappingsChar = mappings.get(cypherchar);
            if (mappingsChar ==  null) {
                System.out.println("The plaintext char is not part of the available chars");
                return false;
            }

            if(mappings.get(cypherchar).equals(String.valueOf(plaintextchar))) {
                currentPlayer.updateAccuracy(true);
            }
            else {
                currentPlayer.updateAccuracy(false);
            }

            return true;
        }

        return false;
    }

    /**
     * Remove a chosen cypher character from a cryptogram
     * @param cypherchar - character whose mapping to is to be removed
     * @return true if a guess was removed, false if no such guess existed
     */
    public boolean undoLetter(String cypherchar) {
        if(guesses.containsKey(cypherchar)) {
            guesses.remove(cypherchar);
            return true;
        }
        return false;
    }

    /**
     * Check if a cypher character (as a string) is in the Cryptogram cypher alphabet
     * @param cypherchar a potential cyphertext character
     * @return true if if it in current Cryptogram's alphabet
     */
    public boolean isValidCypherCharacter(String cypherchar){
        return cryptogram.getAlphabet().contains(cypherchar);
    }

    /**
     * @param c a cypher character
     * @return true if the Cryptogram contains the given cypher character else false
     */
    public boolean cryptogramContainsCharacter(String c) {
        return this.cryptogram.cyphertextContainsChar(c);
    }

    public void viewFrequencies() {
        throw new NotImplementedException();
    }

    public void showSolution() {
        throw new NotImplementedException();
    }

    /**
     * Prints the Cyphertext and the state of the player's
     * plaintext guesses
     */
    public void printGameState(){

        String cyphertext = cryptogram.toString();
        StringBuilder playerGuesses = new StringBuilder();

        out.println("\nCyphertext: " + cyphertext);

        for(String s: cryptogram.getCypherText()){
            if(s.equals(" "))
                playerGuesses.append("  ");
            else if (!guesses.containsKey(s)) {
                for(char ignored : s.toCharArray()){
                    playerGuesses.append('_');
                }
                playerGuesses.append(" ");
            } else {
                playerGuesses.append(guesses.get(s));
                if(s.length()>1)
                    playerGuesses.append(" ");
                playerGuesses.append(" ");
            }
        }

        out.println("Playertext: " + playerGuesses);

    }

    /**
     * Determin the type of the cryptogram object (for testing)
     * @param type that we want to check
     * @return true if the class has the type
     */
    public boolean cryptogramType(Class type){
        return type.isInstance(cryptogram);
    }

    /**
     * Determin if the guesses contain a particular mapping (for testing)
     * @param key
     * @return the value associated with key or null if none exists
     */
    public Character getGuessValue(String key){
        return guesses.getOrDefault(key, null);
    }

    /**
     * Checks if the user has completed the cryptogram by making
     * guesses for all cypher characters.
     *
     * @return True if they have completed it
     */
    public boolean hasGuessesForAllCypherCharacters(){
        if(cryptogram==null)
            return true;
        List<String> cyphertext = cryptogram.getCypherText();
        for(String cypherchar: cyphertext){
            if(!cypherchar.contains(" ") && !guesses.containsKey(cypherchar)){
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if all the user's guesses are correct and complete the cryptogram
     *
     * @return True if all the user's guesses are correct
     */
    public boolean userGuessesAreCorrect(){
        boolean isCorrect = cryptogram.isCorrect(guesses);

        if(isCorrect)
            currentPlayer.incrementCryptogramsSolved();

        return isCorrect;
    }

    public void giveHint(){
        Scanner scanner = new Scanner(System.in);
        out.println("enter a letter you would like a hint for");
        String c = scanner.nextLine().toUpperCase();
        int status = 0;
        Map<String, String> hint = cryptogram.getCorrectMappings();
        if (!hasGuessesForAllCypherCharacters() && isValidCypherCharacter(c)){
            for (String s : hint.keySet()){
                s = c;

                /* Checks if user has already made a guess and wants to change
                choice for a hint
                */
                if (guesses.containsKey(s)) {
                    guesses.put(s, hint.get(s).charAt(0));
                 //   guesses.remove(s); //remove old key, and replace it with new one
                    enterLetter(s, hint.get(s).charAt(0));
                    status += 1;
                }
                else if (hint.containsKey(s)) {
                    enterLetter(s, hint.get(s).charAt(0));
                    status += 10;
                }
                if (status == 1) {
                    out.println("Changed letter at " + s + " to "
                            + hint.get(s).charAt(0));
                    status += 2;
                }
                else if (status == 0) {
                    out.println("Letter not valid");
                    status += 4;
                }
            }
        }
    }

    /**
     * @return the cryptogram field
     */
    public Cryptogram getCryptogram() {
        return cryptogram;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * the type of Cryptogram to play
     */
    public enum GameType {
        LETTER, NUMBER;
    }
}

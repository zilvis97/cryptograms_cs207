package com.teamn.crypto;

import java.io.Serializable;

public class Player implements Serializable{
/**
 * A class that modes a player storing key details about their performance
 */

    String username; // when we start implementing different players add a constructor for this
    int correctGuesses;
    int totalGuesses;

    int cryptogramsPlayed;
    int cryptogramsSolved;

    public Player(String name) {
        username = name;
        totalGuesses = 0;
        correctGuesses = 0;
        cryptogramsPlayed = 0;
        cryptogramsSolved = 0;
    }

    /**
     *
     * @param correct - if entered guess was correct
     * update guesses statistics based on guess correctness
     */
    public void updateAccuracy(Boolean correct) {
        if (correct) {
            this.correctGuesses++;
        } this.totalGuesses++;
    }

    /**
     * Add 1 to the current total number of cryptograms solved
     */
    public void incrementCryptogramsSolved() {
        cryptogramsSolved++;
    }

    /**
     * Add 1 to the current total number of cryptograms played
     */
    public void incrementCryptogramsPlayed() {
        cryptogramsPlayed++;
    }

    /**
     * @return the accuracy in terms of each guess entered
     */
    public float getGuessAccuracy() {
        return totalGuesses != 0 ? (float) this.correctGuesses / (float) this.totalGuesses : 0;
    }

    /**
     * @return the accuracy in terms of solved games / played games
     */
    public float getAccuracyOfSolvedGames(){
        return cryptogramsPlayed != 0 ? (float) this.cryptogramsSolved / (float) this.cryptogramsPlayed : 0;
    }

    /**
     * @return the number of cryptograms successfully solved
     */
    public int getNumCryptogramsSolved() {
        return this.cryptogramsSolved;
    }

    /**
     * @return the total number of cryptograms played
     */
    public int getNumCryptogramsPlayed() {
        return this.cryptogramsPlayed;
    }

    /**
     * @return player's username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * @return A printable string designed to be printed solely for the purposes of marking
     */
    @Override
    public String toString() {
        return "Player{" +
                "username='" + username + '\'' +
                ", totalGuesses=" + totalGuesses +
                ", correctGuesses=" + correctGuesses +
                ", cryptogramsPlayed=" + cryptogramsPlayed +
                ", cryptogramsSolved=" + cryptogramsSolved +
                ", accuracy= " + getGuessAccuracy() +
                "}";
    }
}

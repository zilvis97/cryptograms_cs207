package com.teamn.crypto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    Player player;

    @BeforeEach
    void setUp(){
        player = new Player("name");
    }

    @Test
    @DisplayName("Get accuracy when user solves all cryptograms correctly")
    void getAccuracyCase1() {
        player.updateAccuracy(true);
        player.updateAccuracy(true);
        player.updateAccuracy(true);
        player.updateAccuracy(true);

        assertEquals(1, player.getGuessAccuracy());
    }

    @Test
    @DisplayName("Get accuracy when user solves half of all cryptograms")
    void getAccuracyCase2(){
        player.updateAccuracy(false);
        player.updateAccuracy(false);
        player.updateAccuracy(true);
        player.updateAccuracy(true);

        assertEquals(0.5, player.getGuessAccuracy());
    }

    @Test
    @DisplayName("Get accuracy when user solves no cryptograms")
    void getAccuracyCase3(){
        player.incrementCryptogramsPlayed();

        assertEquals(0, player.getGuessAccuracy());
    }

    @Test
    @DisplayName("Check class handles divide by 0 without bombing")
    void getAccuracyCase4(){
        player.incrementCryptogramsSolved();

        double accuracy = player.getGuessAccuracy();

        assertEquals(0, accuracy);
    }
}
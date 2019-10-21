package com.teamn.crypto;

import java.util.Comparator;

/**
 * Compares players by proportion of successfully solved cryptograms
 */
public class PlayerComparator implements Comparator<Player> {
    @Override
    public int compare(Player p1, Player p2) {
        int p1Accuracy = (int) (p1.getAccuracyOfSolvedGames() * 100);
        int p2Accuracy = (int) (p2.getAccuracyOfSolvedGames() * 100);
        return  p1Accuracy - p2Accuracy;
    }
}

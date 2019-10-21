package com.teamn.crypto;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Models a leaderboard.
 */
public class LeaderBoard {

    List<Player> players;
    Player[] board;

    /**
     * @param boardSize the number of places on the leaderboard
     * @param players the list of players from which to form the leaderboard
     */
    public LeaderBoard(int boardSize, List<Player> players) {
        this.players = players;
        board = new Player[boardSize];
    }

    /**
     * Update the array representing the leaderboard
     */
    public void update() {
        Comparator<Player> reverse = Collections.reverseOrder(new PlayerComparator());
        Collections.sort(players, reverse);

        for(int i = 0; i < players.size() && i < board.length; i++){
            board[i] = players.get(i);
        }
    }

    /**
     * @return the leaderboard
     */
    public Player[] getBoard() {
        return board;
    }

    /**
     * Produces a nicely formatted string representing the leaderboard.
     * @return the leaderboard as a nicely formatted string
     */
    public String printableLeaderBoard(){
        StringBuilder sb = new StringBuilder();

        if(board[0] == null){
            sb.append("There are no players with which to make a leaderboard!");
        } else {
            sb.append("\t** Leaderboard **\n");
            sb.append("------------------------------------------------\n");
            sb.append("   Name      Proportion of cryptograms completed\n");

            for(int i = 0; i < board.length; i++){
                sb.append(i+1);
                sb.append(")");
                if(board[i] == null){
                    sb.append("\n");
                } else {
                    sb.append(" " + board[i].getUsername());
                    sb.append("\t");
                    sb.append((int) (board[i].getAccuracyOfSolvedGames() * 100));
                    sb.append("%\n");
                }
            }
        }

        return sb.toString();
    }
}

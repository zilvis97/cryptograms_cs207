package com.teamn.crypto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LeaderBoardTest {

    List<Player> players;
    LeaderBoard leaderBoard;

    @BeforeEach
    void setup(){
        players = new ArrayList<>();
        leaderBoard = new LeaderBoard(10, players);
        try {
            leaderBoard.update();
        } catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Produces leader board with correct number of spaces")
    void test0(){
        assertEquals(10, leaderBoard.getBoard().length);
    }

    @Test
    @DisplayName("Empty player list results in empty leader board")
    void test1(){
        Player[] players = leaderBoard.getBoard();
        for(Player p: players) assertNull(p);
    }

    @Test
    @DisplayName("Player list size < leaderboard size results in partially full leader board")
    void test2(){
        for(int i = 0; i < 5; i++) {
            players.add(new Player("player" + i));
        }

        leaderBoard.update();
        Player[] board = leaderBoard.getBoard();

        for(int i = 0; i < board.length; i++){
            if(i >= 5){
                assertNull(board[i]);
            } else {
                assertNotNull(board[i]);
            }
        }
    }

    @Test
    @DisplayName("Player list size = leaderboard size results in full leaderboard")
    void test3(){
        for(int i = 0; i < 10; i++) {
            players.add(new Player("player" + i));
        }

        leaderBoard.update();
        Player[] board = leaderBoard.getBoard();

        for(Player p: board){
            assertNotNull(p);
        }
    }

    @Test
    @DisplayName("Player list size > leaderboard size results in full leaderboard of correct size")
    void test4(){
        for(int i = 0; i < 20; i++) {
            players.add(new Player("player" + i));
        }

        leaderBoard.update();
        Player[] board = leaderBoard.getBoard();
        assertEquals(10, board.length);

        for(Player p: board){
            assertNotNull(p);
        }
    }

    @Test
    @DisplayName("Players are ordered correctly when they don't take up the full leaderboard")
    void test5(){
        Player p1 = new Player("player1");
        Player p2 = new Player("player2");
        Player p3 = new Player("player3");

        p2.incrementCryptogramsSolved();
        p2.incrementCryptogramsPlayed();

        p3.incrementCryptogramsSolved();
        p3.incrementCryptogramsPlayed();
        p3.incrementCryptogramsPlayed();

        p1.incrementCryptogramsPlayed();

        players.add(p1);
        players.add(p2);
        players.add(p3);

        leaderBoard.update();
        Player[] board = leaderBoard.getBoard();

        for(int i = 0; i < 3; i++){
            switch (i){
                case 0:
                    assertEquals(p2, board[i]);
                    break;
                case 1:
                    assertEquals(p3, board[i]);
                    break;
                case 2:
                    assertEquals(p1, board[i]);
            }
        }

    }

    @Test
    @DisplayName("Players are ordered correctly when they take up exactly the leaderboard")
    void test6(){
        // Add the players in the list such that leaderBoard will have to have them in reverse order.
        for(int i = 1; i <= 10; i++){
            Player p = new Player("player" + i);

            for(int j = 1; j <= i; j++){
                p.incrementCryptogramsSolved();
            }

            for(int k = 0; k < 10; k++){
                p.incrementCryptogramsPlayed();
            }

            players.add(p);
        }

        leaderBoard.update();
        Player[] board = leaderBoard.getBoard();

        for(int i = 1; i <= 10; i++){
            assertEquals("player" + i, board[10-i].getUsername());
        }
    }

    @Test
    @DisplayName("Players are ordered correctly when there are more of them than there are leaderboard spaces")
    void test7(){
        // Add the players in the list such that leaderBoard will have to have them in reverse order and take best 10.
        // Best 10 will be player 11 - player 20 (in reverse order)
        for(int i = 1; i <= 20; i++){
            Player p = new Player("player" + i);

            for(int j = 1; j <= i; j++){
                p.incrementCryptogramsSolved();
            }

            for(int k = 0; k < 20; k++){
                p.incrementCryptogramsPlayed();
            }

            players.add(p);
        }

        leaderBoard.update();
        Player[] board = leaderBoard.getBoard();

        for(int i = 11; i <= 20; i++){
            assertEquals("player" + i, board[20-i].getUsername());
        }
    }

    @Test
    @DisplayName("printableLeaderBoard results in error message if there are no players")
    void test8(){
        leaderBoard.update();
        assertEquals("There are no players with which to make a leaderboard!",
                leaderBoard.printableLeaderBoard());
    }

    @Test
    @DisplayName("printableString formats partially full leaderboard correctly")
    void test9(){
        Player p1 = new Player("player1");
        Player p2 = new Player("player2");
        Player p3 = new Player("player3");

        p2.incrementCryptogramsSolved();
        p2.incrementCryptogramsPlayed();

        p3.incrementCryptogramsSolved();
        p3.incrementCryptogramsPlayed();
        p3.incrementCryptogramsPlayed();

        p1.incrementCryptogramsPlayed();

        players.add(p1);
        players.add(p2);
        players.add(p3);

        leaderBoard.update();

        assertEquals("\t** Leaderboard **\n" +
                "------------------------------------------------\n" +
                "   Name      Proportion of cryptograms completed\n" +
                "1) player2\t100%\n" +
                "2) player3\t50%\n" +
                "3) player1\t0%\n" +
                "4)\n" +
                "5)\n" +
                "6)\n" +
                "7)\n" +
                "8)\n" +
                "9)\n" +
                "10)\n",
                leaderBoard.printableLeaderBoard());
    }

    @Test
    @DisplayName("printableLeaderBoard formats full leaderboard correctly")
    void test10(){
        // Add the players in the list such that leaderBoard will have to have them in reverse order.
        for(int i = 1; i <= 10; i++){
            Player p = new Player("player" + i);

            for(int j = 1; j <= i; j++){
                p.incrementCryptogramsSolved();
            }

            for(int k = 0; k < 10; k++){
                p.incrementCryptogramsPlayed();
            }

            players.add(p);
        }

        leaderBoard.update();

        assertEquals("\t** Leaderboard **\n" +
                "------------------------------------------------\n" +
                "   Name      Proportion of cryptograms completed\n" +
                "1) player10\t100%\n" +
                "2) player9\t90%\n" +
                "3) player8\t80%\n" +
                "4) player7\t70%\n" +
                "5) player6\t60%\n" +
                "6) player5\t50%\n" +
                "7) player4\t40%\n" +
                "8) player3\t30%\n" +
                "9) player2\t20%\n" +
                "10) player1\t10%\n",
                leaderBoard.printableLeaderBoard());
    }

    @Test
    @DisplayName("Is able to produce leaderboards for sizes larger than 10")
    void test11(){
        leaderBoard = new LeaderBoard(15, players);
        leaderBoard.update();
        Player[] board = leaderBoard.getBoard();
        assertEquals(15, board.length);
    }

    @Test
    @DisplayName("Update to the non full leaderboard is reflected properly")
    void test12(){
        Player p1 = new Player("player1");
        Player p2 = new Player("player2");
        Player p3 = new Player("player3");

        p2.incrementCryptogramsSolved();
        p2.incrementCryptogramsPlayed();

        p3.incrementCryptogramsSolved();
        p3.incrementCryptogramsPlayed();
        p3.incrementCryptogramsPlayed();

        p1.incrementCryptogramsPlayed();

        players.add(p1);
        players.add(p2);
        players.add(p3);

        leaderBoard.update();

        // now do something that will change the order
        p1.incrementCryptogramsSolved();
        p1.incrementCryptogramsSolved();
        p1.incrementCryptogramsSolved();
        p1.incrementCryptogramsPlayed();
        p1.incrementCryptogramsPlayed();
        p1.incrementCryptogramsPlayed();

        leaderBoard.update();

        Player[] board = leaderBoard.getBoard();

        assertEquals(p1, board[1]);
    }

    @Test
    @DisplayName("Update to the full leaderboard is reflected properly")
    void test13(){
        Player one = null;
        // Add the players in the list such that leaderBoard will have to have them in reverse order.
        for(int i = 1; i <= 10; i++){
            Player p = new Player("player" + i);
            if(i==1) one = p;

            for(int j = 1; j <= i; j++){
                p.incrementCryptogramsSolved();
            }

            for(int k = 0; k < 10; k++){
                p.incrementCryptogramsPlayed();
            }

            players.add(p);
        }

        leaderBoard.update();

        for(int i = 0; i < 100; i++){
            one.incrementCryptogramsPlayed();
            one.incrementCryptogramsSolved();
        }

        leaderBoard.update();
        Player[] board = leaderBoard.getBoard();

        assertEquals(one, board[1]);
    }

}

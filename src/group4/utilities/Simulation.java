package group4.utilities;

import group4.AI.Maximus;
import group4.MCTS.James;
import group4.MCTS.Napoleon;
import group4.MCTS.Sean;
import group4.Players.Player;
import group4.logic.LogicBoard;
import group4.randomAI.Bobby;
import group4.ui.GridCoordinate;

import java.util.ArrayList;

import static group4.utilities.BoardOperations.*;

/**
 * Created by jonty on 21/01/2017.
 */

public class Simulation {


    public static void main(String[] args) {

        int[][] Board = new int[][]{

                {0, 0, 1, 3, 3, 3, 0, 0, 0, 0},
                {0, 2, 1, 0, 3, 3, 0, 0, 0, 0},
                {0, 3, 1, 3, 3, 0, 3, 3, 3, 0},
                {3, 3, 3, 3, 0, 0, 0, 1, 0, 0},
                {3, 0, 3, 3, 3, 0, 0, 0, 0, 3},
                {0, 3, 0, 0, 0, 0, 2, 0, 3, 3},
                {3, 3, 0, 0, 2, 0, 3, 0, 0, 0},
                {3, 0, 3, 2, 0, 0, 0, 0, 0, 3},
                {3, 0, 0, 0, 3, 0, 0, 3, 0, 3},
                {0, 0, 0, 3, 3, 3, 3, 0, 0, 0}

       /*
                {3,0,1,0,0,0,2,3,0,0},
                {0,0,3,3,0,3,3,3,3,3},
                {2,0,3,0,0,0,0,0,3,2},
                {3,3,3,0,0,0,0,0,3,3},
                {0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0},
                {1,0,0,0,0,0,0,0,0,1},
                {0,0,0,0,0,0,0,0,0,0},
                {0,0,1,0,0,0,0,1,0,0}

        */
        };

        //System.out.println("Game Over: " + gameOverCheck(Board));
        //printBoard(Board);



        double avgScorePl1 = 0;
        double avgScorePl2 = 0;

        Player player1 = new Player(true);
        Player player2 = new Player(false);
        LogicBoard game;

        ArrayList<GridCoordinate> queens = posQueens(Board, 2);
        ArrayList<GridCoordinate> posDest;
        GridCoordinate queen;

        int threes = 0;
        int limit = 50;
        //First round of Matches Bobby vs Sean
        int count = 0;
        for (int k = 0; k < limit; k++) {
            player1 = new James(true);
            player2 = new Maximus(false, 2);
            game = new LogicBoard(player1, player2);
            //game.setBoard(Board);
            game.runBotGame();

            int scorePl1 = gameScore(game.getBoard(), 1);
            int scorePl2 = gameScore(game.getBoard(), 2);

            avgScorePl1 += scorePl1;
            avgScorePl2 += scorePl2;

            threes = 0;
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (game.getBoard()[i][j] == 3) {
                        threes++;
                    }
                }
            }

            if (scorePl1 > scorePl2) {
                System.out.println( player1 + " wins with = " + scorePl1);
                count++;
            } else if (scorePl2 > scorePl1) {
                System.out.println( player2 + " wins with = " + scorePl2);
            } else if (scorePl2 == scorePl1) {

                if (threes % 2 == 0) {
                    System.out.println( player2 + " wins after moving last");
                } else {
                    System.out.println( player1 + " wins after moving last");
                    count++;
                }
            }
            //printBoard(game.getBoard());
            System.out.println("In " + threes + " turns\n");
        }

        System.out.println(player1 + " won = " + count + " times\tWith average Score of: " + (avgScorePl1/limit));
        System.out.println(player2 + " won = " + (limit - count) + " times	With average Score of: " + avgScorePl2/limit);
    }
}

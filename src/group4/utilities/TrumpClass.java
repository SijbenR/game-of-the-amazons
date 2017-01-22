package group4.utilities;

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

public class TrumpClass {


    public static void main(String[] args) {

        int[][] Board = new int[][]{

                {3, 0, 1, 3, 3, 3, 0, 3, 3, 3},
                {1, 0, 0, 0, 3, 2, 0, 0, 0, 3},
                {3, 0, 3, 3, 3, 3, 0, 3, 2, 3},
                {3, 3, 3, 3, 3, 3, 3, 0, 3, 3},
                {3, 2, 0, 3, 0, 3, 3, 0, 3, 3},
                {3, 0, 3, 3, 0, 3, 3, 0, 3, 3},
                {3, 3, 3, 3, 3, 3, 0, 3, 3, 1},
                {3, 3, 3, 3, 3, 2, 3, 3, 3, 3},
                {0, 3, 1, 0, 3, 3, 3, 0, 3, 3},
                {3, 0, 0, 0, 0, 3, 0, 3, 3, 3}

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

        System.out.println("Game Over: " + gameOverCheck(Board));
        printBoard(Board);
        int scorePl1 = gameScore(Board, 1);
        int scorePl2 = gameScore(Board, 2);

        if (scorePl1 > scorePl2) {
            System.out.println("player 1 wins with = " + scorePl1);

        } else if(scorePl2>scorePl1){
            System.out.println("player 2 wins with = " + scorePl2);
        } else if(scorePl2==scorePl1){
            int threes=0;
            for(int i=0;i<10;i++){
                for(int j=0;j<10;j++){
                    if(Board[i][j]==3){
                        threes++;
                    }
                }
            }
            if(threes%2==0){
                System.out.println("player 2 wins after moving last");
            }else{
                System.out.println("player 1 wins after moving last");
            }
        }


        Player player1;
        Player player2;
        LogicBoard game;

        ArrayList<GridCoordinate> queens = posQueens(Board, 2);
        ArrayList<GridCoordinate> posDest;
        GridCoordinate queen;


        /*
        int count = 0;
        for (int i = 0; i < 1; i++) {
            player1 = new Bobby(true);
            player2 = new Sean(false, 1000);
            game = new LogicBoard(player1, player2);
            game.runBotGame();
            printBoard(Board);
          //  int scorePl1 = gameScore(game.getBoard(), 1);
            //int scorePl2 = gameScore(game.getBoard(), 2);


            }


            System.out.println("Game Over: " + gameOverCheck(Board));


        }

        //System.out.println("player 1 won " + count + " times");
        */
    }

}
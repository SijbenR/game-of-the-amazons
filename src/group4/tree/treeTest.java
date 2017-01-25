package group4.tree;

import group4.MCTS.utcTree;
import group4.Players.Player;
import group4.logic.LogicBoard;
import group4.ui.GridCoordinate;
import group4.utilities.BoardOperations;

import javax.swing.*;

/**
 * Created by jonty on 11/01/2017.
 */
public class treeTest {

    public static void main(String[] args0) {
        /*
        Player player1 = new Player(true);
        Player player2 = new Player(false);

        LogicBoard Board = new LogicBoard(player1,player2);
        BoardOperations boardOperations= new BoardOperations();
        GridCoordinate gridCoordinate =new GridCoordinate(1,8);
       // System.out.println("score for the start is= " + boardOperations.evaluate(Board.getBoard(),1));

      //  NodeTree tree = new NodeTree(Board.getBoard(), 1, false, 3, 5);
        //GridCoordinate[] bestMove = tree.Movethebest();

        */

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
        };

        Player pl1 = new Player(true);
        Player pl2 = new Player(false);

        LogicBoard Lboard = new LogicBoard(pl1, pl2);

        utcTree tree = new utcTree(Lboard.getBoard(), 1, false, 2000, false, true, 1.6);

        //tree.addChildren(tree.root);


        tree.testMethods();



    }
}

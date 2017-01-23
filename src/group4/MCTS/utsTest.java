package group4.MCTS;

/**
 * Created by jonty on 21/01/2017.
 */import group4.Players.Player;
import group4.logic.LogicBoard;
import group4.randomAI.Bobby;
import group4.tree.NodeTree;
import group4.ui.GridCoordinate;
import group4.utilities.BoardOperations;

public class utsTest {

public static void main(String[] args0) {

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
        /*
        Player player1 = new Player(true);
        Player player2 = new Player(false);

        LogicBoard Board = new LogicBoard(player1, player2);
        BoardOperations boardOperations = new BoardOperations();
      //  GridCoordinate gridCoordinate = new GridCoordinate(1, 8);
        // System.out.println("score for the start is= " + boardOperations.evaluate(Board.getBoard(),1));
               */

        utcTree tree = new utcTree(Board, 1, false, 10000, true);
        tree.mctBuild();

        }
}

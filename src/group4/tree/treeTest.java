package group4.tree;

import group4.Players.Player;
import group4.logic.LogicBoard;

/**
 * Created by jonty on 11/01/2017.
 */
public class treeTest {

    public static void main(String[] args0) {
        Player player1 = new Player(true);
        Player player2 = new Player(false);

        LogicBoard Board = new LogicBoard(player1,player2);

        NodeTree tree = new NodeTree(Board.getBoard(), 1, false, 180, 500);
        tree.partBuild(tree.root);


    }
}

package group4.MCTS;

import group4.Players.Player;
import group4.tree.NodeTree;
import group4.ui.GridCoordinate;

import static group4.utilities.BoardOperations.getCopy;
import static group4.utilities.BoardOperations.printArrayint;

/**
 * Created by jonty on 13/01/2017.
 */
public class James extends Player {

    NodeTree tree;
    int[][] Grid;
    GridCoordinate[] move;
    GridCoordinate fuckinggreat;
    GridCoordinate[] queenMove = new GridCoordinate[2];
    public James(boolean isFirst) {
        super(isFirst, true);
    }

    public void giveInput(int[][] Board)	{
        if(fuckinggreat == null) {
            this.Grid = getCopy(Board);
            move = new GridCoordinate[3];
            tree = new NodeTree(Grid, super.getVal(), false, 5, 100);

            move = tree.Movethebest();

            queenMove[0] = move[0];
            queenMove[1] = move[1];

            fuckinggreat = move[2];


        }

    }

    public GridCoordinate[] chooseQueenMove() {
        // System.out.println("JAMES: right before shooting moving Queen");
        // printArrayint(Grid);

        return queenMove;
    }

    public GridCoordinate chooseArrowMove() {
        GridCoordinate temp = new GridCoordinate(fuckinggreat.x, fuckinggreat.y);
        fuckinggreat = null;
        //System.out.println("JAMES: right before shooting arrow");
        // printArrayint(Grid);
        //System.out.println("ArrowMove: \nOrigin: "  + move[1] + "\tDestination: " + move[2]);
        return temp;
    }


}

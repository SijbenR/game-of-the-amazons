package group4.MCTS;

import group4.AI.MoveProducer;
import group4.Players.Player;
import group4.tree.NodeTree;
import group4.ui.GridCoordinate;

import static group4.utilities.BoardOperations.filterBoard;
import static group4.utilities.BoardOperations.getCopy;

/**
 * Created by jonty on 21/01/2017.
 */
public class Sean extends Player implements MoveProducer{
    NodeTree tree;
    int[][] Grid;
    GridCoordinate[] move;
    GridCoordinate fuckinggreat;
    GridCoordinate[] queenMove = new GridCoordinate[2];
    double timeToRun;
    boolean useTer = true;

    public Sean(boolean isFirst, double timeToRun, boolean useTer) {
        super(isFirst,true);
        this.timeToRun=timeToRun;
        this.useTer = useTer;

    }
    public void giveInput(int[][] Board)	{
        if(fuckinggreat == null) {
            this.Grid = getCopy(Board);
            move = new GridCoordinate[3];
            tree = new utcTree(Grid, super.getVal(), false, timeToRun, useTer);

            move = tree.Movethebest();

            queenMove[0] = move[0];
            queenMove[1] = move[1];

            fuckinggreat = move[2];


        }

    }
    public GridCoordinate[] getMove(int[][] Board)	{
        if(fuckinggreat == null) {
            this.Grid = getCopy(Board);
            move = new GridCoordinate[3];
            tree = new utcTree(Grid, super.getVal(), false, timeToRun, useTer);

            move = tree.Movethebest();

            queenMove[0] = move[0];
            queenMove[1] = move[1];

            fuckinggreat = move[2];


        }
        fuckinggreat=null;
        return move;
    }

    public String toString()    {
        String returnString = new String("Bot player Sean - Value = " + getVal());
        return returnString;
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
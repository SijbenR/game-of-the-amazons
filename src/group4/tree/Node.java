package group4.tree;

import group4.logic.tempBoard;
import group4.ui.GridCoordinate;
import group4.utilities.BoardOperations;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Robins on 10.12.2016.
 */
public class Node {

    private Node parent;
    private tempBoard currentBoard;
    private boolean queenMove;
    private ArrayList<Node> Children;
    private GridCoordinate origin, dest;

    //Is only set when setBoard is triggered


    public Node(int[][] Board)   {
        this.parent = null;
        currentBoard = new tempBoard(Board);

        //Determine if it's a queenMove or an ArrowMove
    }

    public Node(Node parent, GridCoordinate origin, GridCoordinate dest)   {

    }

    protected void takeParentBoard()    {
        if(parent != null)  {
            int[][] pBoard = parent.getBoard();
            int[][] newCurrent = BoardOperations.getCopy(pBoard);
            currentBoard = new tempBoard(newCurrent);
        }
    }

    protected int[][] getBoard()    {
        return currentBoard.getMomentaryBoard();
    }


    public boolean isSame()  {
        return false;
    }

    public void isQueenMove()    {
        if(origin == null) {
            queenMove = false;
        }
        else {
            queenMove = true;
        }
    }





}

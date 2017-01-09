package group4.tree;

import group4.ui.GridCoordinate;
import group4.utilities.BoardOperations;
import group4.utilities.TreeTraverse;

import java.util.ArrayList;

import static group4.utilities.BoardOperations.posQueens;

/**
 * Created by Robins on 06.01.2017.
 *
 * WORK IN PROGRESS - DO NOT USE YET!
 */
public class NodeTree {

    private boolean arrowMoveSeperate;

    private int ownVal;

    private Node root;
    private int depth,branch;
    private int curdepth;

    TreeTraverse nodePointer;


    //Tree where Arrow and queen move are separate steps
    public NodeTree(int[][] Board, int ownVal, int depth, int branch)   {
        arrowMoveSeperate = true;
        this.ownVal = ownVal;

        String board = BoardOperations.getBoardAsString(Board);

        //root = new Node(board, ownVal);

        this.depth = depth;
        this.branch = branch;


        nodePointer = new TreeTraverse(Board);
        curdepth = 0;


    }

    public void addChildren(Node parent)    {
        //We know the current Tree from tree traverse, so tahts where we need to ask for the possible moves for the parent Node´s possible moves
        int queens;
        ArrayList<GridCoordinate> queenLoc;


        //Just moving own Queen
        if(parent.ownMove && !parent.arrowMove)  {
            //queenLoc = nodePointer.generat


        }


       while(parent.getChildren().size() < branch)  {




       }

    }

}

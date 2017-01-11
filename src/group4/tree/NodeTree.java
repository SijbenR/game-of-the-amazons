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
    public NodeTree(int[][] Board, int ownVal, boolean arrowMove, int depth, int branch)   {
        arrowMoveSeperate = true;
        this.ownVal = ownVal;

        String board = BoardOperations.getBoardAsString(Board);

        int rootVal;
        boolean startBool;


        if(arrowMove == true)   {
            rootVal = ownVal;
            startBool = false;
        }
        else    {
            if(ownVal == 1)
                rootVal = 2;
            else
                rootVal = 1;
            startBool = true;
        }


        root = new Node(board, startBool, rootVal);

        this.depth = depth;
        this.branch = branch;


        nodePointer = new TreeTraverse(Board);
        curdepth = 0;


    }

    public void addChildren(Node parent)    {
        //We know the current Tree from tree traverse, so tahts where we need to ask for the possible moves for the parent Node´s possible moves
        int queens;

        //First check if the number of Possible moves is lesser than the Branch, because then we need to adjust the limit of Child
        // -> But for that we first need to know whether WE are supposed to move OR the OPPONENT
        int queenVal;
        int limit;
        Node childNode;


        if(!parent.ownMove && !parent.arrowMove) {      //This means WE are supposed to move a Queen

            //Check limit of options
            //limit = nodePointer.countPosOptions(queenVal);

        }
        else if(parent.ownMove && parent.arrowMove)  {  //This means the opponent is supposed to move

            //We set the value we are going to look for the Queens of the opponent
            if(parent.playerVal == 1)
                queenVal = 2;
            else
                queenVal = 1;

            limit = nodePointer.countPosOptions(queenVal);

            //in case we have more options than we need
            if(limit > branch)  {
                limit = branch;
            }

            while(parent.getChildren().size() <= limit) {
                childNode = nodePointer.createChild(parent);
                parent.addChild(childNode);
            }

        }




    }

}

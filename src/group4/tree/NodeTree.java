package group4.tree;

import group4.ui.GridCoordinate;
import group4.utilities.BoardOperations;
import group4.utilities.TreeTraverse;

import java.util.ArrayList;

import static group4.utilities.BoardOperations.bubbleSortNodesByScore;
import static group4.utilities.BoardOperations.posQueens;
import static group4.utilities.BoardOperations.printArrayint;

/**
 * Created by Robins on 06.01.2017.
 *
 * WORK IN PROGRESS - DO NOT USE YET!
 */
public class NodeTree {

    private boolean arrowMoveSeperate;

    private int ownVal;

    public Node root;
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
            startBool = false;
        }


        root = new Node(board, startBool, rootVal);
        root.setArrowMove(!arrowMove);

        this.depth = depth;
        this.branch = branch;


        nodePointer = new TreeTraverse(Board);
        curdepth = 0;


    }

    public void addChildren(Node parent)    {

        if(parent == null)  {
            System.out.println("EXCERPTION CAUGHT");
        }
        //We know the current Tree from tree traverse, so tahts where we need to ask for the possible moves for the parent Node´s possible moves
        int queens;

        //First check if the number of Possible moves is lesser than the Branch, because then we need to adjust the limit of Child
        // -> But for that we first need to know whether WE are supposed to move OR the OPPONENT
        int queenVal;
        int limit;
        Node childNode;

        //System.out.println("OwnVal: " + ownVal);
        if(!parent.ownMove && !parent.arrowMove) {  //Opponent is supposed to shoot an Arrow

            GridCoordinate origin = parent.getDest();

            limit = nodePointer.countPosOptions(origin);
            if(limit > branch)  {
                limit = branch;
            }
            while(parent.getChildren().size() < limit) {
                childNode = nodePointer.createChild(parent);
                parent.addChild(childNode);
            }
        }
        else if(!parent.ownMove && parent.arrowMove) {      //This means WE are supposed to move a Queen

            //Check limit of options

            queenVal = ownVal;
            limit = nodePointer.countPosOptions(queenVal);
            if(limit > branch)  {
                limit = branch;
            }
            else {

            }

            //System.out.println("limit: " + limit);


            //create children
            while(parent.getChildren().size() < limit) {
                childNode = nodePointer.createChild(parent);
                parent.addChild(childNode);
            }
        }
        else if(parent.ownMove && parent.arrowMove)  {  //This means the opponent is supposed to move

            //We set the value we are going to look for the Queens of the opponent
            queenVal = parent.getOpVal();

            limit = nodePointer.countPosOptions(queenVal);

            //in case we have more options than we need
            if(limit > branch)  {
                limit = branch;
            }

            while(parent.getChildren().size() < limit) {
                childNode = nodePointer.createChild(parent);
                parent.addChild(childNode);
            }
        }
        else if(parent.ownMove && !parent.arrowMove) {  //We are supposed dto shoot an arrow now

            // We know the origijn from which we are goiung to shoort
            GridCoordinate origin = parent.getDest();

            limit = nodePointer.countPosOptions(origin);
            if(limit > branch)  {
                limit = branch;
            }
            while(parent.getChildren().size() < limit) {
                childNode = nodePointer.createChild(parent);
                parent.addChild(childNode);
            }

        }
    }

    public void partBuild(Node newNode) {

        System.out.println("NewNode = " + newNode);
        nodePointer.printBoard();

        if(newNode.depthOfNode < depth) {
            if (newNode.getChildren().size() == 0) {
                addChildren(newNode);


                nodePointer.evaluateChildren(newNode);
                bubbleSortNodesByScore(newNode.getChildren());

                System.out.println("Size: " + newNode.getChildren().size());

                nodePointer.performMove(newNode.getChildren().get(0));
                //System.out.println(newNode.getChildren().get(0));
                //nodePointer.printBoard();

                partBuild(newNode.getChildren().get(0));
            }
        }


    }



    public void buildTree() {
        System.out.println("ROOT\n" + root);
        //nodePointer.printBoard();

        addChildren(root);

        ArrayList<Node> children = root.getChildren();
       // System.out.println("Size: " + children.size());
        nodePointer.evaluateChildren(root);
        nodePointer.printBoard();




        bubbleSortNodesByScore(root.getChildren());
        for(Node Child: root.getChildren())    {
            System.out.println(Child);
        }

        //System.out.println("Node: " + root.getChildren().get(root.getChildren().size()-1));

        Node toExpand = root.getChildren().get(0);

        GridCoordinate origin = toExpand.getOrigin();
        GridCoordinate dest = toExpand.getDest();
        boolean arrowMove = toExpand.arrowMove;

        nodePointer.performMove(origin, dest, arrowMove);

        //nodePointer.printBoard();

        addChildren(toExpand);


        nodePointer.evaluateChildren(toExpand);

        bubbleSortNodesByScore(toExpand.getChildren());
        for(Node Child: toExpand.getChildren())    {
            //System.out.println(Child.getScore() + "\t");
        }

        GridCoordinate or2 = toExpand.getChildren().get(0).getOrigin();
        GridCoordinate dest2 = toExpand.getChildren().get(0).getDest();
        boolean ar2 = toExpand.getChildren().get(0).arrowMove;

        //System.out.println("Origin: " + or2 + "\tDest2: " + dest2 + "\tar2: " + ar2 );

        nodePointer.performMove(or2, dest2, ar2);
        nodePointer.printBoard();

        /*
        for(Node child : children)  {
            System.out.println(child);
        }
        */

    }


    public void expand(Node parent)    {

    }

}

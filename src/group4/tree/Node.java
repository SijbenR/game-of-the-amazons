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

    boolean fullyExplored;

    protected Node parent;
    protected ArrayList<Node> Children;

    protected int score;
    protected GridCoordinate origin, dest;

    private int playerVal;

    int[][] Board = new int[10][10];

    //root
    public Node(int[][] Board)   {
        this.Board = Board;
    }


    public Node(Node parent, GridCoordinate origin, GridCoordinate dest)   {

    }



}

package group4.tree;

import group4.ui.GridCoordinate;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Robins on 10.12.2016.
 */
public class Node {

    private Node parent;
    private ArrayList<Node> Children;
    private boolean QueenMove;
    private int originCoord, destCoord;

    //Is only set when setBoard is triggered


    public Node(Node parent, GridCoordinate origin, GridCoordinate Destination, boolean QueenMove) {
        this.parent = parent;

    }

    public boolean isSame()  {
        return false;
    }







}

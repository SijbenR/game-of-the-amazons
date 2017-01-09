package group4.tree;

import group4.logic.tempBoard;
import group4.ui.GridCoordinate;
import group4.utilities.BoardOperations;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Robins on 10.12.2016.
 *
 * WORK IN PROGRESS - DO NOT USE YET!
 */
public class Node {

    boolean fullyExplored;

    protected Node parent;
    protected ArrayList<Node> Children;

    protected int score;
    protected GridCoordinate origin, dest;


    public int playerVal;

    public boolean ownMove, arrowMove;
    String BoardCompressed;


    //root
    public Node(String BoardCompressed, boolean ownMove, int startVal)   {
        this.BoardCompressed = BoardCompressed;
        this.Children = new ArrayList<>();
        this.ownMove = ownMove;
        this.playerVal = startVal;

        this.parent = null;
        this.origin = null;
        this.dest = null;
    }


    public Node(Node parent, GridCoordinate origin, GridCoordinate dest)   {

        this.parent = parent;
        this.Children = new ArrayList<>();

        this.origin = origin;
        this.dest = dest;

    }


    public void addChild(Node Child)   {

        Children.add(Child);

    }

    public void setScore(int score)   {
        this.score = score;
    }

    public int getScore()   {
        return score;
    }

    public GridCoordinate getOrigin()   {
        return origin;
    }

    public GridCoordinate getDest()   {
        return dest;
    }

    public boolean isSame(Node toCompare) {
        if(origin.isSame(toCompare.getOrigin()) && dest.isSame(toCompare.getDest()))    {
            return true;
        }
        else    {
            return false;
        }
    }

    public boolean validAmongChildren(Node toCompare)    {
        for(Node child: Children)   {
            if(isSame(toCompare)) {
                //to compare already amongs children
                return false;
            }
        }
        return  true;
    }

    public ArrayList<Node> getChildren()    {
        return Children;
    }

}

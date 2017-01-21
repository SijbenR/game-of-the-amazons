package group4.tree;

import group4.logic.tempBoard;
import group4.ui.GridCoordinate;
import group4.utilities.BoardOperations;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Robins on 10.12.2016.
 *
 * WORK IN PROGRESS - DO NOT USE YET!
 */
public class Node {

    boolean fullyExplored;

    protected Node parent;
    protected ArrayList<Node> Children;

    protected double score;
    protected GridCoordinate origin, dest;
    protected int depthOfNode;

    public int playerVal;

    public boolean ownMove, arrowMove;
    protected double value;
    public String BoardCompressed;


    //root
    public Node(String BoardCompressed, boolean ownMove, int startVal)   {
        this.BoardCompressed = BoardCompressed;
        this.Children = new ArrayList<Node>();
        this.ownMove = ownMove;
        this.playerVal = startVal;

        this.parent = null;
        this.origin = null;
        this.dest = null;
        this.depthOfNode = 0;
    }


    public Node(Node parent, GridCoordinate origin, GridCoordinate dest)   {

        this.parent = parent;
        this.Children = new ArrayList<Node>();

        this.origin = origin;
        this.dest = dest;
        this.depthOfNode = parent.depthOfNode + 1;
    }


    public void addChild(Node Child)   {
        if(ownMove == true && arrowMove == true)  {
            Child.setOwnMove(false);
            Child.setArrowMove(false);

            if(playerVal == 1)
                Child.setPlayerVal(2);
            else
                Child.setPlayerVal(1);
        }
        else if(ownMove == true && arrowMove == false) {
            Child.setOwnMove(true);
            Child.setArrowMove(true);

            Child.setPlayerVal(playerVal);
        }
        else if(ownMove == false && arrowMove == false)  {
            Child.setOwnMove(false);
            Child.setArrowMove(true);
            Child.setPlayerVal(playerVal);
        }
        else if(ownMove == false && arrowMove == true)  {
            Child.setOwnMove(true);
            Child.setArrowMove(false);

            if(playerVal == 1)
                Child.setPlayerVal(2);
            else
                Child.setPlayerVal(1);
        }
        else    {
            System.out.println("Exception caught");
        }

        //System.out.println("Adding child: " + Child);
        Children.add(Child);

    }

    public void setScore(double score)   {
        this.score = score;
    }

    public double getScore()   {
        return score;
    }
    public void setValue(double value  )   {
        this.value = value;
    }

    public double getValue()   {
        return value;
    }





    public void setOwnMove(boolean ownMove) {
        this.ownMove = ownMove;
    }

    public void setChildren(ArrayList<Node> newChildren) {
        Collections.copy(this.Children, newChildren);
    }


    public void setArrowMove(boolean arrowMove) {
        this.arrowMove = arrowMove;
    }

    public void setPlayerVal(int val) {
        this.playerVal = val;
    }

    public GridCoordinate getOrigin()   {
        return origin;
    }

    public GridCoordinate getDest()   {
        return dest;
    }

    public Node getParent() {return parent;}

    public int getOpVal()   {
        if(playerVal == 1)
            return 2;
        else
            return 1;
    }

    public int getNodeDepth()   {return depthOfNode;    }



    public boolean isSame(Node toCompare) {
        //System.out.println("Origin: " + origin + "\tDestination: " + dest);

        if(origin.isSameGridcoordinate(toCompare.getOrigin()) && dest.isSameGridcoordinate(toCompare.getDest()) && depthOfNode == toCompare.depthOfNode)    {
            return true;
        }
        else    {
            return false;
        }
    }

    public boolean validAmongChildren(Node toCompare)    {
        //System.out.println("ToCompare: " + toCompare);
        for(Node child: Children)   {
            if(child.isSame(toCompare)) {
                //to compare already amongs children
                return false;
            }
        }
        return  true;
    }

    public ArrayList<Node> getChildren()    {
        return Children;
    }


    public String toString()   {
        return new String("Node depth = " + depthOfNode + "\tOrigin: " + origin + "\tDestination: " + dest + "\tOwnmove: " + ownMove + "\tArrowMove: " + arrowMove + "\tPlayerVal: " + playerVal + "\tScore: " + score + "\n");
    }

    public void printNode() {
        System.out.println(toString());
    }


}

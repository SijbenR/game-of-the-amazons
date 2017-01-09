package group4.utilities;

import group4.tree.Node;
import group4.ui.GridCoordinate;

import java.util.ArrayList;

import static group4.utilities.BoardOperations.*;

/**
 * Created by Robins on 06.01.2017.
 *
 * WORK IN PROGRESS - DO NOT USE YET!
 */
public class TreeTraverse {

    private int[][] Board;

    public TreeTraverse(int[][] Board)   {
        this.Board = Board;
    }


    public void performMove(GridCoordinate origin, GridCoordinate dest, boolean arrowMove)   {


        if(getValAt(origin) == 3 && arrowMove)   {
            //Removing Arrow
            setEmpty(Board, origin);
        }
        else if((getValAt(origin) == 1 || getValAt(origin) == 2) && arrowMove && getValAt(dest) == 0)   {
            //Set Arrow
            setArrow(Board, dest);
        }
        else if((getValAt(origin) == 1 || getValAt(origin) == 2) && !arrowMove && getValAt(dest) == 0)   {
            //Move Queen
            int val = getValAt(origin);
            setEmpty(Board, origin);
            setQueenOn(Board, dest, val);
        }
        else if(getValAt(origin) == 0 && !arrowMove && (getValAt(dest) == 1 || getValAt(dest) == 2))   {
            //Move Queen BACK
            int val = getValAt(dest);
            setEmpty(Board, dest);
            setQueenOn(Board, origin, val);
        }
        else    {
            System.out.println("invalid Exception:\nOrigin = " + getValAt(origin) + "\nDestination = " + getValAt(dest) + "\nArrowMove = " + arrowMove);
        }
    }

    private int getValAt(GridCoordinate position) {
        int x = position.x - 1;
        int y = position.y - 1;

        if(!checkBound(Board, y, x))    {
            System.out.println(position + " - Not within Bounds");
        }
        return Board[y][x];
    }

    public GridCoordinate[] generateRanMove(Node parent)    {
        int val;

        //Position 0 => Origin
        //Position 1 => Destination
        GridCoordinate[] returner = new GridCoordinate[2];

        ArrayList<GridCoordinate> posDest;

        //First we check if the now to be computed move is a Queen or an Arow move
        if(parent.arrowMove)    {
            val = 3;
            //Because the previous queen destination is where we are going to shoot an arrow from
            returner[0] = parent.getDest();

            posDest = listPosDest(Board, returner[0]);
            int ran = (int) (posDest.size() * Math.random());
            GridCoordinate tar = posDest.get(ran);
            Node newNode = new Node(parent, returner[0], tar);

            //There might only be a limited number of solutions possible - we have to consider the case when this number is smaller than the allowed branch
            while(!parent.validAmongChildren(newNode))  {
                ran = (int) (posDest.size() * Math.random());

            }
        }


       // ArrayList<GridCoordinate> posQueens = posQueens(Board, )


        return null;
    }




}

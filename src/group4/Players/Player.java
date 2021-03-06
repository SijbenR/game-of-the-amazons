package group4.Players;

import group4.ui.GridCoordinate;

import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;

public class Player {

    GridCoordinate[] amazonPositions = new GridCoordinate[4];
    private boolean isFirst;


    //Bot relevant values
    private boolean isBot;
    private boolean isThinking;
    private boolean status;




    public Player(boolean isFirst) {
        this.isFirst = isFirst;
        this.isBot = false;
    }


    public Player(boolean isFirst, boolean isBot) {
        this.isFirst = isFirst;
        this.isBot = isBot;
    }

    public boolean isBot()	{
        if(isBot) {
            //System.out.println("Player: " + toString());
        }
        return isBot;
    }

    public boolean isFirst() {
        return isFirst;
    }



    public void setFirst() {
        isFirst = true;
    }

    public void setSecond() {
        isFirst = false;
    }

    public void giveInput(int[][] Board) {
    }

    //Function Overload in Bot classes
    public GridCoordinate[] chooseQueenMove()   {
        return null;
    }
    public GridCoordinate chooseArrowMove()   {
        return null;
    }


    //Function Overload in Bot classes
    public GridCoordinate nextMove()	{
        return null;
    }

    public boolean isThinking() {
        return isThinking;
    }



    public int getVal() {
        if (isFirst) {
            return 1;
        } else {
            return 2;
        }
    }



    public void setUpQueens() {
        int startY1, startY2;
        if (isFirst) {
            startY1 = 7;
            startY2 = 9;
        } else {
            startY1 = 2;
            startY2 = 0;
        }
        int i = 0;

        amazonPositions[i++] = new GridCoordinate(0, startY1);
        amazonPositions[i++] = new GridCoordinate(2, startY2);
        amazonPositions[i++] = new GridCoordinate(7, startY2);
        amazonPositions[i++] = new GridCoordinate(9, startY1);
    }

    public GridCoordinate[] getAmazonsPosistions() {
        return amazonPositions;
    }

    public GridCoordinate getOrigin()	{
        return null;
    }

    public GridCoordinate getDestination()	{
        return null;
    }

    public void setActive(boolean value)   {
        status = value;
    }

    public boolean getStatus() {return true;}

    public String toString()    {
        String returnString = new String("Human Player - Value = " + getVal());
        return returnString;
    }

}

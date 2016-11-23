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




    public Player(boolean isFirst) {
        this.isFirst = isFirst;
        this.isBot = false;
    }


    public Player(boolean isFirst, boolean isBot) {
        this.isFirst = isFirst;
        this.isBot = isBot;
    }


    public boolean isFirst() {
        return isFirst;
    }

    public boolean isBot()	{
        return isBot;
    }

    public void setFirst() {
        isFirst = true;
    }

    public void setSecond() {
        isFirst = false;
    }

    //Function Overload in Bot classes
    public void giveInput(int[][] Grid, boolean isQueenMove)	{

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

    public String toString()	{
        if(isFirst == true)	{
            return new String("White");
        }
        else	{
            return new String("Black");
        }
    }

    public void setUpQueens() {
        int startY1, startY2;
        if (isFirst) {
            startY1 = 6;
            startY2 = 9;
        } else {
            startY1 = 3;
            startY2 = 0;
        }
        int i = 0;

        amazonPositions[i++] = new GridCoordinate(0, startY1);
        amazonPositions[i++] = new GridCoordinate(3, startY2);
        amazonPositions[i++] = new GridCoordinate(6, startY2);
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

}

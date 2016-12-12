package group4.utilities;

import group4.ui.GridCoordinate;

import java.util.ArrayList;

/**
 * Created by robin on 08.12.2016.
 */
public class BoardOperations {


    public void moveQueen(int[][] Array, GridCoordinate origin, GridCoordinate dest, int val) {

        int originX = origin.x - 1;
        int originY = origin.y - 1;

        int destX = dest.x - 1;
        int destY = dest.y - 1;

        if(!checkBound(Array, originY, originX) || Array[originY][originX] != val)   {
            System.out.println("Invalid Origin position");
        }
        else if(!checkBound(Array, destY, destX) || Array[destY][originX] != val)  {
            System.out.println("Invalid Destination position");
        }
        else    {
            setEmpty(Array, origin);

        }
    }


    public static ArrayList<GridCoordinate> posQueens(int[][] Array, int val) {
        ArrayList<GridCoordinate> queens = new ArrayList<GridCoordinate>();
        int pos=0;
        GridCoordinate temp;
        for(int x = 1; x < 11; x++)	{
            for(int y = 1; y < 11; y++)	{
                temp = new GridCoordinate(x,y);
                if(queenOn(Array, temp, val))	{
                    queens.add(temp);
                }
                if(queens.size() == 4)	{
                    break;
                }
            }
        }
        return queens;
    }

    public static boolean queenOn(int[][] Array, GridCoordinate position, int val){
        return queenOn(Array, position.y - 1, position.x - 1, val);
    }

    public static boolean queenOn(int[][] Array, int y, int x,int val){
        if(!checkBound(Array,y,x) || Array[y][x] != val)    {
            return false;
        }
        return true;
    }

    public void setQueenOn(int[][] Grid, GridCoordinate position, int val){
        setQueenOn(Grid, position.y, position.x, val);
    }

    public void setQueenOn(int[][] Grid, int y, int x, int val){
        if(checkBound(Grid, y, x) && Grid[y][x] == 0 && (val == 2 || val == 3))   {
            Grid[y][x] = val;
        }
        else    {
            System.out.println("SetQueenOn() Invalid inputs");
        }
    }


    public static int evalMobility(int[][] Array, GridCoordinate position)    {
        calcPosMoves(Array, position, 5);
        int val = countPosMove(Array);
        removePosMoves(Array);
        return val;
    }


    public void setEmpty(int[][] Grid, GridCoordinate position){
        setEmpty(Grid, position.y, position.x);
    }

    public void setEmpty(int[][] Grid, int y, int x){
        if(checkBound(Grid, y, x) && Grid[y][x] != 0)   {
            Grid[y][x] = 0;
        }
    }

    public static void calcPosMoves(int[][] Array, GridCoordinate position, int val){
        calcPosMoves(Array, position.x, position.y, val);
    }

    public static void calcPosMoves(int[][] Array, int x, int y, int val)    {
        removePosMoves(Array);


        int i, j;
        int tempX, tempY;
        int length = Array.length;
        i = y - 1;
        j = x - 1;

        // up
        for (tempY = y - 1; tempY >= 0 && Array[tempY][x] == 0; tempY--) {
            Array[tempY][x] = val;
        }

        // Direction: bottom vertical
        // down
        for (tempY = y + 1; tempY < Array.length && Array[tempY][x] == 0; tempY++) {
            Array[tempY][x] = val;
        }

        // Direction: left horizontal
        for (tempX = x - 1; tempX >= 0 && Array[y][tempX] == 0; tempX--) {
            Array[y][tempX] = val;
        }
        // Direction: right horizontal
        for (tempX = x + 1; tempX < Array[0].length && Array[y][tempX] == 0; tempX++) {
            Array[y][tempX] = val;
        }
        // Direction: top right diagonal
        tempY = y - 1;
        tempX = x + 1;

        while (checkBound(Array, tempY, tempX) && Array[tempY][tempX] == 0) {
            //System.out.println("Position:\tY = " + tempY + "\tX = " + tempX +
            //"\nStill in Bounds? " + checkBound(tempY, tempX));

            Array[tempY][tempX] = val;
            tempY--;
            tempX++;

        }
        // Direction: top left diagonal
        tempY = y - 1;
        tempX = x - 1;

        while (checkBound(Array, tempY, tempX) && Array[tempY][tempX] == 0) {

            Array[tempY][tempX] = val;
            tempY--;
            tempX--;

        }
        // Direction: bottom right diagonal
        tempY = y + 1;
        tempX = x + 1;

        while (checkBound(Array, tempY, tempX) && Array[tempY][tempX] == 0) {
            // System.out.println("Position:\tY = " + tempY + "\tX = " + tempX +
            // "\nStill in Bounds? " + checkBound(tempY, tempX));

            Array[tempY][tempX] = val;
            tempY++;
            tempX++;

        }
        // Direction: bottom left diagonal
        tempY = y + 1;
        tempX = x - 1;

        while (checkBound(Array, tempY, tempX) && Array[tempY][tempX] == 0) {
            Array[tempY][tempX] = val;
            tempY++;
            tempX--;

        }
    }

    public static int countPosMove(int[][] Array)    {
        int count = 0;
        for(int i = 0; i < Array.length; i++)   {
            for(int j = 0; j < Array[0].length; j++)   {
                if (Array[i][j] == 5 || Array[i][j] == 4) {
                    count++;
                }
            }
        }
        return count;
    }

    public static boolean checkBound(int[][] Grid, int y, int x) {
        if ((y >= 0 && y < Grid.length) && (x >= 0 && x < Grid[0].length)) {
            return true;
        } else {
            return false;
        }
    }



    public static int countArrow(int[][] array)  {
        int count = 0;
        for(int i = 0; i < array.length; i++)   {
            for(int j = 0; j < array[0].length; j++)   {
                if(array[i][j] == 3)
                    count++;
            }
        }
        return count;
    }

    public static void removePosMoves(int[][] Array) {
        for(int i = 0; i < Array.length; i++)   {
            for(int j = 0; j < Array.length; i++)   {
                if(Array[i][j] == 4 || Array[i][j] == 5)    {
                    Array[i][j] = 0;
                }
            }
        }
    }

    public static void printArrayint(int [][] array) {
        String s;
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                if (array[i][j] < 10) {
                    s = "";
                } else {
                    s = "0";
                }
                System.out.print(s + array[i][j] + " ");
            }
            System.out.println("");
        }
    }


}

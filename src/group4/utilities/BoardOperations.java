package group4.utilities;

import group4.ui.GridCoordinate;

/**
 * Created by robin on 08.12.2016.
 */
public class BoardOperations {


    public static void calcPosMoves(int[][] Array, int x, int y, int val)    {

    }

    public static void calcPosMoves(int[][] Array, GridCoordinate pos, int val)    {
        calcPosMoves(Array, pos.x - 1, pos.y - 1, val);
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

}


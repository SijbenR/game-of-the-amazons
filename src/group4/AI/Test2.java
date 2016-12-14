package group4.AI;

import group4.utilities.BoardOperations;

import java.util.Arrays;

import static group4.AI.MinMax.deduceMoves2;

/**
 * Created by Administrator on 14/12/2016.
 */
public class Test2 {
    public static void main(String[] args) {
        int[][] board1 = BoardOperations.listToArray(new int[]
                {
                        0, 0, 0, 2, 0, 0, 2, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        2, 0, 0, 0, 0, 0, 0, 0, 0, 2,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        1, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 1, 0, 0, 1, 0, 0, 0,
                }
        );
        int[][] board2 = BoardOperations.listToArray(new int[]
                {
                        0, 0, 0, 2, 0, 0, 2, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        2, 0, 0, 0, 0, 0, 0, 0, 0, 2,
                        0, 0, 0, 1, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        1, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 3, 0, 0, 1, 0, 0, 0,
                }
        );
        System.out.println(Arrays.toString(deduceMoves2(board1,board2,1)));
    }
}

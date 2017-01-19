package group4.AI;

import group4.utilities.BoardOperations;

import java.util.Arrays;

import static group4.AI.MinMax.deduceMoves2;

/**
 * Created by Administrator on 14/12/2016.
 */
public class Test2 {
    public static void main(String[] args) {
        int[][] board = BoardOperations.listToArray(new int[]
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
       long st,et;
       TerritoryEval t=new TerritoryEval();
       MobilityEval m= new MobilityEval();
       double a;
       st=System.nanoTime();
       a=t.evaluate(board,1);
       et=System.nanoTime();
       System.out.println(a+", "+(et-st)/1000000000.0);

        st=System.nanoTime();
        a=m.evaluate(board,1);
        et=System.nanoTime();
        System.out.println(a+", "+(et-st)/1000000000.0);

    }
}

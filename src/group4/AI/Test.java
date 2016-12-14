package group4.AI;
import group4.AI.*;
import group4.utilities.BoardOperations;

import java.io.IOException;
import java.io.PrintWriter;

import static group4.AI.MinMax.getBoardAsString;

public class Test {
    public static void main(String[] args) throws IOException {

        int[][] board= BoardOperations.listToArray(new int[]
                {
                        0,0,0,2,0,0,2,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        2,0,0,0,0,0,0,0,0,2,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        1,0,0,0,0,0,0,0,0,1,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,1,0,0,1,0,0,0,
                }
        );
        boolean player=true;
        MobilityEval eval=new MobilityEval();
        MinMax pl1=new MinMax(1,eval);
        MinMax pl2=new MinMax(2,eval);
        pl1.setDepth(2);
        pl2.setDepth(1);
        int count=1;
        PrintWriter logFile = new PrintWriter("test.log");
        double temp=eval.evaluate(board, 1);
        long t1=System.nanoTime();
        while(temp < 1.0 && temp > 0.0)
        {


            if(player)
            {
                pl1.getMove(board);
                board=pl1.bestBoard;
            }
            else
            {
                pl2.getMove(board);
                board=pl2.bestBoard;
            }
            player=!player;
            temp=eval.evaluate(board, 1);
            logFile.println(count+"\tPlayer "+((player) ? "1" : "2") +" Value for pl1: "+temp);
            logFile.println(getBoardAsString(board));
            logFile.flush();
            System.out.println(count);
            count++;
        }
        long t2=System.nanoTime();
        logFile.close();

        System.out.println((temp==1.0) ? "Player 1 wins" : "Player 2 wins");
        System.out.println("Time: "+(t2-t1)/1000000000.0);


    }
}

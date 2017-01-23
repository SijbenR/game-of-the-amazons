package group4.AI;


import java.util.Arrays;

import static group4.AI.MinMax.getBoardAsString;

public class Test0 {
    public static void main(String[] args)
    {
        Experiment exp=new Experiment()
            .setBoard()
            .setNumTest(60)
            .setPlayer1(new MinMax(1,new TerritoryEval2(1)).setMaxfinal(10).setDepth(1))
            .setPlayer2(new MinMax(2,new MobilityEval(2)).setMaxfinal(10).setDepth(1))
            //.setPlayer1(new RandomAI(1))
            //.setPlayer2(new RandomAI(2))
            .setVerbose(true)
            .setRandomize(true);
        exp.run();




        /*int[][] b=new Experiment().setBoard().getInitBoard();

        System.out.println(getBoardAsString(b));
        System.out.println(RandomAI.getQueensPositions(b,1));
        */

    }
}

package group4.AI;


import group4.MCTS.Sean;

import java.util.Arrays;

import static group4.AI.MinMax.getBoardAsString;

public class Test0 {
    public static void main(String[] args)
    {
        double startC = 2.1;
        while(startC < 3) {
            System.out.println("Current C = " + startC);

            Experiment exp = new Experiment()
                    .setBoard()
                    .setNumTest(50)
                    .setPlayer1(new Sean(true, 1000, false, startC))
                    .setPlayer2(new RandomAI(2))
                    //.setPlayer1(new RandomAI(1))
                    //.setPlayer2(new RandomAI(2))
                    .setVerbose(true)
                    .setRandomize(false);
            exp.run();

            startC += 0.1;
        }


        /*int[][] b=new Experiment().setBoard().getInitBoard();

        System.out.println(getBoardAsString(b));
        System.out.println(RandomAI.getQueensPositions(b,1));
        */

    }
}

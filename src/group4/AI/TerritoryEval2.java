package group4.AI;


import group4.ui.GridCoordinate;
import group4.utilities.BoardOperations;

import java.util.Arrays;

import static group4.AI.MinMax.getBoardAsString;
import static group4.AI.MinMax.getQueensPositions;

public class TerritoryEval2 extends EvaluationFunction{
    public static void main(String[] args)
    {
        int[][] board=new Experiment().setBoard().getInitBoard();
        System.out.println(getBoardAsString(board));
        EvaluationFunction eval= new TerritoryEval2(1);
        eval.evaluate(board);
    }
    private int player;
    private int opp;
    private int futPlay;
    private int futOpp;
    private int nTurns=1;

    public TerritoryEval2(int player)
    {
        this.player=player;
        opp = 3 - player;
        futPlay = player + 3;
        futOpp = opp + 3;
    }
    public TerritoryEval2 setTurns(int n)
    {
        nTurns=n;
        return this;
    }

    @Override
    public double evaluate(int[][] board)
    {
        int[][] b=new int[board.length][];
        for(int i=0;i<board.length;i++)
            b[i]=Arrays.copyOf(board[i],board[i].length);
        return getEval(b);
    }

    private double getEval(int[][] b) {

        int[][] board=new int[b.length][];
        for(int i=0;i<b.length;i++)
            board[i]=b[i].clone();

        int ter1 = 0;
        int ter2 = 0;

        boolean zeros;
        int turns=nTurns;

        do {
            for (GridCoordinate p : getQueensPositions(board, player)) {
            //For each direction
                for (int k1 = -1; k1 < 2; k1++)
                    for (int k2 = -1; k2 < 2; k2++) {
                        if (k1 == 0 && k2 == 0)
                            continue;

                        int i = p.x + k1;
                        int j = p.y + k2;

                        while (j >= 0 && j < board.length && i >= 0 && i < board[0].length) {
                            if (board[j][i] > 0 && board[j][i] < 4) //We encounter either another player's queen, an arrow or an enemy queen
                                break;
                    /*No future state possible yet,
                    except for futPlay which can accept us to rewrite it
                     */
                    board[j][i] = futPlay;
                    i += k1;
                    j += k2;
                }
            }

    }
    for (GridCoordinate p : getQueensPositions(board, opp)) {
        //For each direction
        for (int k1 = -1; k1 < 2; k1++)
            for (int k2 = -1; k2 < 2; k2++) {
                if (k1 == 0 && k2 == 0)
                    continue;

                int i = p.x + k1;
                int j = p.y + k2;

                while (j >= 0 && j < board.length && i >= 0 && i < board[0].length) {

                    if (board[j][i] > 0 && board[j][i] < 4) //We encounter either another player's queen, an arrow or an enemy queen
                        break;
                    /*No future state possible yet,
                    except for futPlay which can accept us to rewrite it
                     */
                    if (board[j][i] == 0)
                        board[j][i] = futOpp;
                    else if (board[j][i] == futPlay)
                        board[j][i] = -1;
                    i += k1;
                    j += k2;

                }
            }
    }
    //System.out.println(getBoardAsString(board));
    zeros = false;
    for (int j = 0; j < board.length; j++)
        for (int i = 0; i < board[0].length; i++) {
            if (board[j][i]==-1)
                board[j][i] = 3;

            else if (board[j][i]==0)
                zeros=true;
            else if (board[j][i]==futPlay)
            {
                ter1+=turns;
                board[j][i] = player;
            }
            else if (board[j][i]==futOpp)
            {
                ter2+=turns;
                board[j][i] = opp;
            }
        }

    turns--;
} while(turns>0 && zeros);
    return ((double) ter1)/(ter1+ter2);

}
}

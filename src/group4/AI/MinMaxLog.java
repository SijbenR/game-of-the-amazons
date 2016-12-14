package group4.AI;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.StringJoiner;

import static group4.AI.MinMax.*;

import group4.ui.GridCoordinate;

public class MinMaxLog {

    private int playerIndex;
    private EvaluationFunction evalF;
    private Comparator<int[][]> maxSort;
    private Comparator<int[][]> minSort;



    public static void main(String[] args) throws IOException {

        int[][] initBoard = new int[10][10];
        initBoard[3][0] = initBoard[0][3] = initBoard[0][6] = initBoard[3][9] = 1;
        initBoard[6][0] = initBoard[9][3] = initBoard[9][6] = initBoard[6][9] = 2;

        int[][] root=initBoard;

        PrintWriter dFile = new PrintWriter("values3depth.csv");
        PrintWriter logFile = new PrintWriter("minmax3depth.log");

        MinMaxLog algor = new MinMaxLog(1, new MobilityEval());
        int k=0;
        System.out.println("Starting.");
        while(k<70){
            double max=0;
            List<int[][]> boards = getPossibleBoards(root, 1, algor.evalF,
                    true, algor.maxSort, algor.minSort, 30, 30);
            double tempo=0;
            for(int i=0;i<boards.size();i++)
            {

                int[][] b=reverse(boards.get(i));
                long t1=System.nanoTime();
                double val = algor.minmax(b, 3,Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, true);
                long t2=System.nanoTime();
                dFile.append(board2CSV(b) + " " + val + "\n");
                if(max<val)
                {
                    max=val;
                    root=b;
                    tempo=(t2-t1)/1000000000.0;
                }
                System.out.print("+");
            }
            logFile.append("Turn:"+(k+1) +
                    " - Player: "+(k%2+1)+
                    String.format((Locale) null, " - Val: %.5f - Time: %.5f", max, tempo) +
                    "\n" + getBoardAsString((k%2==0) ? reverse(root) :root) +
                    "\n+++++++++++++++++++++++++++++++\n");
            System.out.println();
            dFile.flush();
            logFile.flush();
            k++;
            if(max==0 || max==1)
                break;
        }
        System.out.println("Done.");
        dFile.close();
        logFile.close();
    }

    public static String board2CSV(int[][] board) {
        StringJoiner b = new StringJoiner(" ");
        for (int i = 0; i < board.length; i++)
            for (int j = 0; j < board[0].length; j++)
                b.add(board[i][j] + "");
        return b.toString();
    }

    public static int[][] reverse(int[][] b) {
        int[][] b2=new int[b.length][b[0].length];
        int n=b.length-1;
        for (int i = 0; i < b.length; i++)
            for (int j = 0; j < b[0].length; j++) {
                if (b[i][j] == 1)
                    b2[n-i][j] = 2;
                else if (b[i][j] == 2)
                    b2[n-i][j] = 1;
                else
                    b2[n-i][j]=b[i][j];
            }
        return b2;
    }


    public MinMaxLog(int index, EvaluationFunction evalF) throws IOException {
        playerIndex = index;
        this.evalF = evalF;
        maxSort = (b1, b2) -> {
            int d=(int)Math.round((evalF.evaluate(b2, playerIndex) - evalF.evaluate(b1, playerIndex)) * Integer.MAX_VALUE);
            if(d!=0) return d;
            for(int i=0;i<b1.length;i++)
                for(int j=0;j<b1[0].length;j++)
                {
                    if(b1[i][j]-b2[i][j]!=0)
                        return b1[i][j]-b2[i][j];
                }
            return 0;
        };
        minSort = (b1, b2) -> {
            int d=(int) Math.round((evalF.evaluate(b1, playerIndex) - evalF.evaluate(b2, playerIndex)) * Integer.MAX_VALUE);
            if(d!=0) return d;
            for(int i=0;i<b1.length;i++)
                for(int j=0;j<b1[0].length;j++)
                {
                    if(b1[i][j]-b2[i][j]!=0)
                        return b1[i][j]-b2[i][j];
                }
            return 0;
        };

    }

    /*
     * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     * MINMAX STUFF
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */
    public double minmax(int[][] board, int depth, double alpha, double beta, boolean isMax) {
        // terminal node
        double value = evalF.evaluate(board, playerIndex);
        if (depth <= 0 || value == 1.0 || value == 0.0)
            return value;

        List<int[][]> boards = getPossibleBoards(board, (isMax) ? playerIndex : 3 - playerIndex, evalF,
                isMax, maxSort, minSort,30, 100);
        // MAX is playing
        if (isMax) {
            double vmax = -1;
            for (int[][] i : boards) {
                double v = minmax(i, depth - 1, alpha, beta, false);
                if (v > vmax)
                    vmax = v;
                if (v > alpha)
                    alpha = v;
                if (beta <= alpha)
                    break;
            }
            return vmax;
        }
        // MIN is playing
        else {
            double vmin = 3;
            for (int[][] i : boards) {
                double v = minmax(i, depth - 1, alpha, beta, true);
                if (v < vmin)
                    vmin = v;
                if (v < beta)
                    beta = v;
                if (beta <= alpha)
                    break;
            }
            return vmin;
        }
    }

    public static List<GridCoordinate> getQueensPositions(int[][] board, int player) {
        List<GridCoordinate> queens = new LinkedList<>();
        for (int i = 0; i < board.length; i++)
            for (int j = 0; j < board[0].length; j++)
                if (board[i][j] == player)
                    queens.add(new GridCoordinate(i, j));
        return queens;
    }

    public static int[][] moveToBoard(int[][] board, int player, GridCoordinate from, GridCoordinate to,
                                      boolean isArrow) {
        int[][] nBoard = new int[board.length][];
        for (int i = 0; i < board.length; i++)
            nBoard[i] = Arrays.copyOf(board[i], board.length);
        if (!isArrow)
            nBoard[from.x][from.y] = 0;
        nBoard[to.x][to.y] = (isArrow) ? 3 : player;
        return nBoard;
    }

    public static List<GridCoordinate> getPossibleMoves(int[][] board, GridCoordinate from) {
        LinkedList<GridCoordinate> to = new LinkedList<>();
        for (int k1 = -1; k1 < 2; k1++)
            for (int k2 = -1; k2 < 2; k2++) {
                if (k1 == 0 && k2 == 0)
                    continue;

                int i = from.x + k1;
                int j = from.y + k2;

                while (i >= 0 && i < board.length && j >= 0 && j < board[0].length) {
                    if (board[i][j] != 0)
                        break;
                    to.add(new GridCoordinate(i, j));
                    i += k1;
                    j += k2;
                }
            }
        return to;
    }

    public static String getBoardAsString(int[][] b) {
        StringBuilder builder = new StringBuilder();
        for (int[] i : b) {
            for (int j : i)
                builder.append(j + " ");
            builder.append("\n");
        }
        return builder.toString();
    }
}

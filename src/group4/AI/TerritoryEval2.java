package group4.AI;


import group4.utilities.BoardOperations;

import java.util.Arrays;

import static group4.AI.MinMax.getBoardAsString;

public class TerritoryEval2 extends EvaluationFunction{
    public static void main(String[] args)
    {
        int[] li=  new int[]{
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,3,0,3,3,0,0,0,
                        0,0,0,0,3,3,3,0,0,0,
                        2,3,3,3,3,2,3,3,0,0,
                        3,3,1,0,3,3,3,3,2,0,
                        3,0,3,0,3,0,0,3,3,0,
                        0,3,0,3,0,0,1,0,3,3,
                        1,3,0,0,2,0,0,0,0,0,
                        3,3,0,0,3,0,0,0,0,0,
                        0,0,3,1,0,0,0,0,0,0};

        int[][] board= BoardOperations.listToArray(li);
        System.out.println("First");
        System.out.println(getBoardAsString(board));
        TerritoryEval2 eval=new TerritoryEval2();
        System.out.println(eval.evaluate(board,1));
    }
    @Override
    public double evaluate(int[][] board, int player) {
        int[][] b2=territoryBoard(board);
        int p1=0,p2=0;
        for(int i=0;i<b2.length;i++)
            for(int j=0;j<b2[0].length;j++) {
                if (player == b2[i][j])
                    p1++;
                else if((3-player) == b2[i][j])
                    p2++;
            }
        return p1/((double) p1+p2);
    }


    public static int[][] territoryBoard(int[][] board) {
        int length = board[0].length;
        int height = board.length;

        int[][] fromBoard = board;
        int[][] newBoard = new int[length][height];

        boolean zeros = false;
        do {
            zeros = false;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < length; j++) {
                    if (fromBoard[i][j] != 0) {
                        newBoard[i][j] = fromBoard[i][j];
                        continue;
                    }
                    zeros = true;
                /*
                If this cell is reachable by one player in just one move, it belongs to that player.
                If more players can reach it, it is par(-1).
                 */
                    {
                        int status = 0;
                        int dirY = -1;
                        while (dirY <= 1 && status != -1) {
                            int dirX = -1;
                            while (dirX <= 1 && status != -1) {
                                if (dirX == 0 && dirY == 0) {
                                    dirX++;
                                    continue;
                                }
                                int x = i;
                                int y = j;
                                while (x < height && x >= 0 && y < length && y >= 0) {
                                    if (fromBoard[x][y] != 0) {
                                        if (fromBoard[x][y] < 3) {
                                            if (status != 0)
                                                status = (status == fromBoard[x][y]) ? status : -1;
                                            else status = fromBoard[x][y];
                                        }
                                        break;
                                    }
                                    x += dirX;
                                    y += dirY;
                                }
                                dirX++;
                            }
                            dirY++;
                        }

                        newBoard[i][j] = status;
                        //System.out.println(i + "," + j + ":" + status);
                    }//LALALA


                }
            }
            fromBoard=newBoard;
            //System.out.println(getBoardAsString(fromBoard));
        } while(zeros);
        return newBoard;
    }
}

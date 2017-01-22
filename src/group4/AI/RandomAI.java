package group4.AI;


import group4.ui.GridCoordinate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static group4.AI.MinMax.getBoardAsString;
import static group4.AI.MinMax.getQueensPositions;

public class RandomAI implements MoveProducer {

    int player;
    public RandomAI(int player)
    {
        this.player=player;
    }
    @Override
    public GridCoordinate[] getMove(int[][] board) {

        GridCoordinate[] move=new GridCoordinate[3];
        GridCoordinate from=null;
        List<GridCoordinate> froms= getQueensPositions(board, player);
        List<GridCoordinate> tos=null;
        List<GridCoordinate> arrowto;


        Collections.shuffle(froms);
        for (GridCoordinate f: froms ) {
            tos= getPossibleMoves(board, f);
            if (!tos.isEmpty()) {
                from = f;
                break;
            }
        }
        if (from == null) {
            System.err.println(froms);
            System.err.println(getBoardAsString(board));
            throw new RuntimeException("No mobility for player " + player);
        }
        move[0]=from;
        Collections.shuffle(tos);
        move[1]=tos.get(0);
        arrowto=getPossibleMoves(board, move[1]);
        arrowto.add(from);
        Collections.shuffle(arrowto);
        move[2]=arrowto.get(0);
        return move;

    }

    public static List<GridCoordinate> getPossibleMoves(int[][] board, GridCoordinate from) {
        ArrayList<GridCoordinate> to = new ArrayList<>();
        for (int k1 = -1; k1 < 2; k1++)
            for (int k2 = -1; k2 < 2; k2++) {
                if (k1 == 0 && k2 == 0)
                    continue;

                int i = from.x + k1;
                int j = from.y + k2;

                while (j >= 0 && j < board.length && i >= 0 && i < board[0].length) {
                    if (board[j][i] != 0)
                        break;
                    to.add(new GridCoordinate(i, j));
                    i += k1;
                    j += k2;
                }
            }
        return to;
    }



}

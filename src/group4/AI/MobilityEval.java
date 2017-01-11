package group4.AI;

        import static group4.AI.MinMax.getQueensPositions;
        import group4.ui.GridCoordinate;

public class MobilityEval extends EvaluationFunction {



    @Override
    public double evaluate(int[][] board, int player) {
        double pl1=0;
        double pl2=0;
        for(GridCoordinate i: getQueensPositions(board, player))
            pl1+=getNumPossibleMoves(board, i);
        for(GridCoordinate i: getQueensPositions(board, 3-player))
            pl2+=getNumPossibleMoves(board, i);
		/*I thought would be better to return the
		 *ratio of the mobility of the first player
		 *over the total mobility of both
		 */
        return pl1/(pl1+pl2);
    }

    /**
     * Computes the number of possible moves given a board and an origin of the move
     * @param board the board
     * @param from the origin of the move
     * @return the number of possible moves
     */
    public static int getNumPossibleMoves(int[][] board, GridCoordinate from) {

        int num=0;
        for (int k1 = -1; k1 < 2; k1++)
            for (int k2 = -1; k2 < 2; k2++) {
                if (k1 == 0 && k2 == 0)	continue;
                int i = from.x + k1;
                int j = from.y + k2;
                while (i >= 0 && i < board.length && j >= 0 && j < board[0].length) {
                    if (board[i][j] != 0) break;
                    num++;
                    i += k1;
                    j += k2;
                }
            }
        return num;
    }


}

package group4.AI;

        import java.util.List;
        import java.util.Map;
        import java.util.Map.Entry;
        import java.util.TreeMap;

        import group4.ui.GridCoordinate;
        import group4.utilities.BoardOperations;

        import java.util.LinkedList;
        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.Comparator;
        import java.util.TreeSet;

public class MinMax {

    /*
    public static void main(String[] args) {
        int[][] board = new int[10][10];
        int[] list=  new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,3,0,3,3,0,0,0,0,0,0,0,3,3,3,0,0,0,2,3,3,3,3,2,3,3,0,0,3,3,1,0,3,3,3,3,2,0,3,0,3,0,3,0,0,3,3,0,0,3,0,3,0,0,1,0,3,3,1,3,0,0,2,0,0,0,0,0,3,3,0,0,3,0,0,0,0,0,0,0,3,1,0,0,0,0,0,0};
        board= BoardOperations.listToArray(list);
       // board[3][0]= board[0][3] = board[0][6] =board[3][9]  = 1;
        //board[6][0]= board[9][3] = board[9][6] =board[6][9]  = 2;
        //System.out.println(getBoardAsString(board));
        long  starttime = System.currentTimeMillis();
        MinMax m=new MinMax(1,new MobilityEval());
        long  endtime = System.currentTimeMillis();
        //System.out.println(m.getMove(board));
        long totaltime=endtime-starttime;
        //System.out.println("time taken = " + totaltime);
    }
    */

    private int playerIndex;
    private EvaluationFunction evalF;
    private Comparator<int[][]> maxSort;
    private Comparator<int[][]> minSort;
    public GridCoordinate[] lastMove;
    public int[][] bestBoard;
    private int depth=3;
    private int maxstep=100;
    private int maxfinal=400;
    private int count=0;
    /**
     * Prepares the MinMax algorithm setting the index of the player.
     *
     * @param index
     *            the player's index
     */
    public MinMax(int index, EvaluationFunction evalF) {
        playerIndex = index;
        this.evalF = evalF;
        maxSort = (b1, b2) -> {
            return (evalF.evaluate(b2, playerIndex) > evalF.evaluate(b1, playerIndex))  ? 1 : -1;
        };
        minSort = (b1, b2) -> {
            return (evalF.evaluate(b2, playerIndex) > evalF.evaluate(b1, playerIndex))  ? -1 : 1;
        };
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    /**
     * Returns the best move found using the minmax algorithm
     *
     * @param board
     *            the board to analyse
     * @return the coordinates of the squares. [0] contains the square of the
     *         queen that moves, [1] contains where the queen moves to, [2]
     *         contains where the arrow is thrown to.
     */
    public GridCoordinate[] getMove(int[][] board) {
        GridCoordinate[] move = new GridCoordinate[3];
        minmax(board, depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, true, move);
        return move;

    }

    private double minmax(int[][] board, int depth, double alpha, double beta, boolean isMax, GridCoordinate[] moves) {
        // terminal node
        double value = evalF.evaluate(board, playerIndex);
        if (depth <= 0 || value == 1.0 || value == 0.0)
            return value;

        List<int[][]> boards = getPossibleBoards(board, playerIndex, evalF, isMax, minSort, maxSort, maxstep, maxfinal);
        // MAX is playing
        if (isMax) {
            int[][] bmax = null;
            double vmax = Double.NEGATIVE_INFINITY;
            for (int[][] i : boards) {
                double v = minmax(i, depth - 1, alpha, beta, false, null);
                if (v > vmax) {
                    vmax = v;
                    bmax = i;
                }
                if (v > alpha)
                    alpha = v;
                if (beta <= alpha)
                    break;

            }
            if (moves != null)
            {
                bestBoard=bmax;
                deduceMoves(board, bmax, playerIndex, moves);
                lastMove=moves;
            }
            //count++;
            //System.out.println(count);
            //System.out.println(Arrays.toString(moves));
         //   System.out.println(getBoardAsString(bmax));
            return vmax;
        }
        // MIN is playing
        else {
            double vmin = Double.POSITIVE_INFINITY;
            for (int[][] i : boards) {
                double v = minmax(i, depth - 1, alpha, beta, true, null);
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

    /**
     * Version that does not remember the moves Get all the possible boards
     *
     * @param board
     * @param player
     * @return
     */
    public static List<int[][]> getPossibleBoards(int[][] board, int player) {
        //
        List<int[][]> queenBoards = new LinkedList<>();
        List<GridCoordinate> queenTo = new LinkedList<>();
        //
        List<int[][]> finalBoards = new LinkedList<>();

        // First we compute all the possible boards according to the queen moves
        for (GridCoordinate i : getQueensPositions(board, player))
            for (GridCoordinate j : getPossibleMoves(board, i)) {
                queenBoards.add(moveToBoard(board, player, i, j, false));
                queenTo.add(j);
            }

        // Now we compute the boards according to the arrow moves
        for (int i = 0; i < queenBoards.size(); i++)
            for (GridCoordinate k : getPossibleMoves(queenBoards.get(i), queenTo.get(i)))
                finalBoards.add(moveToBoard(queenBoards.get(i), player, queenTo.get(i), k, true));

        return finalBoards;
    }

    /**
     * Returns the possible boards
     *
     * @param board
     *            the board
     * @param player
     *            the player index
     * @param evalF
     *            the evaluation function that we use
     * @param isMax
     *            whether the player is
     * @param maxSort comparator for max
     * @param minSort comparator for min
     * @param maxStepSize max number of the intermediate boards
     * @param maxFinalSize max number of the final boards
     * @return the list of the possible boards that can be generated by a move
     */
    public static List<int[][]> getPossibleBoards(int[][] board, int player,
                                                  EvaluationFunction evalF, boolean isMax, Comparator<int[][]> maxSort, Comparator<int[][]> minSort,
                                                  int maxStepSize, int maxFinalSize) {

        TreeSet<int[][]> finalBoards = new TreeSet<>(isMax ? maxSort : minSort);
        Map<int[][], GridCoordinate> queenStuff=new TreeMap<>(isMax ? maxSort : minSort);

        // First we compute all the possible boards according to the queen moves
        for (GridCoordinate i : getQueensPositions(board, player))
            for (GridCoordinate j : getPossibleMoves(board, i))
                queenStuff.put(moveToBoard(board, player, i, j, false), j);

        int count=0;
        for(Entry<int[][], GridCoordinate> e: queenStuff.entrySet())
        {
            if(count>=maxStepSize)
                break;
            for (GridCoordinate k : getPossibleMoves(e.getKey(),e.getValue()))
                finalBoards.add(moveToBoard(e.getKey(), player, e.getValue(), k, true));
            count++;
        }
        // Now we compute the boards according to the arrow moves
        List<int[][]> finalB=new ArrayList<>(finalBoards);
        return (finalB.size() > maxFinalSize) ?
                finalB.subList(0, maxFinalSize) : finalB;
    }


    /**
     * Translates a move to a new board
     *
     * @param board
     *            original board
     * @param player
     *            the player that moves
     * @param from
     *            the original position of the queen
     * @param to
     *            the new position
     * @param isArrow
     *            if the move regards an arrow or not
     * @return the new board
     */
    public static int[][] moveToBoard(int[][] board, int player, GridCoordinate from, GridCoordinate to,
                                      boolean isArrow) {
        int[][] nBoard = new int[board.length][];
        for (int i = 0; i < board.length; i++)
            nBoard[i] = Arrays.copyOf(board[i], board[0].length);
        if (!isArrow)
            nBoard[from.x][from.y] = 0;
        nBoard[to.x][to.y] = (isArrow) ? 3 : player;
        return nBoard;
    }

    /**
     * Returns all the possible new coordinates for the piece
     *
     * @param board
     *            the original board
     * @param from
     *            the point where the piece is
     * @return all the possible new coordinates
     */
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

    /**
     * Deduce the move (queen and arrow) applied to the board to get from B1 to
     * B2
     *
     * @param B1
     *            the previous board
     * @param B2
     *            the next board
     * @param player
     *            the player that moved
     * @return an array of GridCoordinate. [0] contains the queen's former
     *         position, [1] the queen's final position, [2] the arrow's final
     *         position
     */
    public static void deduceMoves(int[][] B1, int[][] B2, int player, GridCoordinate[] moves) {
        for (int i = 0; i < B1.length; i++)
            for (int j = 0; j < B1[0].length; j++) {
                if (B1[i][j] != B2[i][j]) {
                   // System.out.println(B1[i][j] + " " + B2[i][j]);
                    GridCoordinate pos = new GridCoordinate(j + 1, i + 1);
                    if (B1[i][j] == player) {
                        moves[0] = pos;
                        if (B2[i][j] == 3)
                            moves[2] = pos;
                    } else {
                        if (B2[i][j] == player)
                            moves[1] = pos;
                        else
                            moves[2] = pos;
                    }

                    if (moves[0] != null && moves[1] != null && moves[2]!=null)
                        break;
                }
            }
    }

    public static void deduceMoves2(int[][] B1, int[][] B2, int player, GridCoordinate[] moves) {
        for (int i = 0; i < B1.length; i++)
            for (int j = 0; j < B1[0].length; j++) {
                if (B1[i][j] != B2[i][j]) {
                    // System.out.println(B1[i][j] + " " + B2[i][j]);
                    GridCoordinate pos = new GridCoordinate(i + 1, j + 1);
                    if (B1[i][j] == player) {
                        moves[0] = pos;
                        if (B2[i][j] == 3)
                            moves[2] = pos;
                    } else {
                        if (B2[i][j] == player)
                            moves[1] = pos;
                        else
                            moves[2] = pos;
                    }

                    if (moves[0] != null && moves[1] != null && moves[2]!=null)
                        break;
                }
            }
    }

}


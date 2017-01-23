package group4.AI;

        import java.util.List;
        import java.util.Map;
        import java.util.Map.Entry;
        import java.util.TreeMap;

        import group4.ui.GridCoordinate;
        import group4.utilities.BoardOperations;

        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.Comparator;
        import java.util.TreeSet;

public class MinMax implements MoveProducer {


    public static void main(String[] args) {
        int[][] board = new int[10][10];
        int[] list = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 3, 3, 0, 0, 0, 0, 0, 0, 0, 3, 3, 3, 0, 0, 0, 2, 3, 3, 3, 3, 2, 3, 3, 0, 0, 3, 3, 1, 0, 3, 3, 3, 3, 2, 0, 3, 0, 3, 0, 3, 0, 0, 3, 3, 0, 0, 3, 0, 3, 0, 0, 1, 0, 3, 3, 1, 3, 0, 0, 2, 0, 0, 0, 0, 0, 3, 3, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 3, 1, 0, 0, 0, 0, 0, 0};
        board = BoardOperations.listToArray(list);
        System.out.println(getBoardAsString(board));
        MinMax m = new MinMax(1, new MobilityEval(1))
                .setDepth(8)
                .setMaxfinal(5);

        long starttime = System.nanoTime();
        GridCoordinate[] moves = m.getMove(board);
        long endtime = System.nanoTime();
        System.out.println(Arrays.toString(moves));
        long totaltime = endtime - starttime;
        System.out.println("time taken = " + totaltime / 1000000000.0);
        System.out.println(m.alphacut + ", " + m.betacut);
    }


    private int playerIndex;
    private EvaluationFunction evalF;
    private Comparator<int[][]> maxSort;
    private Comparator<int[][]> minSort;
    public GridCoordinate[] lastMove;
    public int[][] prelastBoard;
    public int[][] lastBoard;
    public int[][] bestBoard;
    private int depth = 3;
    private int maxstep = 100;
    private int maxfinal = 50;
    public int betacut = 0;
    public int alphacut = 0;
    private int nKilMov = 3;
    private int offset=0;

    //GridCoordinate[][] killerMoves=new GridCoordinate[depth+1][];

    private List<GridCoordinate[]>[] killerMoves = new ArrayList[depth + 1];

    /**
     * Prepares the MinMax algorithm setting the index of the player.
     *
     * @param index the player's index
     */
    public MinMax(int index, EvaluationFunction evalF) {
        playerIndex = index;
        this.evalF = evalF;
        maxSort = (b1, b2) -> {
            return (evalF.evaluate(b2) > evalF.evaluate(b1)) ? 1 : -1;
        };
        minSort = (b1, b2) -> {
            return (evalF.evaluate(b2) > evalF.evaluate(b1)) ? -1 : 1;
        };
        killerConstructor();
    }

    private void killerConstructor()
    {
        killerMoves=new ArrayList[depth + 1];
        for (int i=0;i<killerMoves.length;i++)
            killerMoves[i]=new ArrayList<>();
    }
    /*************************
     *
     *            SETTERS
     ******************************/

    public MinMax setKillerMoves(int k)
    {
        nKilMov=k;
        return this;
    }

    public MinMax setOffset(int o)
    {
        this.offset=o;
        return this;
    }
    public MinMax setDepth(int depth) {
        this.depth = depth;
        killerMoves=new ArrayList[depth+1];
        killerConstructor();
        return this;
    }

    public MinMax setMaxfinal(int maxfinal) {
        this.maxfinal = maxfinal; return this;
    }

    public MinMax setMaxstep(int maxstep) {
        this.maxstep = maxstep;return this;
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
        lastBoard=new int[board.length][];
        for (int i = 0; i < board.length ; i++) {
            lastBoard[i]=board[i].clone();
        }
        minmax(board, depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, true, true);
        return lastMove;
    }

    private double minmax(int[][] board, int depth, double alpha, double beta, boolean isMax, boolean first) {
        // terminal node
        double value = evalF.evaluate(board);
        if (depth <= 0 || value == 1.0 || value == 0.0)
            return value;

        List<int[][]> boards = getPossibleBoards(board, isMax, depth);
        // MAX is playing
        if (isMax) {
            int[][] bmax = null;
            double vmax = Double.NEGATIVE_INFINITY;
            for (int[][] i : boards) {
                double v = minmax(i, depth - 1, alpha, beta, false, false);
                if (v > vmax) {
                    vmax = v;
                    bmax = i;
                }
                if (v > alpha)
                    alpha = v;
                if (beta <= alpha) {
                    killerMoves[depth].add(0,deduceMoves2(board,i,playerIndex));
                    if(killerMoves[depth].size()>nKilMov)
                        killerMoves[depth].remove(nKilMov);
                    alphacut++;
                    break;
                }

            }
            if (first)
            {
                if (lastBoard != null)
                    prelastBoard=lastBoard;
                lastBoard=board;
                bestBoard=bmax;
                //deduceMoves(board, bmax, playerIndex, moves);
                lastMove=deduceMoves2(board, bmax, playerIndex);
            }

            //System.out.println(Arrays.toString(moves));
         //   System.out.println(getBoardAsString(bmax));
            return vmax;
        }
        // MIN is playing
        else {
            double vmin = Double.POSITIVE_INFINITY;
            for (int[][] i : boards) {
                double v = minmax(i, depth - 1, alpha, beta, true, false);
                if (v < vmin)
                    vmin = v;
                if (v < beta)
                    beta = v;
                if (beta <= alpha) {
                    killerMoves[depth].add(0,deduceMoves2(board,i,3-playerIndex));
                    if(killerMoves[depth].size()>nKilMov)
                        killerMoves[depth].remove(nKilMov);
                    betacut++;
                    break;

                }
            }
            return vmin;
        }
    }

    public void getBoardss()
    {
        System.err.println(getBoardAsString(prelastBoard));
        System.err.println(getBoardAsString(lastBoard));
        System.err.println(getBoardAsString(bestBoard));
        System.err.println(Arrays.toString(lastMove));
    }

    public List<int[][]> getPossibleBoards(int[][] board, boolean isMax, int depth) {

        int player = isMax ? playerIndex : 3 - playerIndex;
        TreeSet<int[][]> finalBoards = new TreeSet<>(isMax ? maxSort : minSort);
        Map<int[][], GridCoordinate> queenStuff=new TreeMap<>(isMax ? maxSort : minSort);

        // First we compute all the possible boards according to the queen moves
        for (GridCoordinate i : getQueensPositions(board, player))
            for (GridCoordinate j : getPossibleMoves(board, i))
                queenStuff.put(moveToBoard(board, player, i, j, false), j);

        int count=0;
        for(Entry<int[][], GridCoordinate> e: queenStuff.entrySet())
        {

            if(count>=maxstep)
                break;
            for (GridCoordinate k : getPossibleMoves(e.getKey(),e.getValue())) {

                finalBoards.add(moveToBoard(e.getKey(), player, e.getValue(), k, true));
            }
            count++;
        }
        // Now we compute the boards according to the arrow moves
        List<int[][]> finalB=new ArrayList<>(finalBoards);
        finalB= (finalB.size() > maxfinal) ?
                finalB.subList(0, maxfinal) : finalB;
        if (!killerMoves[depth].isEmpty())
        {
            for(GridCoordinate[] k: killerMoves[depth])
            {
                if (Experiment.isLegalMove(board, k, player))
                    finalB.add
                            (0,
                            moveToBoard
                                    (
                                    moveToBoard(board, player, k[0], k[1], false),
                                    player, k[1], k[2], true
                                    )
                            );
            }
        }

        return finalB;
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
            nBoard[from.y][from.x] = 0;
        nBoard[to.y][to.x] = (isArrow) ? 3 : player;
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
        List<GridCoordinate> to = new ArrayList<>();
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

  /*  public static boolean isLegalMove(int[][] board, GridCoordinate[] move, int player)
    {

        //The origin position is not occupied by the player
        if(board[move[0].y][move[0].x]!=player) return false;
        //The destination is already occupied
        if(board[move[1].y][move[1].x] != 0) return false;

        //If the
        if(board[move[2].y][move[2].x]!=0 && (move[0].y !=move[2].y || move[0].x != move[2].x))
            return false;

        return true;
    }
    */



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
    public void deduceMoves(int[][] B1, int[][] B2, int player, GridCoordinate[] moves) {
        for (int i = 0; i < B1.length; i++)
            for (int j = 0; j < B1[0].length; j++) {
                if (B1[i][j] != B2[i][j]) {
                   // System.out.println(B1[i][j] + " " + B2[i][j]);
                    GridCoordinate pos = new GridCoordinate(j + offset , i + offset);
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

    public GridCoordinate[] deduceMoves2(int[][] B1, int[][] B2, int player) {
        GridCoordinate[] moves=new GridCoordinate[3];
        boolean diff=false;
        for (int i = 0; i < B1.length; i++)
            for (int j = 0; j < B1[0].length; j++) {
                if (B1[i][j] != B2[i][j]) {
                    diff=true;
                    // System.out.println(B1[i][j] + " " + B2[i][j]);
                    GridCoordinate pos = new GridCoordinate(j+offset, i+offset );
                    if (B1[i][j] == player)
                    {
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
            if(!diff)
                throw new RuntimeException("No Difference");
        return moves;
    }

    public static List<GridCoordinate> getQueensPositions(int[][] board, int player) {
        List<GridCoordinate> queens = new ArrayList<>();
        for (int j = 0; j < board.length; j++)
            for (int i = 0; i < board[0].length; i++)
                if (board[j][i] == player)
                    queens.add(new GridCoordinate(i, j));
        return queens;
    }
}


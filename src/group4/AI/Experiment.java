package group4.AI;

import group4.ui.GridCoordinate;
import java.util.StringJoiner;

import static group4.AI.MinMax.getBoardAsString;


public class Experiment {
    private int numTest=30;
    private MoveProducer pl1;
    private MoveProducer pl2;
    private int[][] initBoard=null;
    private boolean verbose=false;
    private boolean randomize=false;

    public Experiment setPlayer1(MoveProducer pl1)
    {
        this.pl1=pl1;
        return this;
    }
    public Experiment setPlayer2(MoveProducer pl2)
    {
        this.pl2=pl2;
        return this;
    }
    public int[][] getInitBoard(){ return initBoard;}

    public Experiment setNumTest(int n)
    {
        numTest=n;
        return this;
    }

    public Experiment setRandomize(boolean r)
    {
        randomize=r;
        return this;
    }
    public Experiment setVerbose(boolean f)
    {
        verbose=f;
        return this;
    }

    public Experiment setBoard()
    {
        initBoard=new int[10][];
        initBoard[0]=new int[]{0,0,0,2,0,0,2,0,0,0};
        initBoard[1]=new int[]{0,0,0,0,0,0,0,0,0,0};
        initBoard[2]=new int[]{0,0,0,0,0,0,0,0,0,0};
        initBoard[3]=new int[]{2,0,0,0,0,0,0,0,0,2};
        initBoard[4]=new int[]{0,0,0,0,0,0,0,0,0,0};
        initBoard[5]=new int[]{0,0,0,0,0,0,0,0,0,0};
        initBoard[6]=new int[]{1,0,0,0,0,0,0,0,0,1};
        initBoard[7]=new int[]{0,0,0,0,0,0,0,0,0,0};
        initBoard[8]=new int[]{0,0,0,0,0,0,0,0,0,0};
        initBoard[9]=new int[]{0,0,0,1,0,0,1,0,0,0};
        return this;
    }

    public Experiment setBoard(int[][] board)
    {
        initBoard=board;
        return this;
    }

    public void run()
    {
        MobilityEval eval=new MobilityEval(1);



        //Statistics that will be returned
        long maxTime1=0;
        long maxTime2=0;
        long minTime1=Long.MAX_VALUE;
        long minTime2=Long.MAX_VALUE;
        long avgTime1=0;
        long avgTime2=0;
        int nTurn1=0;
        int nTurn2=0;
        int vict1=0;
        int vict2=0;
        int failed=0;
        int maxTurns=0;
        int minTurns=Integer.MAX_VALUE;
        double avgTurns=0;


        //Let's  it for numTest times
        for(int i=0;i<numTest;i++)
        {
            long btime;
            long dtime;
            int nTurns=0;
            int[][] board=new int[initBoard.length][];
            for (int j=0;j<initBoard.length;j++)
                board[j]=initBoard[j].clone();

            if(randomize)
            {
                moveToBoardF(board, new RandomAI(1).getMove(board),1);
                moveToBoardF(board, new RandomAI(2).getMove(board),2);
            }
            boolean gameDrop=false;
            boolean turn=true;
            double mobRatio=0.5;
            //We initialise the board


            //The game goes on until one has 0 mobility
            do{
                GridCoordinate[] move;
                int[][] lastBoard=new int[board.length][];
                for (int u = 0; u < board.length ; u++) {
                    lastBoard[u]=board[u].clone();
                }
                if (turn) //Player 1
                {
                    //Compute the move
                    btime=System.currentTimeMillis();
                    move=pl1.getMove(board);
                    dtime=System.currentTimeMillis() - btime;
                    boolean diff=false;
                    for (int j = 0; j < board.length; j++) {
                        for (int k = 0; k < board[0].length; k++) {
                            if(board[j][k] != ((MinMax) pl1).lastBoard[j][k])
                                throw new RuntimeException("Sono board differenti");
                        }
                    }
                    //Check if the move is legal and updates the board
                    if(!isLegalMove(lastBoard,move,1)) {
                        //DROP GAME
                        gameDrop=true;
                        break;
                        //System.err.println(isLegalMove(lastBoard,move,2));
                        //((MinMax)pl1).getBoardss();
                        /*System.err.println(getBoardAsString(lastBoard));
                        System.err.println(getBoardAsString(board));
                        System.err.println(move[0]+""+move[1]+""+move[2]);
                        throw new RuntimeException("Illegal Move (player 1)");*/
                    }
                    moveToBoardF(board, move, 1);

                    //Update Statistics
                    avgTime1+=dtime;
                    if (dtime < minTime1) minTime1=dtime;
                    if (dtime > maxTime1) maxTime1=dtime;
                    nTurn1++;

                }
                else //Player 2
                {
                    btime=System.currentTimeMillis();
                    move=pl2.getMove(board);
                    dtime=System.currentTimeMillis() - btime;

                    if(!isLegalMove(board,move,2)){
                        gameDrop=true;
                        break;
                       /* System.err.println(getBoardAsString(board));
                        System.err.println(move[0]+"\n"+move[1]+"\n"+move[2]);
                        throw new RuntimeException("Illegal Move (player 2)");*/
                    }
                    moveToBoardF(board,move,2);

                    //Update Statistics
                    avgTime2+=dtime;
                    if (dtime < minTime2) minTime2=dtime;
                    if (dtime > maxTime2) maxTime2=dtime;
                    nTurn2++;
                }
                nTurns++;
                if (verbose) System.out.print(".");
                //Change turn
                turn=!turn;
                mobRatio=eval.evaluate(board);
            } while(mobRatio < 1 && mobRatio > 0);
            if(gameDrop)
            {
                if(verbose) System.out.println("X");
                failed++;
            }
            else {
                if (verbose) System.out.println(mobRatio == 1 ? 1 : 2);
                if (nTurns > maxTurns) maxTurns = nTurns;
                if (nTurns < minTurns) minTurns = nTurns;
                avgTurns += nTurns;
                if (mobRatio == 1)
                    vict1++;
                else
                    vict2++;
            }
        }
        avgTime1=avgTime1/nTurn1;
        avgTime2=avgTime2/nTurn2;
        avgTurns=avgTurns/numTest;
        StringJoiner msg=new StringJoiner("\n");
        msg
                .add("Number of tests:\t"+numTest)
                .add("Failed tests:\t"+failed)
                .add("Max turns per game:\t"+maxTurns)
                .add("Min turns per game:\t"+minTurns)
                .add("Avg turns per game:\t"+avgTurns)

                .add("-------Player 1-------")
                .add("Max Time:\t"+maxTime1/1000.0+" s")
                .add("Min Time:\t"+minTime1/1000.0+" s")
                .add("Avg Time:\t"+avgTime1/1000.0+" s")
                .add("# Turns:\t"+nTurn1)
                .add("Victories:\t"+vict1)

                .add("-------Player 2-------")
                .add("Max Time:\t"+maxTime2/1000.0+" s")
                .add("Min Time:\t"+minTime2/1000.0+" s")
                .add("Avg Time:\t"+avgTime2/1000.0+" s")
                .add("# Turns:\t"+nTurn2)
                .add("Victories:\t"+vict2);

        System.out.println(msg.toString());

    }

    public static boolean isLegalMove(int[][] board, GridCoordinate[] move, int player)
    {
        if(board==null) System.err.println("Null board");
        if(move==null || move[0]==null) System.err.println("Null move");
        //The origin position is not occupied by the player

        if(board[move[0].y][move[0].x]!=player) {
            return false;
        }
        //The destination is already occupied
        if(board[move[1].y][move[1].x] != 0) {return false;}

        //If the arrow is shot in a occupied place, except if is the previous position of the queen
        if(board[move[2].y][move[2].x]!=0 && (move[0].x !=move[2].x || move[0].y != move[2].y))
        {
            return false;}

        return true;
    }
    public static void moveToBoardF(int[][] board, GridCoordinate[] move, int player) {

        board[move[0].y][move[0].x]=0;
        board[move[1].y][move[1].x]=player;
        board[move[2].y][move[2].x]=3;
    }
}

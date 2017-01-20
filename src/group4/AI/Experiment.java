package group4.AI;

import group4.ui.GridCoordinate;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import static group4.AI.MinMax.getBoardAsString;

/**
 * Created by Administrator on 20/01/2017.
 */
public class Experiment {
    private int numTest=30;
    private MoveProducer pl1;
    private MoveProducer pl2;
    private int[][] initBoard=null;
    private boolean verbose=false;

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
        MobilityEval eval=new MobilityEval();



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

        //Let's test it for numTest times
        for(int i=0;i<numTest;i++)
        {
            long btime;
            long dtime;

            boolean turn=true;
            double mobRatio=0.5;
            //We initialise the board
            int[][] board=new int[initBoard.length][];
            for (int j=0;j<initBoard.length;j++)
                board[j]=initBoard[j].clone();

            //The game goes on until one has 0 mobility
            do{
                GridCoordinate[] move;
                if (turn) //Player 1
                {
                    //Compute the move
                    btime=System.currentTimeMillis();
                    move=pl1.getMove(board);
                    dtime=System.currentTimeMillis() - btime;

                    //Check if the move is legal and updates the board
                    if(!isLegalMove(board,move,1)) {
                        System.err.println(getBoardAsString(board));
                        System.err.println(move[0]+"\n"+move[1]+"\n"+move[2]);
                        throw new RuntimeException("Illegal Move (player 1)");
                    }
                    moveToBoard(board, move, 1);

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
                        System.err.println(getBoardAsString(board));
                        System.err.println(move[0]+"\n"+move[1]+"\n"+move[2]);
                        throw new RuntimeException("Illegal Move (player 2)");}
                    moveToBoard(board,move,2);

                    //Update Statistics
                    avgTime2+=dtime;
                    if (dtime < minTime2) minTime2=dtime;
                    if (dtime > maxTime2) maxTime2=dtime;
                    nTurn2++;
                }
                if (verbose) System.out.print("+");
                //Change turn
                turn=!turn;
                mobRatio=eval.evaluate(board,1);
            } while(mobRatio < 1 && mobRatio > 0);
            if (verbose) System.out.println( mobRatio==1 ? 1 : 2);
            if (mobRatio==1)
                vict1++;
            else
                vict2++;


        }
        avgTime1=avgTime1/nTurn1;
        avgTime2=avgTime2/nTurn2;
        StringJoiner msg=new StringJoiner("\n");
        msg
                .add("Number of tests:\t"+numTest)
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

        //The origin position is not occupied by the player
        if(board[move[0].y][move[0].x]!=player) {System.err.println("Wrong from cell");return false;}
        //The destination is already occupied
        if(board[move[1].y][move[1].x] != 0) {System.err.println("Wrong to cell");return false;}

        //If the arrow is shot in a occupied place, except if is the previous position of the queen
        if(board[move[2].y][move[2].x]!=0 && (move[0].x !=move[2].x || move[0].y != move[2].y))
        {
            System.err.println("Wrong arrow cell");
            return false;}

        return true;
    }
    public static void moveToBoard(int[][] board, GridCoordinate[] move, int player) {

        board[move[0].y][move[0].x]=0;
        board[move[1].y][move[1].x]=player;
        board[move[2].y][move[2].x]=3;
    }
}

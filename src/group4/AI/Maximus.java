package group4.AI;

import group4.Players.Player;
import group4.ui.GridCoordinate;
import group4.utilities.BoardOperations;

import java.util.ArrayList;
import java.util.Arrays;

import static group4.utilities.BoardOperations.filterBoard;

/**
 * Created by jonty on 14/12/2016.
 */
public class Maximus extends Player {


    //bot relevant values when thinking of a next move
    private boolean thinking;
    public boolean active;
    private boolean isQueenMove;
    private GridCoordinate nextMove;
    private int[][] Grid;
    private GridCoordinate origin;
    private GridCoordinate destination;

    private ArrayList<GridCoordinate> queenPos;
    private ArrayList<GridCoordinate> posMoves;

    private GridCoordinate[] fullMove;

    private MinMax calculate;


    private ArrayList<GridCoordinate> opQueenPos;


    public Maximus(boolean isFirst)   {
        super(isFirst, true);
        calculate = new MinMax(super.getVal(), new MobilityEval());
        calculate.setDepth(2);
    }


    public void territory(){

    }

    public void checkBoard(int val)	{
        for(int k = 0; k < Grid.length; k++)    {
            for(int m = 0; m < Grid[0].length; m++)    {
                if(Grid[k][m] == 0)  {
                    posMoves.add(new GridCoordinate(m+1, k+1));
                }
            }
        }

    }


    public GridCoordinate[] chooseQueenMove()	{

        queenPos = new ArrayList<>();
        posMoves = new ArrayList<>();
        GridCoordinate[] returner = new GridCoordinate[2];

        System.out.println("RIGHT BEFORE CALCULATING A MOVE");
        printBoard();

        fullMove = calculate.getMove(filterBoard(Grid));

        System.out.println("BEST MOVE");
        BoardOperations.printArrayint(calculate.bestBoard);

        returner[0] = fullMove[0];
        returner[1] = fullMove[1];
        return returner;
    }

    public GridCoordinate chooseArrowMove()	{

        posMoves= new ArrayList<>();

        //Here zou set dest equal to your ArrowMove


        if(fullMove.length != 0) {
            GridCoordinate ret = fullMove[2];
            fullMove = new GridCoordinate[3];
            return ret;
        }
        else
            return null;
    }


    public void printBoard() {
        System.out.println("");
        String s;
        for (int i = 0; i < Grid.length; i++) {
            for (int j = 0; j < Grid[0].length; j++) {
                if (Grid[i][j] < 10) {
                    s = "";
                } else {
                    s = "0";
                }
                System.out.print(s + Grid[i][j] + " ");
            }
            System.out.println("");
        }
        System.out.println("");
    }


    private void setGrid(int[][] newBoard)	{
        Grid = new int[newBoard.length][newBoard[0].length];
        for (int i = 0; i < Grid.length; i++) {
            System.arraycopy(newBoard[i], 0, Grid[i], 0, newBoard[i].length);
        }
    }

    public void giveInput(int[][] Board)	{
        setGrid(Board);
    }





    public void active(boolean value)	{
        active = value;
    }

}

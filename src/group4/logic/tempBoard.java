package group4.logic;

/**
 * Created by robin on 23.11.2016.
 */
import group4.ui.GridCoordinate;

public class tempBoard {

    int[][] momentaryBoard;
    private GridCoordinate origin;

    public tempBoard(int[][] momentaryBoard)	{
        this.momentaryBoard = momentaryBoard;
        origin = null;
    }



    public int[][] getMomentaryBoard() {
        return momentaryBoard;
    }



    public void print()	{
        String s;
        System.out.println("\n");
        for (int i = 0; i < momentaryBoard.length; i++) {
            for (int j = 0; j < momentaryBoard[0].length; j++) {
                if (momentaryBoard[i][j] < 10) {
                    s = "";
                } else {
                    s = "0";
                }
                System.out.print(s + momentaryBoard[i][j] + " ");
            }
            System.out.println("");
        }
    }


    public boolean isSame(tempBoard comp)	{
        int[][] mine = getMomentaryBoard();
        int[][] compare = comp.getMomentaryBoard();

        for(int i = 0; i < mine.length && mine.length == compare.length; i++)	{
            for(int j = 0; j < mine[0].length && mine[0].length == compare[0].length; j++)	{
                if(mine[i][j] != compare[i][j])
                    return false;
            }
        }
        return true;
    }






}

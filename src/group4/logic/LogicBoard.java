package group4.logic;

import group4.ui.GridCoordinate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import group4.Players.Player;
import group4.ui.GridCoordinate;

public class LogicBoard {

    private final int width = 10;
    private final int height = 10;

    public final int wQueenVal = 1;
    public final int bQueenVal = 2;
    public final int arrowVal = 3;

    public final int amazonPosVal = 4;
    public final int arrowPosVal = 5;

    private Player player1, player2, current;
    private int[][] Grid = new int[height][width];


    public boolean queenSelect;
    public boolean arrowSpotSelect;

    //private ArrayList<Move> Moves;
    private ArrayList<tempBoard> Moves;
    private int currentMoveIndex;
    private ArrayList<int[][]> boardStates = new ArrayList<>();

    private GridCoordinate origin;


    private boolean debugTurnIssues = false;
    private boolean debugHistory = true;
    private boolean printBoards = true;




    // According to sprite
    // {0 = empty, 1 = white queen, 2 = black queen, 3 = arrow, 4 = possible
    // queen spot, 5 = possible arrow spot}
    public LogicBoard(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;

        if (player1.isFirst() == player2.isFirst()) {
            player1.setFirst();
            player2.setSecond();
        }

        if (player1.isFirst() == true) {
            current = player1;
        } else {
            current = player2;
        }

        player1.setUpQueens();
        player2.setUpQueens();
        initializeBoard();

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

    // Four white queens (player 1) and four black queens (player 2) to start
    // with
    private void initializeBoard() {
        queenSelect = false;
        arrowSpotSelect = false;
        Moves = new ArrayList<>();
		/*
		queenSelect = true;
		arrowSpotSelect = false;
		*/
        // For player1
        GridCoordinate[] temp = player1.getAmazonsPosistions();

        for (int i = 0; i < temp.length; i++) {
            Grid[temp[i].y][temp[i].x] = wQueenVal /* + i */;
        }

        // For player2
        temp = player2.getAmazonsPosistions();

        for (int i = 0; i < temp.length; i++) {
            Grid[temp[i].y][temp[i].x] = bQueenVal /* + i */;
        }
        currentMoveIndex = -1;
        addMove();

        System.out.println("CURRENT INDEX: " + currentMoveIndex + "\nCurrent Player: " + getCurrent() + "\nqueenSelect: " + queenSelect + "\narrowSpotSelect: " + arrowSpotSelect + "\n");
    }

    public int[][] getBoard() {
        return Grid;
    }



    public GridCoordinate getOrigin()	{
        return origin;
    }

    public String getBoardAsString()    {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < Grid.length; i++)   {
            for(int j = 0; j < Grid[0].length; j++) {
                builder.append(Grid[i][j]);
            }
        }
        return builder.toString();
    }

    public int[][] listToArray(int[] list) {
        int size = (int) Math.sqrt(list.length);
        int[][] array = new int[size][size];
        int k = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                array[i][j] = list[k];
                k++;
            }
        }
        return array;
    }


    public int[] arrayToList(int[][] array) {
        int[] list = new int[array.length * array.length];
        int k = 0;
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length; j++) {
                list[k] = array[i][j];
                k++;
            }
        }
        return list;
    }



    public String toString(int[][] board) {
        String string = "";
        int[] list = arrayToList(board);
        for (int i = 0; i < list.length; i++) {
            string += list[i];
        }
        return string;
    }

    public ArrayList<int[][]> getBoardStates() {
        return boardStates;
    }

    public int getBoardStatesSize() {
        return boardStates.size();
    }

    public void printAllMoves()	{
        int i = 0;
        while(i < Moves.size())	{
            System.out.println("Move: " + i + " MoveIndex: " + currentMoveIndex);
            Moves.get(i).print();
            System.out.println("\n");
            i++;
        }
    }



    // Removes possible moves
    public void removePossibleMoves() {
        for (int i = 0; i < Grid.length; i++) {
            for (int j = 0; j < Grid[0].length; j++) {
                if (Grid[i][j] == amazonPosVal || Grid[i][j] == arrowPosVal) {
                    Grid[i][j] = 0;
                }
            }
        }
    }

    public void setQueenOfCurrentOn(GridCoordinate point)	{
        int x = point.x - 1;
        int y = point.y - 1;

        Grid[y][x] = getCurrent().getVal();
    }

    public void setQueenOfCurrentOn(int x, int y)	{
        Grid[y][x] = getCurrent().getVal();
    }

    public void setQueenOfOpposing(GridCoordinate point)	{
        int x = point.x - 1;
        int y = point.y - 1;

        if(current == player1)	{
            Grid[y][x] = player2.getVal();
        }
        else 	{
            Grid[y][x] = player1.getVal();
        }
    }

    public void setArrowOn(int x, int y)	{
        Grid[y][x] = arrowVal;
        toggleTurn();
    }

    public void setArrowOn(GridCoordinate position)	{
        int x = position.x - 1;
        int y = position.y - 1;

        Grid[y][x] = arrowVal;
    }

    public void setEmpty(GridCoordinate point)	{
        int x = point.x - 1;
        int y = point.y - 1;

        Grid[y][x] = 0;
    }


    public void addMove()	{
        int[][] t = copyBoard();
        tempBoard tBoard = new tempBoard(t);

        System.out.println("AddMove:\nMoveIndex: " + currentMoveIndex + "\nSize: " + Moves.size());

        if(Moves.size() - 1 == currentMoveIndex)
            Moves.add(tBoard);
        else	{
            while(Moves.size() - 1 > currentMoveIndex)	{
                Moves.remove(Moves.size() - 1);
            }
            Moves.add(tBoard);


        }



        System.out.println("Last State");
        printBoard(Moves.get(Moves.size() - 1).getMomentaryBoard());
        currentMoveIndex++;
        printAllMoves();



    }

    public int[][] copyBoard()	{

        int length = Grid.length;
        int[][] target = new int[length][Grid[0].length];
        for (int i = 0; i < length; i++) {
            System.arraycopy(Grid[i], 0, target[i], 0, Grid[i].length);
        }
        return target;

    }

    public void setBoard(int[][] newBoard)	{
        for (int i = 0; i < Grid.length; i++) {
            System.arraycopy(newBoard[i], 0, Grid[i], 0, newBoard[i].length);
        }


    }



    public void redoMove()	{

        if(currentMoveIndex < Moves.size() - 1)	{
            tempBoard temp = Moves.get(currentMoveIndex);
            int[][] temp2 = temp.getMomentaryBoard();
            Grid = temp2;
            currentMoveIndex++;
            if(!arrowSpotSelect && queenSelect)	{
                arrowSpotSelect = true;
                queenSelect = false;
            }
            else if(arrowSpotSelect && !queenSelect)	{
                toggleTurn();
            }
            else if(!arrowSpotSelect && !queenSelect)	{

            }
            else	{

            }
        }

        if(!arrowSpotSelect && queenSelect)	{

        }
        else if(arrowSpotSelect && !queenSelect)	{

        }
        else if(!arrowSpotSelect && !queenSelect)	{

        }
        else	{

        }
    }


    public void undoMove()	{

        System.out.println("\nBefore Undo:\nCURRENT INDEX: " + currentMoveIndex + "\nSize: " + Moves.size() + "\nCurrent Player: " + getCurrent() + "\nqueenSelect: " + queenSelect + "\narrowSpotSelect: " + arrowSpotSelect + "\n");

		/*
		First case:			Queen has NOT been chosen for move
			->	When goimg back possible go to previous state and allow opposite player to chose where to place Arrow
		Second case:		Queen has been chosen for move but you wanna chosse a differnt queen
			->	Remove possible options
		Third case:			Queen has been placed and you are about to place the Arrow
			->	Revert to previous state where queen was not yet moved
		Fourth case:		Queen has been placed and you are about to place the Arrow
			->	Revert to previous state where queen was not yet moved
		 */

        if(!arrowSpotSelect && !queenSelect)	{

            if(Moves.size() > 1 && currentMoveIndex > 0)	{
                --currentMoveIndex;
                System.out.println("Index set to: " + currentMoveIndex);
                tempBoard temp = Moves.get(currentMoveIndex);
                int[][] temp2 = temp.getMomentaryBoard();

                if(printBoards) {

                    System.out.println("Supposingly new state");
                    printBoard(temp2);
                }

                setBoard(temp2);

                toggleTurn();

                arrowSpotSelect = true;
                queenSelect =  false;


            }
        }
        else if(!arrowSpotSelect && queenSelect)	{
            removePossibleMoves();
            queenSelect = false;
        }
        else if(arrowSpotSelect && !queenSelect)	{
            if(Moves.size() > 0 && currentMoveIndex > 0)	{
                System.out.println("Entered");
                --currentMoveIndex;
                System.out.println("Index set to: " + currentMoveIndex);
                tempBoard temp = Moves.get(currentMoveIndex);
                int[][] temp2 = temp.getMomentaryBoard();



				/*
				if(printBoards) {
					System.out.println("Temp:");
					temp.print();
					System.out.println("Supposingly new state");
					printBoard(temp2);
				}
				*/

                setBoard(temp2);


                arrowSpotSelect = false;
                queenSelect =  false;
            }
        }
        else	{
            if(Moves.size() > 0 && currentMoveIndex > 0) {
                tempBoard temp = Moves.get(--currentMoveIndex);
                int[][] temp2 = temp.getMomentaryBoard();

                if (printBoards) {
                    System.out.println("Temp:");
                    temp.print();
                    System.out.println("Supposingly new state");
                    printBoard(temp2);
                }

                setBoard(temp2);

                if(currentMoveIndex > 1)
                    toggleTurn();
            }
        }

        if(currentMoveIndex < -1)
            currentMoveIndex = -1;



        System.out.println("\nAfter Undo:\nCURRENT INDEX: " + currentMoveIndex + "\nCurrent Player: " + getCurrent() + "\nqueenSelect: " + queenSelect + "\narrowSpotSelect: " + arrowSpotSelect + "\n");

        if(printBoards) {
            System.out.println("Actual Grid");
            printBoard();
        }

    }

    public void setEmpty(int x, int y)	{
        Grid[y][x] = 0;
    }

    // Checks whether the game is over (i.e. is every queen from a certain
    // player locked in? if yes, then game over)
    public boolean isGameOver() {
        int currentPlayerVal = getCurrent().getVal();
        for(int i = 0; i < Grid.length; i++)	{
            for(int j = 0; j < Grid[0].length; j++)	{
                if(Grid[i][j] == currentPlayerVal)	{
                    calcPosMoves(new GridCoordinate(j + 1, i + 1), true);
                    if(countPosMoves() > 0)	{
                        return true;
                    }
                }
            }
        }

        return false;
    }



    public void printBoard(int[][] array) {
        String s;
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                if (array[i][j] < 10) {
                    s = "";
                } else {
                    s = "0";
                }
                System.out.print(s + array[i][j] + " ");
            }
            System.out.println("");
        }
    }





    // Checks if a queens move is possible and takes board boundaries into
    // account to prevent out of boundary issues
    public void calcPosMoves(GridCoordinate position, boolean amazonMove) {

        int val;

        if(amazonMove)	{
            val = amazonPosVal;
        }
        else	{
            val = arrowPosVal;
        }


        removePossibleMoves();
        //Since GridCordinates start at 1,1 instead of 0,0
        int x = position.x - 1;
        int y = position.y - 1;


        //System.out.println("Position: X = " + x + " Y= " + y);




        int i, j;
        int tempX, tempY;
        int length = Grid.length;
        i = y - 1;
        j = x - 1;

        // up
        for (tempY = y - 1; tempY >= 0 && Grid[tempY][x] == 0; tempY--) {
            Grid[tempY][x] = val;
        }

        // Direction: bottom vertical
        // down
        for (tempY = y + 1; tempY < Grid.length && Grid[tempY][x] == 0; tempY++) {
            Grid[tempY][x] = val;
        }

        // Direction: left horizontal
        for (tempX = x - 1; tempX >= 0 && Grid[y][tempX] == 0; tempX--) {
            Grid[y][tempX] = val;
        }
        // Direction: right horizontal
        for (tempX = x + 1; tempX < Grid[0].length && Grid[y][tempX] == 0; tempX++) {
            Grid[y][tempX] = val;
        }
        // Direction: top right diagonal
        tempY = y - 1;
        tempX = x + 1;

        while (checkBound(tempY, tempX) && Grid[tempY][tempX] == 0) {
            //System.out.println("Position:\tY = " + tempY + "\tX = " + tempX +
            //"\nStill in Bounds? " + checkBound(tempY, tempX));

            Grid[tempY][tempX] = val;
            tempY--;
            tempX++;

        }
        // Direction: top left diagonal
        tempY = y - 1;
        tempX = x - 1;

        while (checkBound(tempY, tempX) && Grid[tempY][tempX] == 0) {

            Grid[tempY][tempX] = val;
            tempY--;
            tempX--;

        }
        // Direction: bottom right diagonal
        tempY = y + 1;
        tempX = x + 1;

        while (checkBound(tempY, tempX) && Grid[tempY][tempX] == 0) {
            // System.out.println("Position:\tY = " + tempY + "\tX = " + tempX +
            // "\nStill in Bounds? " + checkBound(tempY, tempX));

            Grid[tempY][tempX] = val;
            tempY++;
            tempX++;

        }
        // Direction: bottom left diagonal
        tempY = y + 1;
        tempX = x - 1;

        while (checkBound(tempY, tempX) && Grid[tempY][tempX] == 0) {
            Grid[tempY][tempX] = val;
            tempY++;
            tempX--;

        }



    }


    public boolean isMovePossible()	{
        int posMoves = countPosMoves();
        if (posMoves == 0) {
            System.out.println("This queen is not able to move anymore!");
            return false;
        }
        else {
            return true;
        }
    }

    private int countPosMoves()	{
        int count = 0;
        for(int i = 0; i < Grid.length; i++)	{
            for(int j = 0; j < Grid[0].length; j++)	{
                if(Grid[i][j] == amazonPosVal)	{
                    count++;
                }
            }
        }
        return count;
    }

    public ArrayList<GridCoordinate> listQueensOfCurrent()	{
        ArrayList<GridCoordinate> queens = new ArrayList<GridCoordinate>();
        int pos=0;
        GridCoordinate temp;
        for(int x = 1; x < 11; x++)	{
            for(int y = 1; y < 11; y++)	{
                temp = new GridCoordinate(x,y);
                if(amazonOfCurrentPlayer(temp))	{
                    queens.add(temp);
                }
                if(queens.size() == 4)	{
                    break;
                }
            }
        }
        return queens;
    }

    public ArrayList<GridCoordinate> listPossibleMoves(GridCoordinate position)	{
        ArrayList<GridCoordinate> posMoves = new ArrayList<GridCoordinate>();
        calcPosMoves(position, false);
        GridCoordinate temp;
        for(int x = 1; x < 11; x++)	{
            for(int y = 1; y < 11; y++)	{
                temp = new GridCoordinate(x,y);
                if(posMoveAt(temp))	{
                    posMoves.add(temp);
                }
            }
        }
        removePossibleMoves();
        return posMoves;

    }

    // Calculates possible moves and takes board boundaries into account to
    // prevent out of boundary issues.


    private boolean checkBound(int y, int x) {
        if ((y >= 0 && y < Grid.length) && (x >= 0 && x < Grid[0].length)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkBound(GridCoordinate position) {
        int x = position.x;
        int y = position.y;

        if ((y >= 0 && y < Grid.length) && (x >= 0 && x < Grid[0].length)) {
            return true;
        } else {
            return false;
        }
    }

    public void toggleTurn() {
        queenSelect = false;
        arrowSpotSelect = false;
        //Change to true
        if(debugTurnIssues)
            System.out.print("Turn ended: ");
        if (current == player1) {

            if(debugTurnIssues)
                System.out.print("player 2 turn now\n");
            current = player2;
        } else {

            if(debugTurnIssues)
                System.out.print("player 1 turn now\n");
            current = player1;
        }


    }

    public Player getCurrent()	{
        return current;
    }

    public boolean posMovesDispl()	{
        for(int i = 0; i < Grid.length; i++)	{
            for(int j = 0; j < Grid[0].length; j++)	{
                if(Grid[i][j] == amazonPosVal){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean amazonOfCurrentPlayer(GridCoordinate position) {
        return amazonOfCurrentPlayer(position, getCurrent());
    }

    public boolean amazonOfCurrentPlayer(GridCoordinate position, Player inQuestion) {
        int posX = position.x - 1;
        int posY = position.y - 1;


        if (Grid[posY][posX] == inQuestion.getVal()) {
            return true;
        } else {
            return false;
        }
    }


    public boolean posMoveAt(GridCoordinate position){
        int posX = position.x - 1;
        int posY = position.y - 1;

        if (Grid[posY][posX] == amazonPosVal || Grid[posY][posX] == arrowPosVal) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkGameOverForCurrent()	{
        return isGameOver(current);
    }

    //rudimentary gameOver Checker
    public boolean isGameOver(Player inQuestion)	{

        ArrayList<GridCoordinate> queenPositions = new ArrayList();

        //First get Positons of all Queens
        for(int i = 0; i < 11; i++)	{
            for(int j = 0; j < 11; j++)	{
                if(amazonOfCurrentPlayer(new GridCoordinate(i,j), inQuestion)){
                    queenPositions.add(new GridCoordinate(i,j));
                }
                if(queenPositions.size() == 4)
                    break;
            }
        }

        //Second, evalueate for every Quuen whether it can still move
        for(GridCoordinate pos : queenPositions)	{
            posMoveAt(pos);
            if(countPosMoves() > 0)
                return false;
        }

        return true;


    }


}
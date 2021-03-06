package group4.logic;

import group4.ui.GridCoordinate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import group4.Players.Player;
import group4.ui.GridCoordinate;
import group4.utilities.BoardOperations;

import static group4.utilities.BoardOperations.gameOverCheck;
import static group4.utilities.BoardOperations.gameScore;

public class LogicBoard {

    boolean DEBUG = false;

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
        if(DEBUG) {

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

        //TODO Put back in???
        //System.out.println("CURRENT INDEX: " + currentMoveIndex + "\nCurrent Player: " + getCurrent() + "\nqueenSelect: " + queenSelect + "\narrowSpotSelect: " + arrowSpotSelect + "\n");
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
            //System.out.println("Move: " + i + " MoveIndex: " + currentMoveIndex);
            //Moves.get(i).print();
            //System.out.println("\n");
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


    public void addMove() {
        int[][] t = copyBoard();
        tempBoard tBoard = new tempBoard(t);
        //TODO put back in ???
        //System.out.println("AddMove:\nMoveIndex: " + currentMoveIndex + "\nSize: " + Moves.size());

        if (Moves.size() - 1 == currentMoveIndex)
            Moves.add(tBoard);
        else {
            while (Moves.size() - 1 > currentMoveIndex) {
                Moves.remove(Moves.size() - 1);
            }
            Moves.add(tBoard);


        }


        //System.out.println("Last State");
        // printBoard(Moves.get(Moves.size() - 1).getMomentaryBoard());
        currentMoveIndex++;
        //printAllMoves();


        ArrayList<GridCoordinate> queenPositions = new ArrayList();

        for (int i = 1; i < Grid.length + 1; i++) {
            for (int j = 1; j < Grid.length + 1; j++) {
                if (amazonOfCurrentPlayer(new GridCoordinate(i, j), current)) {
                    queenPositions.add(new GridCoordinate(i - 1, j - 1));
                }

                if (queenPositions.size() == 4)
                    break;
            }


        }
        //TODO HERE REDO TERRITORY STUFF
        // System.out.println("Territory for " + getCurrent() + " player: " );
        for (int i = 0; i < 4; i++) {
            //System.out.println("\n" + "moves for " + current + i + " queen: " + moveCounter(queenPositions.get(i).x, queenPositions.get(i).y, getBoard()));
            //  System.out.println("\n" + checkTerritory(getBoard(), getCurrent())[i]);
        }
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

        //TODO put back in???
        //System.out.println("\nBefore Undo:\nCURRENT INDEX: " + currentMoveIndex + "\nSize: " + Moves.size() + "\nCurrent Player: " + getCurrent() + "\nqueenSelect: " + queenSelect + "\narrowSpotSelect: " + arrowSpotSelect + "\n");

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



        //System.out.println("\nAfter Undo:\nCURRENT INDEX: " + currentMoveIndex + "\nCurrent Player: " + getCurrent() + "\nqueenSelect: " + queenSelect + "\narrowSpotSelect: " + arrowSpotSelect + "\n");

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
        if(DEBUG) {
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
            //System.out.println("This queen is not able to move anymore!");
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

    //counts the number moves available for a queen
    public int moveCounter(int queenPositionX, int queenPositionY, int[][] board){

        int counter = 0;

        //south
        for(int i= 1; i < 11; i++) {
            if(checkBound(queenPositionY + i,queenPositionX)){
                if (board[queenPositionX][queenPositionY + i] == 0) {
                    counter++;
                }
                else
                    break;

            }
            else
                break;
        }

        //north
        for(int i= 1; i < 11; i++) {
            if(checkBound(queenPositionY - i,queenPositionX)){
                if (board[queenPositionX][queenPositionY - i] == 0) {
                    counter++;
                }
                else
                    break;

            }
            else
                break;
        }

        //west
        for(int i= 1; i < 11; i++) {
            if(checkBound(queenPositionY,queenPositionX - i)){
                if (board[queenPositionX - i][queenPositionY ] == 0) {
                    counter++;
                }
                else
                    break;

            }
            else
                break;
        }


        //east
        for(int i= 1; i < 11; i++) {
            if(checkBound(queenPositionY,queenPositionX + i)){
                if (board[queenPositionX + i][queenPositionY] == 0) {
                    counter++;
                }
                else
                    break;

            }
            else
                break;
        }

        //south west
        for(int i= 1; i < 11; i++) {
            if(checkBound(queenPositionY + i,queenPositionX - i)){
                if (board[queenPositionX - i][queenPositionY + i] == 0) {
                    counter++;
                }
                else
                    break;

            }
            else
                break;
        }

        //south east
        for(int i= 1; i < 11; i++) {
            if(checkBound(queenPositionY + i,queenPositionX + i)){
                if (board[queenPositionX + i][queenPositionY + i] == 0) {
                    counter++;
                }
                else
                    break;

            }
            else
                break;
        }

        //north west
        for(int i= 1; i < 11; i++) {
            if(checkBound(queenPositionY - i,queenPositionX - i)){
                if (board[queenPositionX - i][queenPositionY - i] == 0) {
                    counter++;
                }
                else
                    break;

            }
            else
                break;
        }

        //north east
        for(int i= 1; i < 11; i++) {
            if(checkBound(queenPositionY - i,queenPositionX + i)){
                if (board[queenPositionX + i][queenPositionY - i] == 0) {
                    counter++;
                }
                else
                    break;

            }
            else
                break;
        }

        return counter;
    }

    public int[] checkTerritory(int[][] currentBoard, Player inQuestion){
        int[][] tempBoard = new int[10][10];
        ArrayList<GridCoordinate> queenPositions = new ArrayList();

        int[] returnStatement = new int[4];

        //First get Positons of all Queens, should use better method for this
        for(int i = 1; i < Grid.length + 1; i++) {
            for (int j = 1; j < Grid.length + 1; j++) {
                if (amazonOfCurrentPlayer(new GridCoordinate(i, j), inQuestion)) {
                    queenPositions.add(new GridCoordinate(i, j));
                }
                //Need to get list of all queens, not only current player, territory mess up a little atm
                if (queenPositions.size() == 4)
                    break;
            }
        }

        for(int p = 0; p < queenPositions.size();p++){

            for(int i = 0; i < currentBoard.length; i++){
                for(int j = 0; j < currentBoard[i].length; j++){
                    tempBoard[i][j] = currentBoard[i][j];
                }
            }
            floodFill(tempBoard,queenPositions.get(p).x -1,queenPositions.get(p).y -1, 0, 12, true);
            for(int k = 0; k < 10; k++){
                for(int l = 0; l < 10; l++){
                    if(tempBoard[k][l] == 12){
                        returnStatement[p] ++; }
                }
            }
        }
        for(int i = 0; i < 4; i++){
            //System.out.println(returnStatement[i]);
        }
        return returnStatement;
    }


    public void runBotGame()    {
        while(!gameOverCheck(Grid)) {
            Player Bot = getCurrent();
            //System.out.println("Before passing to bot");
            //logicBoard.printBoard();
            Bot.giveInput(getBoard());

            //QueenMove
            if(!arrowSpotSelect) {
                GridCoordinate[] queenMove = Bot.chooseQueenMove();
                GridCoordinate origin = queenMove[0];

                //System.out.println("Trying to set to empty: " + origin);
                setEmpty(origin);

                GridCoordinate dest = queenMove[1];
                setQueenOfCurrentOn(dest);

                arrowSpotSelect = true;
                calcPosMoves(dest, false);
            }
            else{
                //logicBoard.getCurrent().giveInput(logicBoard.getBoard());
                GridCoordinate dest = Bot.chooseArrowMove();
                setArrowOn(dest);
                removePossibleMoves();
                toggleTurn();
            }
           //BoardOperations.printBoard(getBoard());
        }

        System.out.println("Game Over");
        int scorePl1 = gameScore(Grid, 1);
        int scorePl2 = gameScore(Grid, 2);

        if(scorePl1 > scorePl2){
            System.out.println("player 1 wins - Score: " + scorePl1);
        }
        else   {
            System.out.println("player 2 wins - Score: " + scorePl2);
        }
    }


    public void floodFill(int[][] board, int currentNodeX,int currentNodeY, int target, int replacement, boolean first){

        //1. If target-color is equal to replacement-color, return.
        if(target == replacement){
            return;
        }
        //2. If the color of node is not equal to target-color, return. because of this algorithm ends on start because it starts at queen
        if(first == false) {
            if (board[currentNodeX][currentNodeY] != target) {
                return;
            }
            //3. Set the color of node to replacement-color.
            board[currentNodeX][currentNodeY] = replacement;
        }

        if(checkBound(new GridCoordinate(currentNodeY + 1, currentNodeX))){
            // 4. Perform Flood-fill (one step to the south of node, target-color, replacement-color).
            floodFill(board, currentNodeX, currentNodeY + 1, target, replacement, false );}

        if(checkBound(new GridCoordinate(currentNodeY - 1, currentNodeX))){
            //north
            floodFill(board, currentNodeX , currentNodeY - 1, target, replacement, false );}

        if(checkBound(new GridCoordinate(currentNodeY, currentNodeX - 1))){
            //west
            floodFill(board, currentNodeX - 1, currentNodeY, target, replacement, false );}

        if(checkBound(new GridCoordinate(currentNodeY, currentNodeX + 1))){
            //east
            floodFill(board, currentNodeX + 1, currentNodeY, target, replacement, false );}

        if(checkBound(new GridCoordinate(currentNodeY + 1, currentNodeX - 1))){
            //south west
            floodFill(board, currentNodeX - 1, currentNodeY + 1, target, replacement, false );}

        if(checkBound(new GridCoordinate(currentNodeY + 1, currentNodeX + 1))){
            //south east
            floodFill(board, currentNodeX + 1, currentNodeY + 1, target, replacement, false );}

        if(checkBound(new GridCoordinate(currentNodeY - 1, currentNodeX - 1))){
            //north west
            floodFill(board, currentNodeX - 1, currentNodeY - 1, target, replacement, false );}

        if(checkBound(new GridCoordinate(currentNodeY - 1, currentNodeX + 1))){
            //north east
            floodFill(board, currentNodeX + 1, currentNodeY - 1, target, replacement, false );}

        return;
    }

    //checks if other queens in territory, game should end if all are alone, still not done
    public void checkAround(int[][] board, int currentNodeX,int currentNodeY, int target, int replacement, boolean first, int target2){
        if((target == replacement) || (target2 == replacement)){
            return;
        }
        if(first == false) {
            if (board[currentNodeX][currentNodeY] != target || board[currentNodeX][currentNodeY] != 0   ) {
                return;
            }
            if (board[currentNodeX][currentNodeY] == target){
                board[currentNodeX][currentNodeY] = replacement;
            }
            else if ((board[currentNodeX][currentNodeY] == 0)){
                board[currentNodeX][currentNodeY] = 99;
            }
        }
        if(checkBound(new GridCoordinate(currentNodeY + 1, currentNodeX))){
            // 4. Perform Flood-fill (one step to the south of node, target-color, replacement-color).
            floodFill(board, currentNodeX, currentNodeY + 1, target, replacement, false );}

        if(checkBound(new GridCoordinate(currentNodeY - 1, currentNodeX))){
            //north
            floodFill(board, currentNodeX , currentNodeY - 1, target, replacement, false );}

        if(checkBound(new GridCoordinate(currentNodeY, currentNodeX - 1))){
            //west
            floodFill(board, currentNodeX - 1, currentNodeY, target, replacement, false );}

        if(checkBound(new GridCoordinate(currentNodeY, currentNodeX + 1))){
            //east
            floodFill(board, currentNodeX + 1, currentNodeY, target, replacement, false );}

        if(checkBound(new GridCoordinate(currentNodeY + 1, currentNodeX - 1))){
            //south west
            floodFill(board, currentNodeX - 1, currentNodeY + 1, target, replacement, false );}

        if(checkBound(new GridCoordinate(currentNodeY + 1, currentNodeX + 1))){
            //south east
            floodFill(board, currentNodeX + 1, currentNodeY + 1, target, replacement, false );}

        if(checkBound(new GridCoordinate(currentNodeY - 1, currentNodeX - 1))){
            //north west
            floodFill(board, currentNodeX - 1, currentNodeY - 1, target, replacement, false );}

        if(checkBound(new GridCoordinate(currentNodeY - 1, currentNodeX + 1))){
            //north east
            floodFill(board, currentNodeX + 1, currentNodeY - 1, target, replacement, false );}

        return;
    }
}
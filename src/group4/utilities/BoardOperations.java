package group4.utilities;

import group4.AI.MinMax;
import group4.tree.Node;
import group4.tree.Tree;
import group4.ui.GridCoordinate;

import java.lang.reflect.Array;
import java.util.List;
import java.util.StringJoiner;
import java.util.ArrayList;

import static group4.AI.MinMax.getQueensPositions;
import static group4.AI.MobilityEval.getNumPossibleMoves;

/**
 * Created by robin on 08.12.2016.
 */
public class BoardOperations {


    public static void moveQueen(int[][] Array, GridCoordinate origin, GridCoordinate dest, int val) {

        int originX = origin.x - 1;
        int originY = origin.y - 1;

        int destX = dest.x - 1;
        int destY = dest.y - 1;

        if(!checkBound(Array, originY, originX) || Array[originY][originX] != val)   {
            System.out.println("Invalid Origin position");
        }
        else if(!checkBound(Array, destY, destX) || Array[destY][originX] != val)  {
            System.out.println("Invalid Destination position");
        }
        else    {
            setEmpty(Array, origin);
            setQueenOn(Array, dest,val);
        }
    }


    public static ArrayList<GridCoordinate> posQueens(int[][] Array, int val) {
        ArrayList<GridCoordinate> queens = new ArrayList<GridCoordinate>();
        int pos=0;
        GridCoordinate temp;
        for(int x = 1; x < 11; x++)	{
            for(int y = 1; y < 11; y++)	{
                temp = new GridCoordinate(x,y);
                if(queenOn(Array, temp, val))	{
                    queens.add(temp);
                }
                if(queens.size() == 4)	{
                    break;
                }
            }
        }
        return queens;
    }



    public static boolean queenOn(int[][] Array, GridCoordinate position, int val){
        return queenOn(Array, position.y - 1, position.x - 1, val);
    }

    public static boolean queenOn(int[][] Array, int y, int x,int val){
        if(!checkBound(Array,y,x) || Array[y][x] != val)    {
            return false;
        }
        return true;
    }

    public static void setQueenOn(int[][] Grid, GridCoordinate position, int val){
        setQueenOn(Grid, position.y - 1, position.x - 1, val);
    }

    public static void setQueenOn(int[][] Grid, int y, int x, int val){
        if(checkBound(Grid, y, x) && Grid[y][x] == 0 && (val == 1 || val == 2))   {
            Grid[y][x] = val;
        }
        else    {
            System.out.println("SetQueenOn() Invalid inputs\nChecxkBopund: " + checkBound(Grid, y, x) + "\tX: " + x + "\tY: " + y + "\tVal at Coordiunate: " + Grid[y][x] + "\tVal: " + val);
        }
    }

    public static void setArrow(int[][] Grid, GridCoordinate position){
        setArrow(Grid, position.y - 1, position.x - 1);
    }

    public static void setArrow(int[][] Grid, int y, int x){
        if(checkBound(Grid, y, x) && Grid[y][x] == 0)   {
            Grid[y][x] = 3;
        }
    }


    public static int evalMobility(int[][] Array, GridCoordinate position)    {
        calcPosMoves(Array, position, false);
        int val = countPosMove(Array);
        removePosMoves(Array);
        return val;
    }

    public static double evaluate(int[][] board, int player) {
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
        removePosMoves(board);

        return (pl1-pl2)/(pl1);
    }

    public static int getTerritory(int[][] Board, int playerVal) {
        int wholeTerritory = 0;
        ArrayList<GridCoordinate> queens = posQueens(Board, playerVal);
        for(GridCoordinate queen: queens)   {
            wholeTerritory += getTerForQueen(queen, Board);
        }

        return wholeTerritory;

    }

    public static int getTerForQueen(GridCoordinate Queen, int[][] Board)  {
        int ownVal = getValAt(Board, Queen);
        int[][] tempBoard = getCopy(Board);
        System.out.println("Start: ");
        printArrayint(tempBoard);

        int count = 1;

        //Mark all quens and Arrows as unavailable

        //Enemy Queens
        ArrayList<GridCoordinate> enemy = posQueens(tempBoard, 3 - ownVal);
        for(GridCoordinate enemQueen: enemy)  {
            setValue(tempBoard, 9, enemQueen);
        }
        System.out.println("After marking enemy");
        printArrayint(tempBoard);

        //Arrows
        for(int i = 0; i < tempBoard.length; i++)   {
            for(int j = 0; j < tempBoard[0].length; j++)   {
                if(tempBoard[i][j] == 3)    {
                    setValue(tempBoard, 9, i, j);
                }
            }
        }
        System.out.println("After marking Arrows");
        printArrayint(tempBoard);

        //OwnQueens
        ArrayList<GridCoordinate> own = posQueens(tempBoard, ownVal);
        for(GridCoordinate ownQeen: own)  {
            setValue(tempBoard, 6, ownQeen);
        }

        System.out.println("After marking own");
        printArrayint(tempBoard);


        ArrayList<GridCoordinate> moves;
        GridCoordinate temp;

        calcPosMoves(tempBoard, Queen, false);

        markPossible(tempBoard, count);
        count++;
        System.out.println("After marking possible");
        printArrayint(tempBoard);
        for(int i = 0; i < Board.length; i++)   {
            for(int j = 0; j < Board[0].length; j++)   {
                if(Board[i][j] == count - 1)    {
                    calcPosMoves(tempBoard, new GridCoordinate(j+1, i+1), false);
                    markPossible(tempBoard, count);
                    System.out.println("After marking possible again");
                    printArrayint(tempBoard);
                }
            }
        }

        System.out.println("After marking possible 2");
        printArrayint(tempBoard);

        /*

        //While there is an empty spot
        while(checkForEmptySpot(tempBoard)) {
            markPossible(tempBoard, count);
            System.out.println("For count: " + count);
            printArrayint(tempBoard);
            moves = new ArrayList<GridCoordinate>();

            for(int i = 0; i < 10; i++) {
                for(int j = 0; j < 10; j++) {
                    if(tempBoard[i][j] == count)    {
                        moves.add(new GridCoordinate(j+1, i+1));
                    }
                }
            }

            for(GridCoordinate move : moves)    {
                calcPosMoves(tempBoard, move, false);
            }

            count++;
        }


        int countAll = 0;
        for(int i = 0; i < tempBoard.length;i++) {
            for(int j = 0; j < tempBoard[0].length;j++) {
                if(tempBoard[i][j] < 6)
                countAll += tempBoard[i][j];
            }
        }
        */

        return 0;
    }





    public static boolean checkForEmptySpot(int[][] Board)   {
        for(int i = 0; i < Board.length; i++)   {
            for(int j = 0; j < Board[0].length; j++)   {
                if(Board[i][j] == 0)    {
                    return  true;
                }
            }
        }
        return false;
    }

    public static void markPossible(int[][] Board, int val, ArrayList<GridCoordinate> positions)   {
        for(GridCoordinate pos: positions) {
            markPossible(Board, val, pos);
        }

    }


    public static void markPossible(int[][] Board, int val, GridCoordinate position)   {
        calcPosMoves(Board, position, false);
        for(int i = 0; i < Board.length; i++)   {
            for(int j = 0; j < Board[0].length; j++)    {
                if(Board[i][j] == 4)    {
                    Board[i][j] = val;
                }
            }
        }
    }

    public static void markPossible(int[][] Board, int val)   {
        for(int i = 0; i < Board.length; i++)   {
            for(int j = 0; j < Board[0].length; j++)   {
                if(Board[i][j] == 4)    {
                    Board[i][j] = 1;
                }
            }
        }
    }





    public static void bubbleSortNodesByScore(ArrayList<Node> children)   {
        Node temp;
        if (children.size()>1) // check if the number of orders is larger than 1
        {
            for (int x=0; x<children.size(); x++) // bubble sort outer loop
            {
                for (int i=0; i < children.size() - x - 1; i++) {
                    if (children.get(i).getScore() < children.get(i + 1).getScore()) {
                        temp = children.get(i);
                        children.set(i, children.get(i + 1));
                        children.set(i + 1, temp);
                    }
                }

            }
        }
    }



    public static void setEmpty(int[][] Grid, GridCoordinate position){
        setEmpty(Grid, position.y - 1, position.x - 1);
    }

    public static void setEmpty(int[][] Grid, int y, int x){
        if(checkBound(Grid, y, x) && Grid[y][x] != 0)   {
            Grid[y][x] = 0;
        }
    }

    public static void setValue(int[][] Grid, int specVal, GridCoordinate pos){
        setValue(Grid, specVal, pos.y - 1, pos.x - 1);
    }

    public static void setValue(int[][] Grid, int specVal, int y, int x){
        if(checkBound(Grid, y, x) && Grid[y][x] != 0)   {
            Grid[y][x] = specVal;
        }
    }


    public static ArrayList<GridCoordinate> ranQueenMove(int[][] Board, int val)  {
        ArrayList<GridCoordinate> queens = posQueens(Board, val);
        boolean choosen = false;
        int ran = 0;

        while(!choosen) {
            ran = (int)(Math.random() * 4);
            calcPosMoves(Board, queens.get(ran), false);
            if(countPosMove(Board) >= 1)
                choosen = true;
        }

        GridCoordinate origin = queens.get(ran);

        List<GridCoordinate> moves = MinMax.getPossibleMoves(Board, origin);

        ran = (int)(Math.random() * moves.size());
        GridCoordinate dest = moves.get(ran);

        ArrayList<GridCoordinate> Move = new ArrayList<>();
        Move.add(origin);
        Move.add(dest);
        return Move;

    }

    public static void calcPosMoves(int[][] Array, GridCoordinate position, boolean arrowMove){
        calcPosMoves(Array, position.x - 1, position.y - 1, arrowMove);
    }

    public static void calcPosMoves(int[][] Array, int x, int y, boolean arrowMove)    {
        removePosMoves(Array);


        int val;
        if(arrowMove)   {
            val = 5;
        }
        else
            val = 4;

        int i, j;
        int tempX, tempY;
        int length = Array.length;
        i = y - 1;
        j = x - 1;

        // up
        for (tempY = y - 1; tempY >= 0 && Array[tempY][x] == 0; tempY--) {
            Array[tempY][x] = val;
        }

        // Direction: bottom vertical
        // down
        for (tempY = y + 1; tempY < Array.length && Array[tempY][x] == 0; tempY++) {
            Array[tempY][x] = val;
        }

        // Direction: left horizontal
        for (tempX = x - 1; tempX >= 0 && Array[y][tempX] == 0; tempX--) {
            Array[y][tempX] = val;
        }
        // Direction: right horizontal
        for (tempX = x + 1; tempX < Array[0].length && Array[y][tempX] == 0; tempX++) {
            Array[y][tempX] = val;
        }
        // Direction: top right diagonal
        tempY = y - 1;
        tempX = x + 1;

        while (checkBound(Array, tempY, tempX) && Array[tempY][tempX] == 0) {
            //System.out.println("Position:\tY = " + tempY + "\tX = " + tempX +
            //"\nStill in Bounds? " + checkBound(tempY, tempX));

            Array[tempY][tempX] = val;
            tempY--;
            tempX++;

        }
        // Direction: top left diagonal
        tempY = y - 1;
        tempX = x - 1;

        while (checkBound(Array, tempY, tempX) && Array[tempY][tempX] == 0) {

            Array[tempY][tempX] = val;
            tempY--;
            tempX--;

        }
        // Direction: bottom right diagonal
        tempY = y + 1;
        tempX = x + 1;

        while (checkBound(Array, tempY, tempX) && Array[tempY][tempX] == 0) {
            // System.out.println("Position:\tY = " + tempY + "\tX = " + tempX +
            // "\nStill in Bounds? " + checkBound(tempY, tempX));

            Array[tempY][tempX] = val;
            tempY++;
            tempX++;

        }
        // Direction: bottom left diagonal
        tempY = y + 1;
        tempX = x - 1;

        while (checkBound(Array, tempY, tempX) && Array[tempY][tempX] == 0) {
            Array[tempY][tempX] = val;
            tempY++;
            tempX--;

        }
    }

    public static ArrayList<GridCoordinate> listPosDest(int[][] Array, GridCoordinate start)  {
        ArrayList<GridCoordinate> posMoves = new ArrayList<GridCoordinate>();

        calcPosMoves(Array, start, false);
        GridCoordinate pos;
        for(int k = 1; k < 11; k++) {
            for(int l = 1; l < 11; l++) {
                pos = new GridCoordinate(k,l);
                if(getValAt(Array, pos) == 4 || getValAt(Array, pos) == 5)  {
                    posMoves.add(pos);
                }
            }
        }
        return posMoves;
    }

    public static int getValAt(int[][] Board, GridCoordinate pos)    {
        return getValAt(Board, pos.x - 1, pos.y - 1);
    }

    public static int getValAt(int[][] Board, int x, int y)    {
        return Board[y][x];
    }

    public static int countPosMove(int[][] Array)    {
        int count = 0;
        for(int i = 0; i < Array.length; i++)   {
            for(int j = 0; j < Array[0].length; j++)   {
                if (Array[i][j] == 5 || Array[i][j] == 4) {
                    count++;
                }
            }
        }
        return count;
    }

    public static boolean checkBound(int[][] Grid, int y, int x) {
        if ((y >= 0 && y < Grid.length) && (x >= 0 && x < Grid[0].length)) {
            return true;
        } else {
            return false;
        }
    }





    public static int countArrow(int[][] array)  {
        int count = 0;
        for(int i = 0; i < array.length; i++)   {
            for(int j = 0; j < array[0].length; j++)   {
                if(array[i][j] == 3)
                    count++;
            }
        }
        return count;
    }

    public static void removePosMoves(int[][] Array) {
        for(int i = 0; i < Array.length; i++)   {
            for(int j = 0; j < Array.length; j++)   {
                if(Array[i][j] == 4 || Array[i][j] == 5)    {
                    Array[i][j] = 0;
                }
            }
        }
    }

    public static int[][] filterBoard(int[][] Array) {
        int[][] n=new int[10][10];
        for(int i = 0; i < Array.length; i++)   {
            for(int j = 0; j < Array[0].length; j++)   {
                if(Array[i][j] == 4 || Array[i][j] == 5)    {
                    n[i][j] = 0;
                } else n[i][j]=Array[i][j];
            }
        }
        return n;
    }
    public static void printArrayint(int [][] array) {
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
        System.out.println("\n");
    }

    public static int[][] getCopy(int[][] Board)     {
        int length = Board.length;
        int[][] target = new int[length][Board[0].length];
        for (int i = 0; i < length; i++) {
            System.arraycopy(Board[i], 0, target[i], 0, Board[i].length);
        }
        return target;
    }
    public static String board2CSV(int[][] board, String delim) {
        StringJoiner b = new StringJoiner(delim);
        for (int i = 0; i < board.length; i++)
            for (int j = 0; j < board[0].length; j++)
                b.add(board[i][j] + "");
        return b.toString();
    }

    public static int[][] reverse(int[][] b) {
        int[][] b2=new int[b.length][b[0].length];
        int n=b.length-1;
        for (int i = 0; i < b.length; i++)
            for (int j = 0; j < b[0].length; j++) {
                if (b[i][j] == 1)
                    b2[n-i][j] = 2;
                else if (b[i][j] == 2)
                    b2[n-i][j] = 1;
                else
                    b2[n-i][j]=b[i][j];
            }
        return b2;
    }

    public static int[][] stringToBoard(String board)
    {
        String[] stringArray = board.split("");
        int[] list = new int[stringArray.length];
        System.out.println();
        for (int i = 0; i < stringArray.length; i++) {
            list[i] = Integer.parseInt(stringArray[i]);
            //System.out.print(list[i] + " ");
        }

        return listToArray(list);
    }

    public static int[][] listToArray(int[] list) {
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


    public static String getBoardAsString(int[][] Grid)    {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < Grid.length; i++)   {
            for(int j = 0; j < Grid[0].length; j++) {
                builder.append(Grid[i][j]);
            }
        }
        return builder.toString();
    }








}
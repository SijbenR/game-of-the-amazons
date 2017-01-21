package group4.randomAI;


import group4.Players.Player;
import group4.logic.LogicBoard;
import group4.ui.GridCoordinate;
import org.omg.PortableInterceptor.ObjectReferenceFactory;

import java.util.ArrayList;

import static group4.utilities.BoardOperations.*;

public class Bobby extends Player {


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


	private ArrayList<GridCoordinate> opQueenPos;


	public Bobby(boolean isFirst)   {
		super(isFirst, true);
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

	public void giveInput(int[][] Board)	{
		this.Grid = getCopy(Board);
	}


	public GridCoordinate[] chooseQueenMove()	{

		queenPos = new ArrayList<>();
		posMoves = new ArrayList<>();
		GridCoordinate[] returner = new GridCoordinate[2];


		updatePosQueens();
		int ran = (int)(Math.random() * queenPos.size());
		GridCoordinate chosenQueen = queenPos.get(ran);
		while(listPosDest(Grid, chosenQueen).size() == 0)	{
			ran = (int)(Math.random() * queenPos.size());
			chosenQueen = queenPos.get(ran);
		}


		System.out.println("Choosen Queen at: " + chosenQueen);
		returner[0] = chosenQueen;

		updatePossibleMoves(chosenQueen);
		printBoard();
		removePossibleMoves();
		System.out.println("Amount of Possible moves for Queen: " + isQueenMove + "\n is = " + posMoves.size());

		ran = (int)(Math.random() * posMoves.size());
		GridCoordinate dest = posMoves.get(ran);
		System.out.println("Choosen Destination at: " + dest);

		returner[1] = dest;

		return returner;
	}

	public GridCoordinate chooseArrowMove()	{

		posMoves= new ArrayList<>();

		System.out.println("Possible Moves");
		printBoard();
		countPosMoves(5);

		int ran = (int)(Math.random() * posMoves.size());
		GridCoordinate dest = posMoves.get(ran);
		System.out.println("Shooting Arrow at: " + dest);


		if(posMoves.size() > 0) {
			return dest;
		}
		else
			return null;
	}



	//Calculates the position of all OWN Queens
	public void updatePosQueens()   {
		queenPos = posQueens(Grid, super.getVal());
	}

	//Calculates the position of all OPPONENT Queens
	public void updateOpPosQueens()   {
		for(int i = 0; i < Grid.length; i++)    {
			for(int j = 0; j < Grid[0].length; j++)    {
				if(Grid[i][j] != super.getVal() && Grid[i][j] != 0 && Grid[i][j] != 3)    {
					opQueenPos.add(new GridCoordinate(j+1, i+1));
				}
				if(opQueenPos.size() == 4)
					break;
			}
		}
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

	public void updatePossibleMoves(GridCoordinate position)   {
		posMoves = new ArrayList<>();
		calcPosMoves(Grid, position, false);
		posMoves = listPosDest(Grid, position);
		System.out.println("Amount of possible moves: " + posMoves.size());


	}

	public void countPosMoves(int val)	{
		for(int k = 0; k < Grid.length; k++)    {
			for(int m = 0; m < Grid[0].length; m++)    {
				if(Grid[k][m] == val)  {
					posMoves.add(new GridCoordinate(m+1, k+1));
				}
			}
		}

	}





	public void setOrigin(GridCoordinate position)   {
		origin = position;
	}

	public void setDestination(GridCoordinate position)  {
		destination = position;
	}


	//relevant for Checks on Board
	private boolean checkBound(int y, int x) {
		if ((y >= 0 && y < Grid.length) && (x >= 0 && x < Grid[0].length)) {
			return true;
		} else {
			return false;
		}
	}

	public void removePossibleMoves() {
		for (int i = 0; i < Grid.length; i++) {
			for (int j = 0; j < Grid[0].length; j++) {
				if (Grid[i][j] == 4 || Grid[i][j] == 5) {
					Grid[i][j] = 0;
				}
			}
		}
	}

	//First used as Indication from where the Queen was placed from, after that saves from where Arrow is shot from
	public GridCoordinate getOrigin()	{
		if(origin != null) {
			GridCoordinate retuner = new GridCoordinate(origin.x, origin.y);
			origin = null;
			return retuner;
		}
		else
			return null;
	}

	public GridCoordinate getDestination()	{
		if(destination != null) {
			GridCoordinate retuner = new GridCoordinate(destination.x, destination.y);
			destination = null;
			return retuner;
		}
		else
			return null;
	}


	public void active(boolean value)	{
		active = value;
	}

}


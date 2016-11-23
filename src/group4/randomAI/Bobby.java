package group4.randomAI;


import group4.Players.Player;
import group4.logic.LogicBoard;
import group4.ui.GridCoordinate;

import java.util.ArrayList;

public class Bobby extends Player {


	//bot relevant values when thinking of a next move
	private boolean thinking;
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

	public GridCoordinate getNextMove(int[][] Grid, boolean isQueenMove)	{

		this.Grid = Grid;
		this.isQueenMove = isQueenMove;

		thinking = true;

		return  null;
	}

	//This Bot does random decisions
	public void chooseMove() {

		updateOpPosQueens();
		int ran = (int)(Math.random() * 4);
		GridCoordinate choosenQueen = queenPos.get(ran);

		if(isQueenMove == false)
			setOrigin(choosenQueen);
		else
			setOrigin(null);



		updatePossibleMoves(choosenQueen, isQueenMove);
		ran = (int)(Math.random() * posMoves.size());
		GridCoordinate dest = queenPos.get(ran);

		setDestination(dest);


	}




	//Calculates the position of all OWN Queens
	public void updatePosQueens()   {
		for(int i = 0; i < Grid.length; i++)    {
			for(int j = 0; j < Grid[0].length; j++)    {
				if(Grid[i][j] == super.getVal())    {
					queenPos.add(new GridCoordinate(j+1, i+1));
				}
				if(queenPos.size() == 4)
					break;
			}
		}
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

	public void updatePossibleMoves(GridCoordinate position, boolean amazonMove)   {
		int x = position.x - 1;
		int y = position.y - 1;


		//System.out.println("Position: X = " + x + " Y= " + y);


		int val;

		if(amazonMove)	{
			val = 4;
		}
		else	{
			val = 5;
		}

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

		for(int k = 0; k < Grid.length; k++)    {
			for(int m = 0; m < Grid[m].length; m++)    {
				if(Grid[k][m] == 4 || Grid[k][m] == 5)  {
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





}


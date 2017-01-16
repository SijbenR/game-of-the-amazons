package group4.MCTS;

/**
 * Created by jonty on 03/12/2016.
 */


import group4.Players.Player;
import group4.logic.LogicBoard;
import group4.randomAI.Bobby;
import group4.ui.GridCoordinate;
import org.omg.PortableInterceptor.ObjectReferenceFactory;

import java.util.ArrayList;

public class territory  {


  //bot relevant values when thinking of a next move
  private boolean thinking;
  public boolean active;
  private boolean isQueenMove;
  private GridCoordinate nextMove;
  private int[][] Grid;
  private GridCoordinate origin;
  private GridCoordinate destination;
  public LogicBoard logicBoard;


  private ArrayList<GridCoordinate> queenPos;
  private ArrayList<GridCoordinate> searchedBoard;


  private ArrayList<GridCoordinate> opQueenPos;


  public void territory(){
      logicBoard.checkTerritory(Grid, logicBoard.getCurrent());

  }




  public void checkBoard(int val)	{
    for(int k = 0; k < Grid.length; k++)    {
      for(int m = 0; m < Grid[0].length; m++)    {
        if(Grid[k][m] == 0)  {
          searchedBoard.add(new GridCoordinate(m+1, k+1));
          Grid[k][m]= val;

        }
        else
          if (Grid[k][m] == val)  {

          }

      }
    }

  }
}
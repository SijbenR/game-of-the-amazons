package group4.AI;

import group4.ui.GridCoordinate;

public interface MoveProducer {
    public GridCoordinate[] getMove(int[][] board);

}

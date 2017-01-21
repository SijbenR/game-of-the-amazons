package group4.AI;

import static group4.utilities.BoardOperations.getTerritory;
import static group4.utilities.BoardOperations.removePosMoves;

public class TerritoryEval extends EvaluationFunction {
    @Override
    public double evaluate(int[][] board, int player) {
        double pl1= getTerritory(board, player, true);
        double pl2= getTerritory(board, 3-player, false);
        removePosMoves(board);
        return pl1/(pl1+pl2);
    }
}

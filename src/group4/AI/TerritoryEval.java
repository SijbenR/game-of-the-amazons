/*package group4.AI;

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


public static List<int[][]> getPossibleBoards(int[][] board, int player,
        EvaluationFunction evalF, boolean isMax,
        Comparator<int[][]> maxSort, Comparator<int[][]> minSort,
        int maxStepSize, int maxFinalSize) {

        TreeSet<int[][]> finalBoards = new TreeSet<>(isMax ? maxSort : minSort);
        Map<int[][], GridCoordinate> queenStuff=new TreeMap<>(isMax ? maxSort : minSort);

        // First we compute all the possible boards according to the queen moves
        for (GridCoordinate i : getQueensPositions(board, player))
        for (GridCoordinate j : getPossibleMoves(board, i))
        queenStuff.put(moveToBoard(board, player, i, j, false), j);

        int count=0;
        for(Entry<int[][], GridCoordinate> e: queenStuff.entrySet())
        {
        if(count>=maxStepSize)
        break;
        for (GridCoordinate k : getPossibleMoves(e.getKey(),e.getValue()))
        finalBoards.add(moveToBoard(e.getKey(), player, e.getValue(), k, true));
        count++;
        }
        // Now we compute the boards according to the arrow moves
        List<int[][]> finalB=new ArrayList<>(finalBoards);
        return (finalB.size() > maxFinalSize) ?
        finalB.subList(0, maxFinalSize) : finalB;
        }

        }
*/
package group4.gameoftheamazons.logic;

import group4.gameoftheamazons.ui.GridCoordinate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class LogicBoard {

    public int width, height;
    private int[][] boardArray, currentBoard;
    private int[][] example1, example2;
    private boolean DEBUG = false;
    private ArrayList<int[][]> boardList = new ArrayList<>();
    private ArrayList<int[][]> boardStates = new ArrayList<>();

    // According to sprite
    // {0 = empty, 1 = white queen, 2 = black queen, 3 = arrow, 4 = possible queen spot, 5 = possible arrow spot}
    public LogicBoard(int width, int height) {
        this.width = width;
        this.height = height;
        boardArray = new int[width][height];
        //initializeBoard();
        example1 = new int[][]{
                {0, 0, 0, 2, 2, 2, 2, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 3, 3, 3, 3, 3, 3, 0, 0},
                {0, 0, 3, 3, 3, 3, 3, 3, 0, 0},
                {0, 0, 3, 3, 3, 3, 3, 3, 0, 0},
                {0, 0, 3, 3, 3, 3, 3, 3, 0, 0},
                {0, 0, 3, 3, 3, 3, 3, 3, 0, 0},
                {0, 0, 3, 3, 3, 3, 3, 3, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 1, 1, 1, 0, 0, 0},
        };
        example2 = new int[][]{
                {0, 0, 0, 2, 0, 0, 2, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0, 0, 0, 0, 2},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 1, 0, 0, 0},
        };
        boardList.add(example1);
        boardList.add(example2);
        initializeBoard();
        boardStates.clear();
    }

    public int[][] getBoard(int index) {
        return boardStates.get(index);
    }

    public int getBoardStatesSize() {
        return boardStates.size();
    }

    public void saveBoard(int[][] board) {
        int[][] tempBoard = new int[board.length][board[0].length];
        int[][] lastBoard;
        int last;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                tempBoard[i][j] = board[i][j];
            }
        }
        tempBoard = Arrays.copyOf(tempBoard, tempBoard.length);
        boardStates.add(tempBoard);
        if (boardStates.size() > 0) {
            for (int i = 0; i < boardStates.size() - 1; i++) {
                if (Arrays.equals(boardStates.get(i), boardStates.get(i+1))) {
                    boardStates.remove(i);
                }
            }
        }
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

    // Four white queens (player 1) and four black queens (player 2) to start with
    private void initializeBoard() {
        boardArray[0][3] = 2;
        boardArray[0][6] = 2;
        boardArray[3][0] = 2;
        boardArray[3][9] = 2;
        boardArray[6][0] = 1;
        boardArray[6][9] = 1;
        boardArray[9][3] = 1;
        boardArray[9][6] = 1;
    }

    public ArrayList<int[][]> getBoardStates() {
        return boardStates;
    }

    public int[][] getBoard() {
        return boardArray;
    }

    public int[][] getCurrentBoard() {
        return currentBoard;
    }

    public void setBoard(int[][] currentBoard) {
        this.currentBoard = currentBoard;
    }

    // Removes possible moves
    public int[][] removePossibleMoves(int[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == 4 || board[i][j] == 5) {
                    board[i][j] = 0;
                }
            }
        }
        return board;
    }

    // Checks whether the game is over (i.e. is every queen from a certain player locked in? if yes, then game over)
    public boolean isGameOver(int[][] board, int index) {
        int[][] tempBoard = new int[board.length][board[0].length];
        int counter = 0;
        boolean gameOver = false;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                tempBoard[i][j] = board[i][j];
            }
        }
        tempBoard = removePossibleMoves(tempBoard);
        ArrayList<GridCoordinate> queens = calculateQueenPositions(tempBoard, index);
        if (DEBUG) {
            System.out.println("* * * * * * * * * *");
            printBoard(tempBoard);
            System.out.println("* * * * * * * * * *");
            System.out.println("The following queens are checked:");
        }
        for (int i = 0; i < queens.size(); i++) {
            if (DEBUG) {
                System.out.println("Queen of player " + index + " | x: " + queens.get(i).x + ", y: " + queens.get(i).y);
            }
            if (!isMovePossible(tempBoard, queens.get(i).x, queens.get(i).y)) {
                counter++;
                if (DEBUG) {
                    System.out.println("Counter: " + counter);
                }
                if (counter == queens.size()) {
                    gameOver = true;
                }
            }
        }
        return gameOver;
    }

    // Calculates queen positions
    public ArrayList<GridCoordinate> calculateQueenPositions(int[][] board, int index) {
        ArrayList<GridCoordinate> queens = new ArrayList<>();
        for (int i = 0; i < board.length; i++ ) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == index) {
                    GridCoordinate boardPosition = new GridCoordinate(j + 1, i + 1);
                    queens.add(boardPosition);
                }
            }
        }
        return queens;
    }

    // Checks if a queens move is possible and takes board boundaries into account to prevent out of boundary issues
    public boolean isMovePossible(int[][] board, int x, int y) {
        int i, j;
        int k, l;
        int length = board.length;
        boolean possible = false;
        i = y - 1; j = x - 1;

        // Direction: top vertical
        k = i - 1;
        if (k >= 0) {
            if (board[k][j] == 0) {
                possible = true;
            }
        }
        // Direction: bottom vertical
        k = i + 1;
        if (k < length) {
            if (board[k][j] == 0) {
                possible = true;
            }
        }
        // Direction: left horizontal
        l = j - 1;
        if (l >= 0) {
            if (board[i][l] == 0) {
                possible = true;
            }
        }
        // Direction: right horizontal
        l = j + 1;
        if (l < length) {
            if (board[i][l] == 0) {
                possible = true;
            }
        }
        // Direction: top right diagonal
        k = i - 1; l = j + 1;
        if (k >= 0 && l < length) {
            if (board[k][l] == 0) {
                possible = true;
            }
        }
        // Direction: top left diagonal
        k = i - 1; l = j - 1;
        if (k >= 0 && l >= 0) {
            if (board[k][l] == 0) {
                possible = true;
            }
        }
        // Direction: bottom right diagonal
        k = i + 1; l = j + 1;
        if (k < length && l < length) {
            if (board[k][l] == 0) {
                possible = true;
            }
        }
        // Direction: bottom left diagonal
        k = i + 1; l = j - 1;
        if (k < length && l >= 0) {
            if (board[k][l] == 0) {
                possible = true;
            }
        }

        if (!possible) {
            System.out.println("This queen is not able to move anymore!");
        }
        return possible;
    }

    // Calculates possible moves and takes board boundaries into account to prevent out of boundary issues.
    public int[][] calculatePossibleMoves(int[][] board, int x, int y, int index) {
        int i, j;
        int k, l;
        int length = board.length;
        i = y - 1; j = x - 1;

        // Direction: top vertical
        k = i - 1;
        while (k >= 0) {
            if (board[k][j] == 0) {
                board[k][j] = index;
            } else {
                break;
            }
            k--;
        }

        // Direction: bottom vertical
        k = i + 1;
        while (k < length) {
            if (board[k][j] == 0) {
                board[k][j] = index;
            } else {
                break;
            }
            k++;
        }

        // Direction: left horizontal
        l = j - 1;
        while (l >= 0) {
            if (board[i][l] == 0) {
                board[i][l] = index;
            } else {
                break;
            }
            // hellos
            l--;
        }

        // Direction: right horizontal
        l = j + 1;
        while (l < length) {
            if (board[i][l] == 0) {
                board[i][l] = index;
            } else {
                break;
            }
            l++;
        }

        // Direction: top right diagonal
        k = i - 1; l = j + 1;
        if (k >= l) {
            while (k >= 0 && l < length) {
                if (board[k][l] == 0) {
                    board[k][l] = index;
                } else {
                    break;
                }
                k--; l++;
            }
        } else {
            while (k >= 0 && l < length) {
                if (board[k][l] == 0) {
                    board[k][l] = index;
                } else {
                    break;
                }
                k--; l++;
            }
        }

        // Direction: top left diagonal
        k = i - 1; l = j - 1;
        if (k >= l) {
            while (l >= 0 && l < length) {
                if (board[k][l] == 0) {
                    board[k][l] = index;
                } else {
                    break;
                }
                k--; l--;
            }
        } else {
            while (k >= 0 && l < length) {
                if (board[k][l] == 0) {
                    board[k][l] = index;
                } else {
                    break;
                }
                k--; l--;
            }
        }

        // Direction: bottom right diagonal
        k = i + 1; l = j + 1;
        if (k >= l) {
            while (l >= 0 && k < length) {
                if (board[k][l] == 0) {
                    board[k][l] = index;
                } else {
                    break;
                }
                k++; l++;
            }
        } else {
            while (k >= 0 && l < length) {
                if (board[k][l] == 0) {
                    board[k][l] = index;
                } else {
                    break;
                }
                k++; l++;
            }
        }

        // Direction: bottom left diagonal
        k = i + 1; l = j - 1;
        if (k >= l) {
            while (l >= 0 && k < length) {
                if (board[k][l] == 0) {
                    board[k][l] = index;
                } else {
                    break;
                }
                k++; l--;
            }
        } else {
            while (l >= 0 && k < length) {
                if (board[k][l] == 0) {
                    board[k][l] = index;
                } else {
                    break;
                }
                k++; l--;
            }
        }
        return board;
    }
}

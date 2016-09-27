package group4.gameoftheamazons.logic;

public class LogicBoard {

    public int width, height;
    private int[][] boardArray, currentBoard;

    // According to sprite
    // {0 = empty, 1 = white queen, 2 = black queen, 3 = arrow, 4 = possible queen spot, 5 = possible arrow spot}
    public LogicBoard(int width, int height) {
        this.width = width;
        this.height = height;
        boardArray = new int[width][height];
        initializeBoard();
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
    public void removePossibleMoves() {
        for (int i = 0; i < boardArray.length; i++) {
            for (int j = 0; j < boardArray[0].length; j++) {
                if (boardArray[i][j] == 4 || boardArray[i][j] == 5) {
                    boardArray[i][j] = 0;
                }
            }
        }
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
            System.out.println("This queen is not able to move anymore, choose another one!");
        }
        return possible;
    }

    // Calculates possible moves and takes board boundaries into account to prevent out of boundary issues.
    public int[][] calculatePossibleMoves(int[][] board, int xStart, int yStart, int pieceIndex) {
        int i, j;
        int k, l;
        int length = board.length;
        i = yStart - 1; j = xStart - 1;

        // Direction: top vertical
        k = i - 1;
        while (k >= 0) {
            if (board[k][j] == 0) {
                board[k][j] = pieceIndex;
            } else {
                break;
            }
            k--;
        }

        // Direction: bottom vertical
        k = i + 1;
        while (k < length) {
            if (board[k][j] == 0) {
                board[k][j] = pieceIndex;
            } else {
                break;
            }
            k++;
        }

        // Direction: left horizontal
        l = j - 1;
        while (l >= 0) {
            if (board[i][l] == 0) {
                board[i][l] = pieceIndex;
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
                board[i][l] = pieceIndex;
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
                    board[k][l] = pieceIndex;
                } else {
                    break;
                }
                k--; l++;
            }
        } else {
            while (k >= 0 && l < length) {
                if (board[k][l] == 0) {
                    board[k][l] = pieceIndex;
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
                    board[k][l] = pieceIndex;
                } else {
                    break;
                }
                k--; l--;
            }
        } else {
            while (k >= 0 && l < length) {
                if (board[k][l] == 0) {
                    board[k][l] = pieceIndex;
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
                    board[k][l] = pieceIndex;
                } else {
                    break;
                }
                k++; l++;
            }
        } else {
            while (k >= 0 && l < length) {
                if (board[k][l] == 0) {
                    board[k][l] = pieceIndex;
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
                    board[k][l] = pieceIndex;
                } else {
                    break;
                }
                k++; l--;
            }
        } else {
            while (l >= 0 && k < length) {
                if (board[k][l] == 0) {
                    board[k][l] = pieceIndex;
                } else {
                    break;
                }
                k++; l--;
            }
        }
        return board;
    }
}

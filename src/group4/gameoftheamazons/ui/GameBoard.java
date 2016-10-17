package group4.gameoftheamazons.ui;

import group4.gameoftheamazons.components.ImageLoader;
import group4.gameoftheamazons.logic.LogicBoard;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class GameBoard extends JPanel implements MouseMotionListener, MouseListener, ActionListener {

    private final Color GREEN = new Color(126, 211, 33, 255);
    private final Color BLACK = new Color(0, 0, 0, 255);
    private final Color RED = new Color(254, 0, 0, 255);
    private final Color LIGHT_GRAY = new Color(220, 220, 220);
    private Color color;
    private Color even = new Color(209,140,72,255);
    private Color odd = new Color(255,207,159,255);
    private Shape square;
    private int xCor, yCor;
    private int counter;
    private int gridXCor, gridYCor;
    private int currentPiece;
    private Timer timer;
    private LogicBoard logicBoard;
    private boolean setPath = false, setStart = false, setEnd = false, startAnimation = false;
    private boolean entersBoard = false;
    private boolean player1 = false, player2 = false;
    private boolean printMe1, printMe2, printMe3;
    private boolean selectQueen = true, selectPossibleQueenSpot = false, selectPossibleArrowSpot = false;
    private boolean updateNeeded;
    private boolean DEBUG = false;
    public Image[] piece, letter, figure;
    public int x, y, dx, dy, speed, delay = 1000/60;
    public int sizeX, sizeY;
    public int boardX, boardY;
    public int width;
    public int height;
    public int[][] boardArray, tempArray;

    public Screen screen;

    ArrayList<Shape> cells = new ArrayList<>();
    ArrayList<Shape> marker = new ArrayList<>();
    ArrayList<Integer> pieces = new ArrayList<>();
    ArrayList<GridCoordinate> markerCoordinates = new ArrayList<>();
    ArrayList<GridCoordinate> pieceCoordinates = new ArrayList<>();

    // Constructor
    public GameBoard(int sizeX, int sizeY, int gridX, int gridY, int boardX, int boardY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.boardX = boardX;
        this.boardY = boardY;
        width = sizeX / gridX;
        height = sizeY / gridY;
        setSize(sizeX, sizeY);
        setBackground(LIGHT_GRAY);
        addMouseListener(this);
        addMouseMotionListener(this);
        timer = new Timer(delay, this);
        logicBoard = new LogicBoard(boardX, boardY);
        boardArray = logicBoard.getBoard();
        //logicBoard.printBoard(boardArray);
        logicBoard.saveBoard(boardArray);
        counter = 0;
    }

    // Sets the mode
    public void setMode(boolean setPath, boolean player1, boolean player2, boolean startAnimation) {
        this.setPath = setPath;
        this.player1 = player1;
        this.player2 = player2;
        this.startAnimation = startAnimation;
    }

    public int getCounter() {
        return counter;
    }

    public ArrayList<int[][]> getBoardStates() {
        return logicBoard.getBoardStates();
    }

    public int[][] getBoard() {
        return logicBoard.getBoard();
    }

    public void returnBoard(int index) {
        boardArray = logicBoard.getBoard(index);
    }

    public int getBoardStatesSize() {
        return logicBoard.getBoardStatesSize();
    }

    public void printBoard(int[][] board) {
        logicBoard.printBoard(board);
    }

    public void save(int[][] board) {
        logicBoard.save(board);
    }

    // Paint method
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        // Anti-aliasing (smooth rendering)
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // Bilinear interpolation (smooth scaling, slow down system)
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // Load Sprite Images
        try {
            piece = ImageLoader.loadImage("../images/pieces-1.5x.png", 2*50, 2*50);
            letter = ImageLoader.loadImage("../images/letters-1.5x.png", 2*50, 2*50);
            figure = ImageLoader.loadImage("../images/figures-1.5x.png", 2*50, 2*50);
        } catch (IOException e) {
            System.out.println("Error loading image");
        }

        // Board
        drawCells(g2, width, height);
        drawBoard(g2, boardArray);

        // Boardletters
        for (int i = 0; i < letter.length; i++) {
            g2.drawImage(letter[i], (i + 1) * width, 0 * height, width, height, this);
            g2.drawImage(letter[i], (i + 1) * width, (letter.length + 1) * height, width, height, this);

        }

        // Boardfigures
        for (int j = 0; j < figure.length; j++) {
            g2.drawImage(figure[j], 0 * width, (j + 1) * height, width, height, this);
            g2.drawImage(figure[j], (figure.length + 1) * width, (j + 1) * height, width, height, this);
        }

        // Drawing of the animation
        if (startAnimation) {
            int xStart = markerCoordinates.get(0).x;
            int yStart = markerCoordinates.get(0).y;
            int xEnd = markerCoordinates.get(1).x;
            int yEnd = markerCoordinates.get(1).y;
            if (DEBUG) {
                System.out.println(xStart + "," + yStart + " - " + xEnd + "," + yEnd);
            }
            move(xStart, yStart, xEnd, yEnd);
        }

        // Drawing of the cell or grid pointers
        if (entersBoard) {
            g2.setPaint(RED);
            Rectangle2D cellPointer = new Rectangle2D.Double(gridXCor * width + 2, gridYCor * height + 2, width - 4, height - 4);
            g2.setStroke(new BasicStroke(4));
            g2.draw(cellPointer);
        }

        // Drawing of the boardmarkers and placement of the animation image
        if (marker.size() > 0) {
            g2.setPaint(BLACK);
            for (int i = 0; i < marker.size(); i++) {
                g2.draw(marker.get(i));
            }
            g2.drawImage(piece[currentPiece - 1], x, y, width, height, this);
        }

    }

    public void move(int xStart, int yStart, int xEnd, int yEnd) {
        dx = xEnd - xStart;
        dy = yEnd - yStart;
        timer.start();
        repaint();
        if (x <= 3) {
            speed = 3;
        }
        if (x > 3 && x <= 6) {
            speed = 2;
        }
        if (x > 6) {
            speed = 1;
        }
        dx = dx*speed; dy = dy*speed;
        x = x + dx; y = y + dy;
        if (DEBUG) {
            System.out.println("x: " + x + ", y: " + y);
        }
        if (dx > dy) {
            if (dx < 0) {
                if (x <= xEnd * width) {
                    cycle(xEnd, yEnd);
                }
            }
            if (dx > 0) {
                if (x >= xEnd * width) {
                    cycle(xEnd, yEnd);
                }
            }
            if (dx == 0 && dy > 0) {
                if (y >= yEnd * height) {
                    cycle(xEnd, yEnd);
                }
            }
            if (dx == 0 && dy < 0) {
                if (y <= yEnd * height) {
                    cycle(xEnd, yEnd);
                }
            }
        } else {
            if (dy < 0) {
                if (y <= yEnd * height) {
                    cycle(xEnd, yEnd);
                }
            }
            if (dy > 0) {
                if (y >= yEnd * height) {
                    cycle(xEnd, yEnd);
                }
            }
            if (dy == 0 && dx > 0) {
                if (x >= xEnd * width) {
                    cycle(xEnd, yEnd);
                }
            }
            if (dy == 0 && dx < 0) {
                if (x <= xEnd * width) {
                    cycle(xEnd, yEnd);
                }
            }
        }
    }

    // Just a helper method
    public void cycle(int xEnd, int yEnd) {
        timer.stop();
        startAnimation = false;
        updateBoard(yEnd - 1, xEnd - 1, currentPiece);
    }

    // Updates the movement of the current board piece and calculates possible arrow moves when needed
    public void updateBoard(int row, int column, int value) {
        boardArray[row][column] = value;
        marker.clear();
        markerCoordinates.clear();
        GridCoordinate point = new GridCoordinate(column + 1, row + 1);
        markerCoordinates.add(point);
        if (DEBUG) {
            System.out.println("Marker added | (x,y) = " + point.x + "," + point.y);
        }
        logicBoard.removePossibleMoves(boardArray);
        if (updateNeeded) {
            logicBoard.calculatePossibleMoves(boardArray, point.x, point.y, 5);
            marker.add(new Rectangle2D.Double(gridXCor * width + 2, gridYCor * height + 2, width - 4, height - 4));
        }
        int[][] tmp = Arrays.copyOf(boardArray, boardArray.length);
        logicBoard.saveBoard(tmp);
        if(logicBoard.isGameOver(boardArray, 1)) {
            System.out.println("*******************************");
            System.out.println("* Game is over, player 2 wins *");
            System.out.println("*******************************");
            screen.setLabel("The game is over. Player 2 (black) wins.");
            player1 = false; player2 = false;
        }
        if(logicBoard.isGameOver(boardArray, 2)) {
            System.out.println("*******************************");
            System.out.println("* Game is over, player 1 wins *");
            System.out.println("*******************************");
            screen.setLabel("The game is over. Player 1 (white) wins.");
            player1 = false; player2 = false;
        }

    }

    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    public void undo() {
        ArrayList<GridCoordinate> tempNodesCoordinates = new ArrayList<>();
        ArrayList<Shape> tempNodes = new ArrayList<>();
        ArrayList<Shape> tempStart = new ArrayList<>();
        ArrayList<Shape> tempEnd = new ArrayList<>();
    }

    // Draws the logical board
    public void drawBoard(Graphics2D g, int[][] array) {
        pieces.clear();
        pieceCoordinates.clear();
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                if (array[i][j] != 0) {
                    drawPiece(g, array[i][j] - 1, j + 1, i + 1);
                }
            }
        }
    }

    // Draws a piece on the board
    public void drawPiece(Graphics2D g, int index, int x, int y) {
        g.drawImage(piece[index], x*width, y*height, width, height, this);
    }

    // Draws the board grid
    public void drawCells(Graphics2D g, int width, int height) {
        for (int i = 0; i < sizeX / width; i++) {
            for (int j = 0; j < sizeY / height; j++) {
                if (i >=1 & j>=1 & i < sizeX/width - 1 & j < sizeY/height - 1) {
                    if (i % 2 == 0) {
                        if (j % 2 == 0) {
                            color = even;
                        } else {
                            color = odd;
                        }
                    } else {
                        if (j % 2 != 0) {
                            color = even;
                        } else {
                            color = odd;
                        }
                    }
                    drawSquare(g, i * (width), j * (height), width, height, color);
                }
            }
        }
    }

    // Draws a grid square
    public void drawSquare(Graphics2D g, int x, int y, int width, int height, Color color) {
        square = new Rectangle2D.Double(x, y, width, height);
        cells.add(square);
        g.setPaint(color);
        g.fill(square);
    }

    // Mouse listener: when moved
    public void mouseMoved(MouseEvent e) {
        for (Shape r : cells) {
            if (r.contains(e.getPoint())) {
                gridXCor = e.getX() / width;
                gridYCor = e.getY() / height;
                entersBoard = true;
                repaint();
            }
        }
    }

    // Mouse listener: when button pressed
    public void mousePressed(MouseEvent e) {
        xCor = e.getX();
        yCor = e.getY();

        // Player 1
        if (player1) {
            System.out.println("\nPlayer 1 (white)");
            screen.setLabel("Player 1 (white) is up. Please select your amazon.");
            if (selectQueen) {
                System.out.println("Select queen - Player 1 (white)");
                if (checkPiece(1) && logicBoard.isMovePossible(boardArray, gridXCor, gridYCor)) {
                    marker.clear();
                    markerCoordinates.clear();
                    currentPiece = 1;
                    GridCoordinate point = new GridCoordinate(gridXCor, gridYCor);
                    marker.add(new Rectangle2D.Double(gridXCor * width + 2, gridYCor * height + 2, width - 4, height - 4));
                    markerCoordinates.add(point);
                    logicBoard.calculatePossibleMoves(boardArray, point.x, point.y, 4);
                    x = gridXCor * width; y = gridYCor * height;
                    printMe1 = true;
                    selectQueen = false; selectPossibleQueenSpot = true; updateNeeded = true;
                    int[][] tmp = Arrays.copyOf(boardArray, boardArray.length);
                    logicBoard.saveBoard(tmp);
                }
            }
            if (selectPossibleQueenSpot) {
                System.out.println("Select possible queen spot - Player 1 (white)");
                screen.setLabel("Select where to move the amazon.");
                if (checkPiece(4)) {
                    GridCoordinate point = new GridCoordinate(gridXCor, gridYCor);
                    marker.add(new Rectangle2D.Double(gridXCor * width + 2, gridYCor * height + 2, width - 4, height - 4));
                    markerCoordinates.add(point);
                    if (DEBUG) {
                        System.out.println("\nQueen move: (" + markerCoordinates.get(0).x + "," + markerCoordinates.get(0).y + ") -> (" + markerCoordinates.get(1).x + "," + markerCoordinates.get(1).y + ")");
                    }
                    printMe1 = false; printMe2 = true;
                    selectPossibleQueenSpot = false; startAnimation = true;
                    boardArray[markerCoordinates.get(0).y - 1][markerCoordinates.get(0).x - 1] = 0;
                    selectPossibleArrowSpot = true;
                    int[][] tmp = Arrays.copyOf(boardArray, boardArray.length);
                    //logicBoard.saveBoard(tmp);
                }
            }
            if (selectPossibleArrowSpot) {
                System.out.println("Select possible arrow spot - Player 1 (white)");
                screen.setLabel("Select where to shoot an arrow.");
                if (checkPiece(5)) {
                    currentPiece = 3;
                    GridCoordinate point = new GridCoordinate(gridXCor, gridYCor);
                    marker.add(new Rectangle2D.Double(gridXCor * width + 2, gridYCor * height + 2, width - 4, height - 4));
                    markerCoordinates.add(point);
                    if (DEBUG) {
                        System.out.println("\nArrow move: (" + markerCoordinates.get(0).x + "," + markerCoordinates.get(0).y + ") -> (" + markerCoordinates.get(1).x + "," + markerCoordinates.get(1).y + ")");
                    }
                    printMe2 = false; printMe3 = true;
                    selectPossibleArrowSpot = false; updateNeeded = false; startAnimation = true;
                    player1 = false; player2 = true; selectQueen = true;
                }
            }
        }

        // Player 2
        if (player2) {
            System.out.println("\nPlayer 2 (black)");
            screen.setLabel("Player 2 (black) is up. Please select your amazon.");
            if (selectQueen) {
                System.out.println("Select queen - Player 2 (black)");
                if (checkPiece(2) && logicBoard.isMovePossible(boardArray, gridXCor, gridYCor)) {
                    marker.clear(); markerCoordinates.clear();
                    currentPiece = 2;
                    GridCoordinate point = new GridCoordinate(gridXCor, gridYCor);
                    marker.add(new Rectangle2D.Double(gridXCor * width + 2, gridYCor * height + 2, width - 4, height - 4));
                    markerCoordinates.add(point);
                    x = gridXCor*width; y = gridYCor*height;
                    logicBoard.calculatePossibleMoves(boardArray, point.x, point.y, 4);
                    selectQueen = false; selectPossibleQueenSpot = true; updateNeeded = true;
                    int[][] tmp = Arrays.copyOf(boardArray, boardArray.length);
                    logicBoard.saveBoard(tmp);
                }
            }
            if (selectPossibleQueenSpot) {
                //logicBoard.saveBoard(boardArray);
                System.out.println("Select possible queen spot - Player 2 (black)");
                screen.setLabel("Select where to move the amazon.");
                if (checkPiece(4)) {
                    GridCoordinate point = new GridCoordinate(gridXCor, gridYCor);
                    marker.add(new Rectangle2D.Double(gridXCor * width + 2, gridYCor * height + 2, width - 4, height - 4));
                    markerCoordinates.add(point);
                    if (DEBUG) {
                        System.out.println("\nQueen move: (" + markerCoordinates.get(0).x + "," + markerCoordinates.get(0).y + ") -> (" + markerCoordinates.get(1).x + "," + markerCoordinates.get(1).y + ")");
                    }
                    selectPossibleQueenSpot = false; startAnimation = true;
                    boardArray[markerCoordinates.get(0).y - 1][markerCoordinates.get(0).x - 1] = 0;
                    selectPossibleArrowSpot = true;
                    int[][] tmp = Arrays.copyOf(boardArray, boardArray.length);
                    //logicBoard.saveBoard(tmp);
                }
            }
            if (selectPossibleArrowSpot) {
                System.out.println("Select possible arrow spot - Player 2 (black)");
                screen.setLabel("Select where to shoot an arrow.");
                if (checkPiece(5)) {
                    currentPiece = 3;
                    GridCoordinate point = new GridCoordinate(gridXCor, gridYCor);
                    marker.add(new Rectangle2D.Double(gridXCor * width + 2, gridYCor * height + 2, width - 4, height - 4));
                    markerCoordinates.add(point);
                    if (DEBUG) {
                        System.out.println("\nArrow move: (" + markerCoordinates.get(0).x + "," + markerCoordinates.get(0).y + ") -> (" + markerCoordinates.get(1).x + "," + markerCoordinates.get(1).y + ")");
                    }
                    selectPossibleArrowSpot = false; updateNeeded = false; startAnimation = true;
                    player2 = false; player1 = true; selectQueen = true;
                    screen.setLabel("Player 1 (white) is up. Please select your amazon.");
                }
            }
        }

        repaint();
    }


    // Check selected piece with the logical board
    public boolean checkPiece(int piece) {
        if (boardArray[gridYCor-1][gridXCor-1] == piece) {
            return true;
        } else {
            return false;
        }
    }

    // Returns the content of the arraylist as a string
    public String toString(int[][] board) {
        return logicBoard.toString(board);
    }

    public String listToString(int[] list) {
        return logicBoard.listToString(list);
    }

    public int[][] listToArray(int[] list) {
        return logicBoard.listToArray(list);
    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public void mouseDragged(MouseEvent e) {

    }

}

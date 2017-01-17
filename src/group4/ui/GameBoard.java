package group4.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

import group4.AI.Maximus;
import group4.MCTS.James;
import group4.Players.Player;
import group4.components.ImageLoader;
import group4.logic.LogicBoard;
import group4.randomAI.Bobby;
import group4.utilities.BoardOperations;

public class GameBoard extends JPanel implements MouseMotionListener, MouseListener, ActionListener {

    private final Color GREEN = new Color(126, 211, 33, 255);
    private final Color BLACK = new Color(0, 0, 0, 255);
    private final Color RED = new Color(254, 0, 0, 255);
    private Color color;
    private Color even = new Color(209, 140, 72, 255);
    private Color odd = new Color(255, 207, 159, 255);
    private Shape square;
    private int xCor, yCor;
    private int counter;

    private int gridXCor, gridYCor;
    private GridCoordinate currentCoord;

    private int currentPiece;
    private Timer timer;
    public LogicBoard logicBoard;
    private boolean setPath = false, setStart = false, setEnd = false, startAnimation = false;
    private boolean entersBoard = false;
    private boolean updateNeeded;
    private boolean DEBUG = false;
    public Image[] piece, letter, figure;
    public int x, y, dx, dy, speed, delay = 50 / 60;
    public int sizeX, sizeY;

    public int width;
    public int height;
    public int[][] boardArray, tempArray;
    private GridCoordinate arrowPoint = new GridCoordinate(0,0);

    ArrayList<Shape> cells = new ArrayList();
    ArrayList<Shape> marker = new ArrayList<>();
    ArrayList<Integer> pieces = new ArrayList<>();
    ArrayList<GridCoordinate> markerCoordinates = new ArrayList();
    ArrayList<GridCoordinate> pieceCoordinates = new ArrayList<>();


    // Constructor
    public GameBoard(int sizeX, int sizeY, int gridX, int gridY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;

        width = sizeX / gridX;
        height = sizeY / gridY;
        System.out.println("width: " + width + ", height: " + height);
        setSize(sizeX, sizeY);
        setBackground(Color.lightGray);
        addMouseListener(this);
        addMouseMotionListener(this);
        timer = new Timer(delay, this);
        System.out.println("New timer");

        Player player1 = new James( true);
        Player player2 = new Maximus(false,2);

        logicBoard = new LogicBoard(player1, player2);
        boardArray = logicBoard.getBoard();
        System.out.println("\nInitial board:");
        //logicBoard.printBoard(boardArray);
        // logicBoard.calculateQueenDirections(7,10);
        counter = 1;
    }




    // Sets the mode
    public void setMode(boolean setPath, boolean setStart, boolean setEnd, boolean startAnimation) {
        this.setPath = setPath;
        this.setStart = setStart;
        this.setEnd = setEnd;
        this.startAnimation = startAnimation;
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
            //System.out.println("Piece: " + (currentPiece - 1) + "/tx: " + x + "/ty: " + y);
            if(currentPiece != 0)
                g2.drawImage(piece[currentPiece - 1], x, y, width, height, this);
        }




    }

    public void move(int xStart, int yStart, int xEnd, int yEnd) {
        dx = xEnd - xStart;
        dy = yEnd - yStart;
        //logicBoard.printBoard(boardArray);
        timer.start();
        //System.out.println("Animation started");
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
        x = x + dx;
        y = y + dy;
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
        //System.out.println("Animation: false");



        if(!logicBoard.arrowSpotSelect && logicBoard.queenSelect)	{
            //System.out.println("ENTERED: Choosing Queen");
            currentPiece = logicBoard.getCurrent().getVal();
            logicBoard.arrowSpotSelect = true;
            logicBoard.queenSelect = false;
        }
        else if(logicBoard.arrowSpotSelect){
            //System.out.println("ENTERED: Choosing Arrow");
            currentPiece = logicBoard.arrowVal;
        }


        //System.out.println("Currentpiece: " + currentPiece);
        updateBoard(yEnd - 1, xEnd - 1, currentPiece);



    }



    // Updates the movement of the current board piece and calculates possible arrow moves when needed
    public void updateBoard(int row, int column, int value) {
        //System.out.println("Value: " + value);
        if(logicBoard.getCurrent().isBot())
            System.out.println("Bot is playing");


        if(value == 3)	{

            logicBoard.setArrowOn(column, row);

            logicBoard.removePossibleMoves();
            logicBoard.addMove();



            //logicBoard.queenSelect = false;
            //logicBoard.arrowSpotSelect = false;
        }
        else	{

            logicBoard.setQueenOfCurrentOn(column, row);
            if(logicBoard.getCurrent().isBot()) {
                updateNeeded = true;


            }

        }
        boardArray = logicBoard.getBoard();
        //System.out.println("Board looks like:");
        //logicBoard.printBoard(boardArray);
        marker.clear();
        markerCoordinates.clear();

        //System.out.println("Markers clear");
        GridCoordinate point = new GridCoordinate(column + 1, row + 1);

        markerCoordinates.add(point);



        //System.out.println("Marker added | (x,y) = " + point.x + "," + point.y);
        logicBoard.removePossibleMoves();
        boardArray = logicBoard.getBoard();
        //System.out.println("\nRemoved possible moves:");
        //logicBoard.printBoard(boardArray);

        if(logicBoard.getCurrent().isBot() && value == 3)   {
            System.out.println("ENTERED update changer");
            updateNeeded = false;
        }

        System.out.println("Update needed: " + updateNeeded);
        //logicBoard.printBoard();

        if (updateNeeded) {

            //if((logicBoard.getCurrent().isBot() && !logicBoard.arrowSpotSelect) || !logicBoard.getCurrent().isBot())
            logicBoard.calcPosMoves(point, false);
            logicBoard.addMove();
            boardArray = logicBoard.getBoard();
            //System.out.println("Board updated:");
            //logicBoard.printBoard(boardArray);
            marker.add(new Rectangle2D.Double(gridXCor * width + 2, gridYCor * height + 2, width - 4, height - 4));
            updateNeeded = false;
        }

        counter++;
    }

    public void printAllSteps() {
        logicBoard.printAllMoves();
    }

    public void actionPerformed(ActionEvent e) {repaint();
    }

    public void undo() {
        if(!startAnimation) {
            ArrayList<GridCoordinate> tempNodesCoordinates = new ArrayList<>();
            ArrayList<Shape> tempNodes = new ArrayList<>();
            ArrayList<Shape> tempStart = new ArrayList<>();
            ArrayList<Shape> tempEnd = new ArrayList<>();
            System.out.println("Entered");
            logicBoard.undoMove();
            markerCoordinates.clear();
            if (logicBoard.getOrigin() != null) {
                markerCoordinates.add(logicBoard.getOrigin());
            }

            boardArray = logicBoard.getBoard();
            System.out.println("new current");
            //logicBoard.printBoard();
        }





    }

    public ArrayList<int[][]> getBoardStates() {
        return logicBoard.getBoardStates();
    }

    public int getBoardStatesSize() {
        return logicBoard.getBoardStatesSize();
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





        //only executed when Animation is done and current player isnÂ´t a bot
        if(!startAnimation && !logicBoard.getCurrent().isBot())		{



            if(!logicBoard.arrowSpotSelect && !logicBoard.queenSelect)	{
                GridCoordinate currentPoint = new GridCoordinate(gridXCor, gridYCor);
                if(logicBoard.amazonOfCurrentPlayer(currentPoint)){
                    System.out.println("Select queen of Player: " + logicBoard.getCurrent());

                    logicBoard.calcPosMoves(currentPoint, true);

                    if (logicBoard.isMovePossible()) {
                        //System.out.println("Gridx =  " + gridXCor + " Y = " + gridYCor);
                        GridCoordinate point = new GridCoordinate(gridXCor, gridYCor);
                        marker.clear();

                        markerCoordinates.clear();
                        currentPiece = logicBoard.getCurrent().getVal();
                        //System.out.println("Point: " + point);
                        marker.add(new Rectangle2D.Double(gridXCor * width + 2, gridYCor * height + 2, width - 4, height - 4));
                        markerCoordinates.add(point);

                        //logicBoard.calculatePossibleMoves(boardArray, point.x, point.y, 4);
                        x = gridXCor * width;
                        y = gridYCor * height;

                        updateNeeded = true;
                        logicBoard.queenSelect = true;
                    }
                }
            }
            else if (!logicBoard.arrowSpotSelect && logicBoard.queenSelect) {

                GridCoordinate point = new GridCoordinate(gridXCor, gridYCor);

                if(!logicBoard.amazonOfCurrentPlayer(point) && logicBoard.posMoveAt(point))	{
                    logicBoard.removePossibleMoves();

                    System.out.println("Select possible queen spot for Player: " + logicBoard.getCurrent());
                    System.out.println("Point: " + point);
                    marker.add(new Rectangle2D.Double(gridXCor * width + 2, gridYCor * height + 2, width - 4, height - 4));
                    markerCoordinates.add(point);
                    //System.out.println("\nQueen move: (" + markerCoordinates.get(0).x + "," + markerCoordinates.get(0).y + ") -> (" + markerCoordinates.get(1).x + "," + markerCoordinates.get(1).y + ")");
                    startAnimation = true;


                    //boardArray[markerCoordinates.get(0).y - 1][markerCoordinates.get(0).x - 1] = 0;
                    logicBoard.setEmpty(markerCoordinates.get(0));
                    boardArray = logicBoard.getBoard();
                    System.out.println("\nIntermediate board:");
                    //logicBoard.printBoard(boardArray);
                    arrowPoint = point;
                }
                else if(logicBoard.amazonOfCurrentPlayer(point) && !logicBoard.posMoveAt(point))	{
                    //Check in with roger
                    logicBoard.queenSelect = false;

                }
            }
            else if (logicBoard.arrowSpotSelect && !logicBoard.queenSelect) {
                System.out.println("Select possible arrow spot - Player 1 (white)");

                GridCoordinate point = new GridCoordinate(gridXCor, gridYCor);
                if (logicBoard.posMoveAt(point)) {

                    currentPiece = logicBoard.arrowVal;
                    //System.out.println("Origin: " + markerCoordinates.get(0));
                    marker.add(new Rectangle2D.Double(gridXCor * width + 2, gridYCor * height + 2, width - 4, height - 4));
                    markerCoordinates.add(point);

                    int i = 0;
                    while(i < markerCoordinates.size()) {
                        System.out.println(markerCoordinates.get(i));
                        i++;
                    }

                    //System.out.println("\nArrow move: (" + markerCoordinates.get(0).x + "," + markerCoordinates.get(0).y + ") -> (" + markerCoordinates.get(1).x + "," + markerCoordinates.get(1).y + ")");
                    //System.out.println("Origin: " + markerCoordinates.get(0) + "\tDest: " + markerCoordinates.get(1));


                    updateNeeded = false; startAnimation = true;
                    int j = 0;
                    while(j < markerCoordinates.size()) {
                        //System.out.println("Number " + j + ": " + markerCoordinates.get(j));
                        j++;
                    }

                }
                else	{
                    //System.out.println("Invalid Position");
                }
            }




        }


        //System.out.println("\nAnimation: " + startAnimation);
        System.out.println("QueenSelect: " + logicBoard.queenSelect);
        System.out.println("ArrowSelect: " + logicBoard.arrowSpotSelect + "\n");

        repaint();
    }

    public int[][] listToArray(int[] list) {
        return logicBoard.listToArray(list);
    }

    public void setBoard(int[][] Array) {
        logicBoard.setBoard(Array);
        //logicBoard.printBoard();

        boardArray = logicBoard.getBoard();
        int val = BoardOperations.countArrow(boardArray);
        if(val % 2 != 0)
            logicBoard.toggleTurn();
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
    public String toString() {
        String string = logicBoard.getBoardAsString();
        return string;
    }

    public void loadBoardin()   {
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

    //Bot is ACTIVATED HERE
    public void activateBot()   {
        if(logicBoard.getCurrent().isBot()) {

            Player Bot = logicBoard.getCurrent();
            System.out.println("Before passing to bot");
            //logicBoard.printBoard();
            Bot.giveInput(logicBoard.getBoard());

            //QueenMove
            if(!logicBoard.arrowSpotSelect) {
                GridCoordinate[] queenMove = Bot.chooseQueenMove();
                GridCoordinate origin = queenMove[0];

                System.out.println("Trying to set to empty: " + origin);
                logicBoard.setEmpty(origin);

                GridCoordinate dest = queenMove[1];

                updateBoard(dest.y - 1, dest.x - 1, Bot.getVal());
                logicBoard.arrowSpotSelect = true;
            }
            else{
                //logicBoard.getCurrent().giveInput(logicBoard.getBoard());
                GridCoordinate dest = Bot.chooseArrowMove();
                updateBoard(dest.y - 1, dest.x - 1, 3);
            }


            boardArray = logicBoard.getBoard();
            repaint();

            try {
                Thread.sleep(500);
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }

        }
    }

}
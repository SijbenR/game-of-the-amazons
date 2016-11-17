package group4.gameoftheamazons.ui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Screen {

    private JFrame frame;
    private ControlPanel controlPanel;
    private GameBoard gameBoard;
    private JButton button1, button2, button3, button4, button5, button6, button7, button8;
    private String message;
    private int counter;

    public int sizeX;
    public int sizeY;
    public int gridX;
    public int gridY;

    public static JLabel label;

    public Screen(int sizeX, int sizeY, int gridX, int gridY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.gridX = gridX;
        this.gridY = gridY;
        frame = new JFrame();
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(200,200,200));
        topPanel.setSize(sizeX, 500);
        gameBoard = new GameBoard(sizeX + gridX*2, sizeY + gridY*2, gridX + gridX/5, gridY + gridY/5, gridX, gridY);
        controlPanel = new ControlPanel(sizeX, sizeY - sizeX);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(sizeX + gridX*2, sizeY + gridY*2 + 80);
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(gameBoard, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.SOUTH);
        label = new JLabel("The Game Of The Amazons. Press 'Start' to play.");
        label.setForeground(new Color(50,50,50));
        topPanel.add(label);
        addButtons();
        addListeners();
        frame.setTitle("");
        frame.setVisible(true);
        counter = gameBoard.getCounter();
    }

    public void addButtons() {
        button1 = new JButton("Print");
        button2 = new JButton("Move");
        button3 = new JButton("Clear");
        button4 = new JButton("Start");
        button5 = new JButton("Save");
        button6 = new JButton("Redo");
        button7 = new JButton("Undo");
        button8 = new JButton("Load");
        controlPanel.add(button7);
        controlPanel.add(button6);
        controlPanel.add(button5);
        controlPanel.add(button8);
        //controlPanel.add(button1);
        controlPanel.add(button3);
        controlPanel.add(button4);
    }

    public static void setLabel(String text) {
        label.setText(text);
    }

    // Listeners
    public void addListeners() {
        // Print
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int size = gameBoard.getBoardStatesSize();
                for (int i = 0; i < size; i++) {
                    System.out.println("\nBoard-index: " + i);
                    System.out.println("-------------------");
                    gameBoard.printBoard(gameBoard.getBoardStates().get(i));
                }
            }
        });
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });
        // Start
        button4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameBoard.setMode(false, true, false, false);
                setLabel("Player 1 (white) is up. Please select an amazon.");
            }
        });
        // Clear
        button3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.remove(gameBoard);
                frame.revalidate();
                frame.repaint();
                gameBoard = new GameBoard(sizeX + gridX*2, sizeY + gridY*2, gridX + gridX/5, gridY + gridY/5, gridX, gridY);
                frame.add(gameBoard);
                frame.revalidate();
                frame.repaint();
                message = "Your game has been reset. You can now start playing again.";
                gameBoard.setMode(true, false, false, false);
                counter = 0;
                setLabel("The Game Of The Amazons. Press 'Start' to play.");
            }
        });
        // Save
        button5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //gameBoard.setMode(false, false, false, true);
                showSaveFileDialog();
                //gameBoard.save(gameBoard.boardArray);
            }
        });
        // Redo
        button6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = gameBoard.getBoardStatesSize() - 1;
                if ((index + counter) < index) {
                    counter++;
                }
                gameBoard.returnBoard(index + counter);
                frame.repaint();
                System.out.println("index: " + (index + counter));
            }
        });
        // Undo
        button7.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = gameBoard.getBoardStatesSize() - 1;
                if ((index + counter) > 0) {
                    counter--;
                }
                gameBoard.returnBoard(index + counter);
                frame.repaint();
                System.out.println("index: " + (index + counter));
            }
        });
        // Load
        button8.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadFileDialog();
            }
        });
    }

    public void showSaveFileDialog() {
        JFrame parentFrame = new JFrame();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to save");
        int userSelection = fileChooser.showSaveDialog(parentFrame);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            System.out.println("Save as file: " + fileToSave.getAbsolutePath());
            try {
                writeFileTo(fileToSave.getAbsoluteFile());
                message = "The board has been saved successfully.";
                JOptionPane.showMessageDialog(frame, message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadFileDialog() {
        JFrame parentFrame = new JFrame();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a board to load");
        String string;
        String[] stringArray = {};
        boolean success = false;
        int userSelection = fileChooser.showOpenDialog(parentFrame);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try {
                File fileSelected = fileChooser.getSelectedFile();
                System.out.println(fileSelected.getName());
                string = readFileFrom(fileSelected.getAbsolutePath());
                stringArray = string.split("");
                message = "The board has been loaded successfully.";
                JOptionPane.showMessageDialog(frame, message);
                success = true;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (success) {
            int[] list = new int[stringArray.length];
            for (int i = 0; i < stringArray.length; i++) {
                list[i] = Integer.parseInt(stringArray[i]);
            }
            int[][] board = gameBoard.listToArray(list);
            gameBoard.boardArray = board.clone();
            frame.repaint();
        }
    }

    public void writeFileTo(File output) throws IOException {
        FileWriter fw = new FileWriter(output);
        String string = gameBoard.toString(gameBoard.boardArray);
        fw.write(string);
        fw.close();
    }

    public String readFileFrom(String input) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(input));
        return new String(encoded);
    }

}

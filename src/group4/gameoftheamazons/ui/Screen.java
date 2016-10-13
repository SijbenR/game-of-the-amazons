package group4.gameoftheamazons.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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

    public Screen(int sizeX, int sizeY, int gridX, int gridY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.gridX = gridX;
        this.gridY = gridY;
        frame = new JFrame();
        gameBoard = new GameBoard(sizeX + gridX*2, sizeY + gridY*2, gridX + gridX/5, gridY + gridY/5, gridX, gridY);
        controlPanel = new ControlPanel(sizeX, sizeY - sizeX);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(sizeX + gridX*2 - 8, sizeY + gridY*2 + 60 - 4);
        frame.add(gameBoard);
        frame.add(controlPanel, BorderLayout.SOUTH);
        addButtons();
        addListeners();
        frame.setTitle("The Game Of The Amazons");
        frame.setVisible(true);
        counter = gameBoard.getCounter();
    }

    public void addButtons() {
        button1 = new JButton("Print");
        button2 = new JButton("Move");
        button3 = new JButton("End");
        button4 = new JButton("Restart");
        button5 = new JButton("Save");
        button6 = new JButton("Redo");
        button7 = new JButton("Undo");
        button8 = new JButton("Restore");
        controlPanel.add(button7);
        controlPanel.add(button6);
        controlPanel.add(button5);
        controlPanel.add(button8);
        controlPanel.add(button4);
        controlPanel.add(button1);
    }

    // Listeners
    public void addListeners() {
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
                message = "You can now set the start position.";
                //JOptionPane.showMessageDialog(frame, message);
                gameBoard.setMode(false, true, false, true);
                frame.repaint();
            }
        });
        button3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                message = "You can now set the end position.";
                //JOptionPane.showMessageDialog(frame, message);
                gameBoard.setMode(false, false, true, false);
            }
        });
        button4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.remove(gameBoard);
                frame.revalidate();
                frame.repaint();
                gameBoard = new GameBoard(sizeX + gridX*2, sizeY + gridY*2, gridX + gridX/5, gridY + gridY/5, gridX, gridY);
                frame.add(gameBoard);
                frame.revalidate();
                frame.repaint();
                message = "Your game has been reset. You can now start playing again.";
                //JOptionPane.showMessageDialog(frame, message);
                gameBoard.setMode(true, false, false, false);
                counter = 0;
            }
        });
        button5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameBoard.setMode(false, false, false, true);
                showSaveFileDialog();
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
        button8.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

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
                message = "Your design has been exported.";
                JOptionPane.showMessageDialog(frame, message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void writeFileTo(File output) throws IOException {
        FileWriter fw = new FileWriter(output);
        String string = gameBoard.toString();
        fw.write(string);
        fw.close();
    }

    public void print() {
        System.out.println(gameBoard.toString());
    }

}

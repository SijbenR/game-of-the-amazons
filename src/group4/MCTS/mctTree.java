package group4.MCTS;

import group4.tree.Node;
import group4.tree.NodeTree;

import java.util.Stack;

import static group4.utilities.BoardOperations.*;

/**
 * Created by Robins on 24.01.2017.
 */
public class mctTree extends NodeTree {


    protected int depthTilSim = 3;

    public mctTree(int[][] Board, int ownVal, boolean arrowMove, int branch) {
        super(Board, ownVal, arrowMove, 2, branch);

    }

    public void buildTree() {
        addChildren(root);
        //nodePointer.evaluateChildrenBySelect(root);
        bubbleSortNodesByScore(root.getChildren());

        Node best = root.getChildren().get(0);

        super.nodePointer.performMove(best);
        partBuild(best);

        for(int i = 0; i < best.getChildren().size(); i++)    {
            System.out.println("Child : " + best.getChildren().get(i));
            System.out.println("Wins = " + best.getChildren().get(i).getWins());
            System.out.println("Losses = " + best.getChildren().get(i).getLosses() +"\n");
        }
    }


    public void partBuild(Node newNode) {



        if(newNode.getNodeDepth() < depthTilSim)    {

            if (newNode.getChildren().size() == 0 && !gameOverCheck(super.nodePointer.getBoard())) {
                addChildren(newNode);

                for(Node child : newNode.getChildren()) {
                    System.out.println("For: " + child + "\nParent = " + child.getParent());
                    printBoard(super.nodePointer.getBoard());
                    super.nodePointer.performMove(child);

                    int[][] tempBoard = getCopy(super.nodePointer.getBoard());
                    if(gameOverCheck(tempBoard)) {
                        if(gameScore(tempBoard, ownVal) > (gameScore(tempBoard, 3-ownVal))   )   {
                            child.setWin(true);
                        }
                        else    {
                            child.setWin(false);
                        }
                    }
                    else    {
                        partBuild(child);
                        if(child.getWins() != 0 || child.getLosses() != 0)  {
                            newNode.addWins(child.getWins());
                            newNode.addLosses(child.getLosses());
                        }
                    }

                    child.visited();
                    super.nodePointer.performMove(child);
                }

            }
        }
        else  if (newNode.getNodeDepth() >= 1 && newNode.getNodeDepth() == depthTilSim)  {
            Node tempNode = newNode;
            Stack<Node> Moves = new Stack();
            Node newMove;

            //Keep simulating until  gameOver
            while(!gameOverCheck(super.nodePointer.getBoard())) {
                newMove = nodePointer.createChild(tempNode);
                tempNode.addChild(newMove);
                tempNode.visited();
                Moves.push(newMove);

                super.nodePointer.performMove(newMove);

                //System.out.println("Performed = " + newMove);
                //super.nodePointer.printBoard();

                tempNode = newMove;
            }
            System.out.println("GameOver reached");
            printBoard(super.nodePointer.getBoard());
            //Set Win or loss to bottom Node
            if(gameScore(super.nodePointer.getBoard(), ownVal) > gameScore(super.nodePointer.getBoard(), 3 -ownVal))    {
                System.out.println("Win for us");
                tempNode.setWin(true);
            }
            else    {
                System.out.println("Loss for us");
                tempNode.setWin(false);
            }

            Node temp;
            //Move back up
            while(Moves.size() != 0)    {
                temp = Moves.pop();
                super.nodePointer.performMove(temp);

                //System.out.println("Reverted = " + temp);
                //super.nodePointer.printBoard();

                if(temp.getParent().getLosses() == 0 || temp.getParent().getWins() == 0)    {

                    temp.getParent().addWins(temp.getWins());
                    temp.getParent().addLosses(temp.getLosses());
                }
            }
        }


    }

    public int getStillUnknown(Node cur)    {
        int i = 0;
        for(Node child : cur.getChildren()) {
            if(!child.wasVisited())  {
                return i;
            }
            else    {
                i++;
            }
        }
        return i;
    }



}


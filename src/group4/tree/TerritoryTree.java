package group4.tree;

import group4.ui.GridCoordinate;

import java.util.ArrayList;

import static group4.utilities.BoardOperations.bubbleSortNodesByScore;

/**
 * Created by robin on 17.01.2017.
 */
public class TerritoryTree extends NodeTree {
    public TerritoryTree(int[][] Board, int ownVal, boolean arrowMove, int depth, int branch) {
        super(Board, ownVal, arrowMove, depth, branch);
    }

    public Node bestchoice(){
        partBuild(root);
        bubbleSortNodesByScore(root.getChildren());
        bubbleSortNodesByScore(root.getChildren().get(0).getChildren());
        return root.getChildren().get(0);
    }

    public void partBuild(Node newNode) {

        //  System.out.println("NewNode = " + newNode);
        //nodePointer.printBoard();

        // System.out.println(++numberofnodesexplored);
        if (newNode.depthOfNode < super.depth) {
            if (newNode.getChildren().size() == 0) {
                addChildren(newNode);


                nodePointer.evaluateChildrenByTer(newNode);
                bubbleSortNodesByScore(newNode.getChildren());

                // System.out.println("Size: " + newNode.getChildren().size());
                for (int i = 0; i < 5 && i < newNode.getChildren().size(); i++) {
                    nodePointer.performMove(newNode.getChildren().get(i));
                    //System.out.println(newNode.getChildren().get(0));
                    //nodePointer.printBoard();
                    partBuild(newNode.getChildren().get(i));
                    newNode.setScore( avgchildscore(newNode));
                    nodePointer.performMove(newNode.getChildren().get(i));
                }

            }
        }


    }

    public GridCoordinate[] Movethebest(){
        //System.out.println("ENTERED NEW MOVE BEST");
        Node best = bestchoice();
        //System.out.println(best.getChildren().get(0));
        if(best.getChildren().size() != 0) {
            Node bestArrow = best.getChildren().get(0);


            nodePointer.retToRoot();
            //System.out.println("Root: ");
            //nodePointer.printBoard();

            //System.out.println("QueenMove: " + best);
            //   nodePointer.performMove(best);
            // nodePointer.printBoard();

            //System.out.println("Best Arrow: " + bestArrow);
            // nodePointer.performMove(best.getChildren().get(0));
            //nodePointer.printBoard();
            GridCoordinate[] move = new GridCoordinate[3];
            move[0] = best.origin;
            move[1] = best.dest;
            move[2] = best.getChildren().get(0).dest;
       //     System.out.println("best Move - From = " + best.origin + " To = " + best.dest + " shooting Arrow to = " + best.getChildren().get(0).dest);

            //System.out.println("best score = " + best.score);
            return move;
        }
        else
            return null;
    }


    public GridCoordinate[] MoveOtherBest(int pos1){

     //   System.out.println("Position: " + pos1);

        Node best = root.getChildren().get(pos1);
        if(best.getChildren().size() == 0)  {
            MoveOtherBest(pos1 + 1);
        }


        GridCoordinate[] move = new GridCoordinate[3];
        move[0] = best.origin;
        move[1] = best.dest;
        move[2] = best.getChildren().get(0).dest;
     //   System.out.println("best Move - From = " + best.origin + " To = " + best.dest + " shooting Arrow to = " + best.getChildren().get(0).dest);
        return move;
    }


    public void buildTree() {

        addChildren(root);

        ArrayList<Node> children = root.getChildren();
        // System.out.println("Size: " + children.size());
        nodePointer.evaluateChildrenByTer(root);
        //nodePointer.printBoard();




        bubbleSortNodesByScore(root.getChildren());
        for(Node Child: root.getChildren())    {
            //System.out.println(Child);
        }

        //System.out.println("Node: " + root.getChildren().get(root.getChildren().size()-1));

        Node toExpand = root.getChildren().get(0);

        GridCoordinate origin = toExpand.getOrigin();
        GridCoordinate dest = toExpand.getDest();
        boolean arrowMove = toExpand.arrowMove;

        nodePointer.performMove(origin, dest, arrowMove);

        //nodePointer.printBoard();

        addChildren(toExpand);


        nodePointer.evaluateChildrenByTer(toExpand);

        bubbleSortNodesByScore(toExpand.getChildren());
        for(Node Child: toExpand.getChildren())    {
            //System.out.println(Child.getScore() + "\t");
        }

        GridCoordinate or2 = toExpand.getChildren().get(0).getOrigin();
        GridCoordinate dest2 = toExpand.getChildren().get(0).getDest();
        boolean ar2 = toExpand.getChildren().get(0).arrowMove;

        //System.out.println("Origin: " + or2 + "\tDest2: " + dest2 + "\tar2: " + ar2 );

        nodePointer.performMove(or2, dest2, ar2);


    }
}

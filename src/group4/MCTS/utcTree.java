
package group4.MCTS;

import group4.tree.Node;

import group4.tree.NodeTree;
import group4.tree.Tree;
import group4.ui.GameBoard;
import group4.ui.GridCoordinate;
import group4.utilities.*;
import group4.logic.LogicBoard;

import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;

import static group4.utilities.BoardOperations.bubbleSortNodesByScore;
import static group4.utilities.BoardOperations.bubbleSortNodesByValue;
import static group4.utilities.BoardOperations.evaluate;

/**
 * Created by I6106804 on 18-1-2017.
 */
public class utcTree extends NodeTree{
    boolean useTer = true;

    ArrayList<Node> ListOfNodes = new ArrayList<>();
    double TimeToRun;


    BoardOperations Boardop = new BoardOperations();

    double c= Math.sqrt(2);

    public utcTree(int[][] Board, int ownVal, boolean arrowMove, double timeToRun, boolean useTer) {

        super(Board, ownVal, arrowMove, 15, 22);
        this.TimeToRun = timeToRun;
        this.useTer = useTer;
    }

    public GridCoordinate[] Movethebest(){
        //  System.out.println("entered 1");
        Node best = bestchoice();
     //   System.out.println("best choice" + best);
//        System.out.println(best.getChildren().get(0));
        int pick=0;
       // System.out.print("fuck");
        while (best.getChildren().get(pick)==null) {
            pick++;
         //   System.out.print("fuck");
        }

        Node bestArrow = best.getChildren().get(pick);

        super.nodePointer.retToRoot();
        //System.out.println("Root: ");
        //nodePointer.printBoard();

        //System.out.println("QueenMove: " + best);
        //   nodePointer.performMove(best);
        // nodePointer.printBoard();

        //System.out.println("Best Arrow: " + bestArrow);
        // nodePointer.performMove(best.getChildren().get(0));
        //nodePointer.printBoard();
        GridCoordinate[] move = new GridCoordinate[3];
        move[0] = bestArrow.getParent().getOrigin();
        move[1] = bestArrow.getOrigin();
        move[2] = bestArrow.getDest();
        // System.out.println("best score = " + best.getValue());
        return move;
    }



    public Node bestchoice(){
        //  System.out.println("entered 2");
        utcbuild(root);
        int pick=0;

       // System.out.println("Amount Children: " + root.getChildren().size());



        bubbleSortNodesByValue(root.getChildren());
    /*    for(Node child : root.getChildren())    {
            System.out.println(child);
        }
*/
        while(root.getChildren().get(pick).getChildren()==null)
        {
            pick++;
            //System.out.print("printing pick "+ pick);
        }
        bubbleSortNodesByValue(root.getChildren().get(pick).getChildren());
        return root.getChildren().get(pick);
    }

    public void utcbuild(Node root){

    //    System.out.println("entered 3");

        double starttime=System.currentTimeMillis();
        double endtime=System.currentTimeMillis();
        //System.out.println("For root: " + root);
        addChildren(root);
       // System.out.println(root.getChildren().size());

        if(!useTer) {
            super.nodePointer.evaluateChildren(root);
        }
        else    {
            super.nodePointer.evaluateChildrenByTer(root);
        }

        allNodes(root);
        selectionscore(root);
        // System.out.println("entered 5");
      //  Node toPick = bestnode(returnAllNodes(root),root);
        //  System.out.println("entered 6");

     //   System.out.println("Endtime = " + endtime + " Starttime = " + starttime + " TimeToRun = " + TimeToRun);

        while(endtime-starttime < TimeToRun){

          //  System.out.println("entered 4");
            building(ListOfNodes.get(0));
            endtime=System.currentTimeMillis();
        }
        nodePointer.retToRoot();

       // nodePointer.performMove(root.getChildren().get(0));
        //nodePointer.performMove(root.getChildren().get(0).getChildren().get(0));

      //  nodePointer.printBoard();

    }
    public void building(Node node){
       // System.out.println("Entered Building with " + node);
        selectionscore(node);
        allNodes(root);
        //System.out.println("Size of AllNodes: " + ListOfNodes.size());
        bubbleSortNodesByValue(ListOfNodes);

        super.nodePointer.retToRoot();
        Stack<Node> stack=new Stack<>();
        Node toadd= ListOfNodes.get(0);
        while(toadd.getParent()!=null){
            stack.push(toadd);
            toadd= toadd.getParent();
        }
        while (stack.size()>0) {
            super.nodePointer.performMove(stack.pop());
        }

        //System.out.println("EXPANDING: " + ListOfNodes.get(0));
        addChildren(ListOfNodes.get(0));


        if(!useTer) {
            super.nodePointer.evaluateChildren(ListOfNodes.get(0));
        }
        else    {
            super.nodePointer.evaluateChildrenByTer(ListOfNodes.get(0));
        }

        backpropagate(ListOfNodes.get(0).getChildren().get(0));
        bubbleSortNodesByValue(ListOfNodes);
    }




    public double selection(double eval, double n, double t){
        double score;

        score= eval + c* Math.sqrt(Math.log(t)/n);

        return score;
    }
    public int countnodes(Node parent, int t){

        if(parent.getChildren().size()>0){
            for (int i=0; i<parent.getChildren().size();i++){
                countnodes(parent.getChildren().get(i), ++t);

            }
        }
        return t;
    }



    public void selectionscore(Node parent){
        Node counting= parent;
        int count=parent.getChildren().size();


//todo need a second score saved in the nodes for the selection score
//todo once a second score is saved then we can use that rather than evaluating the board again
        for (int i=0; i<parent.getChildren().size();i++){
            if(parent.getChildren().get(i).ownMove){
                parent.getChildren().get(i).setValue(selection(parent.getChildren().get(i).getScore(), parent.getChildren().size(), countnodes(parent,1)));
            }else{
                parent.getChildren().get(i).setValue(selection(parent.getChildren().get(i).getScore(), parent.getChildren().size(), countnodes(parent,1)));

            }
        }
    }

    public void backpropagate(Node leaf){
        if(leaf.getParent()!=null){
            leaf.getParent().setScore(avgScoreChild(leaf.getParent()));
            backpropagate(leaf.getParent());
        }


    }
    public double avgScoreChild(Node parent){
        double avg=0;
        for(int i=0; i< parent.getChildren().size(); i++){
            avg+= parent.getChildren().get(i).getScore();
        }
        avg=avg/parent.getChildren().size();

        return avg;
    }
    public ArrayList<Node> returnAllNodes(Node parent){
        allNodes(parent);

        return ListOfNodes;
    }

    public void allNodes(Node parent){
        //should be first given the root


        if(parent.getChildren().size()>0){
            for(int i=0; i<parent.getChildren().size(); i++) {
                ListOfNodes.add(parent.getChildren().get(i));
                allNodes(parent.getChildren().get(i));
            }

        }

        //return allNodes;
    }

    public Node bestnode(ArrayList<Node> allNodes, Node root){

        allNodes(root);
       // System.out.println(allNodes.size());
        bubbleSortNodesByValue(allNodes);
        return allNodes.get(0);
    }

    public static void bubbleSortNodesByValue(ArrayList<Node> children)   {
        Node temp;
        if (children.size()>1) // check if the number of orders is larger than 1
        {
            for (int x=0; x<children.size(); x++) // bubble sort outer loop
            {
                for (int i=0; i < children.size() - x - 1; i++) {
                    if (children.get(i).getValue() < children.get(i + 1).getValue()) {
                        temp = children.get(i);
                        children.set(i, children.get(i + 1));
                        children.set(i + 1, temp);
                    }
                }

            }
        }
    }
}
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

import static group4.utilities.BoardOperations.*;

/**
 * Created by I6106804 on 18-1-2017.
 */
public class utcTree extends NodeTree{

    boolean useTer = false;
    boolean useSelect= true;

    ArrayList<Node> ListOfNodes = new ArrayList<>();
    double TimeToRun;


    BoardOperations Boardop = new BoardOperations();

    double c= Math.sqrt(2);

    public utcTree(int[][] Board, int ownVal, boolean arrowMove, double timeToRun, boolean useTer) {

        super(Board, ownVal, arrowMove, 15, 22);
        this.TimeToRun = timeToRun;
        this.useTer = useTer;
    }

    public utcTree(int[][] Board, int ownVal, boolean arrowMove, double timeToRun, boolean useTer, boolean useSelect, double newC) {

        super(Board, ownVal, arrowMove, 15, 22);
        this.TimeToRun = timeToRun;

        this.useTer = useTer;

        this.useSelect = useSelect;
        this.c = newC;
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

        if(!useTer && !useSelect) {
            super.nodePointer.evaluateChildren(root);
        }
        else if  (useTer && !useSelect)  {
            super.nodePointer.evaluateChildrenByTer(root);
        }
        else    {
            super.nodePointer.evaluateChildren(root);
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



        if(!useTer && !useSelect) {
            addChildren(ListOfNodes.get(0));
            super.nodePointer.evaluateChildren(ListOfNodes.get(0));
        }
        else if(!useSelect)   {
            addChildren(ListOfNodes.get(0));
            super.nodePointer.evaluateChildrenByTer(ListOfNodes.get(0));
        }
        else    {


            for(int i = 0; i < 10; i++) {
                Node newNode = nodePointer.generateRanMove(ListOfNodes.get(0));
                ListOfNodes.get(0).addChild(newNode);
            }

            for(int i = ListOfNodes.get(0).getChildren().size() - 1; i >= (ListOfNodes.get(0).getChildren().size() - 10); i--) {
                simulate(ListOfNodes.get(0).getChildren().get(i));
            }


        }

        int i = 0;

        while(ListOfNodes.get(i).getChildren().size() == 0) {
            i++;
        }

        backpropagate(ListOfNodes.get(i).getChildren().get(0));
        bubbleSortNodesByValue(ListOfNodes);
    }





    public void simulate(Node parent)  {
        //We assume nodepointer is at the right position

        Node newMove, newParent;
        newParent =  parent;
        Stack<Node> simMoves = new Stack<Node>();

        int breaker = 0;

        //Adding new Moves until the game is over
        while(!gameOverCheck(super.nodePointer.getBoard()) && mobCheck(super.nodePointer.getBoard())) {
            //System.out.println("Simulation loop 1");
            //System.out.println("For: " + newParent);
            super.nodePointer.performMove(newParent);
            //super.nodePointer.printBoard();
            System.out.println("New Parent = " + newParent);
            newMove = super.nodePointer.generateRanMove(newParent);
            newParent.addChild(newMove);

            if(newMove.getNodeDepth() > 33) {
                System.out.println("Simulation loop");
                nodePointer.printBoard();
            }
            simMoves.push(newMove);
            newParent = newMove;
            breaker++;
        }

        System.out.println("Got out");

        //Adding the win /Loss to the parent
        if(gameScore(super.nodePointer.getBoard(), ownVal) > gameScore(super.nodePointer.getBoard(), 3 - ownVal))    {
            parent.addWin();
        }
        else    {
            parent.addLoss();
        }


        System.out.println("Values Saved:\nWins = " + parent.getWins() + "\tLosses = " + parent.getLosses());

        //System.out.println("Returning to root:");
        //super.nodePointer.retToRoot();
        //super.nodePointer.printBoard();

        /*
        //Moving the nodepointer back
        while(simMoves.size() != 0) {
            newMove = simMoves.pop();
            super.nodePointer.performMove(newMove);
            System.out.println("Simulation loop 2");
        }
        */


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
        for (int i=0; i<parent.getChildren().size();i++) {


            if (useSelect) {
                int wins = parent.getChildren().get(i).getWins();
                int losses = parent.getChildren().get(i).getLosses();
                if ((wins + losses) != 0) {
                    parent.getChildren().get(i).setScore(wins / (wins + losses));
                } else {
                    simulate(parent.getChildren().get(i));
                    wins = parent.getChildren().get(i).getWins();
                    losses = parent.getChildren().get(i).getLosses();
                    parent.getChildren().get(i).setScore(wins / (wins + losses));
                }
            } else {
                if (parent.getChildren().get(i).ownMove) {
                    parent.getChildren().get(i).setValue(selection(parent.getChildren().get(i).getScore(), parent.getChildren().size(), countnodes(parent, 1)));
                } else {
                    parent.getChildren().get(i).setValue(selection(parent.getChildren().get(i).getScore(), parent.getChildren().size(), countnodes(parent, 1)));
                }


            }
        }
    }

    public void backpropagate(Node leaf){
        if(leaf.getParent()!=null){
            //leaf.getParent().setScore(avgScoreChild(leaf.getParent()));

            if(useSelect)   {
                int wins = 0;
                int losses = 0;

                for(Node child : leaf.getParent().getChildren()) {
                    wins += child.getWins();
                    losses += child.getLosses();
                }

                //System.out.println("BackProp: Wins = " + wins + " Losses = " + losses);

                leaf.getParent().setWins(wins);
                leaf.getParent().setLosses(losses);
            }
            else    {
                leaf.getParent().setScore(avgScoreChild(leaf.getParent()));
            }

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

    public void mctBuild()  {
        super.branch = 20;

        //Generate Random children for root (root = already visited)
        addMCTChildren(root);

        //Evaluate the children
        super.nodePointer.evaluateChildren(root);

        //BubbleSort
        BoardOperations.bubbleSortNodesByValue(root.getChildren());

        //Take best one
        Node toExpand = root.getChildren().get(0);

        //Generate RandomChildren
        addMCTChildren(toExpand);
        super.nodePointer.performMove(toExpand);

        //For each child => Play until finish)
        recBuild(toExpand);
        int j = 0;


        System.out.println("First Layer: " + root.getChildren().size());

        while(root.getChildren().get(j).getChildren().size() == 0)    {
            System.out.println("Second Layer: " + root.getChildren().get(j).getChildren().size());
            j++;
        }
        Node child = root.getChildren().get(j).getChildren().get(0);

        for(int i = 1; i < root.getChildren().get(0).getChildren().size(); i++)   {
            if(child.getWins() <= root.getChildren().get(0).getChildren().get(i).getWins() && child.getLosses() > root.getChildren().get(0).getChildren().get(i).getLosses())  {
                child = root.getChildren().get(0).getChildren().get(i);
            }
        }

        System.out.println("First Move = " + root.getChildren().get(0));
        System.out.println("Arrow = " + child);


    }

    public void recBuild(Node child)  {
        if(child.getChildren().size() == 0 && !gameOverCheck(super.nodePointer.getBoard())) {
            //If children.size == 0 && !gameOverCheck
            //Generate randomchildren
            System.out.println("Entered for: " + child);

            addMCTChildren(child);
            super.nodePointer.performMove(child);

            System.out.println("Amount of children = " + child.getChildren().size());
            recBuild(child);
        }
        else if(child.getChildren().size() != 0 && !gameOverCheck(super.nodePointer.getBoard()) && (child.getChildren().size()) > getStillUnknown(child))   {
            //else if(children.size != 0 && !gameOverCheck && Child(size-1) not visited )
            //Go to first one that has not been visited
            int notyetVis = getStillUnknown(child);
            Node newChild = child.getChildren().get(notyetVis);

            //Perform move
            super.nodePointer.performMove(newChild);
            System.out.println("Stuck in 1");
            //Repeat process
            recBuild(newChild);
        }
        else if(child.getChildren().size() != 0 && !gameOverCheck(super.nodePointer.getBoard()) && child.getChildren().get(child.getChildren().size()-1).wasVisited())   {
            //All nodes have been visited
            //So we count all Wins and losses
            double wins = 0;
            double losses = 0;

            //Set to visited
            child.visited();
            for(Node kid : child.getChildren())   {
                if(kid.isWin() || kid.getWins() > 0) {
                    child.addWins(kid.getWins());
                }
                if(!kid.isWin() || kid.getLosses() > 0)    {
                    child.addLosses(kid.getLosses());
                }
            }
            // for that Node and return
            super.nodePointer.performMove(child);
            System.out.println("Stuck in 2");
            recBuild(child.getParent());
        }
        else if(gameOverCheck(super.nodePointer.getBoard()))    {
            //Our gameScore
            int ownScore = gameScore(super.nodePointer.getBoard(), super.ownVal);

            //Enemy gameScore
            int enemScore = gameScore(super.nodePointer.getBoard(), (3-super.ownVal));

            //Set to visited
            child.visited();
            if(ownScore > enemScore)    {
                child.setWin(true);
            }
            else    {
                child.setWin(false);
            }
            super.nodePointer.performMove(child);
            System.out.println("Stuck in 3");
            recBuild(child.getParent());
        }
        System.out.println("I have no idea how I got here");
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












    public GridCoordinate[] testMethods()   {

        super.branch = 10;

        //add children
        addChildren(root);

        //evaluate the first layer with the normal eval and sort
        //nodePointer.evaluateChildren(root);

        //Look at first one and move the nodepointer there
        //ArrayList<Node> firstLayer = root.getChildren();


        //Simulate for ll Children for node
        for(int k = 0; k < root.getChildren().size(); k++) {
            //System.out.println("Reached 1");
            super.nodePointer.performMove(root.getChildren().get(k));
            //System.out.println("Reached 2");
            simulate(root.getChildren().get(k));
            //System.out.println("Reached 3");
            super.nodePointer.performMove(root.getChildren().get(k));
        }

        backpropagate(root.getChildren().get(0));

        double startTime = System.currentTimeMillis();
        double endTime = System.currentTimeMillis();
        while(endTime - startTime < TimeToRun) {
            allNodes(root);
            //System.out.println("Size of AllNodes: " + ListOfNodes.size());
            bubbleSortNodesByValue(ListOfNodes);


            //Expansion
            Node toExpand = ListOfNodes.get(0);
            int limit = 10;
            int i = 0;
            while(i < limit)    {
                Node newNode = super.nodePointer.generateRanMove(toExpand);
                //System.out.println("Reached, adding " + newNode);
                toExpand.addChild(newNode);
                i++;
            }

            //Simulation
            for(int k = 0; k < toExpand.getChildren().size(); k++) {
                super.nodePointer.performMove(toExpand.getChildren().get(k));
                simulate(toExpand.getChildren().get(k));
                super.nodePointer.performMove(toExpand.getChildren().get(k));

            }


            backpropagate(toExpand.getChildren().get(0));
            endTime = System.currentTimeMillis();

        }

        allNodes(root);
        bubbleSortNodesByValue(root.getChildren());

        Node bestFirst = root.getChildren().get(0);

        bubbleSortNodesByValue(root.getChildren().get(0).getChildren());
        Node bestSecond = root.getChildren().get(0).getChildren().get(0);


        nodePointer.retToRoot();
        GridCoordinate[] returnVal = new GridCoordinate[3];

        returnVal[0] = bestFirst.getOrigin();
        returnVal[1] = bestFirst.getDest();
        returnVal[2] = bestSecond.getDest();

        return returnVal;
        /*
        nodePointer.printBoard();

        System.out.println("First Move: " + bestFirst);
        nodePointer.performMove(bestFirst);
        nodePointer.printBoard();

        System.out.println("Best Second: " + bestSecond);
        nodePointer.performMove(bestSecond);
        nodePointer.printBoard();
        */
        /*
        //System.out.println("First Node: " + firstLayer.get(0));
        nodePointer.performMove(firstLayer.get(0));

        //add Limited amount of children and
        Node toExpand = firstLayer.get(0);
        super.nodePointer.performMove(toExpand);

        int limit = 10;
        int i = 0;
        while(i < limit)    {
            Node newNode = super.nodePointer.generateRanMove(toExpand);
            //System.out.println("Reached, adding " + newNode);
            toExpand.addChild(newNode);
            i++;
        }

        //TODO SCORE HERE?

        //perform a Simulation on one
        int pos = 0;
        //System.out.println("For Node: " + toExpand.getChildren().get(pos));
        for(int k = 0; k < toExpand.getChildren().size(); k++) {
            super.nodePointer.performMove(toExpand.getChildren().get(k));
            simulate(toExpand.getChildren().get(k));
            super.nodePointer.performMove(toExpand.getChildren().get(k));

        }

        //TODO Backprop HERE?

        backpropagate(toExpand.getChildren().get(0));

        System.out.println("Node: " + toExpand);



        System.out.println("Parent Loss = " + toExpand.getLosses() + "\t Win = " + toExpand.getWins() + "\n");

        /*
        for(int p = 0; p < toExpand.getChildren().size(); p++)    {
            System.out.println("For Child " + p + " Wins = "  + toExpand.getChildren().get(p).getWins() + "\nLosses = " + toExpand.getChildren().get(p).getLosses());
        }
        */


    }











}
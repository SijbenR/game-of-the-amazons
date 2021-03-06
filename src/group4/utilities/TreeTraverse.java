package group4.utilities;

import com.sun.org.apache.xpath.internal.operations.Or;
import group4.tree.Node;
import group4.ui.GameBoard;
import group4.ui.GridCoordinate;
import org.omg.CORBA.PERSIST_STORE;

import java.util.ArrayList;
import java.util.Stack;

import static group4.utilities.BoardOperations.*;
import static group4.utilities.BoardOperations.listPosDest;

/**
 * Created by Robins on 06.01.2017.
 *
 * WORK IN PROGRESS - DO NOT USE YET!
 */
public class TreeTraverse {

    private int[][] Board;

    Node currentNode;
    Node root;
    int ownVal, opVal;


    public TreeTraverse(int[][] Board, Node root)   {
        this.root = root;
        currentNode = root;
        this.Board = Board;

        if(root.ownMove)    {
            ownVal = root.playerVal;
        }
        else    {
            ownVal = root.getOpVal();
        }

    }


    public void performMove(Node Move)   {


        //System.out.println("For Node: " + Move + "\nCurrent = " + currentNode);
        //printBoard();

        if(Move.getParent() != currentNode && Move != currentNode) {

            Node temp = Move;
            Stack<Node> path = new Stack<>();


            while(temp.getParent() != null) {
                //System.out.println("TT loop check");
                path.push(temp);
                temp = temp.getParent();
            }

            retToRoot();

            while(path.size() != 0) {

                Node tNode = path.pop();

                GridCoordinate origin = tNode.getOrigin();
                GridCoordinate dest = tNode.getDest();
                boolean arrowMove = tNode.arrowMove;
                performMove(origin, dest, arrowMove);
            }



        }


        /*
        //First make sure that you are at the correct parent
        if(currentNode != Move.getParent() && Move.getParent() != null) {
            //ArrayList<Node> path = new ArrayList<>();
            Stack<Node> path = new Stack<>();
            Node temp = Move;
            while(temp.getParent() != null) {
                temp = Move.getParent();
                path.push(temp);
            }
            //get Nodepointer back to root
            while (currentNode != root) {
                currentNode = currentNode.getParent();
                performMove(currentNode);
                if (currentNode == Move.getParent())
                    break;
            }
            // now go along the path
            //We wanna movbe up
        }
        */

        if(currentNode == Move) {
            currentNode = Move.getParent();
        }
        else {
            currentNode = Move;
        }
        GridCoordinate origin = Move.getOrigin();
        GridCoordinate dest = Move.getDest();
        boolean arrowMove = Move.arrowMove;
        performMove(origin, dest, arrowMove);

    }

    //TODO Maybe conmtinue here
    /*
    public boolean checkStack(Node check, Stack<>) {
    }
    */


    public void performMove(GridCoordinate origin, GridCoordinate dest, boolean arrowMove)   {
        removePosMoves(Board);
        //System.out.println("BEFORE: ");
        //printBoard();




        if(getValAt(origin) == 3 && arrowMove)   {
            //Removing Arrow
            setEmpty(Board, origin);
        }
        else if((getValAt(origin) == 1 || getValAt(origin) == 2) && arrowMove && getValAt(dest) == 0)   {
            //Set Arrow
            //System.out.println("ENTERED!!!!");
            setArrow(Board, dest);
        }
        else if((getValAt(origin) == 1 || getValAt(origin) == 2) && arrowMove && getValAt(dest) == 3)   {
            //Set Arrow
            //TODO Possible error here in the future
            setEmpty(Board, dest);
        }
        else if((getValAt(origin) == 1 || getValAt(origin) == 2) && !arrowMove && getValAt(dest) == 0)   {
            //Move Queen
            int val = getValAt(origin);
            setEmpty(Board, origin);
            setQueenOn(Board, dest, val);
        }
        else if(getValAt(origin) == 0 && !arrowMove && (getValAt(dest) == 1 || getValAt(dest) == 2))   {
            //Move Queen BACK
            int val = getValAt(dest);
            setEmpty(Board, dest);
            setQueenOn(Board, origin, val);
        }
        else    {
            System.out.println("invalid Exception:\nOrigin = " + getValAt(origin) + "\nDestination = " + getValAt(dest) + "\nArrowMove = " + arrowMove);
        }
        //System.out.println("\n\nAFTER: ");
        //printBoard();
        //System.out.println("\n");

    }

    private int getValAt(GridCoordinate position) {
        int x = position.x - 1;
        int y = position.y - 1;

        if(!checkBound(Board, y, x))    {
            System.out.println(position + " - Not within Bounds");
        }
        return Board[y][x];
    }

    public int[][] getBoard()  {
        return Board;
    }


    public Node generateMCTRanMove(Node parent)    {
        //TODO CAREFUL MAYBE TAKE OUT
        performMove(parent);
        return generateRanMove(parent);
    }

    public Node createMCTChild(Node parent)   {
        return generateMCTRanMove(parent);
    }


    public Node generateRanMove(Node parent)    {
        int val;

        //Position 0 => Origin
        //Position 1 => Destination
        GridCoordinate[] returner = new GridCoordinate[2];


        ArrayList<GridCoordinate> posDest;


        //First we check if the now to be computed move is a Queen or an Arow move

        if(!parent.ownMove && parent.arrowMove)   {
            //Now we move a Queen
            //STARTCASE 1
            int queenVal = parent.getOpVal();
            //

            ArrayList<GridCoordinate> queens = posQueens(Board, queenVal);


            //Filter out all queens that can't move anymore
            int count = 0;
            GridCoordinate queen;
            //System.out.println(queen);

            for(int i = 0; i < queens.size(); i++)  {
                System.out.println("Reached 0");
                queen = queens.get(i);
                posDest = listPosDest(Board, queen);
                count = posDest.size();
                if(count == 0)  {
                    //Two cases
                    //
                    // 1. i < size - 1
                    // 2. i == size - 1

                    if(i < (queens.size() - 1))  {
                        int j=i;
                        while( j < queens.size() - 1)    {
                            queens.set(j, queens.get(j+1));
                            j++;
                        }
                        queens.remove(queens.size() - 1);
                        j = 0;
                    }else if (i == (queens.size() - 1))   {
                        queens.remove(queens.size() - 1);
                    }

                }
                //

            }
            System.out.println("Reached 1");
            if(queens.size() != 0)  {

                System.out.println("More than one queen");
                printBoard();

                //randomly choose queen
                int ran = (int) (queens.size() * Math.random());
                GridCoordinate origin = queens.get(ran);

                //System.out.println("From: " + origin);
                //TODO WORK ON THIS ONE - IMMOBILIZED QUEENS STILL IN LIST ????
                removePosMoves(Board);

                //randomly choose destination
                posDest = listPosDest(Board, origin);
                while(posDest.size() == 0)  {
                    //System.out.println("Reached 3");

                    ran = (int) (queens.size() * Math.random());
                    origin = queens.get(ran);
                    posDest = listPosDest(Board, origin);
                }
                //System.out.println("From: 2" + origin);
                //BoardOperations.printBoard(Board);
                removePosMoves(Board);
                ran = (int) (posDest.size() * Math.random());
                GridCoordinate tar = posDest.get(ran);
                //System.out.println("QueenSize: " + queens.size());
                Node newNode = new Node(parent, origin, tar);
                //System.out.println("NewNode: " + newNode);


                if(parent.getChildren().size() > 0) {
                    System.out.println("Reached 4");

                    //parent.getChildren().get(0).printNode();
                    //System.out.println("Size: " + parent.getChildren().size());


                    //TODO Temp fix here - if possible, try to solve

                    int iterations = 0;
                    /*
                    printBoard();
                    int pos = 0;
                    for(GridCoordinate tempQ: queens)   {
                        pos += listPosDest(Board, tempQ).size();
                    }
                    System.out.println("Possbile: " + pos + " Children already existent: " + parent.getChildren().size());
                    */
                    while (!parent.validAmongChildren(newNode) && posDest.size() > 1 && posDest.size() > parent.getChildren().size()) {
                        System.out.println("NOT VALID = " + newNode);

                        ran = (int) (queens.size() * Math.random());
                        origin = queens.get(ran);

                        posDest = listPosDest(Board, origin);
                        ran = (int) (posDest.size() * Math.random());

                        while(posDest.size()==0){
                            System.out.println("NOT VALID  2");
                            ran = (int) (queens.size() * Math.random());
                            origin = queens.get(ran);

                            posDest = listPosDest(Board, origin);
                            ran = (int) (posDest.size() * Math.random());

                        }
                        tar = posDest.get(ran);

                        newNode = new Node(parent, origin, tar);
                        iterations++;
                    }
                }

                return newNode;
            }
            else {System.out.println("EXCEPTION CAUGHT IN TREETRAVERSE - generateRanMove\nOwnMove = " + parent.ownMove + "\tArrowMove = " + parent.arrowMove);}

        }
        else if(parent.ownMove && !parent.arrowMove)    {
            //Because the previous queen destination is where WE are going to shoot an arrow from
            //STARTCASE 2
            //System.out.println("For: " + parent);
            //printBoard();

            returner[0] = parent.getDest();

            //ToDO maybe take out
            int[][] tempBoard = getCopy(getBoard());
            setEmpty(tempBoard, parent.getOrigin());


            posDest = listPosDest(tempBoard, returner[0]);
            int ran = (int) (posDest.size() * Math.random());
            GridCoordinate tar = posDest.get(ran);
            Node newNode = new Node(parent, returner[0], tar);

            //System.out.println(posDest.size())

            while(!parent.validAmongChildren(newNode) && posDest.size() > 1 && posDest.size() > parent.getChildren().size())   {
                ran = (int) (posDest.size() * Math.random());
                tar = posDest.get(ran);
                newNode = new Node(parent, returner[0], tar);
                //System.out.println("Stuck");
            }

            return newNode;
        }
        else if(parent.ownMove && parent.arrowMove)    {
            //Was your own Move and you just shot an arrow so now it's the OPPONENTs turn to MOVE a QUEEN
            //INTERMEDIATE CASE


            //Want to get all posible Queens from opponent
            int queenVal = parent.getOpVal();


            ArrayList<GridCoordinate> queens = posQueens(Board, queenVal);
            GridCoordinate queen;
            //Filter out all queens that can't move anymore

            for(int i = 0; i < queens.size(); i++)  {
                int count = 0;
                queen = queens.get(i);
                posDest = listPosDest(Board, queen);
                count = posDest.size();
                if(count == 0)  {
                    //System.out.println("No options for: " + queen);
                    //Two cases
                    //
                    // 1. i < size - 1
                    // 2. i == size - 1

                    if(i < (queens.size() - 1))  {
                        int j=i;
                        while( j < queens.size() - 1)    {
                            queens.set(j, queens.get(j+1));
                            j++;
                        }
                        /*
                        for(GridCoordinate pos : queens)    {
                            //System.out.println(pos);
                        }
                        System.out.println("Removing: " + queens.get(queens.size() - 1));
                        */
                        queens.remove(queens.size() - 1);
                        //System.out.println("New Size: " + queens.size());
                        i = 0;
                    }else if (i == (queens.size() - 1))   {
                        //System.out.println("REACHED END REMOVING: " + queens.get(queens.size() - 1));
                        queens.remove(queens.size() - 1);
                    }

                }
                //System.out.println("Not getting out 1");

            }
            //System.out.println("Reached");

            if(queens.size() != 0)  {

                //randomly choose queen
                int ran = (int) (queens.size() * Math.random());
                GridCoordinate origin = queens.get(ran);
                removePosMoves(Board);

                //randomly choose destination
                posDest = listPosDest(Board, origin);
                //BoardOperations.printBoard(Board);
                removePosMoves(Board);
                ran = (int) (posDest.size() * Math.random());
                //printBoard();

                while(posDest.size() == 0)  {
                    ran = (int) (queens.size() * Math.random());
                    origin = queens.get(ran);

                    posDest = listPosDest(Board, origin);
                }
                ran = (int) (posDest.size() * Math.random());

                GridCoordinate tar = posDest.get(ran);

                Node newNode = new Node(parent, origin, tar);

                //TODO Temp fix here - if possible, try to solve
                int iterations = 0;
//todo was using print
                // printBoard();

//todo was using print
                /*for(GridCoordinate pos : queens)  {
                  //  System.out.println("Queen: " + pos);
                }
                int pos = 0;
                for(GridCoordinate tempQ: queens)   {
                    pos += listPosDest(Board, tempQ).size();
                }
                System.out.println("Possbile: " + pos + " Children already existent: " + parent.getChildren().size());
*/

                while(!parent.validAmongChildren(newNode) && iterations < 10000)  {
                    ran = (int) (queens.size() * Math.random());
                    origin = queens.get(ran);

                    posDest = listPosDest(Board, origin);
                    ran = (int) (posDest.size() * Math.random());

                    while(posDest.size()==0){
                        ran = (int) (queens.size() * Math.random());
                        origin = queens.get(ran);

                        posDest = listPosDest(Board, origin);
                        ran = (int) (posDest.size() * Math.random());

                    }

                    tar = posDest.get(ran);

                    newNode = new Node(parent, origin, tar);

                    //.out.println("Loop 1");
                    iterations++;
                }

                return newNode;
            }
            else {System.out.println("EXCEPTION CAUGHT IN TREETRAVERSE - generateRanMove\nOwnMove = " + parent.ownMove + "\tArrowMove = " + parent.arrowMove);}
        }
        else    {
            //!parent.ownMove &&  !parent.arrowMove
            //The Opponent just moved and now the Opponent is supposed to shoot an arrow

            returner[0] = parent.getDest();

            //ToDO maybe take out
            int[][] tempBoard = getCopy(getBoard());
            setEmpty(tempBoard, parent.getOrigin());

            posDest = listPosDest(tempBoard, returner[0]);
            int ran = (int) (posDest.size() * Math.random());
            GridCoordinate tar = posDest.get(ran);
            Node newNode = new Node(parent, returner[0], tar);

            while(!parent.validAmongChildren(newNode) && posDest.size() > 1 && posDest.size() > parent.getChildren().size())   {
                ran = (int) (posDest.size() * Math.random());
                tar = posDest.get(ran);
                newNode = new Node(parent, returner[0], tar);
            }

            return newNode;

        }

        return null;
    }

    public void retToRoot()    {
        int[][] newBoard = stringToBoard(root.BoardCompressed);
        currentNode = root;
        for (int i = 0; i < newBoard.length; i++) {
            System.arraycopy(newBoard[i], 0, Board[i], 0, newBoard[i].length);
        }
    }



    //For Arrow shots
    public int countPosOptions(GridCoordinate Origin)    {
        calcPosMoves(Board, Origin, true);
        int count = countPosMove(Board);
        //printBoard();
        removePosMoves(Board);
        return count;
    }

    //Queens
    public int countPosQueenOptions(GridCoordinate Origin)    {
        int count;
        calcPosMoves(Board, Origin, false);
        count = countPosMove(Board);
        removePosMoves(Board);
        return count;
    }

    public int countPosOptions(int queenVal)    {
        ArrayList<GridCoordinate> Queens = posQueens(Board, queenVal);
        int count = 0;
        for(GridCoordinate position: Queens)    {
            calcPosMoves(Board, position, false);
            count += countPosMove(Board);
            removePosMoves(Board);
        }
        return count;
    }

    //For QueenMoves
    public Node createChild(Node parent)   {
        return generateRanMove(parent);
    }

    public void printBoard()    {
        BoardOperations.printBoard(Board);
        //System.out.println("\n");
    }

    public void evaluateChildrenByTer(Node parent)   {

        GridCoordinate origin, dest;
        boolean arrowMove;
        if(parent.getChildren().size() == 0) {
            System.out.println("Exception caught for: " + parent);
        }
        for(Node child: parent.getChildren())   {
            //System.out.println("Perfming for: " + child);
            origin = child.getOrigin();
            dest = child.getDest();
            arrowMove = child.arrowMove;
            performMove(child);
            //System.out.println("For Node: " + child);
            //printBoard();


            if(!child.ownMove)  {
                child.setScore(evaluateTer(Board, child.getOpVal(), false));
            }
            else {
                child.setScore(evaluateTer(Board, child.playerVal, true));
            }
            //System.out.println("Score: " + child.getScore());
            //printBoard();
            //Revert the move
            performMove(child);
            //printBoard();
        }

    }

    public void evaluateChildren(Node parent)   {

        GridCoordinate origin, dest;
        boolean arrowMove;
        if(parent.getChildren().size() == 0) {
            System.out.println("Exception caught for: " + parent);
        }
        for(Node child: parent.getChildren())   {
            //System.out.println("Perfming for: " + child);
            origin = child.getOrigin();
            dest = child.getDest();
            arrowMove = child.arrowMove;
            performMove(child);
            //System.out.println("For Node: " + child);
            //printBoard();


            if(!child.ownMove)  {
                child.setScore(evaluate(Board, child.getOpVal()));
            }
            else {
                child.setScore(evaluate(Board, child.playerVal));
            }
            //System.out.println("Score: " + child.getScore());
            //printBoard();
            //Revert the move
            performMove(child);
            //printBoard();
        }

    }



    protected double c = Math.sqrt(2);

    public double selection(double eval, double n, double t){
        double score;

        score= eval + c* Math.sqrt(Math.log(t)/n);

        return score;
    }
}
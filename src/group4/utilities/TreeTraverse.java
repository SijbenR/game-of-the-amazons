package group4.utilities;

import com.sun.org.apache.xpath.internal.operations.Or;
import group4.tree.Node;
import group4.ui.GridCoordinate;
import org.omg.CORBA.PERSIST_STORE;

import java.util.ArrayList;
import java.util.Stack;

import static group4.utilities.BoardOperations.*;

/**
 * Created by Robins on 06.01.2017.
 *
 * WORK IN PROGRESS - DO NOT USE YET!
 */
public class TreeTraverse {

    private int[][] Board;

    Node currentNode;
    Node root;

    public TreeTraverse(int[][] Board, Node root)   {
        this.root = root;
        currentNode = root;
        this.Board = Board;
    }


    public void performMove(Node Move)   {



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
            for(int i = 0; i < queens.size(); i++)  {
                count = countPosQueenOptions(queens.get(i));
                if(count == 0)  {
                    queens.remove(i);
                    i = 0;
                }
                removePosMoves(Board);
            }

            if(queens.size() != 0)  {

                //randomly choose queen
                int ran = (int) (queens.size() * Math.random());
                GridCoordinate origin = queens.get(ran);
                //TODO WORK ON THIS ONE - IMMOBILIZED QUEENS STILL IN LIST ????
                //randomly choose destination
                posDest = listPosDest(Board, origin);
                ran = (int) (posDest.size() * Math.random());
                GridCoordinate tar = posDest.get(ran);
                //System.out.println("QueenSize: " + queens.size());
                Node newNode = new Node(parent, origin, tar);
                //System.out.println("NewNode: " + newNode);


                if(parent.getChildren().size() > 0) {
                    //parent.getChildren().get(0).printNode();
                    //System.out.println("Size: " + parent.getChildren().size());


                    while (!parent.validAmongChildren(newNode)) {
                        //System.out.println("NOT VALID = " + newNode);

                        ran = (int) (queens.size() * Math.random());
                        origin = queens.get(ran);

                        posDest = listPosDest(Board, origin);
                        ran = (int) (posDest.size() * Math.random());
                        tar = posDest.get(ran);

                        newNode = new Node(parent, origin, tar);
                    }
                }

                return newNode;
            }
            else {System.out.println("EXCEPTION CAUGHT IN TREETRAVERSE - generateRanMove\nOwnMove = " + parent.ownMove + "\tArrowMove = " + parent.arrowMove);}

        }
        else if(parent.ownMove && !parent.arrowMove)    {
            //Because the previous queen destination is where WE are going to shoot an arrow from
            //STARTCASE 2

            returner[0] = parent.getDest();

            posDest = listPosDest(Board, returner[0]);
            int ran = (int) (posDest.size() * Math.random());
            GridCoordinate tar = posDest.get(ran);
            Node newNode = new Node(parent, returner[0], tar);

            while(!parent.validAmongChildren(newNode))   {
                ran = (int) (posDest.size() * Math.random());
                tar = posDest.get(ran);
                newNode = new Node(parent, returner[0], tar);
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
            int count = 0;
            for(int i = 0; i < queens.size(); i++)  {
                queen = queens.get(i);
                count = countPosOptions(queen);
                if(count == 0)  {
                    queens.remove(queen);
                    i = 0;
                }
                removePosMoves(Board);
            }

            if(queens.size() != 0)  {

                //randomly choose queen
                int ran = (int) (queens.size() * Math.random());
                GridCoordinate origin = queens.get(ran);

                //randomly choose destination
                posDest = listPosDest(Board, origin);
                ran = (int) (posDest.size() * Math.random());
                GridCoordinate tar = posDest.get(ran);

                Node newNode = new Node(parent, origin, tar);

                while(!parent.validAmongChildren(newNode))  {
                    ran = (int) (queens.size() * Math.random());
                    origin = queens.get(ran);

                    posDest = listPosDest(Board, origin);
                    ran = (int) (posDest.size() * Math.random());
                    tar = posDest.get(ran);

                    newNode = new Node(parent, origin, tar);
                }

                return newNode;
            }
            else {System.out.println("EXCEPTION CAUGHT IN TREETRAVERSE - generateRanMove\nOwnMove = " + parent.ownMove + "\tArrowMove = " + parent.arrowMove);}
        }
        else    {
            //!parent.ownMove &&  !parent.arrowMove
            //The Opponent just moved and now the Opponent is supposed to shoot an arrow

            returner[0] = parent.getDest();

            posDest = listPosDest(Board, returner[0]);
            int ran = (int) (posDest.size() * Math.random());
            GridCoordinate tar = posDest.get(ran);
            Node newNode = new Node(parent, returner[0], tar);

            while(!parent.validAmongChildren(newNode))   {
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
        printArrayint(Board);
        System.out.println("\n");
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



}

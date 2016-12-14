package group4.tree;


import group4.logic.LogicBoard;

import java.awt.List;
import java.util.ArrayList;

import javax.swing.tree.TreeNode;

public class Tree<T> {
	private Node<T> root;
	
	
public class Node<T>{
	private T data;	
	private int[][] board;
	private double score;
	//score is for a equation determined by machine learning techniques we will imlement later
	private Node<T> parent;
	private boolean player;
	private boolean flag;
	public ArrayList<Node<T>> children;
	private int visits;
	private int wins;
	// the number of wins this node leads to 
	
}

	//this should be in the simulation
	private int numSimulations = 0;

	//traverse the tree(get children), for every node it visits, calculate UCT, return node where UCT got the highest value
	//make arraylist of the nodes, add all the nodes from the tree, traverse the tree, if the node you are at is not in arraylist add it
	//store best node, comparing to prev best node
	//then at best node, make a new move, unless the game is over, expand, , chose random(or a more optimal) move, add it to tree as a node
	//node should hold board state, every 2nd node is for opponent? simulate for opponent, chose random/optimal move, win/lose update nodes
	//backpropagation, repeat

	//should probably have this somewhere else than the tree class

	private Node<T> bestNode;
	//selection with post order traverse
	public Node<T> selection(Node<T> child){
		double bestValue = 0;

		for(Node<T> each : child.children){
			if(UCT(child, Math.sqrt(2)) > bestValue)
				bestNode = child;

			selection(each);
		}
		return bestNode;
	}

	//need expand(new child), simulate(random or more optimal move), backpropagation (update previous nodes)

	//need simulation for numSimulation
	//exploration value normally sqr 2, can try with more different values next period
	public double UCT(Node<T> thisNode, double explorationConstant){
		double value;
		value = (thisNode.wins/ numSimulations)+explorationConstant*Math.sqrt(Math.log(thisNode.visits)/numSimulations);

		return value;
	}

	//end of mcts stuff

public Tree(T rootData) {
    root = new Node<T>();
    root.data = rootData;
    root.children = new ArrayList<Node<T>>();
}	
public void AddChild(Node<T> child, Node<T> parent,boolean white, boolean flag){
	if(root==null)
		root= (Node<T>) child;
	else{
		parent.children.add(parent.children.size()+1, child);

		//Possible way without static?
		//child.board= LogicBoard.getBoard();



		// this needs to be changed to our new ways
		child.parent= parent;
		child.visits=0; //this gets updated each time the node is visited
		child.wins=0; //this gets updated each time the game ends
		
		//TODO
		child.player= true; //true for white, false for black
		child.flag = true; // true for flag move, false for queen move 
		//child.score= Algorithm.setscore; //need to implement our scoring system to add this
		
	}
	}
	
public void visitNode(Node<T> Node){
		Node.visits++;
	}
	
public void Nodelead2Win(Node<T> Node){
		Node.wins++;
	}
public void RemoveChild(Node<T> child ){
	Node<T> parent= child.parent;
	
	for(int i=0; i<parent.children.size(); i++){
		if (parent.children.get(i)== child)
			parent.children.remove(i);
	}
	child.board= null;
	child.parent= null;
	child.visits=(Integer) null; 
	child.wins=(Integer) null; 
	child.player= (Boolean) null; 
	child.flag = (Boolean) null; 
	
}
		
}




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

public Tree(T rootData) {
    root = new Node<T>();
    root.data = rootData;
    root.children = new ArrayList<Node<T>>();
}	
public void AddChild(Node<T> child, Node<T> parent){
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




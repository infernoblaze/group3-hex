package Players;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import Game.Board;
import Game.Game;

public class MonteCarloPlayer implements Player {

	private Game game;
	private int playerId;
	
	private int boardDimensions;
	
	private ArrayList<Node> unsimulatedNodes;
	private ArrayList<Node> unpropagatedNodes;
		
	public int[] getNextMove()
	{
		Board board = game.getBoard();
		boardDimensions = board.getDimensions();
		
		unsimulatedNodes = new ArrayList<Node>();
		unpropagatedNodes = new ArrayList<Node>();
		
		// 1. Selection
		// 2. Expansion
		// 3. Simulation
		// 4. Backpropagation
		
		Node root = new Node(board.clone());
		
		expandAll(root, playerId);

		simulate();		
		backpropagate();

		for (Node node : root.children)
			expandRandomely(node, 5, getOpponent(node.lastPlayer));
				
		simulate();		
		backpropagate();
		
		for (int i = 0; i < 20; i++) {
			Collections.sort(root.children, Node.getValueComparator());
			
			int j = 0;
			for (Node node : root.children) {
				if (j > 15) break;
				
				expandRandomely(node, 1, getOpponent(node.lastPlayer));
				
				for (Node subNode : node.children) {
					expandRandomely(subNode, 1, getOpponent(subNode.lastPlayer));
				}
				
				j++;
			}
					
			simulate();	
			backpropagate();
			
		}
		
		int[] nextMove = {-2, -2};
		double maxValue = Double.NEGATIVE_INFINITY;
		
		for (Node node : root.children) {
			int[] lastMove = node.board.getLastPiece();
			
//			System.out.println("("+lastMove[0]+","+lastMove[1]+"): " + node.value);
		
			if (node.value > maxValue) {
				nextMove = node.board.getLastPiece();
				maxValue = node.value;
			}
		}
		
		return nextMove;
	}
		
	private void expandAll(Node node, int player) {
		ArrayList<Integer[]> blankFields = getBlankFields(node.board);
				
		for (Integer[] nextMove : blankFields) {
			Board childBoard = node.board.clone();			
			childBoard.setPiece(nextMove[0].intValue(), nextMove[1].intValue(), player);
			
			Node childNode = new Node(childBoard, player);
			childNode.parent = node;
			node.children.add(childNode);	
			
			unsimulatedNodes.add(childNode);
		}
	}
	
	private void expandRandomely(Node node, int numberOfChildren, int player) {
		ArrayList<Integer[]> blankFields = getBlankFields(node.board);
		numberOfChildren = numberOfChildren < blankFields.size() ? numberOfChildren : blankFields.size();
		
		for (int i = 0; i < numberOfChildren; i++) {
			int r = (int) (Math.random() * blankFields.size());
			Integer[] nextMove = blankFields.get(r);
			blankFields.remove(r);

			Board childBoard = node.board.clone();			
			childBoard.setPiece(nextMove[0].intValue(), nextMove[1].intValue(), player);
			
			Node childNode = new Node(childBoard, player);
			childNode.parent = node;
			node.children.add(childNode);		
			
			unsimulatedNodes.add(childNode);
		}
	}

	private void simulate() {
		int perfectPiceCount = game.getBoard().getPieceCount() + 1;
		
		for (Node node : unsimulatedNodes) {
			if (!node.children.isEmpty())
				continue;
			
			Board simulatedBoard = node.board.clone();
			int end[] = simulateGame(simulatedBoard, node.lastPlayer);
			if (end[0] == playerId)
				if (end[1] == perfectPiceCount) {
					node.value += Float.POSITIVE_INFINITY;
					unpropagatedNodes.add(node);
					break;
				}
				else
					node.value += 1.0f;
			else
				node.value += -1.0f;
			
			unpropagatedNodes.add(node);
		}
		
		unsimulatedNodes.clear();
	}
	
	private int[] simulateGame(Board board, int player) {
		int winner = board.checkEnd();
		if (winner != 0)
			return new int[] {winner, board.getPieceCount()};
		
		ArrayList<Integer[]> blankFields = getBlankFields(board);
		
		int r = (int) (Math.random() * blankFields.size());
		Integer[] nextMove = blankFields.get(r);
		
		int opponent = getOpponent(player);
		
		board.setPiece(nextMove[0].intValue(), nextMove[1].intValue(), opponent);

		return simulateGame(board, opponent);
	}
	
	private void backpropagate() {
		for (Node node : unpropagatedNodes) {
			if (!node.children.isEmpty())
				continue;
			
			backpropagateNode(node, node.value);
		}
		
		unpropagatedNodes.clear();
	}
	
	private void backpropagateNode(Node node, float value) {
		if (node.parent == null)
			return;
		
		node.parent.value += value;
		
		backpropagateNode(node.parent, value);
	}
	
	private ArrayList<Integer[]> getBlankFields(Board board) {
		ArrayList<Integer[]> blankFields = new ArrayList<Integer[]>();
		for (int i = 0; i < boardDimensions; i++)
			for (int j = 0; j < boardDimensions; j++)
				if (board.getField(i, j) == 0)
				{
					blankFields.add(new Integer[] {i, j});
				}

		return blankFields;
	}
	
	private int getOpponent(int aPlayer) {
		return (aPlayer == Game.PLAYER_ONE ? Game.PLAYER_TWO : Game.PLAYER_ONE);
	}
	
	public void setGame(Game theGame)
	{
		this.game = theGame;
	}
	
	public void setPlayerId(int thePlayer)
	{
		this.playerId = thePlayer;
	}
	
	public int getPlayerId()
	{
		return this.playerId;
	}
	
	private static class Node {
		public Node parent;
		public ArrayList<Node> children;
		public Board board;
		public float value;
		public int lastPlayer;

		public Node(Board board) {
			children = new ArrayList<Node>();
			this.board = board;
		}

		public Node(Board board, int lastPlayer) {
			children = new ArrayList<Node>();
			this.board = board;
			this.lastPlayer = lastPlayer;
		}
		
		public final static Comparator<Node> getValueComparator() {
			return new Comparator<Node>() {
				public int compare(Node a, Node b) {
					return (int)(b.value - a.value);
				}
			};
		}
	}

	public void setCanSwapSides(boolean state) {
	}
	
}

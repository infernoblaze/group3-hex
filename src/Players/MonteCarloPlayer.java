package Players;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import Game.Board;
import Game.Game;
import Game.LiteBoard;

public class MonteCarloPlayer implements Player {

	private Game game;
	private int playerId;
	
	private int boardDimensions;
	
	private ArrayList<Node> unsimulatedNodes, unpropagatedNodes;
	
	private float UCTCoeficient;
	private int timeOut;
	
	private boolean hasAWinner;
	
	public MonteCarloPlayer() {
		this(0.2f, 2000);
	}
	
	public MonteCarloPlayer(float aUCTCoeficient, int aTimeOut) {
		UCTCoeficient = aUCTCoeficient;
		timeOut = aTimeOut;
	}
	
	public int[] getNextMove()
	{
		LiteBoard board = game.getBoard().getLiteBoard();
		boardDimensions = board.getDimensions();
		
		unsimulatedNodes = new ArrayList<Node>();
		unpropagatedNodes = new ArrayList<Node>();
		
		hasAWinner = false;
		
		// 1. Selection
		// 2. Expansion
		// 3. Simulation
		// 4. Backpropagation
		
		Node root = new Node(board.clone());

		long startTime = System.currentTimeMillis();
		
		expandAll(root, playerId);

		simulate();		
		backpropagate();

		int stepCounter = 0;
		while (!hasAWinner) {
			if (stepCounter > 10) {
				stepCounter = 0;
				if (System.currentTimeMillis() - startTime > timeOut) {
					break;
				}
			}
			
			Node selectedNode = select(root);
			expandRandomely(selectedNode, 10, getOpponent(selectedNode.lastPlayer));
			expandAll(selectedNode, getOpponent(selectedNode.lastPlayer));
			
			simulate();
			backpropagate();
			
			stepCounter++;
		}
				
		Node bestNode = null;
		float maxWinRatio = Float.NEGATIVE_INFINITY;
		
		for (Node node : root.children) {
			float winRatio = node.wins / (float)(node.wins + node.loses);
			
			System.out.println(Arrays.toString(node.board.getLastPiece()) + ": " + winRatio + " (" + node.wins + "/" + node.loses + ") visits: " + node.visits + " UCTValue: " + calculateUCTValue(node));
			
			if (node.value > 1000000) {
				bestNode = node;
				break;
			}
			
			if (winRatio > maxWinRatio) {
				bestNode = node;
				maxWinRatio = winRatio;
			}
		}
		
		System.out.println("\n");
		
		return bestNode.board.getLastPiece();
	}
	
	private Node select(Node rootNode) {
		Node selectedNode = rootNode;
		
		while (!selectedNode.children.isEmpty()) {
			float maxUCTValue = Float.NEGATIVE_INFINITY;
			Node bestNode = null;
			
			for (Node childNode : selectedNode.children) {
				Float UCTValue = calculateUCTValue(childNode);
				if (UCTValue > maxUCTValue) {
					maxUCTValue = UCTValue;
					bestNode = childNode;
				}
			}

			selectedNode = bestNode;
		}
		
		return selectedNode;
	}
	
	private float calculateUCTValue(Node node) {
		return node.wins / (node.wins + node.loses) +
				UCTCoeficient * (float)Math.sqrt(Math.log(node.parent.visits) / node.visits); 
	}
	
	private void expandAll(Node node, int player) {
//		System.out.println("Expanding all.");
		
		ArrayList<Integer[]> blankFields = getBlankFields(node.board);
				
		for (Integer[] nextMove : blankFields) {
			LiteBoard childBoard = node.board.clone();			
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
		
		boolean nodeHasNoChildren = node.children.isEmpty();
		
		for (int i = 0; i < numberOfChildren; i++) {
			int r = (int) (Math.random() * blankFields.size());
			Integer[] nextMove = blankFields.get(r);
			blankFields.remove(r);

			Node newNode = null;
			
			if (!nodeHasNoChildren) {
				for (Node childrenNode : node.children) {
					int[] lastPiece = childrenNode.board.getLastPiece();
					if (lastPiece[0] == nextMove[0].intValue() &&
							lastPiece[1] == nextMove[1].intValue()) {
						newNode = childrenNode;
//						System.out.println("Found an existing node");
						break;
					}
				}
			}
			
			if (newNode == null) {
				LiteBoard newBoard = node.board.clone();
				newBoard.setPiece(nextMove[0].intValue(), nextMove[1].intValue(), player);
				
				newNode = new Node(newBoard, player);
				newNode.parent = node;
				node.children.add(newNode);
			}
			
			unsimulatedNodes.add(newNode);
		}

//		if (boardDimensions * boardDimensions - node.board.getPieceCount() < node.children.size() * 2) {
//			expandAll(node, player);
//		}
	}

	private void simulate() {
		int perfectPiceCount = game.getBoard().getPieceCount() + 1;
		
		for (Node node : unsimulatedNodes) {
			if (!node.children.isEmpty())
				continue;
			
			LiteBoard simulatedBoard = node.board.clone();
			int end[] = simulateGame(simulatedBoard, node.lastPlayer);
			if (end[0] == playerId)
				if (end[1] == perfectPiceCount) {
					node.value += Float.POSITIVE_INFINITY;
					unpropagatedNodes.add(node);
					hasAWinner = true;
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
	
	private int[] simulateGame(LiteBoard board, int player) {
		if (board.getPieceCount() > board.getDimensions() * 2 - 2) {
			int winner = LiteBoard.checkEnd(board);
			if (winner != 0)
				return new int[] {winner, board.getPieceCount()};
		}
		
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
		node.visits++;
		if (value > 0)
			node.wins++;
		else
			node.loses++;
		
		if (node.parent == null)
			return;
		
		node.parent.value += value;
		
		backpropagateNode(node.parent, value);
	}
	
	private ArrayList<Integer[]> getBlankFields(LiteBoard board) {
		ArrayList<Integer[]> blankFields = new ArrayList<Integer[]>();
		for (int i = 0; i < boardDimensions; i++)
			for (int j = 0; j < boardDimensions; j++)
				if (board.getPiece(i, j) == 0)
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
		public LiteBoard board;
		public float value;
		public int visits, wins, loses;
		public int lastPlayer;

		public Node(LiteBoard board) {
			children = new ArrayList<Node>();
			this.board = board;
		}

		public Node(LiteBoard board, int lastPlayer) {
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

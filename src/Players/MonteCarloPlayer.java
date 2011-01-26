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
	private boolean canSwapSides, swappingAllowed, oponentMightSwap, threadedSimulations;
	
	private ArrayList<Node> unsimulatedNodes, unpropagatedNodes;
	
	private float UCTCoeficient;
	private int timeout, countout;
	
	private boolean hasAWinner;
	
	/**
	 * Sets up the default configuration of Monte-Carlo
	 */
	public MonteCarloPlayer() {
		this(1.0f, 4000, 10000, true);
	}
	
	/**
	 * Constructor for Monte-Carlo Tree Search player
	 * @param aUCTCoeficient The coefficient for UCT value
	 * @param aTimeout Maximum time of tree expansion
	 * @param aCountout Maximum node count of node visits
	 * @param swapping Enables ability to swap board
	 */
	public MonteCarloPlayer(float aUCTCoeficient, int aTimeout, int aCountout, boolean swapping) {
		UCTCoeficient = aUCTCoeficient;
		timeout = aTimeout;
		countout = aCountout;
		this.swappingAllowed = swapping;
	}
	
	public int[] getNextMove()
	{
		LiteBoard board = game.getBoard().getLiteBoard();
		boardDimensions = board.getDimensions();
		
		unsimulatedNodes = new ArrayList<Node>();
		unpropagatedNodes = new ArrayList<Node>();
		
		hasAWinner = false;
		totalSimulationTime = 0;
		
		if (swappingAllowed && board.getPieceCount() == 0) {
			oponentMightSwap = true;
		} else {
			oponentMightSwap = false;
		}
		
		// 1. Selection
		// 2. Expansion
		// 3. Simulation
		// 4. Back-propagation
		
		Node root = new Node(board.clone());
		root.blankFields = getBlankFields(board);

		long startTime = System.currentTimeMillis();
		
		expandAll(root, playerId);
		
		Node swappedNode = null;
		if (canSwapSides) {
			Board swappedClumsyBoard = game.getBoard().clone();
			swappedClumsyBoard.swapSides();
			LiteBoard swappedBoard = swappedClumsyBoard.getLiteBoard();
			swappedNode = new Node(swappedBoard, playerId);
			swappedNode.parent = root;
			swappedNode.blankFields = getBlankFields(swappedBoard);
			root.children.add(swappedNode);
			unsimulatedNodes.add(swappedNode);
		}

		simulate();		
		backpropagate();

		int stepCounter = 0;
		while (!hasAWinner) {
			if (stepCounter > 10) {
				stepCounter = 0;
				if (System.currentTimeMillis() - startTime > timeout || root.visits > countout) {
					break;
				}
			}
			
			Node selectedNode = select(root);
			expandRandomly(selectedNode, 10, getOpponent(selectedNode.lastPlayer));
			expandAll(selectedNode, getOpponent(selectedNode.lastPlayer));
			
			simulate();
			backpropagate();
			
			stepCounter++;
		}
				
		Node bestNode = null;
		float maxWinRatio = Float.NEGATIVE_INFINITY;
		
		for (Node node : root.children) {
			float winRatio = node.wins / (float)(node.wins + node.loses);
			
//			System.out.println(Arrays.toString(node.board.getLastPiece()) + ": " + winRatio + " (" + node.wins + "/" + node.loses + ") visits: " + node.visits + " UCTValue: " + calculateUCTValue(node));
			
			if (node.value > 1000000) {
				bestNode = node;
				break;
			}
			
			if (winRatio > maxWinRatio) {
				bestNode = node;
				maxWinRatio = winRatio;
			}
		}
		
		System.out.println("Approximate time spent while simulating: " + totalSimulationTime);
		System.out.println("Total visits: " + root.visits + "\n");
		
		int[] nextMove = bestNode.board.getLastPiece();
		
		if (canSwapSides && bestNode == swappedNode)
			nextMove = new int[] {-1, -1};
		
		return nextMove;
	}
	
	/**
	 * Selects a node based on UCT value
	 * @param rootNode Node to start search with
	 * @return The best leaf node
	 */
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
	
	/**
	 * Calculates the UCT value for a node
	 * @param node The node to get evaluated
	 * @return Calculated UCT vcore
	 */
	private float calculateUCTValue(Node node) {
		return node.wins / (node.wins + node.loses) +
				UCTCoeficient * (float)Math.sqrt(Math.log(node.parent.visits) / node.visits); 
	}
	
	/**
	 * Expands all children nodes of the node
	 * @param node The node to get expanded
	 * @param player The player which turn it is
	 */
	private void expandAll(Node node, int player) {
//		System.out.println("Expanding all.");
		
		for (int[] nextMove : node.blankFields) {
			LiteBoard newBoard = node.board.clone();			
			newBoard.setPiece(nextMove[0], nextMove[1], player);
			
			Node newNode = new Node(newBoard, player);
			newNode.parent = node;
			newNode.blankFields = getBlankFields(newBoard);
			node.children.add(newNode);	
			
			unsimulatedNodes.add(newNode);
		}
		
		if (oponentMightSwap && node.board.getPieceCount() == 1 && player == getOpponent(playerId)) {
			LiteBoard swappedBoard = new LiteBoard(boardDimensions);
			
			int[] lastPiece = node.board.getLastPiece();
			swappedBoard.setPiece(lastPiece[0], lastPiece[1], getOpponent(playerId));
			
			Node swappedNode = new Node(swappedBoard, getOpponent(playerId));
			swappedNode.parent = node;
			swappedNode.blankFields = getBlankFields(swappedBoard);
			node.children.add(swappedNode);
			unsimulatedNodes.add(swappedNode);
		}
		
	}
	
	/**
	 * Expands number of node's children 
	 * @param node The node to expand
	 * @param numberOfChildren The number of random children expanded
	 * @param player The player which turn it is
	 */
	private void expandRandomly(Node node, int numberOfChildren, int player) {
		int[][] blankFields = node.blankFields;
		numberOfChildren = numberOfChildren < blankFields.length ? numberOfChildren : blankFields.length;
		
		boolean nodeHasNoChildren = node.children.isEmpty();
		
		for (int i = 0; i < numberOfChildren; i++) {
			int r = (int) (Math.random() * blankFields.length);
			int[] nextMove = blankFields[r];

			Node newNode = null;
			
			if (!nodeHasNoChildren) {
				for (Node childrenNode : node.children) {
					int[] lastPiece = childrenNode.board.getLastPiece();
					if (lastPiece[0] == nextMove[0] &&
							lastPiece[1] == nextMove[1]) {
						newNode = childrenNode;
//						System.out.println("Found an existing node");
						break;
					}
				}
			}
			
			if (newNode == null) {
				LiteBoard newBoard = node.board.clone();
				newBoard.setPiece(nextMove[0], nextMove[1], player);
				
				newNode = new Node(newBoard, player);
				newNode.parent = node;
				newNode.blankFields = getBlankFields(newBoard);
				node.children.add(newNode);
			}
			
			unsimulatedNodes.add(newNode);
		}

	}

	private long totalSimulationTime;
	
	/**
	 * Runs simulations for all recently expanded nodes
	 */
	private void simulate() {
		long startTime = System.nanoTime();
		
		int perfectPieceCount = game.getBoard().getPieceCount() + 1;
		
		for (Node node : unsimulatedNodes) {
//			if (!node.children.isEmpty())
//			continue;
			
			LiteBoard simulatedBoard = node.board.clone();
			int end[] = simulateGame(simulatedBoard, node.lastPlayer);
			if (end[0] == playerId)
				if (end[1] == perfectPieceCount) {
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
		
		totalSimulationTime += System.nanoTime() - startTime;
	}
	
	/**
	 * Recursively simulates a random game
	 * @param board The board of the random game
	 * @param player The player which turn it is
	 * @return Array of the winner and the piece count on the board at winning situation
	 */
	private int[] simulateGame(LiteBoard board, int player) {
		if (board.getPieceCount() > board.getDimensions() * 2 - 2) {
			int winner = LiteBoard.checkEnd(board);
			if (winner != 0)
				return new int[] {winner, board.getPieceCount()};
		}
		
		int[][] blankFields = getBlankFields(board);
		
		int r = (int) (Math.random() * blankFields.length);
		int[] nextMove = blankFields[r];
		
		int opponent = getOpponent(player);
		
		board.setPiece(nextMove[0], nextMove[1], opponent);

		return simulateGame(board, opponent);
	}
	
	/**
	 * Back-propagates the results of recently simulated games
	 */
	private void backpropagate() {
		for (Node node : unpropagatedNodes) {
			if (!node.children.isEmpty())
				continue;
			
			backpropagateNode(node, node.value);
		}
		
		unpropagatedNodes.clear();
	}
	
	/**
	 * Recursively back-propagates a node
	 * @param node The node that is back-propagated
	 * @param value The value
	 */
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
	
	/**
	 * Returns all possible moves of the board
	 * @param board The board to be examined
	 * @return An array of all possible moves
	 */
	private int[][] getBlankFields(LiteBoard board) {
		int[][] blankFields = new int[boardDimensions * boardDimensions - board.getPieceCount()][2];
		int field = 0;
		for (int i = 0; i < boardDimensions; i++) {
			for (int j = 0; j < boardDimensions; j++) {
				if (board.getPiece(i, j) == 0) {
					blankFields[field][0] = i;
					blankFields[field][1] = j;
					field++;
				}
			}
		}
		
		return blankFields;
	}
	
	/**
	 * Return the opponent player
	 * @param aPlayer The player
	 * @return The opponent player
	 */
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
	
	/**
	 * A class that represents a node 
	 */
	private static class Node {
		public Node parent;
		public ArrayList<Node> children;
		public LiteBoard board;
		public float value;
		public int visits, wins, loses;
		public int lastPlayer;
		public int[][] blankFields;

		public Node(LiteBoard board) {
			children = new ArrayList<Node>();
			this.board = board;
		}

		public Node(LiteBoard board, int lastPlayer) {
			children = new ArrayList<Node>();
			this.board = board;
			this.lastPlayer = lastPlayer;
		}
	}

	public void setCanSwapSides(boolean state) {
		if (swappingAllowed)
			canSwapSides = state;			
		else
			canSwapSides = false;
	}
	
}

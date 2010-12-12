package Players;

import java.util.ArrayList;

import Game.Board;
import Game.Game;

public class MonteCarloPlayer implements Player
{

	private Game game;
	private int playerId;

	private int boardDimensions;

	public int[] getNextMove()
	{
		System.out.println("Lalala, Monte Carlo, pumpurum.");

		Board board = game.getBoard();
		boardDimensions = board.getDimensions();

		TreeLeaf top = new TreeLeaf(board.clone());

		int otherPlayer = (playerId == Game.PLAYER_ONE ? Game.PLAYER_TWO : Game.PLAYER_ONE);
		populateTree(top, top.board, otherPlayer, 1);

		int[] nextMove = {-2, -2};
		double maxValue = Double.NEGATIVE_INFINITY;

		for (TreeLeaf leaf : top.children) {
			int[] lastMove = leaf.board.getLastPiece();

			if (leaf.value > maxValue) {
				nextMove = lastMove;
				maxValue = leaf.value;
			}
		}

		return nextMove;
	}

	private double populateTree(TreeLeaf leaf, Board board, int player, int depth)
	{

		if (board.getDimensions() * 2 <= board.getPieceCount()) {
			int endState = board.checkEnd();
			if (endState != 0)
			{
				if (endState == playerId) {
					if (depth == 2)
					{
						return 100.0;
					}
					else
						return 1.0 / depth;
				}
				else
				{
					return -1.0 / depth;
				}
			}
		}

		int branchCount = (depth == 1 ? 30 : (depth == 2 ? 30 : (depth == 3 ? 5 : 1)));

		ArrayList<Integer[]> blankFields = new ArrayList<Integer[]>();
		for (int i = 0; i < boardDimensions; i++)
			for (int j = 0; j < boardDimensions; j++)
				if (board.getField(i, j) == 0)
				{
					blankFields.add(new Integer[] {i, j});
				}

		int otherPlayer = (player == Game.PLAYER_ONE ? Game.PLAYER_TWO : Game.PLAYER_ONE);

		double sum = 0.0;

		for (int i = 0; i < branchCount && !blankFields.isEmpty(); i++)
		{
			Board childBoard;
			TreeLeaf childLeaf;

			if (branchCount == 1)
				childBoard = board;
			else
				childBoard = board.clone();

			int r = (int) (Math.random() * blankFields.size());
			Integer[] nextMove = blankFields.get(r);
			blankFields.remove(r);
			childBoard.setPiece(nextMove[0].intValue(), nextMove[1].intValue(), otherPlayer);

			if (leaf == null || depth > 3)
				childLeaf = null;
			else {
				childLeaf = new TreeLeaf(childBoard);
				childLeaf.parent = leaf;
				leaf.children.add(childLeaf);
			}

			double childSum = populateTree(childLeaf, childBoard, otherPlayer, depth + 1);

			if (childLeaf != null)
				childLeaf.value = childSum;

			sum = sum + childSum;
		}

		if (leaf != null)
			leaf.value = sum;

		return sum;
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

	private class TreeLeaf {
		public TreeLeaf parent;
		public ArrayList<TreeLeaf> children;
		public Board board;
		public double value;

		public TreeLeaf(Board board) {
			children = new ArrayList<TreeLeaf>();
			this.board = board;
		}
	}

}

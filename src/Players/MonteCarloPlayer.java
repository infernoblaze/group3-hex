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
		
		System.out.println("Gonna look for a move the Monte Carlo way, bi@tch.");
		
		Board board = game.getBoard();
		boardDimensions = board.getDimensions();
		
		TreeLeaf top = new TreeLeaf(board.clone());
		
		int otherPlayer = (playerId == Game.PLAYER_ONE ? Game.PLAYER_TWO : Game.PLAYER_ONE);
		populateTree(top, otherPlayer, 1);
		
		int[] nextMove = {-2, -2};
		double maxValue = -10000000000000000000000.0;
		
		for (TreeLeaf leaf : top.children) {
			int[] lastMove = leaf.leaf.getLastPiece();
			System.out.println(leaf.value + " at (" + lastMove[0] + "," + lastMove[1] + ")");
			if (leaf.value > maxValue) {
				nextMove = lastMove;
				maxValue = leaf.value;
			}
		}
		
		return nextMove;
	}
	
	public double populateTree(TreeLeaf parentLeaf, int player, int depth)
	{
		Board board = parentLeaf.leaf;
		
//		if (board.getDimensions() * 2 < board.getPieceCount()) {
			int endState = board.checkEnd();
			if (endState == playerId) {
				return 1.0 / depth;
			} else if (endState != 0) {
				return -1.0 / depth;
			}
//		}
				
		int branchCount = (depth == 1 ? 36 : (depth == 2 ? 10 : (depth == 3 ? 2 : 1)));
		
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
			int r = (int) (Math.random() * blankFields.size());
			Integer[] nextMove = blankFields.get(r);
			blankFields.remove(r);
			Board childBoard = board.clone();
			childBoard.setPiece(nextMove[0].intValue(), nextMove[1].intValue(), player);
			
			TreeLeaf childLeaf = new TreeLeaf(childBoard);
			childLeaf.parent = parentLeaf;
			parentLeaf.children.add(childLeaf);
			
			sum = sum + populateTree(childLeaf, otherPlayer, depth + 1);
		}
		
		parentLeaf.value = sum;
		
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
		public Board leaf;
		public ArrayList<TreeLeaf> children;
		public double value;
		
		public TreeLeaf(Board leaf) {
			children = new ArrayList<TreeLeaf>();
			this.leaf = leaf;
		}
	}
	
}

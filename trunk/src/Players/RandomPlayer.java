package Players;

import java.util.ArrayList;

import Game.Board;
import Game.Game;

public class RandomPlayer implements Player {

	private Game game;
	private int playerId;
	
	public int[] getNextMove()
	{
		ArrayList<Integer[]> blankFields = new ArrayList<Integer[]>();
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}
		
		Board board = game.getBoard();
		
		for (int i = 0; i < board.getDimensions(); i++)
			for (int j = 0; j < board.getDimensions(); j++)
				if (board.getField(i, j) == 0)
				{
					Integer[] field = {i, j};
					blankFields.add(field);
				}
		
		Integer[] nextMove = blankFields.get((int) (Math.random() * blankFields.size()));
		return new int[] { nextMove[0].intValue(), nextMove[1].intValue() };
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

	@Override
	public void setCanSwapSides(boolean state) {
		// TODO Auto-generated method stub
		
	}

}

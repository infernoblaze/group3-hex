package Players;

import Game.Game;

/**
 * Interface for all the AIs. 
 *
 */
public interface Player {
	
	/**
	 * Specifies the game board that the AI will use to calculate it's next
	 * move. This should be set once by the game when it is started. 
	 * @param aBoard the board of game
	 */
	public void setGame(Game theGame);
	
	/**
	 * Specifies the player that this AI is playing. Like Game.PLAYER_ONE
	 * @param thePlayer the player id
	 */
	public void setPlayerId(int thePlayer);
	
	/**
	 * Returns the player identifier
	 * @return numeric player id
	 */
	public int getPlayerId();
	
	public void setCanSwapSides(boolean state);
	
	/**
	 * Game should call this method whenever it needs a new "move" from the AI
	 * player. Then the AI should do all the things that it needs to do and
	 * return x,y coordinates of the new piece.
	 * @return array of two integers for x,y coordinates
	 */
	public int[] getNextMove();
	
}

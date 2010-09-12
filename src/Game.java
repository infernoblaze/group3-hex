/**
 * This class is a controller for the game. Please keep it simple and think
 * twice before writing something. Comments are always appreciated and don't
 * forget JavaDocs too. Cheers and good luck!
 *
 */
public class Game {
	
	/**
	 * Initializes a game and sets the board. (In future AIs will be specified
	 * here too.)
	 * @param theBoard the board of the game
	 */
	public Game(Board theBoard) {
		
	}

	/**
	 * Check whether there is a board and AI set up if there are any and start a
	 * game. If everything is fine return true, otherwise false. 
	 * @return true if the game is successfully started, false otherwise
	 */
	public boolean start() {
		return true;
	}
	
	/**
	 * Checks whether there is a path that connects two sides of the board. In
	 * that case the game ends. 
	 * @return true if there are, false if not.
	 */
	public boolean checkEnd() {
		return false;
	}
	
	/**
	 * A constant that represents player one
	 */
	static public final int PLAYER_ONE = 1;
	
	/**
	 * A constant the represents player two; 
	 */
	static public final int PLAYER_TWO = 2;
}

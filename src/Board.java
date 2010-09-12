/**
 * This class represents a game board. Keep it simple.
 * 
 */
public class Board {
	
	/**
	 * Creates a new board with specified dimensions and specifies which sides
	 * belong to which player.
	 * @param dimensions the size of the board
	 */
	public Board(int dimensions) {
		
	}
	
	/**
	 * Puts a piece on the hex board.
	 * @param x the x coordinate on the board
	 * @param y the y coordinate on the board
	 * @param player the player the piece belongs to
	 * @return true if inside bounds and no other piece is already at that
	 * place. 
	 */
	public boolean setPiece(int x, int y, int player) {
		return true;
	}
	
	/**
	 * Swaps the pieces of the players if there's only one piece on the board. 
	 * @return true is there's only piece on the board, false otherwise.
	 */
	public boolean swapSides() {
		return true;
	}
	
	/**
	 * Returns the size of the board.
	 * @return the size of one side of the board
	 */
	public int getDimensions() {
		return 0;
	}
	
	/**
	 * Returns the player id that the piece belongs to or -1 if the field is
	 * empty.
	 * @param x the x coordinate on the board
	 * @param y the y coordinate on the board
	 * @return returns player id of the piece or -1 if the field is blank 
	 */
	public int getField(int x, int y) {
		return -1;
	}
	
	// TODO: How do we determine which side belongs to which player?
	
}

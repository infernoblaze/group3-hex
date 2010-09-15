/**
 * This class represents a game board. Keep it simple.
 * 
 */
public class Board 
{
	private int x, counter;
	private Cell[][] board;
	private Cell border;
	
	/**
	 * Creates a new board with specified dimensions.
	 * border cell is used to determine when cell's link is a border.
	 * border cells are in included in the main array.	 * 
	 * @param dimensions the size of the board
	 */
	public Board(int dimensions) 
	{
		x = dimensions;		
		board = new Cell[x][x];		
		border = new Cell(3);

		for (int i = 0; i < board.length; i++)
		{
			for (int j = 0; j < board[i].length; j++) 
			{
				board[i][j] = new Cell(0);				
				int a = j-1;
				int b = i-1;
				int c = j+1;
				int d = i+1;
				
				if(a < 0 || b < 0 || c > board.length - 1 || d > board.length - 1)
				{
					board [i][j].aCell = border;
				}
				
				else
				{
					board [i][j].aCell = board[i+1][j-1];
				}
				
				if(a < 0 || b < 0 || c > board.length - 1 || d > board.length - 1)
				{
					board [i][j].bCell = border;
				}
				
				else
				{
					board [i][j].bCell = board[i+1][j];
				}
				
				if(a < 0 || b < 0 || c > board.length - 1 || d > board.length - 1)
				{
					board [i][j].cCell = border;
				}
				
				else
				{
					board [i][j].cCell = board[i][j+1];
				}
				
				if(a < 0 || b < 0 || c > board.length - 1 || d > board.length - 1)
				{
					board [i][j].dCell = border;
				}
				
				else
				{
					board [i][j].dCell = board[i-1][j+1];
				}
				
				if(a < 0 || b < 0 || c > board.length - 1 || d > board.length - 1)
				{
					board [i][j].eCell = border;
				}
				
				else
				{
					board [i][j].eCell = board[i-1][j];
				}
				
				if(a < 0 || b < 0 || c > board.length - 1 || d > board.length - 1)
				{
					board [i][j].fCell = border;
				}
				
				else
				{
					board [i][j].fCell = board[i-1][j-1];
				}				
			}
		}		
	}
	
	/**
	 * class for the cells of the board.
	 * a cell knows who is next to it and if there's a stone from player 1 or 2 or if its
	 * empty.
	 * 0 = cell is empty
	 * 1 = stone from player 1
	 * 2 = stone from player 2
	 * 3 = border
	 */
	static class Cell
	{
		Cell aCell;
		Cell bCell;
		Cell cCell;
		Cell dCell;
		Cell eCell;
		Cell fCell;
		
		int value;
		
		public Cell(int value)
		{
			this.value = value;
		}
	}
	
	/**
	 * Puts a piece on the hex board.
	 * counter is used to keep a record of the amount of moves in a game
	 * @param x the x coordinate on the board
	 * @param y the y coordinate on the board
	 * @param player the player the piece belongs to(1 or 2)
	 */
	public void setPiece(int x, int y, int player) 
	{
		if(board[y][x].value == 0)
		{
			board[y][x].value = player; 
			counter++;	
		}
	}
	
	/**
	 * Swaps the stone of the players if there's only one piece on the board. 
	 * 
	 */
	public void swapSides() 
	{
		if(counter == 1)
		{
			for (int i = 0; i < board.length; i++)
			{
				for (int j = 0; j < board[i].length; j++) 
				{
					if(board[i][j].value == 1)
					{
						board[i][j].value = 2;
					}
					if(board[i][j].value == 2)
					{
						board[i][j].value = 1;
					}
				}
			}
		}	
	}
	
	/**
	 * Returns the size of the board.
	 * @return the size of one side of the board
	 */
	public int getDimensions() 
	{
		return x;
	}
	
	/**
	 * Returns the player id that the piece belongs to or 0 if the field is
	 * empty.
	 * @param x the x coordinate on the board
	 * @param y the y coordinate on the board
	 * @return returns player id of the piece or 0 if the field is blank 
	 */
	public int getField(int x, int y) 
	{
		return board[y][x].value;
	}
	
	// TODO: How do we determine which side belongs to which player?
	
}

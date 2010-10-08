package Game;
/**
 * This class represents a game board. Keep it simple.
 * 
 */
public class Board 
{
	private int x, counter;
	private Cell[][] board;
	private Cell border1;
	private Cell border2;
	private Cell border3;
	private Cell border4;
	
	/**
	 * Creates a new board with specified dimensions.
	 * border cell is used to determine when cell's link is a border.
	 * @param dimensions the size of the board
	 */
	public Board(int dimensions) 
	{
		x = dimensions;		
		board = new Cell[x][x];		
		border1 = new Cell(3);
		border2 = new Cell(4);
		border3 = new Cell(5);
		border4 = new Cell(6);

		for (int i = 0; i < board.length; i++)
		{
			for (int j = 0; j < board[i].length; j++) 
			{
				board[i][j] = new Cell(0);				
				int a = j-1;
				int b = i-1;
				int c = j+1;
				int d = i+1;
				
				if(a < 0)
				{
					board [i][j].nwCell = border1;
				}
				
				else if(b < 0)
				{
					board [i][j].nwCell = border2;
				}
				
				else if(c > board.length - 1)
				{
					board [i][j].nwCell = border3;
				}
				
				else if(d > board.length - 1)
				{
					board [i][j].nwCell = border4;
				}
				
				else
				{
					board [i][j].nwCell = board[i+1][j-1];
				}
				
				if(a < 0)
				{
					board [i][j].nCell = border1;
				}
				
				else if(b < 0)
				{
					board [i][j].nCell = border2;
				}
				
				else if(c > board.length - 1)
				{
					board [i][j].nCell = border3;
				}
				
				else if(d > board.length - 1)
				{
					board [i][j].nCell = border4;
				}
				
				else
				{
					board [i][j].nCell = board[i+1][j];
				}
				
				if(a < 0)
				{
					board [i][j].neCell = border1;
				}
				
				else if(b < 0)
				{
					board [i][j].neCell = border2;
				}
				
				else if(c > board.length - 1)
				{
					board [i][j].neCell = border3;
				}
				
				else if(d > board.length - 1)
				{
					board [i][j].neCell = border4;
				}
				
				else
				{
					board [i][j].neCell = board[i][j+1];
				}
				
				if(a < 0)
				{
					board [i][j].seCell = border1;
				}
				
				else if(b < 0)
				{
					board [i][j].seCell = border2;
				}
				
				else if(c > board.length - 1)
				{
					board [i][j].seCell = border3;
				}
				
				else if(d > board.length - 1)
				{
					board [i][j].seCell = border4;
				}
				
				else
				{
					board [i][j].seCell = board[i-1][j+1];
				}
				
				if(a < 0)
				{
					board [i][j].sCell = border1;
				}
				
				else if(b < 0)
				{
					board [i][j].sCell = border2;
				}
				
				else if(c > board.length - 1)
				{
					board [i][j].sCell = border3;
				}
				
				else if(d > board.length - 1)
				{
					board [i][j].sCell = border4;
				}
				
				else
				{
					board [i][j].sCell = board[i-1][j];
				}
				
				if(a < 0)
				{
					board [i][j].swCell = border1;
				}
				
				else if(b < 0)
				{
					board [i][j].swCell = border2;
				}
				
				else if(c > board.length - 1)
				{
					board [i][j].swCell = border3;
				}
				
				else if(d > board.length - 1)
				{
					board [i][j].swCell = border4;
				}
				
				else
				{
					board [i][j].swCell = board[i-1][j-1];
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
	 * 3,4,5,6 = border
	 */
	static class Cell
	{
		Cell nwCell;
		Cell nCell;
		Cell neCell;
		Cell seCell;
		Cell sCell;
		Cell swCell;
		
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
}

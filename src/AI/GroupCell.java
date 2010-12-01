package AI;

import Game.Board;
import Game.Board.Cell;

/**
 * creates a group of cells or unit
 * @author Jose Sue Smith
 *
 */
public class GroupCell
{
	private Board board;
	private int counter, x, y, player, usedCounter;
	private int [][] used, group;
	private boolean found = false;
	
	/**
	 * creates a group of cells or unit
	 * @param b board
	 * @param p player
	 * @param a usedList
	 * @param c counter of used list
	 */
	public GroupCell(Board b, int p, int [][] a, int c)
	{
		board = b;
		player = p;
		group = new int[board.getDimensions()*board.getDimensions()][2];
		//used list keep track of checked cells		
		used = a;
		//usedCounter keeps track of amount of checked used cells
		usedCounter = c;
		counter = 0; //size of the group
		search();
		neighbors(x,y);
	}
	
	/**
	 * searches for a cell that has been used by player
	 */
	public void search()
	{
            for(int i = 0; i< board.getDimensions(); i++)
		{
			for(int j = 0; j< board.getDimensions(); j++)
			{
				int a = board.getField(i, j);
				Cell aCell = board.getCell(i, j);
				if (a == player && unchecked(aCell))
				{
					x = i;
					y = j;
					used[usedCounter][0] = x;
					used[usedCounter][1] = y;
					usedCounter++;
					found = true;
					break;
				}				
			}
		}
	}
	
	/**
	 * checks if a used cell has already been revised in board
	 * @param a a cell to be checked
	 * @return true if cell is unchecked
	 */
	public boolean unchecked(Cell a)
	{
		for(int i = 0 ; i<used.length; i++)
		{
			if(used[i][0] == a.getX() && used[i][1] == a.getY())
			{
				return false;
			}
		}
		return true;
	}
	/**
	 * checks if a cell is already in the group
	 * @param a a cell to be checked
	 * @return true if cell is not yet in group
	 */
	public boolean uncheckedGroup(Cell a)
	{
		for(int i = 0 ; i<group.length; i++)
		{
			if(group[i][0] == a.getX() && group[i][1] == a.getY())
			{
				return false;
			}
		}
		return true;
	}
	/**
	 * states if a new group has been found
	 * @return true if a new group has been found and false if not
	 */
	public boolean found()
	{
		return found;
	}
	
	/**
	 * checks if neighboring cells of new cell are occupied by player to create a group
	 */
	public void neighbors(int x1, int y1)
	{
		Cell a = board.getCell(x1, y1);
		Cell [] list = a.getNeighbours();	
		
		for(int i = 0; i<list.length; i++)
		{
			Cell b = list[i];				
			if(b.getValue()== player && unchecked(b) && uncheckedGroup(b))
			{			
				group[counter][0] = b.getX();
				group[counter][1] = b.getY();
				used[usedCounter][0] = b.getX();
				used[usedCounter][1] = b.getY();
				counter++;
				usedCounter++;
				neighbors(b.getX(),b.getY());
			}			
		}		
	}	
	
	/**
	 *  List containing cells that have already been checked
	 * @return List with cells that have already been checked
	 */
	public int[][] usedList()
	{
		return used;
	}
	/**
	 * List of cells in current group
	 * @return List of cells in current group
	 */
	public int[][] group()
	{
		return group;
	}
	/**
	 * counter of cells that have already been checked
	 * @return counter of cells that have already been checked
	 */
	public int getCounter()
	{
		return usedCounter;
	}
}

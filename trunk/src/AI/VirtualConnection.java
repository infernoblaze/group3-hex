package AI;

import Game.Board;
import Game.Board.Cell;

/**
 * checks for virtual connections from group of cells
 * @author Jose Sue Smith
 */
public class VirtualConnection 
{
	private GroupCell a,b;
	private Cell[] blanks1, blanks2;
	private Board board;
	private int counter, c1, c2;
	
	/**
	 * checks for virtual connections from 2 groups of cells
	 * @param aBoard board to which the groups belong
	 * @param aGroup Group 1 to be examined
	 * @param bGroup Group 2 to be examined
	 */
	public VirtualConnection(GroupCell aGroup,GroupCell bGroup, Board aBoard)
	{
		a = aGroup;
		b = bGroup;
		board = aBoard;
		counter = 0;
	}
	
	/**
	 * check for virtual connections from different group of cells
	 * @return 0 if there are no connections, 1 if connection is complete or 2 if its semi-complete
	 */
	public int check()
	{
		blankList();
		
		for(int i = 0; i<c1;i++)
		{
			for(int j = 0; j<c2;j++)
			{				
				if(blanks1[i].getX() == blanks2[j].getX() && blanks1[i].getY() == blanks2[j].getY())
				{				
					counter++;
				}
			}			
		}		
		
		if(counter == 0)
		{
			return 0;
		}
		
		if(counter == 1)
		{
			return 2;
		}
		
		return 1;
	}
	
	/**
	 * creates list of blank cells from both groups to compare
	 */
	public void blankList()
	{
		Cell[] group = a.getCells();
		c1 = 0;
		blanks1 = new Cell[board.getDimensions()*board.getDimensions()];
		for(int i = 0; i<a.getSize();i++)
		{
			Cell[] list = group[i].getNeighbours();
			for(int j = 0; j<list.length;j++)
			{
				
				if(list[j].getValue() == 0)
				{
					if(c1==0)
					{
						blanks1[c1] = list[j];
						c1++;
					}
					else if(unchecked(list[j], blanks1,c1))
					{
						blanks1[c1] = list[j];
						c1++;
					}					
				}
			}
		}	
		
		Cell[] group1 = b.getCells();
		c2 = 0;
		blanks2 = new Cell[board.getDimensions()*board.getDimensions()];
		for(int i = 0; i<b.getSize();i++)
		{
			Cell[] list1 = group1[i].getNeighbours();
			for(int j = 0; j<list1.length;j++)
			{
	
				if(list1[j].getValue() == 0)
				{
					if(c2==0)
					{
						blanks2[c2] = list1[j];
						c2++;
					}				
					else if(unchecked(list1[j],blanks2,c2))
					{
						blanks2[c2] = list1[j];
						c2++;
					}
				}
			}
		}
	}
	
	/**
	 * checks if a cell has already been checked in a list
	 * @param c cell to be checked in a List
	 * @param list list to be checked
	 * @param size size of the list to be checked
	 * @return true if it has not been checked, otherwise false
	 */
	public boolean unchecked(Cell c, Cell[] list, int size)
	{
		for(int i = 0; i<size;i++)
		{
			Cell aCell = list[i];
			if(aCell.getX()==c.getX() && aCell.getY() == c.getY())
			{
				return false;
			}
		}
		return true;
	}
}

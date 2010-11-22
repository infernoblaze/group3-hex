package AI;

import Game.Board;
import Game.Board.Cell;

/**
 * checks for virtual connections from group of cells
 * @author Jose Sue Smith
 */
public class VirtualConnection 
{
	private Board board;
	private int[][] blanks1, blanks2, equals;
	private int counter, counter1 , counter2, player;
	private GroupCell a,b;
	
	/**
	 * checks for virtual connections from 2 groups of cells
	 * @param b1 board to which the groups belong
	 * @param aGroup Group 1 to be examined
	 * @param bGroup Group 2 to be examined
	 */
	public VirtualConnection(Board b1, GroupCell aGroup, GroupCell bGroup)
	{
		board = b1;
		a = aGroup;
		b = bGroup;
		counter = 0;
		counter1 = 0;
		counter2 = 0;
		blanks1 = new int[board.getDimensions()*board.getDimensions()][2];
		blanks2 = new int[board.getDimensions()*board.getDimensions()][2];
		equals = new int[board.getDimensions()*board.getDimensions()][2];
		blankLists();
	}
	
	/**
	 * check for virtual connections from different group of cells
	 * @return 0 if there are no connections, 1 if connection is complete or 2 if its semi-complete
	 */
	public int check()
	{
		for(int i = 0; i<counter1; i++)
		{
			for(int j = 0; j<counter2; j++)
			{
				if(blanks1[i][0] == blanks2[j][0])
				{
					equals[counter][0] = blanks1[i][0];
					counter++;
				}
			}
		}
		if(counter == 0)
		{
			return 0;
		}
		
		if(counter >2)
		{
			return 1;
		}
		
		return 2;
	}
	
	
	
	/**
	 * checks if a cell has already been checked in a list
	 * @param a cell to be checked in aList
	 * @param aList list to be checked
	 * @return true if it has not been checked, otherwise false
	 */
	public boolean unchecked(Cell a, int [][] aList)
	{
		for(int i = 0 ; i<aList.length; i++)
		{
			if(aList[i][0] == a.getX() && aList[i][1] == a.getY())
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * creates list with blank cells from groups
	 */
	public void blankLists()
	{
		for(int i = 0; i<a.getCounter();i++)
		{
			Cell cell1 = board.getCell(a.group()[i][0], a.group()[i][1]);
			Cell [] list1 = cell1.getNeighbours();
			
			for(int j = 0;j<list1.length;j++)
			{
				Cell aCell = list1[j];
				if(aCell.getValue() == 0 && unchecked(aCell,blanks1))
				{
					blanks1[counter1][0] = aCell.getX();
					blanks1[counter1][1] = aCell.getY();
					counter1++;
				}
			}
		}
		
		for(int i = 0; i<b.getCounter();i++)
		{
			Cell cell2 = board.getCell(b.group()[i][0], b.group()[i][1]);
			Cell [] list2 = cell2.getNeighbours();
			
			for(int j = 0;j<list2.length;j++)
			{
				Cell bCell = list2[j];
				if(bCell.getValue() == 0 && unchecked(bCell,blanks2))
				{
					blanks2[counter2][0] = bCell.getX();
					blanks2[counter2][1] = bCell.getY();
					counter2++;
				}
			}
		}
	}
	
	/**
	 * List of cells that are bridges
	 * @return List of cells that are bridges 
	 */
	public int[][] getEquals()
	{
		return equals;
	}
	
}

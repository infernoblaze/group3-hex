package AI;

import java.util.ArrayList;

import Game.Board;
import Game.Board.Cell;

/**
 * uses virtual connections to determine how good a board is towards a player
 * @author Jose Sue Smith
 */
public class And_Or 
{
	private Board board;
	private int player, counter, size, complete, semicomplete;
	private int[][] used;
	private ArrayList<GroupCell> list;
	
	/**
	 * uses virtual connections to determine how good a board is towards a player
	 * @param b board to be examined
	 * @param p player for which it is examined
	 */
	public And_Or(Board b, int p)
	{
		board = b;
		player = p;	
		complete = 0;
		semicomplete = 0;
		size = board.getDimensions() * board.getDimensions();
		used = new int [size][2];
		counter = 0;
	}
	
	/**
	 * creates an Array List containing all groups of cells from a board
	 */
	public void groups()
	{
		list = new ArrayList<GroupCell>();
		
		for(int i = 0; i<size; i++)
		{
			GroupCell group = new GroupCell(board,player,used,counter);
			if(group.found())
			{
				list.add(group);
				counter = group.getCounter();
				used = group.usedList();
			}
		}
	}	
	
	/**
	 * determines if there is a virtual connection between 2 groups
	 * @param a group of cells 1
	 * @param b group of cells 2
	 * @return 0 if there are no connections, 1 if connection is complete or 2 if its semi-complete
	 */
	public int type(GroupCell a, GroupCell b)
	{		
		VirtualConnection checker = new VirtualConnection(board,a,b);
		return checker.check();
	}
	
	/**
	 * checks if a group of cells creates a virtual connection with a border
	 * @param aGroup group of cells to be checked
	 * @return returns 1 if there is a semiconnection, 2 if there is a complete connection and 0 if there is no connection
	 */
	public int borderCheck(GroupCell aGroup)
	{
		int size = board.getDimensions();
		int c1 = 0;
		for(int i = 0; i<aGroup.getCounter();i++)
		{
			Cell cell1 = board.getCell(aGroup.group()[i][0], aGroup.group()[i][1]);
			Cell [] list1 = cell1.getNeighbours();
			if(player == 1)
			{
				if(cell1.getX() == 1)
				{
					for(int j = 0; j<list1.length; j++)
					{
						if(list1[j].getX()==0)
						{
							if(list1[j].getValue() == 0)
							{
								c1++;
							}
						}						
					}
				}
				
				if(cell1.getX() == size-2)
				{
					for(int j = 0; j<list1.length; j++)
					{
						if(list1[j].getX()==size-1)
						{
							if(list1[j].getValue() == 0)
							{
								c1++;
							}
						}						
					}
				}
			}
			
			if(player == 2)
			{
				if(cell1.getY() == 1)
				{
					for(int j = 0; j<list1.length; j++)
					{
						if(list1[j].getY()==0)
						{
							if(list1[j].getValue() == 0)
							{
								c1++;
							}
						}						
					}
				}
				
				if(cell1.getY() == size-2)
				{
					for(int j = 0; j<list1.length; j++)
					{
						if(list1[j].getY()==size-1)
						{
							if(list1[j].getValue() == 0)
							{
								c1++;
							}
						}						
					}
				}
			}
		}
		return c1;
	}
	/**
	 * evaluates the value of current board according to its virtual connections
	 * @return	the value of current board
	 */
	public int evaluate()
	{
		ArrayList<GroupCell> aList = list;
		for(int i = 0; i<aList.size();i++)
		{
			if(borderCheck(aList.get(i)) == 2)
			{
				complete++;
			}
			if(borderCheck(aList.get(i)) == 1)
			{
				semicomplete++;
			}
		}
		while(aList.size()>1)
		{
			int value = type(aList.get(0), aList.get(1)); 
			if(value == 1)
			{
				complete++;
			}
			if(value == 2)
			{
				semicomplete++;
			}
			aList.remove(0);
		}
		
		
		int score = complete*3 + semicomplete;
		System.out.println("Score " + score);
		return score;
	}
	
	
}

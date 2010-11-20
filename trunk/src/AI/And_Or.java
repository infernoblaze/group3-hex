package AI;

import java.util.ArrayList;

import Game.Board;

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
	 * evaluates the value of current board according to its virtual connections
	 * @return	the value of current board
	 */
	public int evaluate()
	{
		while(list.size()>0)
		{
			int value = type(list.get(0), list.get(1)); 
			if(value == 1)
			{
				complete++;
			}
			if(value == 2)
			{
				semicomplete++;
			}
			list.remove(0);
		}
		int score = complete*3 + semicomplete;
		
		return score;
	}
	
	
}

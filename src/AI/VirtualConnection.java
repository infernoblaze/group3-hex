package AI;

import java.util.ArrayList;

import Game.Board;

/**
 * checks for virtual connections from group of cells
 * @author Jose Sue Smith
 *
 */
public class VirtualConnection 
{
	Board board;
	int player;
	int[][] used;
	int counter;
	int size;
	ArrayList<GroupCell> a, b;
	
	public VirtualConnection(ArrayList<GroupCell> aList, ArrayList<GroupCell> bList)
	{
		a = aList;
		b = bList;
	}
	
	/**
	 * check for virtual connections from different group of cells
	 * @return 0 if there are no connections, 1 if connection is complete or 2 if its semi-complete
	 */
	public int check()
	{
		return 0;
	}
	
	
	
	
}

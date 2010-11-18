package AI;

import java.util.ArrayList;

import Game.Board;

/**
 * uses virtual connections to find best move
 * semi connection is better than connection
 * @author Jose Sue Smith
 *
 */
public class And_Or 
{
	Board board;
	int player;
	int[][] used;
	int counter;
	int size;
	ArrayList<GroupCell> list;
	
	public And_Or(Board b, int p)
	{
		board = b;
		player = p;	
		size = board.getDimensions() * board.getDimensions();
		used = new int [size][2];
		counter = 0;
	}
	
	public void groups()
	{
		list = new ArrayList<GroupCell>();
		
		for(int i = 0; i<size; i++)
		{
			GroupCell group = new GroupCell(board,player,used,counter);
			if(group.found)
			{
				list.add(group);
				counter = group.getCounter();
				used = group.usedList();
			}
		}
	}
	

	public int type(ArrayList<GroupCell> a , ArrayList<GroupCell> b)
	{
		VirtualConnection checker = new VirtualConnection(a,b);
		return checker.check();
	}
	
	
}

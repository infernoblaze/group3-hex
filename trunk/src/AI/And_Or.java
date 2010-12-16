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
	private int player, complete, semicomplete;;
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
		list = new ArrayList<GroupCell>();
		groups();
    }
        
    /**
     * creates an Array List containing all groups of cells from a board
     */
    public void groups() 
    {
    	for(int i = 0; i<board.getDimensions(); i++)
		{
			for(int j = 0; j<board.getDimensions(); j++)
			{        				
				Cell cell = board.getCell(i, j);
				if(cell.getValue() == player)
				{			
    				if(list.size()==0)
    				{
    					GroupCell x = new GroupCell(board,cell);
    					x.grow(cell);
    					list.add(x);
    				}
    				else if(unchecked(cell))
    				{
    					GroupCell x = new GroupCell(board,cell);
    					x.grow(cell);
    					list.add(x);	
    				}
    			}				
    		}
    	}		
    }    
        
    /**
     * used to check if a cell has been already included in a group
     * @param aCell cell to be checked
     * @return true if the cell has not been checked, otherwise false
     */
    public boolean unchecked(Cell aCell)
    {
    	for(int i = 0;i<list.size();i++)
        {
        	GroupCell a = list.get(i);
        	for(int j = 0; j<a.getSize();j++)
        	{
        		Cell[] bCell =a.getCells();
        		if(bCell[j].getX()==aCell.getX() && bCell[j].getY() == aCell.getY())
        		{
        			return false;
        		}
        	}
        }
        return true;
  	}
        
    /**
     * determines if there is a virtual connection between 2 groups
     * @param a group of cells 1
     * @param b group of cells 2
     * @return 0 if there are no connections, 1 if connection is complete or 2 if its semi-complete
     */
     public int type(GroupCell a, GroupCell b)
     {               
    	 VirtualConnection checker = new VirtualConnection(a,b,board);
         return checker.check();
     }
     
     /**
      * used to get a List of all current groups
      * @return list with all current groups
      */
     public ArrayList<GroupCell> getList()
     {
    	 return list;
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
    	 Cell[] cell1 = aGroup.getCells();
    	 for(int i = 0; i<aGroup.getSize();i++)
         {
    		 Cell [] list1 = cell1[i].getNeighbours();
             if(player == 1)
             {
            	 if(cell1[i].getX() == 1)
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
                         
                 if(cell1[i].getX() == size-2)
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
                if(cell1[i].getY() == 1)
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
                        
                if(cell1[i].getY() == size-2)
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
     * @return the score of current board
     */
     public int evaluate()
    {
    	ArrayList<GroupCell> l = list;
		for(int i = 0; i<l.size();i++)
        {
                if(borderCheck(l.get(i)) == 2)
                {
                        complete++;
                }
                if(borderCheck(l.get(i)) == 1)
                {
                        semicomplete++;
                }
        }
		while(l.size()>1)
		{
			int value = type(l.get(0), l.get(1));
			if(value == 1)
            {
                    complete+=2;
            }
            if(value == 2)
            {
                    semicomplete+=2;
            }
            l.remove(0);
		}
		int score = complete*3 + semicomplete;
		return score;
    }
}
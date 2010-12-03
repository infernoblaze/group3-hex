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
		private Cell cell;
		private Cell[] group;
		private int counter;
	        
        /**
         * creates a group of cells or unit
         * @param b board
         * @param c initial cell from group
         */
        public GroupCell(Board b, Cell c)
        {
        	board = b;
    		cell = c;
    		group = new Cell[board.getDimensions()*board.getDimensions()];
    		group[0] = cell;
    		counter = 1;      
        }
        
        /**
         * checks if the neighbors of the cell are also from the same player, to include them in the group
         * @param aCell cell to which the neighbors would be examined
         */
    	public void grow(Cell aCell)
    	{
    		
    		Cell[] list = aCell.getNeighbours();		
    		for(int i = 0; i<list.length;i++)
    		{
    			Cell bCell = list[i];
    			if(bCell.getValue() == aCell.getValue() && used(bCell))
    			{
    				group[counter] = bCell;
    				counter++;
    				grow(bCell);			
    			}
    		}
    	}
        
        /**
         * checks if a cell has already been included in the group
         * @param a a cell to be checked
         * @return true if cell is unchecked
         */
        public boolean used(Cell a)
    	{
    		for(int i = 0; i<counter;i++)
    		{
    			if(group[i].getX() == a.getX() && group[i].getY() == a.getY())
    			{
    				return false;
    			}
    		}
    		return true;
    	}
        
        /**
         * used to get the group of cells
         * @return group of cells
         */
    	public Cell[] getCells()
    	{
    		return group;
    	}
    	
    	/**
    	 * used to get the size of the group
    	 * @return size
    	 */
    	public int getSize()
    	{
    		return counter;
    	}
       
        
        
}

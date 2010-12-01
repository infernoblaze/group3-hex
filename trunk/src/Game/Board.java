package Game;

import java.util.ArrayList;

/**
 * This class represents a game board. Keep it simple.
 *
 */
public class Board implements Cloneable {

    /**
     * class for the cells of the board.
     * a cell knows who is next to it and if there's a stone from player 1 or 2 or if its
     * empty.
     * 0 = cell is empty
     * 1 = stone from player 1
     * 2 = stone from player 2
     * 3,4,5,6 = border
     */
    public class Cell {

        Cell nwCell, wCell, neCell, seCell, eCell, swCell;
        int value, x, y;

        public Cell(int value) {
            this.value = value;
        }

        public Cell[] getNeighbours() {
            return new Cell[]{nwCell, neCell, eCell, seCell, swCell, wCell};
        }

        public int getValue() {
            return this.value;
        }
        
        public int getX()
        {
        	return x;
        }
        
        public int getY()
        {
        	return y;
        }
    }
    private int size, counter;
    private Cell[][] board;
    private Cell borderLeft, borderRight, borderTop, borderBottom;

    /**
     * Creates a new board with specified dimensions.
     * border cell is used to determine when cell's link is a border.
     * @param dimensions the size of the board
     */
    public Board(int dimensions) {

        board = new Cell[dimensions][dimensions];
        size = dimensions;

        borderLeft = new Cell(3);
        borderLeft.x = -1;
        borderLeft.y = -1;
        borderRight = new Cell(4);
        borderRight.x = -1;
        borderRight.y = -1;
        borderTop = new Cell(5);
        borderTop.x = -1;
        borderTop.y = -1;
        borderBottom = new Cell(6);
        borderBottom.x = -1;
        borderBottom.y = -1;

        for (int i = 0; i < dimensions; i++) {
            for (int j = 0; j < dimensions; j++) {
                board[i][j] = new Cell(0);
                board[i][j].x = i;
                board[i][j].y = j;
            }
        }

        findNeighbourCells();
    }
   
    public int necessaryMoves(int PlayerID, int x, int y) {
        Board hexy = this.clone();
        if(hexy.checkEnd() == PlayerID) {
            return 0;
        }
        ArrayList<Cell> path = new ArrayList<Cell>();
        Cell current = this.getCell(x, y);
        checkedFields = new boolean[size][size];
        Cell[] neighbours = current.getNeighbours();
        for(int i = 0 ; i < neighbours.length ; i++) {
            if(checkedFields[neighbours[i].getX()][neighbours[i].getY()] == true) {

            }
            checkedFields[neighbours[i].getX()][neighbours[i].getY()] = true;

        }
            return 0;
    }

    private void findNeighbourCells() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Cell cell = board[i][j];

                int left = i - 1;
                int right = i + 1;
                int top = j - 1;
                int bottom = j + 1;

                if (top > -1) {
                    cell.nwCell = board[i][top];
                    if (right < size) {
                        cell.neCell = board[right][top];
                    } else {
                        cell.neCell = borderRight;
                    }
                } else {
                    cell.nwCell = borderTop;
                    cell.neCell = borderTop;
                }

                if (bottom < size) {
                    cell.seCell = board[i][bottom];
                    if (left > -1) {
                        cell.swCell = board[left][bottom];
                    } else {
                        cell.swCell = borderLeft;
                    }
                } else {
                    cell.seCell = borderBottom;
                    cell.swCell = borderBottom;
                }

                if (right < size) {
                    cell.eCell = board[right][j];
                } else {
                    cell.eCell = borderRight;
                }

                if (left > -1) {
                    cell.wCell = board[left][j];
                } else {
                    cell.wCell = borderLeft;
                }
            }
        }

    }
    private int[] lastPiece;

    /**
     * Puts a piece on the hex board.
     * counter is used to keep a record of the amount of moves in a game
     * @param x the x coordinate on the board
     * @param y the y coordinate on the board
     * @param player the player the piece belongs to(1 or 2)
     */
    public void setPiece(int x, int y, int player) {
        if (board[x][y].value == 0) {
            board[x][y].value = player;
            counter++;
            lastPiece = new int[]{x, y};
        }
    }

    public int[] getLastPiece() {
        return lastPiece;
    }

    /**
     * Swaps the stone of the players if there's only one piece on the board.
     */
    public void swapSides() {
        if (counter == 1) {
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    if (board[i][j].value == 1) {
                        board[i][j].value = 2;
                    } else if (board[i][j].value == 2) {
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
    public int getDimensions() {
        return size;
    }

    /**
     * Returns the number of pieces on the board.
     * @return the size of one side of the board
     */
    public int getPieceCount() {
        return counter;
    }

    /**
     * Returns the player id that the piece belongs to or 0 if the field is
     * empty.
     * @param x the x coordinate on the board
     * @param y the y coordinate on the board
     * @return returns player id of the piece or 0 if the field is blank
     */
    public int getField(int x, int y) {
        return board[x][y].value;
    }

    public Cell getCell(int x, int y) {
        return board[x][y];
    }

    /**
     * Checks whether there is a path that connects two sides of the board. In
     * that case the game ends.
     * @return 1 if player one wins, 2 if player two wins, 0 otherwise.
     */
    public int checkEnd() {
        int dimensions = this.getDimensions();
        
        for (int i = 0; i < this.getDimensions(); i++) {
            checkedFields = new boolean[dimensions][dimensions];
            if (findPath(Game.PLAYER_ONE, 0, i)) {
                return 1;
            }
            checkedFields = new boolean[dimensions][dimensions];
            if (findPath(Game.PLAYER_TWO, i, 0)) {
                return 2;
            }
        }
        return 0;
    }
    private boolean[][] checkedFields;

    private boolean findPath(int playerId, int x, int y) {
        Cell cell = this.getCell(x, y);
        checkedFields[x][y] = true;

        if (cell.getValue() != playerId) {
            return false;
        }

        for (Cell neighbour : cell.getNeighbours()) {
            if (playerId == Game.PLAYER_ONE && neighbour.getValue() == 4) {
                return true;
            } else if (playerId == Game.PLAYER_TWO && neighbour.getValue() == 6) {
                return true;
            }

            if (neighbour.getValue() == playerId
                    && checkedFields[neighbour.x][neighbour.y] == false
                    && findPath(playerId, neighbour.x, neighbour.y)) {
                return true;
            }
        }

        return false;
    }

        public String toString()
        {
                String string = "";
        for(int i = 0 ; i < size ; i++)
        {
            for (int j = 0; j < size ; j++)
                string += "{"+board[i][j].value+"}";
            
            string += "\n";
            
            for (int k = -1 ; k < i ; k++)
                string += "  ";
        }
        
        return string;
    }
        
        /**
         * @deprecated Please use toString instead.
         */
    public void printBoard() {
        System.out.println("Deprecated, please use toString()");
        
        System.out.println(toString());
    }

    public int[][] getBoard() {
        int[][] iBoard = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                iBoard[i][j] = board[i][j].value;
            }
        }
        return iBoard;
    }

    public Board clone() {
        Board clone = new Board(size);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j].value != 0) {
                    clone.setPiece(i, j, board[i][j].value);
                }
            }
        }

        return clone;
    }
}
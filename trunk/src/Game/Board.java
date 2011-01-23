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

        Cell[] neighbours;
        int value, x, y, resW, resB;

        public Cell(int value) {
            neighbours = new Cell[6];
            this.value = value;
            if (value == 0) {
                this.resB = 1;
                this.resW = 1;
            }
            if (value == 1) {
                this.resB = 100000;
                this.resW = 0;
            }
            if (value == 2) {
                this.resB = 0;
                this.resW = 100000;
            }
        }

        public Cell[] getNeighbours() {
            return neighbours;
        }

        public int getValue() {
            return this.value;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    public class Group { // NEW CLASS!!!
        Cell[] cells;
        Cell[] neighbours;
        private int size, playerID;

        public Group(Cell aCell) {
            cells = new Cell[1];
            cells[0] = aCell;
            size = 1;
            neighbours = aCell.getNeighbours();
            playerID = aCell.getValue();
        }

        public Cell[] getCells() {
            return cells;
        }

        public int size() {
            return size;
        }

        public Cell[] getNeighbours() {
            return neighbours;
        }

        public void add(Group aGroup) {
            for(int i = size ; i < size+aGroup.size() ; i++)
            cells[i] = aGroup.getCells()[i-size];
            size+=aGroup.size();
            Cell[] oldNeighbours = neighbours;
            Cell[] newNeighbours = aGroup.getNeighbours();
            neighbours = getDistinctCells(oldNeighbours, newNeighbours);
        }

        public Cell[] getDistinctCells(Cell[] a, Cell[] b) {
            int number;
            Cell[] newCells = new Cell[a.length+b.length];
            for(int i = 0 ; i < a.length ; i++) {
                newCells[i] = a[i];
            }
            for(int j = 0 ; j < b.length ; j++) {
                newCells[j+a.length] = b[j];
            }
            number = newCells.length;
            for(int i = 0 ; i < newCells.length ; i++) {
                for(int j = 0 ; j < newCells.length ; j++) {
                    if(newCells[i] == newCells[j]) {
                        newCells[i] = null;
                        newCells[j] = null;
                        number -=2;
                    }
                }
            }
            Cell[] Cells = new Cell[number];
            int counter = 0;
            for(int i = 0 ; i < newCells.length ; i++) {
                if(newCells[i] != null) {
                    Cells[counter] = newCells[i];
                    counter++;
                }
            }
            return Cells;
        }
    }

    public boolean isBridge(Cell a, Cell b) {
            return ((a.x == b.x-1 && a.y == b.y+2) ||
                    (a.x == b.x+1 && a.y == b.y+1) ||
                    (a.x == b.x+2 && a.y == b.y-1) ||
                    (a.x == b.x-2 && a.y == b.y+1) ||
                    (a.x == b.x-1 && a.y == b.y-1) ||
                    (a.x == b.x+1 && a.y == b.y-2));
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

//        for (int i = 0; i < dimensions; i++) {
//            for (int j = 0; j < dimensions; j++) {
//                board[i][j].links = new Link[]{new Link(board[i][j], board[i][j].nwCell), new Link(board[i][j], board[i][j].wCell), new Link(board[i][j], board[i][j].neCell), new Link(board[i][j], board[i][j].seCell), new Link(board[i][j], board[i][j].eCell), new Link(board[i][j], board[i][j].swCell)};
//
//            }
//        }

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
                    cell.neighbours[0] = board[i][top];
                    if (right < size) {
                        cell.neighbours[2] = board[right][top];
                    } else {
                        cell.neighbours[2] = borderRight;
                    }
                } else {
                    cell.neighbours[0] = borderTop;
                    cell.neighbours[2] = borderTop;
                }

                if (bottom < size) {
                    cell.neighbours[3] = board[i][bottom];
                    if (left > -1) {
                        cell.neighbours[5] = board[left][bottom];
                    } else {
                        cell.neighbours[5] = borderLeft;
                    }
                } else {
                    cell.neighbours[3] = borderBottom;
                    cell.neighbours[5] = borderBottom;
                }

                if (right < size) {
                    cell.neighbours[4] = board[right][j];
                } else {
                    cell.neighbours[4] = borderRight;
                }

                if (left > -1) {
                    cell.neighbours[1] = board[left][j];
                } else {
                    cell.neighbours[1] = borderLeft;
                }
            }
        }
        borderTop.neighbours = new Cell[size];
        borderBottom.neighbours = new Cell[size];
        borderLeft.neighbours = new Cell[size];
        borderRight.neighbours = new Cell[size];

        for (int i = 0; i < size; i++) {
            borderTop.neighbours[i] = board[i][0];
            borderBottom.neighbours[i] = board[i][size - 1];
            borderLeft.neighbours[i] = board[0][i];
            borderRight.neighbours[i] = board[size - 1][i];
        }
        borderLeft.resB = 100000;
        borderLeft.resW = 0;

        borderRight.resB = 100000;
        borderRight.resW = 0;

        borderTop.resW = 100000;
        borderTop.resB = 0;

        borderBottom.resW = 100000;
        borderBottom.resB = 0;

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
            if (player == 1) {
                board[x][y].resB = 100000;
                board[x][y].resW = 0;
            }
            if (player == 2) {
                board[x][y].resB = 0;
                board[x][y].resW = 100000;
            }
        }
    }
    public void removePiece(int x, int y) {
        board[x][y].value = 0;
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
                        board[i][j].resW = 100000;
                        board[i][j].resB = 0;

                    } else if (board[i][j].value == 2) {
                        board[i][j].value = 1;
                        board[i][j].resW = 0;
                        board[i][j].resB = 100000;
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

    public String toString() {
        String string = "";
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                string += "{" + board[j][i].value + "}";
            }

            string += "\n";

            for (int k = -1; k < i; k++) {
                string += "  ";
            }
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

    public double evaluate(int PlayerID) {
        double value = 0;
        checkedFields = new boolean[size][size];
        pathResistances = new ArrayList<Integer>();     
        getRes(1, borderLeft, 0);
        double resW = (double) getMinPath();
        checkedFields = new boolean[size][size];
        pathResistances = new ArrayList<Integer>();
        getRes(2, borderTop, 0);
        double resB = (double) getMinPath();
        value = (PlayerID == 1) ? (resB / resW) : (resW / resB);
//        System.out.println(toString());
//        System.out.println("resW = " + (int) resW);
//        System.out.println("resB = " + (int) resB);
//        System.out.println("Value = " + value);
        return value;
    }
    private ArrayList<Integer> pathResistances = new ArrayList<Integer>();

    public void getRes(int PlayerID, Cell current, int score) {
//        System.out.println(PlayerID + ": Current Cell: X= " + current.x + ", Y = " + current.y + ", Value = " + current.value);
        if (current.getX() != -1) {
            checkedFields[current.getX()][current.getY()] = true;
        }
        Cell[] c = current.getNeighbours();
        Cell border = (PlayerID == 1) ? borderRight : borderBottom;
        int compare = (PlayerID == 1) ? current.x : current.y;

        if (current.value == 0) {
            score += 1;
        }
        for (int i = 0; i < c.length; i++) {
            if (c[i] == border) {
//                System.out.println(PlayerID + ": New Path: " + (score));
                pathResistances.add(score);
            }
        }
        for (int i = 0; i < c.length; i++) {
            if (c[i].getX() != -1) {
                if (checkedFields[c[i].getX()][c[i].getY()] == false && c[i].value == (PlayerID) && ((PlayerID == 1) ? c[i].getX()>compare : c[i].getY()>compare)) {
                    if (current.getX() != -1) {
                        checkedFields[c[i].getX()][c[i].getY()] = true;
                    }
                    getRes(PlayerID, c[i], score);
                }
            }
        }
        for (int i = 0; i < c.length; i++) {
            if (c[i].getX() != -1) {
                if (checkedFields[c[i].getX()][c[i].getY()] == false && c[i].value == (PlayerID)) {
                    if (current.getX() != -1) {
                        checkedFields[c[i].getX()][c[i].getY()] = true;
                    }
                    getRes(PlayerID, c[i], score);
                }
            }
        }
        for (int i = 0; i < c.length; i++) {
            if (c[i].getX() != -1) {
                if (checkedFields[c[i].getX()][c[i].getY()] == false && c[i].value == 0 && ((PlayerID == 1) ? c[i].getX()>compare : c[i].getY()>compare)) {
                    if (current.getX() != -1) {
                        checkedFields[c[i].getX()][c[i].getY()] = true;
                    }
                    score+=1;
                    getRes(PlayerID, c[i], score);
                }
            }
        }
        for (int i = 0; i < c.length; i++) {
            if (c[i].getX() != -1) {
                if (checkedFields[c[i].getX()][c[i].getY()] == false && c[i].value == 0) {
                    if (current.getX() != -1) {
                        checkedFields[c[i].getX()][c[i].getY()] = true;
                    }
                    score += 1;
                    getRes(PlayerID, c[i], score);
                }
            }
        }
    }

    private int getMinPath() {
        int min = Integer.MAX_VALUE;
//        System.out.println("Size = " + pathResistances.size());
        for (int i = 0; i < pathResistances.size(); i++) {
//            System.out.println("Path No " + (i+1) + " = " + pathResistances.get(i));
            if (pathResistances.get(i) < min) {
                min = pathResistances.get(i);
            }
        }
        return min;
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

    @Override
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
    
    public LiteBoard getLiteBoard() {
    	int dimensions = getDimensions();
    	LiteBoard liteBoard = new LiteBoard(dimensions);
    	
    	for (int i = 0; i < dimensions; i++) {
			for (int j = 0; j < dimensions; j++) {
				liteBoard.setPiece(i, j, getField(i, j));
			}
		}
    	
    	return liteBoard;
    }
}

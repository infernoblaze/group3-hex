package Game;

import java.util.Arrays;

import Game.Board.Cell;

public class LiteBoard implements Cloneable {

	private byte[][] board;
	private int pieceCount;
	private int[] lastPiece;
	
	public LiteBoard(int dimension) {
		board = new byte[dimension][dimension];
	}
	
	public LiteBoard(byte[][] board) {
		setBoard(board);
	}

	public byte[][] getBoard() {
		return board;
	}
	
	public void setBoard(byte[][] board) {
		if (board.length != board[0].length) {
			System.err.println("The dimensions of the board is not equal.");
			return;
		}
		
		this.board = board;
		
		pieceCount = 0;
		int dimensions = this.board.length;
		for (int i = 0; i < dimensions; i++) {
			for (int j = 0; j < dimensions; j++) {
				if (board[i][j] != 0)
					pieceCount++;
			}
		}
	}
	
	public byte getPiece(int x, int y) {
		return board[x][y];
	}
	
	public void setPiece(int x, int y, int value) {
		setPiece(x, y, (byte)value);
	}
	
	public void setPiece(int x, int y, byte value) {
		board[x][y] = value;
		pieceCount++;
		lastPiece = new int[] {x, y};
	}
	
	public int getDimensions() {
		return board.length;
	}
	
	public int[] getLastPiece() {
		return lastPiece;
	}
	
	public int getPieceCount() {
		return pieceCount;
	}
	
	public LiteBoard clone() {
		int dimensions = getDimensions();
		byte[][] clonedBoard = new byte[dimensions][dimensions];
		
		for (int i = 0; i < dimensions; i++) {
			clonedBoard[i] = board[i].clone();
		}
		
		return new LiteBoard(clonedBoard);
	}
	
	public static int checkEnd(LiteBoard board) {
		int dimensions = board.getDimensions();
		byte[][] boardArray = board.getBoard();
		
		boolean[][] checkedFields;
		
		for (int i = 0; i < dimensions; i++) {
		    checkedFields = new boolean[dimensions][dimensions];
		    if (findPath(Game.PLAYER_ONE, 0, i, boardArray, checkedFields))
		    	return 1;
		    
		    checkedFields = new boolean[dimensions][dimensions];
		    if (findPath(Game.PLAYER_TWO, i, 0, boardArray, checkedFields))
		    	return 2;
			
		}
		
		return 0;
	}
	

    private static boolean findPath(int playerId, int x, int y, byte[][] board, boolean[][] checkedFields) {
        checkedFields[x][y] = true;

        if (board[x][y] != playerId)
            return false;
                
        int dimensions = board.length;

        if (playerId == Game.PLAYER_ONE && x >= dimensions - 1)
            return true;
        if (playerId == Game.PLAYER_TWO && y >= dimensions - 1)
            return true;
        
        if (x <= dimensions - 2) {
        	int neighbourX = x + 1;
        	int neighbourY = y;
        	
            if (board[neighbourX][neighbourY] == playerId
                    && checkedFields[neighbourX][neighbourY] == false
                    && findPath(playerId, neighbourX, neighbourY, board, checkedFields)) {
                return true;
            }
        }
        
        if (x > 0) {
        	int neighbourX = x - 1;
        	int neighbourY = y;
        	
            if (board[neighbourX][neighbourY] == playerId
                    && checkedFields[neighbourX][neighbourY] == false
                    && findPath(playerId, neighbourX, neighbourY, board, checkedFields))
                return true;
        }

        if (y <= dimensions - 2) {
        	int neighbourX = x;
        	int neighbourY = y + 1;
        	
            if (board[neighbourX][neighbourY] == playerId
                    && checkedFields[neighbourX][neighbourY] == false
                    && findPath(playerId, neighbourX, neighbourY, board, checkedFields))
                return true;
        }

        if (y > 0) {
        	int neighbourX = x;
        	int neighbourY = y - 1;
        	
            if (board[neighbourX][neighbourY] == playerId
                    && checkedFields[neighbourX][neighbourY] == false
                    && findPath(playerId, neighbourX, neighbourY, board, checkedFields))
                return true;
        }

        if (x <= dimensions - 2 && y > 0) {
        	int neighbourX = x + 1;
        	int neighbourY = y - 1;
        	
            if (board[neighbourX][neighbourY] == playerId
                    && checkedFields[neighbourX][neighbourY] == false
                    && findPath(playerId, neighbourX, neighbourY, board, checkedFields))
                return true;
        }

        if (x > 0 && y <= dimensions - 2) {
        	int neighbourX = x - 1;
        	int neighbourY = y + 1;
        	
            if (board[neighbourX][neighbourY] == playerId
                    && checkedFields[neighbourX][neighbourY] == false
                    && findPath(playerId, neighbourX, neighbourY, board, checkedFields))
                return true;
        }

        return false;
    }
    
    public static String toString(LiteBoard board) {
        String string = "";
        
        int dimensions = board.getDimensions();
        byte[][] boardArray = board.getBoard();
        
        for (int i = 0; i < dimensions; i++) {
            for (int j = 0; j < dimensions; j++)
                string += "{" + boardArray[j][i] + "}";

            string += "\n";

            for (int k = -1; k < i; k++)
                string += "  ";
        }

        return string;
    }
	
}

package Tests;

import Game.LiteBoard;

public class BoardPathFindingTest {

	public static void main(String[] args) {
		
		LiteBoard board = new LiteBoard(6);
		board.setPiece(0, 1, 1);
		board.setPiece(0, 2, 1);
		board.setPiece(1, 2, 1);
		board.setPiece(2, 2, 1);
		board.setPiece(3, 1, 1);
		board.setPiece(4, 1, 1);
		board.setPiece(5, 1, 1);
		
		System.out.println(LiteBoard.toString(board));
		
		System.out.println("Winner: " + LiteBoard.checkEnd(board));

		
		board.setPiece(1, 0, 2);
		board.setPiece(2, 0, 2);
		board.setPiece(2, 1, 2);
		board.setPiece(2, 2, 2);
		board.setPiece(1, 3, 2);
		board.setPiece(1, 4, 2);
		board.setPiece(1, 5, 2);
		
		System.out.println(LiteBoard.toString(board));
		
		System.out.println("Winner: " + LiteBoard.checkEnd(board));
	}
	
}

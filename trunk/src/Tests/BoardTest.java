package Tests;

import Game.Board;
import Game.LiteBoard;

public class BoardTest {

	public static void main(String[] args) {
		
		Board boardOne = new Board(11);
		Board[] boardOneClones = new Board[10000];
		
		long experimentOneStarts = System.currentTimeMillis();
		
		for (int i = 0; i < 10000; i++) {
			boardOneClones[i] = boardOne.clone();
		}
		
		long experimentOneEnds = System.currentTimeMillis();
		long experimentOneTime = experimentOneEnds - experimentOneStarts;
		
		
		LiteBoard boardTwo = new LiteBoard(11);
		LiteBoard[] boardTwoClones = new LiteBoard[10000];
		
		long experimentTwoStarts = System.currentTimeMillis();
		
		for (int i = 0; i < 10000; i++) {
			boardTwoClones[i] = boardTwo.clone();
		}
		
		long experimentTwoEnds = System.currentTimeMillis();
		long experimentTwoTime = experimentTwoEnds - experimentTwoStarts;
		
		
		System.out.println("Experiment one: " + experimentOneTime);		
		System.out.println("Experiment two: " + experimentTwoTime);
		System.out.println("The increase in percent: " + (int)(experimentOneTime / experimentTwoTime));
		
	}
	
}

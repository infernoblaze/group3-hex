package Players;

import Game.Game;

public class HumanPlayer implements Player {

	private Game game;
	private int playerId;
	private int[] nextMove;
	
	public synchronized int[] getNextMove() {
		try {
			this.wait();
		} catch (Exception e) {
//                        System.err.println("Gotta run, can't wait!");
                        return null;
		}
		int[] move = {nextMove[0], nextMove[1]}; 
		this.nextMove = null;
		return move;
	}
	
	public synchronized boolean setNextMove(int[] nextMove) {
		
		if (game.getBoard().getField(nextMove[0], nextMove[1]) == 0) {
			this.nextMove = nextMove;
			
			this.notify();
			return true;
		}
		
		return false;
	}
	
	public synchronized boolean swapSides() {
		this.nextMove = new int[] { -1, -1 };
		
		this.notify();
		return true;
	}

	public void setGame(Game theGame) {
		this.game = theGame;
	}
	
	public void setPlayerId(int thePlayer) {
		this.playerId = thePlayer;
	}
	
	public int getPlayerId() {
		return this.playerId;
	}

	@Override
	public void setCanSwapSides(boolean state) {
		// TODO Auto-generated method stub
		
	}

}

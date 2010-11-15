/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Players;

import AI.Minimax;
import Game.Game;
import Game.HexyBoard;

/**
 *
 * @author Lukas
 */
public class MinimaxPlayer implements Player{

    private Game game;
    private int playerId;
    private int[] nextMove;
    private Minimax mima;

    public void setGame(Game theGame) {
        game = theGame;
    }

    public void setPlayerId(int thePlayer) {
        playerId = thePlayer;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int[] getNextMove() {
        HexyBoard mine = new HexyBoard(game.getBoard().getBoard());
        mima = new Minimax(mine,2);
        return mima.getNextMove(playerId);
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Players;

import AI.MiniMax;
import Game.Game;
import Game.HexyBoard;

/**
 *
 * @author Lukas
 */
public class MinimaxPlayer implements Player{

    private Game game;
    private int playerId;
    private MiniMax mima;

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
        mima = new MiniMax(mine,2);
        return mima.getNextMove(playerId);
    }

}

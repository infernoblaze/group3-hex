/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package AI;

import Game.HexyBoard;

/**
 *
 * @author Lukas
 */
public class HexElement {
    private int[][] board;
    private int[] move; // the move which created the board
    private HexyBoard bo;
    private int eval;
    private int playerToMakeMove;

    public HexElement(HexyBoard b, int[] m) {
        board = b.getBoard();
        bo = b;
        move = m;
       try{playerToMakeMove =
                b.getBoard()[m[0]][m[1]];}
       catch(Exception e) {
                playerToMakeMove = 2;
       }
    }

    public void evaluate(int e) {
        eval = e;
    }

    public int[][] getBoard() {
        return board;
    }

    public int[] getMove() {
        return move;
    }
    
    public HexyBoard board() {
        return bo;
    }

    public int getPlayer () {
        return playerToMakeMove;
    }

    public int getValue() {
        return eval;
    }
}

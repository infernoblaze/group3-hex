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

    public HexElement(HexyBoard b, int[] m) {
        board = b.getBoard();
        bo = b;
        move = m;
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

    public int getValue() {
        return eval;
    }
}

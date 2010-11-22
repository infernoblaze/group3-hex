/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import java.util.ArrayList;

/**
 *
 * @author Lukas
 */
public class HexyBoard extends Board {

    /**
     * creates a board which has the values as the integer array
     * @param b the integer array
     */
    public HexyBoard(int[][] b) {
        super(b.length);
        for (int i = 0; i < super.getDimensions(); i++) {
            for (int j = 0; j < super.getDimensions(); j++) {
                super.setPiece(i, j, b[i][j]);
            }
        }
    }

    /**
     * finds all possible moves which can be done from
     * @return an Array list of all possible moves
     */
    public HexyBoard getMirrorImage() {
        int[][] mir = new int[getDimensions()][getDimensions()];
        for (int i = (getDimensions() - 1); i >= 0; i--) {
            for (int j = (getDimensions() - 1); j >= 0; j--) {
                mir[getDimensions() - 1 - i][getDimensions() - 1 - j] =
                        this.getBoard()[i][j];
            }
        }
        HexyBoard mirror = new HexyBoard(mir);
        return mirror;
    }

    public ArrayList<int[]> findPossibleMoves(int PlayerID) { // mirror imgaes and rotation included --> needs to be done

        ArrayList<HexyBoard> boards = new ArrayList<HexyBoard>();
        ArrayList<int[]> moves = new ArrayList<int[]>();
        for (int i = 0; i < super.getDimensions(); i++) {
            for (int j = 0; j < super.getDimensions(); j++) {
                if (super.getCell(i, j).getValue() == 0) {
                    moves.add(new int[]{i, j});
                }
            }
        }
        return moves;
    }
}

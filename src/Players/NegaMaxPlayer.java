/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Players;

import Game.*;
import java.util.ArrayList;

/**
 *
 * @author Lukas
 */
public class NegaMaxPlayer implements Player {

    private Game game;
    private int playerId;
    private int boardDimensions;
    private int[][] board;
    private double alpha;
    private double beta;
    private int[] lastmove;
    private int[] bestmove;
    private ArrayList<int[]> possibleMoves;
    private int depth;


    public NegaMaxPlayer(int maxDepth) {
        depth = maxDepth;
        board = game.getBoard().getBoard();
    }

    public void setGame(Game theGame) {
        this.game = theGame;
    }

    public void setPlayerId(int thePlayer) {
        playerId = thePlayer;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void switchPlayer() {
        playerId = playerId%2+1;
    }

    public void setCanSwapSides(boolean state) {
    }

    public int[] getNextMove() {
        alpha = Double.MIN_VALUE;
        beta = Double.MAX_VALUE;
        double bestmove = negaMax(depth, alpha, beta);

        return new int[]{0, 0};
    }

    private void doNextMove() {
        int[] move = possibleMoves.remove(0);
        int x = move[0];
        int y = move[1];
        lastmove = new int[]{x, y, board[x][y]};
        board[x][y] = playerId;
        switchPlayer();
    }

    private void undoMove() {
        board[lastmove[0]][lastmove[1]] = lastmove[2];
        switchPlayer();
    }

    public void findPossibleMoves() {
        possibleMoves.clear();
        for (int i = 0; i < boardDimensions; i++) {
            for (int j = 0; j < boardDimensions; j++) {
                if (board[i][j] == 0) {
                    possibleMoves.add(new int[]{i, j});
                }
            }
        }
    }

    private boolean movesLeft() {
        return !(possibleMoves.size() == 0);
    }

    private double negaMax(int depth, double alpha, double beta) {
        if (depth == 0) {
            return 0; // evaluate();!!!!!
        }
        findPossibleMoves();
        while (movesLeft()) {
            doNextMove();
            double value = -negaMax(depth - 1, -beta, -alpha);
            undoMove();
            if (value >= beta) {
                return beta;
            }
            if (value > alpha) {
                alpha = value;
            }
        }
        return alpha;
    }
}

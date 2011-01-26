/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Players;

import AI.And_Or;
import Game.*;
import java.util.ArrayList;

/**
 *
 * @author Lukas
 */
public class NegaMaxPlayer implements Player {

    private Game game;
    private int playerId, Player;
    private int boardDimensions;
    private Board theBoard;
    private int[][] board;
    private double alpha;
    private double beta;
    private int[] bestmove, nextmove, lastmove;
    private ArrayList<int[]> moves;
    private ArrayList<double[]> levelOne;
    private ArrayList<double[]> bestMoves;
    private int depth;
    private And_Or and;
    private boolean swapAllowed;

    public NegaMaxPlayer(int maxDepth) {
        depth = maxDepth;
        nextmove = new int[]{0, 0};
        lastmove = new int[]{-1, 0};
        moves = new ArrayList<int[]>();
        levelOne = new ArrayList<double[]>();
        bestMoves = new ArrayList<double[]>();
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
        Player = Player % 2 + 1;
    }

    public void setCanSwapSides(boolean state) {
        swapAllowed = state;

    }

    public int[] getNextMove() {
        long start = System.currentTimeMillis();
        theBoard = game.getBoard().clone();
        board = theBoard.getBoard();
        boardDimensions = theBoard.getDimensions();
        alpha = -10000.0;
        beta = 10000.0;
        levelOne.clear();
        bestMoves.clear();
        Player = playerId;
        double best = negamax(depth, alpha, beta);
        findBestMove(best);
//        System.out.println((System.currentTimeMillis()-start));
        return bestmove;
    }

    private void doMove(int x, int y) {
        board[x][y] = Player;
        theBoard.setPiece(x, y, Player);
        switchPlayer();
    }

    private void reMove(int x, int y) {
        board[x][y] = 0;
        theBoard.removePiece(x, y);
        switchPlayer();
    }

    private void findBestMove(double best) {
        for (int i = 0; i < levelOne.size(); i++) {
            if (levelOne.get(i)[2] == best) {
                bestMoves.add(levelOne.get(i));
            }
        }
        bestmove = new int[]{(int) bestMoves.get(0)[0], (int) bestMoves.get(0)[1]};
    }

    private double negamax(int currentDepth, double alpha, double beta) {
        if (currentDepth == 0) {
            if (theBoard.getPieceCount() >= theBoard.getDimensions() * 2 - 1) {
                if (theBoard.checkEnd() == Player) {
                    return 1000;
                } else if (theBoard.checkEnd() == (Player % 2 + 1)) {
                    return -1000;
                }
            }
            and = new And_Or(theBoard, Player);
            double valueRes = theBoard.evaluate(Player);
            double valueAnd = and.evaluate();
            double val = valueRes * 30 + valueAnd;
//            System.out.println("Ev: "+val);
            return val;
        }
        if (swapAllowed) {
            theBoard.swapSides();
            double value = -negamax(currentDepth - 1, -beta, -alpha);
            theBoard.swapSides();
            if (currentDepth == depth) {
                levelOne.add(new double[]{-1, -1, value});
            }

            if (value >= beta) {
                return beta;
            }
            if (value > alpha) {
                alpha = value;
            }
        }
        for (int i = 0; i < boardDimensions; i++) {
            for (int j = 0; j < boardDimensions; j++) {
                if (board[i][j] == 0) {
                    doMove(i, j);
                    double value = -negamax(currentDepth - 1, -beta, -alpha);
                    if (currentDepth == depth) {
                        levelOne.add(new double[]{i, j, value});
                    }
                    reMove(i, j);
                    if (value >= beta) {
                        return beta;
                    }
                    if (value > alpha) {
                        alpha = value;
                    }
                }
            }
        }
        return alpha;
    }
}

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
 * @author Lukas Kang
 * @version 26-01-2011
 * This class returns a move using a negamax algorithm
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

    /**
     * Creates an instance of the class with a given search depth
     * @param maxDepth the search depth limt for the algorithm
     */
    public NegaMaxPlayer(int maxDepth) {
        depth = maxDepth;
        nextmove = new int[]{0, 0};
        lastmove = new int[]{-1, 0};
        moves = new ArrayList<int[]>();
        levelOne = new ArrayList<double[]>();
        bestMoves = new ArrayList<double[]>();
    }

   /**
     * Specifies the game board that the AI will use to calculate it's next
     * move. This should be set once by the game when it is started.
     * @param the Game thr game which will be played
     */
    public void setGame(Game theGame) {
        this.game = theGame;

    }
    /**
     * Specifies the player that this AI is playing. Like Game.PLAYER_ONE
     * @param thePlayer the player id
     */
    public void setPlayerId(int thePlayer) {
        playerId = thePlayer;
    }
    /**
     * Returns the player identifier
     * @return numeric player id
     */
    public int getPlayerId() {
        return playerId;
    }
    /**
     * Switches the player
     */
    public void switchPlayer() {
        Player = Player % 2 + 1;
    }
    /**
     * Specifies whether the algorithm can use the swap sides move
     * @param state
     */
    public void setCanSwapSides(boolean state) {
        swapAllowed = state;

    }
    /**
     * Game should call this method whenever it needs a new "move" from the AI
     * player. Then the AI should do all the things that it needs to do and
     * return x,y coordinates of the new piece.
     * @return array of two integers for x,y coordinates
     */
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
    /**
     * Puts a stone on the board
     * @param x the x coordinate of the move
     * @param y the y coordinate of the move
     */
    private void doMove(int x, int y) {
        board[x][y] = Player;
        theBoard.setPiece(x, y, Player);
        switchPlayer();
    }
    /**
     * Removes a stone from the board
     * @param x the x coordinate of the stone which should be removed
     * @param y the y coordinate of the stone which should be removed
     */
    private void reMove(int x, int y) {
        board[x][y] = 0;
        theBoard.removePiece(x, y);
        switchPlayer();
    }
    /**
     * Finds the best move in the first level of the game tree and stores it in the array "bestmove"
     * @param best the best value which should be determined by the negamax algorithm
     */
    private void findBestMove(double best) {
        for (int i = 0; i < levelOne.size(); i++) {
            if (levelOne.get(i)[2] == best) {
                bestMoves.add(levelOne.get(i));
            }
        }
        bestmove = new int[]{(int) bestMoves.get(0)[0], (int) bestMoves.get(0)[1]};
    }
    /**
     * Algorithm which builds the tree and determines the value the best move has
     *
     * @param currentDepth initially this should be the depth limit
     * @param alpha the alpha value which is the lower bound for all min levels
     * @param beta  the beta value which is the upper bound for all max levels
     * @return the value the best move on the first level has
     */
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

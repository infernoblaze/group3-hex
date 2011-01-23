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
    }

    public int[] getNextMove() {
        theBoard = game.getBoard().clone();
        System.out.println(theBoard.toString());
        board = theBoard.getBoard();
        boardDimensions = theBoard.getDimensions();
        alpha = -10000.0;
        beta = 10000.0;
        levelOne.clear();
        bestMoves.clear();
        Player = playerId;
        double best = max(depth, alpha, beta);
        System.out.println("Best Move : " + best);
        findBestMove(best);
        System.out.println("Doing Move: " + bestmove[0] + ", " + bestmove[1]);
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
        double max = -1000000000;
        for (int i = 0; i < levelOne.size(); i++) {
            if (levelOne.get(i)[2] > max) {
                max = levelOne.get(i)[2];
            }
        }
        for (int i = 0; i < levelOne.size(); i++) {
            if (levelOne.get(i)[2] == max) {
                bestMoves.add(levelOne.get(i));
            }
        }
        int number = (int) (Math.random() * bestMoves.size());
        bestmove = new int[]{(int) bestMoves.get(number)[0], (int) bestMoves.get(number)[1]};
    }

    private double max(int currentDepth, double alpha, double beta) {
        if (currentDepth == 0) {
            if (theBoard.getPieceCount() >= theBoard.getDimensions() * 2 - 1) {
                if (theBoard.checkEnd() == playerId) {
                    return 1000;
                }
                else if(theBoard.checkEnd() == (playerId%2+1)) {
                    return -1000;
                }
            }
            and = new And_Or(theBoard, playerId);
            double val = theBoard.evaluate(playerId)*10+and.evaluate();
//            System.out.println(val);
            return val;
        }
        for (int i = 0; i < boardDimensions; i++) {
            for (int j = 0; j < boardDimensions; j++) {
                if (board[i][j] == 0) {
                    doMove(i, j);
                    double value = min(currentDepth - 1, alpha, beta);
                    System.out.println("Minimum of Last Ones: New Node: Depth: "+(depth-currentDepth+1)+", Evaluation : "+value);
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

    private double min(int currentDepth, double alpha, double beta) {
        if (currentDepth == 0) {
            if (theBoard.getPieceCount() >= theBoard.getDimensions() * 2 - 1) {
                if (theBoard.checkEnd() == playerId) {
                    return 1000;
                }
                else if(theBoard.checkEnd() ==(playerId%2+1)) {
                    return -1000;
                }
            }
            and = new And_Or(theBoard, playerId);
            double val = theBoard.evaluate(playerId)*10+and.evaluate();
//            System.out.println(val);
            return val;
        }
        for (int i = 0; i < boardDimensions; i++) {
            for (int j = 0; j < boardDimensions; j++) {
                if (board[i][j] == 0) {
                    doMove(i, j);
                    double value = max(currentDepth - 1, alpha, beta);
                    System.out.println("Maximum of Last Ones: New Node: Depth: "+(depth-currentDepth+1)+", Evaluation : "+value);
                    if (currentDepth == depth) {
                        levelOne.add(new double[]{i, j, value});
                    }
                    reMove(i, j);
                    if (value <= alpha) {
                        return alpha;
                    }
                    if (value < beta) {
                        beta = value;
                    }
                }
            }
        }
        return beta;
    }
}
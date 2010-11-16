/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import Game.HexyBoard;
import java.util.ArrayList;

/**
 *
 * @author Lukas
 */
public class Minimax {

    private MiniMaxTree tree;
    private int maxDepth;

    public Minimax(HexyBoard root, int d) {
        tree = new MiniMaxTree(root);
        maxDepth = d;
    }
    /**
     * returns the next move to be made
     * @param PlayerID the Player whose turn it is
     * @return the next move
     */
    public int[] getNextMove(int PlayerID) {

        for (int i = 0; i < maxDepth; i++) {
            createNextLevel(PlayerID);
            if (PlayerID == 1) {
                PlayerID = 2;
            } else {
                PlayerID = 1;
            }
        }

        //here comes the minimax evaluation or hexy evaluation
        Node rooty = (Node) tree.root();
        rooty.printTree();
        return rooty.getChildren()[(int)(Math.random()*rooty.getChildren().length)].element().getMove();
    }

    public void createNextLevel(int PlayerID) {
        Node root = (Node) tree.root();
        if (root.getChildren() == null) {
            root.expandNode(PlayerID);
        } else {
            ArrayList<Node> leaves = root.findLeaves();
            for (int i = 0; i < leaves.size(); i++) {
                leaves.get(i).expandNode(PlayerID);
            }
        }
    }
//    public boolean finished() {
//        Node r = (Node) tree.root();
//        return r.finished();
//    }
}

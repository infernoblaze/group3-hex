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
public class MiniMax {

    private MiniMaxTree tree;
    private int maxDepth;
    private Node root;
    private boolean evaluation;

    public MiniMax(HexyBoard rootBoard, int d, boolean e) {
        maxDepth = d; 
        tree = new MiniMaxTree(new Node(new HexElement(rootBoard, null), null, null, null , 0, maxDepth));
        root = (Node)tree.root();
        root.setTree(tree);
    }
    /**
     * returns the next move to be made
     * @param PlayerID the Player whose turn it is
     * @return the next move
     */
    public int[] getNextMove(int PlayerID) {


        Node rooty = (Node) tree.root();
        root.buildTree(evaluation);
//        root.printTree();
//        tree.count(root);
//        System.out.println("Tree Size : "+tree.size());
        Node nextMove = rooty.getMaxChild();
        return nextMove.element().getMove();
    }

//    public void createNextLevel(int PlayerID) {
//        Node root = (Node) tree.root();
//        if (root.getChildren() == null) {
//            root.expandNode();
//        } else {
//            ArrayList<Node> leaves = root.findLeaves();
//            for (int i = 0; i < leaves.size(); i++) {
//                leaves.get(i).expandNode();
//            }
//        }
//    }
}

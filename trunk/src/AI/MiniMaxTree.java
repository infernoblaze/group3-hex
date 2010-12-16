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
public class MiniMaxTree implements Tree<HexElement> {

    private int size;
    private Node root;
    private ArrayList<Integer> HashBoards = new ArrayList<Integer>();

    public MiniMaxTree(Node r) {
        root = r;
    }

    public boolean BoardUsed(HexyBoard b) {      
        return HashBoards.contains(b.hashCode());
    }

    public void addBoard(HexyBoard b) {
        HashBoards.add(b.hashCode());
    }

    public ArrayList<Integer> getUsed() {
        return HashBoards;
    }

    public void count(Node r) {
        size++;
        if (r.getChildren() != null) {
            for (int i = 0; i < r.getChildren().length; i++) {
                count(r.getChildren()[i]);
            }
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return (size == 0);
    }

    public Position root() {
       return root;
    }

    public Position parent(Node p) {
        return p.getParent();
    }

    public boolean isInternal(Node p) {
        return (p.getChildren().length != 0);
    }

    public boolean isExternal(Node p) {
        return (p.getChildren().length == 0);
    }

    public boolean isRoot(Node p) {
        return(p == root);
    }

    public void swapElements(Node p, Node q) {
        HexElement help = (HexElement) p.element();
        p.setElement(q.element());
        q.setElement(help);
    }

    public HexElement replaceElement(HexElement e, Node p) {
        HexElement help = (HexElement) p.element();
        p.setElement(e);
        return help;
    }

}

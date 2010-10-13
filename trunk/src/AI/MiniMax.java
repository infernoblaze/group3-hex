/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package AI;

/**
 *
 * @author Lukas
 */
public class MiniMax<E> implements Tree {

    private int size;
    private int height = 1;
    private Node root;

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
        E help = (E) p.element();
        p.setElement(q.element());
        q.setElement(help);
    }

    public Object replaceElement(Object e, Node p) {
        E help = (E) p.element();
        p.setElement(e);
        return help;
    }

}

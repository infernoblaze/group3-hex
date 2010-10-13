package AI;

/**
 *
 * @author Lukas
 */
public class Node<E> implements Position{

    private E element;
    private Node parent;
    private Node[] children;


    public Node(E e, Node p, Node[] c){
        element = e;
        parent = p;
        children = c;
    }

    public E element() {
        return element;
    }

    public void setElement(E e) {
        element = e;
    }

    public Node[] getChildren() {
        return children;
    }

    public void setChildren(Node[] newChildren) {
        children = newChildren;
    }

    public void addChild(Node parent, Node newChild) {
        Node c[] = parent.getChildren();
        Node newC[] = new Node[c.length+1];
        for(int i = 0 ; i < c.length ; i++) {
            newC[i] = c[i];
        }
        newC[newC.length] = newChild;
        parent.setChildren(newC);
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node newParent) {
        parent = newParent;
    }
}

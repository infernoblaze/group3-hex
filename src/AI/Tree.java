package AI;

/**
 *
 * @author Lukas
 */
public interface Tree<E> {

    public int size();
    public boolean isEmpty();
    public Position root();
    public Position parent(Node p);
    public boolean isInternal(Node p);
    public boolean isExternal(Node p);
    public boolean isRoot(Node p);
    public void swapElements(Node p, Node q);
    public E replaceElement(E e, Node p);




}

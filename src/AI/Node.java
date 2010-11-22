package AI;

import Game.HexyBoard;
import java.util.ArrayList;

/**
 *
 * @author Lukas
 */
public class Node implements Position<HexElement> {

    private HexElement element;
    private Node parent;
    private int depth, maxDepth, alpha, beta;
    private Node[] children;
    private MiniMaxTree tree;

    /**
     * creates a new Node
     * @param e the element of the Node
     * @param p the parent of the Element - null for the root
     * @param c the children of the node - null if none
     */
    public Node(HexElement e, Node p, Node[] c, MiniMaxTree t, int d, int max) {
        element = e;
        parent = p;
        children = c;
        depth = d;
        maxDepth = max;
        alpha = Integer.MIN_VALUE;
        beta = Integer.MAX_VALUE;
        tree = t;
    }

    public void setTree(MiniMaxTree t) {
        tree = t;
    }

    /**
     * returns the current element of the node
     * @return the current element
     */
    public HexElement element() {
        return element;
    }

    /**
     * sets the Element of the node
     * @param e the new Element
     */
    public void setElement(HexElement e) {
        element = e;
    }

    /**
     * returns the children of the Node
     * @return the current children
     */
    public Node[] getChildren() {
        return children;
    }

    /**
     * returns all siblings of the node
     * @return all siblings in an array
     */
    public Node[] getSiblings() {
        ArrayList<Node> sibling = new ArrayList();
        Node[] siblings = new Node[parent.children.length - 1];
        for (int i = 0; i < parent.children.length; i++) {
            sibling.add(children[i]);
        }
        sibling.remove(this);
        for (int i = 0; i < siblings.length; i++) {
            siblings[i] = sibling.get(i);
        }
        return siblings;
    }

    /**
     * sets the children of the node
     * @param newChildren the new children
     */
    public void setChildren(Node[] newChildren) {
        children = newChildren;
    }

    /**
     * adds a new child to the node
     * @param newChild the new child
     */
    public void addChild(Node newChild) {
        Node c[] = getChildren();
        Node newC[] = new Node[c.length + 1];
        for (int i = 0; i < c.length; i++) {
            newC[i] = c[i];
        }
        newC[newC.length] = newChild;
        setChildren(newC);
    }

    /**
     * returns the current Parent
     * @return current Parent
     */
    public Node getParent() {
        return parent;
    }

    /**
     * Sets the Parent Node
     * @param newParent the new parent node
     */
    public void setParent(Node newParent) {
        parent = newParent;
    }

    /**
     * This Method returns if this Nodes Board has a winnign situation
     * @return returns true if it is a winning situation, false if not
     */
    public boolean isEndNode() {
        if (depth > element.board().getDimensions()) {
            return (depth == maxDepth);
        } else {
            return (depth == maxDepth || element.board().checkEnd() != 0);
        }
    }

    public int getDepth() {
        return depth;
    }

    public Node[] expandNode(int PlayerID) {
        HexyBoard mine = element().board();
        ArrayList<int[]> moves = mine.findPossibleMoves(PlayerID);
        ArrayList<HexyBoard> boards = new ArrayList<HexyBoard>();
        for (int i = 0; i < moves.size(); i++) {
            int[][] newB = mine.getBoard();
            newB[moves.get(i)[0]][moves.get(i)[1]] = PlayerID;
            HexyBoard b = new HexyBoard(newB);

//            if(!tree.BoardUsed(b)) {
            boards.add(b);
//            tree.addBoard(b);
//            }

        }
        Node[] myChildren = new Node[boards.size()];
        for (int i = 0; i < boards.size(); i++) {
            myChildren[i] = new Node(new HexElement(boards.get(i), moves.get(i)), this, null, tree, this.getDepth() + 1, maxDepth);
        }
        setChildren(myChildren);
        return myChildren;
    }

    public ArrayList<Node> findLeaves() {
        ArrayList<Node> leaves = new ArrayList<Node>();
        if (getChildren() == null) {
            leaves.add(this);
        } else {
            for (int i = 0; i < getChildren().length; i++) {
                leaves.addAll(getChildren()[i].findLeaves());
            }
        }
        return leaves;
    }

    public void printNode() {
        System.out.println("Depth = " + this.getDepth() + ", Evaluation = " + this.element().getValue());
    }

    public void buildTree() {
        if (this.isEndNode()) {
            evaluate();
            return;
        } else {
            int childrenLength = expandNode(element.getPlayer()).length;
            for (int i = 0; i < childrenLength; i++) {
                getChildren()[i].buildTree();
            }
        }
        evaluate();
    }

    public void printTree() {
        for (int i = 0; i < depth; i++) {
            System.out.print(" ");
        }
        printNode();
        if (children != null) {
            for (int i = 0; i < children.length; i++) {
                children[i].printTree();
            }
        }
    }

    private void evaluate() {

        And_Or a = new And_Or(this.element.board(), depth % 2);
        a.groups();

        if (isEndNode()) {
            this.element.evaluate(a.evaluate()); 
        }
        if (children != null) {
            if (this.depth % 2 != 0) {
                this.element().evaluate(min());
            } else {
                this.element().evaluate(max());
            }
        }
    }

    private int min() {
        int min = children[0].element().getValue();
        for (int i = 1; i < children.length; i++) {
            if (children[i].element().getValue() < min) {
                min = children[i].element().getValue();
            }
        }
        return min;
    }

    public Node getMaxChild() {
        Node max = children[0];
        for (int i = 1; i < children.length; i++) {
            if (children[i].element().getValue() > max.element().getValue()) {
                max = children[i];
            }
        }

        return max;
    }

    public Node getMinChild() {
        Node min = children[0];
        for (int i = 1; i < children.length; i++) {
            if (children[i].element().getValue() < min.element().getValue()) {
                min = children[i];
            }
        }

        return min;
    }

    private int max() {
        int max = children[0].element().getValue();
        for (int i = 1; i < children.length; i++) {
            if (children[i].element().getValue() > max) {
                max = children[i].element().getValue();
            }
        }
        return max;
    }
}

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
    private int depth;
    private Node[] children;

    /**
     * creates a new Node
     * @param e the element of the Node
     * @param p the parent of the Element - null for the root
     * @param c the children of the node - null if none
     */

    public Node(HexElement e, Node p, Node[] c, int d) {
        element = e;
        parent = p;
        children = c;
        depth = d;
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
        return element.board().checkEnd() != 0;
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
            boards.add(b);
        }
        Node[] myChildren = new Node[moves.size()];
        for (int i = 0; i < moves.size(); i++) {
//            boards.get(i).printBoard();
//            System.out.println();
            myChildren[i] = new Node(new HexElement(boards.get(i), moves.get(i)), this, null,this.getDepth()+1);
        }
        setChildren(myChildren);
//        for(int i = 0 ; i < myChildren.length ; i++) {
//            myChildren[i].element().board().printBoard();
//        }
        return myChildren;
    }

    public ArrayList<Node> findLeaves() {
        ArrayList<Node> leaves = new ArrayList<Node>();
        if(getChildren() == null) {
            leaves.add(this);
        }
        else {
            for(int i = 0 ; i < getChildren().length ; i ++) {
                leaves.addAll(getChildren()[i].findLeaves());
            }
        }
        return leaves;
    }

    public void printNode() {
        System.out.println("Depth = "+this.getDepth()+", Evaluation = "+this.element().getValue());
    }

    public void printTree() {
        for(int i = 0 ; i < depth ; i++) {
            System.out.print(" ");
        }
        printNode();
        if(children!=null)
        for(int i = 0 ; i < children.length ; i++) {
            children[i].printTree();
        }
    }

    /**
     * This Method returns if the Tree with this node as root is finished
     * @return returns if this subtree is complete: All leaf nodes are winning situations
     */

//    public boolean finished() { // Problems HERE!!!!!
//        System.out.println("finishLOOP");
//        boolean b = false;
//        if (children == null && !isEndNode()) {
//            System.out.println("End Node found!");
//            return false;
//        }
//        if (isEndNode()) {
//            return true;
//        }
//        for (int i = 0; i < children.length; i++) {
//            if (!children[i].isEndNode()) {
//                System.out.println("child:"+ (i+1)+"is not an EndNode!");
//                if (!children[i].finished()) {
//                    System.out.println("b = false;");
//                   return false;
//                }
//            }
//        }
//        return b;
//    }
}

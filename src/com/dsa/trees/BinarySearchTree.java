package com.dsa.trees;

import java.util.Stack;

@SuppressWarnings({"WeakerAccess", "unused"})
public class BinarySearchTree {
    private static class Node {
        Node left;
        Node right;
        int key;

        Node(int val) {
            this.key = val;
            //this.parent = parent;
        }
    }

    private Node root;

    public BinarySearchTree() {
        root = null;
    }

    /**
     * Inserts a new node in the tree.
     *
     * The insertion takes cares of duplicates and links them as the
     * right child of the original node. This way they form a linked
     * list down the chain with head pointing to the original node.
     */
    public void insert(int key) {
        if (root == null) {
            root = new Node(key);
            return;
        }

        Node newNode = new Node(key);
        Node current = root;
        Node parent = null;

        while (current != null) {
            parent = current;
            if (current.key > key)
                current = current.left;
            else if (current.key < key)
                current = current.right;
            else {
                // make sure all the duplicates form a linked list
                // where the head points to the original node.
                newNode.right = current.right;
                current.right = newNode;
                return;
            }
        }

        // at this point, parent will be pointing to a leaf node
        if (parent.key > key)
            parent.left = newNode;
        else parent.right = newNode;
    }

    private void throwExceptionIfTreeIsEmpty(Node... node) {
        Node examine = node.length == 0 ? root : node[0];
        if (examine == null)
            throw new RuntimeException("Tree is empty!");
    }

    /*
     * The idea is, if we arrive to a node after being done with
     * its right subtree, we mark the node as visited. When the
     * root is marked as visited, we are done walking the tree.
    public void inorderWalkUsingParentNode() {
        if (root == null)
            return;

        Node current = root;
        Node visited = null;

        while (root != visited) {
            if (current == visited) {
                // if current node is at the right subtree of its parent
                // mark the parent as visited
                if (current == current.parent.right)
                    visited = current.parent;

                current = current.parent;
                continue;
            }

            if (current.left != visited && current.left != null) {
                current = current.left;
                continue;
            }

            // done with the left subtree, print the value
            System.out.println(current.key);

            if (current.right != visited && current.right != null) {
                current = current.right;
                continue;
            }

            // done with the right subtree, mark as visited
            visited = current;
        }
    }*/

    public void inorderWalkUsingStack() {
        if (root == null)
            return;

        Node current = root;
        Stack<Node> nodes = new Stack<>();

        while (current != null || nodes.size() > 0) {
            while (current != null) {
                nodes.push(current);
                current = current.left;
            }

            current = nodes.pop();
            System.out.println(current.key);
            current = current.right;
        }
    }

    public void inorderWalkRecursive(Node root) {
        if (root != null) {
            inorderWalkRecursive(root.left);
            System.out.println(root.key);
            inorderWalkRecursive(root.right);
        }
    }

    /**
     * Returns the first node N found in the tree for which
     * N.key == key.
     */
    public Node search(int key) {
        throwExceptionIfTreeIsEmpty();
        Node keyNode = root;
        while (keyNode != null && keyNode.key != key)
            keyNode = keyNode.key > key ? keyNode.left : keyNode.right;
        return keyNode;
    }

    public Node searchRecursive(Node root, int key) {
        if (root == null || root.key == key)
            return root;
        if (root.key > key)
            return searchRecursive(root.left, key);
        return searchRecursive(root.right, key);
    }

    /**
     * There are two cases that we should take care of.
     * <p>
     * 1. When the keyNode (N such that N.key = key) has non-
     * empty right subtree, then the minimum of N.left subtree
     * is the successor.
     * </p>
     * <p>
     * 2. When the key node (N such that N.key = key) has
     * empty right subtree, then the successor either does not exist
     * or it's the lowest node X whose left subtree contains the parent
     * of N or N itself.
     * </p>
     */
    public Node getInorderSuccessor(int key) {
        throwExceptionIfTreeIsEmpty();

        Node current = root;
        Node lastLeft = null;

        while (current.key != key) {
            // We assume that the key exists in the tree
            if (current.key > key) {
                lastLeft = current;
                // Save the node from where we turned left the last time
                current = current.left;
            } else
                current = current.right;
        }

        if (current.right != null)
            return minimum(current.right);

        return lastLeft;
    }

    /**
     * There are two cases that we should take care of.
     * <p>
     * 1. When the key node (N such that N.key = key) has non-
     * empty left subtree, then the maximum of N.right subtree
     * is the predecessor.
     * </p>
     * <p>
     * 2. When the key node (N such that N.key = key) has
     * empty left subtree, then the predecessor either does not exist
     * or it's the lowest node X whose right subtree contains the parent
     * of N or N itself.
     * </p>
     */
    public Node getInorderPredecessor(int key) {
        throwExceptionIfTreeIsEmpty();
        Node current = root;
        Node lastRight = null;

        while (current.key != key) {
            // We assume that the key exists in the tree
            if (current.key > key)
                current = current.left;
            else {
                // Save the node from where we turned right the last time
                lastRight = current;
                current = current.right;
            }
        }

        if (current.left != null)
            return maximum(current.left);

        return lastRight;
    }

    /*public Node getInorderSuccessorUsingParent(int key) {
        Node node = search(key);

        if (node.right != null)
            return minimum(node.right);

        Node parent = node.parent;
        while (parent != null && parent.right == node) {
            node = parent;
            parent = node.parent;
        }
        return parent;
    }

    public Node getInorderPredecessorUsingParent(int key) {
        Node node = search(key);

        if (node.left != null)
            return maximum(node.left);

        Node parent = node.parent;
        while (parent != null && parent.left == node) {
            node = parent;
            parent = node.parent;
        }
        return parent;
    }*/

    /**
     * Maximum is the node with highest key in the tree.
     */
    private Node maximum(Node subtree) {
        throwExceptionIfTreeIsEmpty(subtree);
        while (subtree.right != null)
            subtree = subtree.right;
        return subtree;
    }

    /**
     * Minimum is the node with lowest key in the tree.
     */
    private Node minimum(Node subtree) {
        throwExceptionIfTreeIsEmpty(subtree);
        while (subtree.left != null)
            subtree = subtree.left;
        return subtree;
    }

    public Node minimumRecursive (Node root) {
        if (root.left == null)
            return root;
        return minimumRecursive(root.left);
    }

    public Node maximumRecursive (Node root) {
        if (root.right == null)
            return root;
        return maximumRecursive(root.right);
    }

    private Node parentSearchHelper(int key) {
        Node current = root;
        Node parent = null;

        while (current != null && current.key != key) {
            parent = current;
            current = current.key > key ? current.left : current.right;
        }

        if (current == null)
            throw new RuntimeException("The key does not exist!");

        return parent;
    }

    /**
     * Aids {@code delete} by cleaning up duplicates, if there are any.
     *
     *<p>Because of the way insertion of duplicates is done, if a
     * node N contains duplicates, they are all present as successive
     * right children of N and they don't have any left pointers. This
     * guarantees if we keep going down the right subtree of N, we will
     * discover all k duplicates of N present in the tree in k steps.</p>
     */
    private void duplicateDeleteHelper(Node keyNode) {
        int key = keyNode.key;
        while (keyNode.right != null && keyNode.right.key == key) {
            Node duplicate = keyNode.right;
            keyNode.right = duplicate.right;
        }
    }

    /**
     * Given a key and its parent (which may be null, e.g.: in the case
     * when keyNode is root), finds and returns the node containing the
     * key in this tree. The method is added to save some lines in the
     * already lengthy {@code delete} procedure.
     */
    private Node keyNodeSearchHelper(Node parent, int key) {
        if (parent == null)
            return root;
        if (parent.left != null && parent.left.key == key)
            return parent.left;
        else return parent.right;
    }

    /**
     * Deletes nodes with the given key along with its duplicates.
     *
     * <p>We need to handle three cases.
     *  <ol>
     *   <li>
     *    <p>When the node to be deleted is a leaf node:</p>
     *    <p>In this case, all we do is unlink the leaf node from
     *       its parent.</p>
     *   </li>
     *   <li>
     *    <p>When the node to be deleted has only one child:</p>
     *    <p>In this case, we need to replace the node with it's left
     *       or right child.</p>
     *   </li>
     *   <li>
     *    <p>When the node to be deleted has both, left and right children:</p>
     *    <p>In this case, we replace the node with its successor. In the
     *       process of doing so, we will have to take care of two cases.</p>
     *    <ol>
     *     <li>
     *      <p>Successor is the direct right child of node:</p>
     *      <p>In this case, all we do is replace the node with successor,
     *         leaving the successor's right child alone. Note that the
     *         successor won't have any left child because it is the smallest
     *         element in node's right subtree.</p>
     *     </li>
     *     <li>
     *       <p>Successor is not the direct right child of node:</p>
     *       <p>In this case, we first have to replace the successor with its
     *          right child, meaning, we take the right child of successor and
     *          attach it to the successor's parent as its left child. Then we,
     *          replace the node with successor.</p>
     *     </li>
     *    </ol>
     *   </li>
     *     </ol>
     * </p>
     */
    public void delete(int key) {
        // todo: can we clean this method up a little?
        if (root == null)
            return;

        // if root is to be deleted, parent will be null
        Node parent = parentSearchHelper(key);
        Node keyNode = keyNodeSearchHelper(parent, key);
        boolean isKeyNodeRoot = parent == null;
        boolean isKeyNodeInLeft = !isKeyNodeRoot && parent.left == keyNode;

        // start by getting rid of the duplicates, if any
        duplicateDeleteHelper(keyNode);

        // CASE 1: keyNode is a leaf node
        if (keyNode.left == null && keyNode.right == null) {
            if (isKeyNodeRoot)
                root = null;
            else if (isKeyNodeInLeft)
                parent.left = null;
            else
                parent.right = null;
        }

        // CASE 2a: keyNode has no left child
        else if (keyNode.left == null) {
            if (isKeyNodeRoot)
                root = keyNode.right;
            else if (isKeyNodeInLeft)
                parent.left = keyNode.right;
            else
                parent.right = keyNode.right;
        }

        // CASE 2b: keyNode has no right child
        else if (keyNode.right == null) {
            if (isKeyNodeRoot)
                root = keyNode.left;
            else if (isKeyNodeInLeft)
                parent.left = keyNode.left;
            else
                parent.right = keyNode.left;
        }

        // CASE 3: KeyNode has both, left and right children
        else {
            // find successor of keyNode
            // NOTE: duplicates are already deleted
            Node successor = keyNode.right;

            // find parent of successor
            Node successorsParent = null;
            while (successor.left != null) {
                successorsParent = successor;
                successor = successor.left;
            }

            // CASE 3a: successor is direct right child of keyNode
            if (successorsParent == null) {
                if (isKeyNodeRoot)
                    root = successor;
                else if (isKeyNodeInLeft)
                    parent.left = successor;
                else
                    parent.right = successor;
            }

            // CASE 3b: successor is somewhere in the right subtree of keyNode
            else {
                boolean successorInLeft = successorsParent.left == successor;
                if (successor.right != null) {
                    // replace successor with its right children
                    if (successorInLeft)
                        successorsParent.left = successor.right;
                    else
                        successorsParent.right = successor.right;
                } else {
                    // successor has no right children
                    if (successorInLeft)
                        successorsParent.left = null;
                    else
                        successorsParent.right = null;
                }

                // lastly, swap keys of keyNode and it's successor
                keyNode.key = successor.key;
            }
        }
    }

    public static void main(String[] args) {
        BinarySearchTree bst = new BinarySearchTree();
        bst.insert(100);

        bst.insert(50);
        bst.insert(25);
        bst.insert(12);
        bst.insert(18);
        bst.insert(37);
        bst.insert(75);
        bst.insert(62);
        bst.insert(68);
        bst.insert(87);
        bst.insert(200);
        bst.insert(150);
        bst.insert(125);
        bst.insert(175);
        bst.insert(300);
        bst.insert(250);
        bst.insert(400);

        bst.insert(50);
        bst.inorderWalkRecursive(bst.root);
    }
}
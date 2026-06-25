package lab9;

import java.util.*;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author Your name here
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private class Node {
        /* (K, V) pair stored in this Node. */
        private K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private Node root;  /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /** Returns the value mapped to by KEY in the subtree rooted in P.
     *  or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
        if (p == null) {
            return null;
        }
        int compareValue = key.compareTo(p.key);
        if (compareValue < 0) {
            return getHelper(key, p.left);
        } else if (compareValue > 0) {
            return getHelper(key, p.right);
        } else {
            return p.value;
        }
    }

    /** Returns the value to which the specified key is mapped, or null if this
     *  map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return getHelper(key, this.root);
    }

    /** Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
      * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {
        if (p == null) {
            size = size + 1;
            return new Node(key, value);
        }
        int compareValue = key.compareTo(p.key);
        if (compareValue < 0) {
            p.left = putHelper(key, value, p.left);
        } else if (compareValue > 0) {
            p.right = putHelper(key, value, p.right);
        } else { //found match
            p.value = value;
        }
        return p;
    }

    /** Inserts the key KEY
     *  If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        this.root = putHelper(key, value, this.root);
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }



    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> llSet = new LinkedHashSet<>();
        if (this.root == null) {
            return null;
        }
        setHelper(this.root, llSet);
        return llSet;
    }

    //return the inOrderTraversal starting rooted at Node p
    private void setHelper(Node p, Set<K> set) {
        if (p == null) {
            return;
        }
        setHelper(p.left, set);
        set.add(p.key);
        setHelper(p.right, set);
    }

    /** Removes KEY from the tree if present
     *  returns VALUE removed,
     *  null on failed removal.
     */
    @Override
    public V remove(K key) {
        ResultWrapper result = removeHelper(this.root, key);
        if (result.value == null) { //if removing from empty tree
            return null;
        }

        //if found and deleted, decrease size
        size = size - 1;
        this.root = result.nodeP;
        return result.value;
    }

    private ResultWrapper removeHelper(Node p, K key) {
        if (p == null) {
            return new ResultWrapper(null, null);
        }
        ResultWrapper result;
        int compareKeyValue = key.compareTo(p.key);
        if (compareKeyValue < 0 ) {
            result = removeHelper(p.left, key); //return here after removeHelper(10,10) finishes. p calling it was 30
            p.left = result.nodeP;
        } else if (compareKeyValue > 0) {
            result = removeHelper(p.right, key);
            p.right = result.nodeP;
        } else {
            //we found the key. deletion phase
            if (p.left == null) {
                return new ResultWrapper(p.right, p.value);
            } else if (p.right == null) {
                return new ResultWrapper(p.left, p.value);
            } else {
                //two children. we need to find the successor of p (the smallest greater than p)
                //go right, then go as far left as possible
                Node successor = p.right;
                while (successor.left != null) {
                    successor = successor.left;
                } //reached far left.

                //copy contents of successor node into the original calling node p
                p.key = successor.key;

                //we need to swap the successorNode.value and p.value
                //so when we find the successorNode to delete, we can grab the value we were originally supposed to remove
                V tempSwap = p.value;
                p.value = successor.value;
                successor.value = tempSwap;

                //delete successor node. need this result to bubble up to the top
                result = removeHelper(p.right, successor.key);
                p.right = result.nodeP;
            }
        }
        return new ResultWrapper(p,result.value);
    }

    /** Removes the key-value entry for the specified key only if it is
     *  currently mapped to the specified value.  Returns the VALUE removed,
     *  null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        ResultWrapper result = removeHelper(this.root, key, value);
        if (result.value == null) { //if removing from empty tree
            return null;
        }
        size = size - 1;
        this.root = result.nodeP;
        return result.value;
    }

    private ResultWrapper removeHelper(Node p, K key, V value) {
        if (p == null) {
            return new ResultWrapper(null, null);
        }
        ResultWrapper result;
        int compareKeyValue = key.compareTo(p.key);
        if (compareKeyValue < 0 ) {
            result = removeHelper(p.left, key, value); //return here after removeHelper(10,10) finishes. p calling it was 30
            p.left = result.nodeP;
        } else if (compareKeyValue > 0) {
            result = removeHelper(p.right, key, value);
            p.right = result.nodeP;
        } else {
            //we found the key. deletion phase
            if (p.value.equals(value)) {
                if (p.left == null) {
                    return new ResultWrapper(p.right, p.value);
                } else if (p.right == null) {
                    return new ResultWrapper(p.left, p.value);
                } else {
                    //two children. we need to find the successor of p (the smallest greater than p)
                    //go right, then go as far left as possible
                    Node successor = p.right;
                    while (successor.left != null) {
                        successor = successor.left;
                    } //reached far left.

                    //copy contents of successor node into the original calling node p
                    p.key = successor.key;

                    //we need to swap the successorNode.value and p.value
                    //so when we find the successorNode to delete, we can grab the value we were originally supposed to remove
                    V tempSwap = p.value;
                    p.value = successor.value;
                    successor.value = tempSwap;

                    //delete successor node (which holds the targetValue). need this result to bubble up to the top
                    result = removeHelper(p.right, successor.key, value);
                    p.right = result.nodeP;
                }
            } else {
                //Value doesn't match
                //return the resultWrapper with reference p in-tact and null-value because no object was deleted
                return new ResultWrapper(p, null);
            }
        }
        return new ResultWrapper(p,result.value);
    }

    @Override
    public Iterator<K> iterator() {
        return new BSTMapIterator();
    }

    private class BSTMapIterator implements Iterator<K> {
        private Deque<Node> stack;
        private Node p;

        //constructor will push far left branch onto the stack. bottom element of stack will be root
        public BSTMapIterator() {
            stack = new ArrayDeque<>();
            p = root;
            while (p != null) {
                stack.push(p);
                p = p.left;
            }
        }

        //as long as the stack isn't empty we have another key to process
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        //pop off the most far-left node. store its key value in a return variable
        //from this poppedNode, we can go to right subtree
        //go to right subtree, then go as far left as possible again
        public K next() {
            Node poppedNode = stack.pop();
            K returnKey = poppedNode.key;
            Node p = poppedNode.right;
            while (p != null) {
                stack.push(p);
                p = p.left;
            }
            return returnKey;
        }
    }

    private class ResultWrapper {
        private Node nodeP;
        private V value;

        ResultWrapper(Node p, V value) {
            this.nodeP = p;
            this.value = value;
        }
    }
}

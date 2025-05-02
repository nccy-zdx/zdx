package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author nccy_wp
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
        V value=null;
        if(p==null) return null;
        if(key.compareTo(p.key)<0){
            value=getHelper(key, p.left);
        }//go to left. smaller
        else if(key.compareTo(p.key)>0){
            value=getHelper(key, p.right);
        }//go to right. larger.
        else{
            value=p.value;
        }//equal condition.
        return value;
    }

    /** Returns the value to which the specified key is mapped, or null if this
     *  map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        V value=getHelper(key, root);
        return value;
    }

    /** Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
      * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {
        Node newnode=new Node(key, value);
        if(p==null) return newnode;
        if(key.compareTo(p.key)<0){
            p.left=putHelper(key, value, p.left);
        }//left samller condition.
        else if(key.compareTo(p.key)>0){
            p.right=putHelper(key, value, p.right);
        }
        else{
            p.value=value;
        }//equal condition.
        return p;
    }

    /** Inserts the key KEY
     *  If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        root=putHelper(key, value, root);
        ++size;
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    private void setHelper(Set<K> set,Node p){
        if(p==null) return;
        setHelper(set, p.left);
        set.add(p.key);
        setHelper(set, p.right);
    }//Depth first travel.Inorder.

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        if(root==null) return null;
        Set<K> SetOfKeys=new HashSet<K>();
        setHelper(SetOfKeys, root);
        return SetOfKeys;
    }

    //Helper method to find the Node to be removed.
    private Node removeHelper(K key,Node p){
        if(key.compareTo(p.key)<0){
            return removeHelper(key, p.left);
        } //left smaller.
        else if(key.compareTo(p.key)>0){
            return removeHelper(key, p.right);
        } //right larger.
        else{
            return p;
        } //equal.
    }

    private void NoChildRemove(K key,Node p){
        if(key.compareTo(p.key)<0){
            if(p.left.key.compareTo(key)==0){
                p.left=null;
            }//remove
            else NoChildRemove(key, p.left);
        } //left smaller.
        else if(key.compareTo(p.key)>0){
            if(p.right.key.compareTo(key)==0){
                p.right=null;
            }//remove
            else NoChildRemove(key, p.right);
        } //right larger.
        else{
            p=null;
        }
    }

    private void OneChildRemove(K key,Node p){
        if(key.compareTo(p.key)<0){
            if(p.left.key.compareTo(key)==0){
                if(p.left.right!=null){
                    p.left=p.left.right;
                }
                else{
                    p.left=p.left.left;
                }//Remove
            }
            else NoChildRemove(key, p.left);
        } //left smaller.
        else if(key.compareTo(p.key)>0){
            if(p.right.key.compareTo(key)==0){
                if(p.right.left!=null){
                    p.right=p.right.left;
                }
                else{
                    p.right=p.right.right;
                }//remove
            }
            else NoChildRemove(key, p.right);
        } //right larger.
        else{
            if(p.right!=null){
                p=p.right;
            }
            else{
                p=p.left;
            }//remove
        }
    }

    private void TwoChildRemove(K key,Node p){
        if(key.compareTo(p.key)<0){
            if(p.left.key.compareTo(key)==0){
                p.left=leftmaxmove(p.left);
            }
            else TwoChildRemove(key, p.left);
        } //left smaller.
        else if(key.compareTo(p.key)>0){
            if(p.right.key.compareTo(key)==0){
                p.right=leftmaxmove(p.right);
            }
            else TwoChildRemove(key, p.left);
        } //right larger.
        else{
            Node leftmaxParent=root.left;
            Node leftmax=root.left;
            while (leftmax.right!=null) {
                leftmaxParent=leftmax;
                leftmax=leftmax.right;
            }
            if(leftmax.left!=null) leftmaxParent.right=leftmax.left;
            else leftmaxParent.right=null;
            if(leftmax!=leftmaxParent){
                leftmax.right=root.right;
                leftmax.left=root.left;
                root=leftmax;
            }
            else{
                leftmax.right=root.right;
                root=leftmax;
            }
        } //equal condition.
    }

    //Helper method to save space.
    private Node leftmaxmove(Node p){
        Node leftmaxParent=p;
        Node leftmax=p;
        while (leftmax.right!=null) {
            leftmaxParent=leftmax;
            leftmax=leftmax.right;
        }
        if(leftmax.left!=null) leftmaxParent.right=leftmax.left;
        else leftmaxParent.right=null;
        leftmax.left=p.left;
        leftmax.right=p.right;
        return leftmax;
    }

    /** Removes KEY from the tree if present
     *  returns VALUE removed,
     *  null on failed removal.
     */
    @Override
    public V remove(K key) {
        V value=get(key);
        if(value==null) return null;
        Node removeNode=removeHelper(key, root);
        if(removeNode.left==null&&removeNode.right==null){
            NoChildRemove(key,root);
        } //no child case.
        else if(removeNode.left!=null&&removeNode.right!=null){
            TwoChildRemove(key, root);
        } //two child case.
        else{
            OneChildRemove(key,root);
        }// one child case.
        --size;
        return value;
    }

    /** Removes the key-value entry for the specified key only if it is
     *  currently mapped to the specified value.  Returns the VALUE removed,
     *  null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        if(get(key)!=value) return null;
        else{
            return remove(key);
        }
    }

    private class BSTMapIterator implements Iterator<K>{
        Stack<Node> s=new Stack<Node>();
        public BSTMapIterator(){
            s.push(root);
        }
        public boolean hasNext(){
            return (!s.isEmpty());
        }
        public K next(){
            Node n=s.pop();
            int num;
            if(n.left!=null&&n.right!=null){
                num=2;
                s.push(n.right);
                s.push(n.left);
            }
            else if(n.left==null&&n.right==null) num=0;
            else{
                num=1;
                if(n.right==null) s.push(n.left);
                else s.push(n.right);
            }
            return n.key;
        }
    }

    @Override
    public Iterator<K> iterator() {
        return new BSTMapIterator();
    }
}
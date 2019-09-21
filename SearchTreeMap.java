package sherCoin;

import java.util.Comparator;

public class SearchTreeMap<Key,Value> {
	/*class BSTnode<K, V> {
		// *** fields ***
		private Key key;
		private Value value;
		private BSTnode<Key, Value> left, right;

		// *** constructor ***
		public BSTnode(Key key, Value value,
				BSTnode<Key, Value> left, BSTnode<Key, Value> right) {
			this.key = key;
			this.value = value;
			this.left = left;
			this.right = right;
		}

		// *** methods ***

		public Key getKey() {
			return key;
		}
		public Value getValue() { return value; }
		public void setValue(Value newV) { value = newV; }

		public BSTnode<Key, Value> getLeft() {

			return left;
		}
		public BSTnode<Key, Value> getRight() {

			return right;
		}

	}
	private BSTnode<Key, Value> root; // ptr to the root of the BST

	// *** constructor ***
	public SearchTreeMap() { root = null; } 

	public boolean lookup(Key key) {
		return lookup(root, key);
	}

	private boolean lookup(BSTnode<Key, Value> n, Key key) {
		if (n == null) {
			return false;
		}

		if (n.getKey().equals(key)) {
			return true;
		}
		String temp = key.toString();
		String temp2 = n.key.toString();
		if (temp.compareTo(temp2) < 0) {
			// key < this node's key; look in left subtree
			return lookup(n.getLeft(), key);
		}

		else {
			// key > this node's key; look in right subtree
			return lookup(n.getRight(), key);
		}
	}

	public Value put(Key key, Value value) {
		BSTnode p;   // Help variable


		if ( root == null )
		{  // Insert into an empty BST

			root = new BSTnode( key, value,null,null );
			return null;
		}




		//  If found, put() is update....
		if ( lookup(key))
		{
			p.value = value;                  // Update value
			return (Value) p.value;
		}


		BSTnode q = new BSTnode( key, value, null,null );

		// q.root = p;         // p is the parent
		String temp = key.toString();
		String temp2 = p.key.toString();

		if ( temp.compareTo(temp2 ) < 0 )
			p.left = q;                   // Add q as left child
		else
			p.right = q;                  // Add q as right child
		return value;
	}*/
	protected int stmComparisonCtr = 0;

	private Node<Key,Value> root = null;
	private int size = 0;

	private class Node<Key,Value> 
	{
		Key key;
		Value value;
		Node<Key,Value> left, right;
		Node<Key, Value> parent;

		Node(Key key, Value value, Node<Key,Value> left, Node<Key,Value> right) 
		{
			this.key = key;
			this.value = value;
			this.left = left;
			this.right = right;
			parent=null;
		}

		public Object getKey() {

			return key;
		}
		public Node<Key, Value> getLeft() {

			return left;
		}
		public Node<Key, Value> getRight() {

			return right;
		}

		public void setLeft(Node<Key,Value> newLeft) {
			left = newLeft;

		}

		public void setRight(Node<Key,Value> newRight) {
			right = newRight;

		}

	}

	private Node<Key, Value> search(Key key) {
		Node<Key, Value> n = root;
		String temp = key.toString();
		String temp2;
		while (n != null) {
			stmComparisonCtr++;

			temp2 = n.key.toString();
			int comp = temp.compareTo(temp2);
			if (comp == 0) {
				return n;
			}
			if (comp < 0) {
				n = n.left;
			}
			else {
				n = n.right;
			}
		}
		return null;
	}
	final Node<Key,Value> getNode(Object key) {
		// Offload comparator-based version for sake of performance

		if (key == null)
			throw new NullPointerException();
		Comparable<? super Key> k = (Comparable<? super Key>) key;
		Node<Key,Value> p = root;
		while (p != null) {
			stmComparisonCtr++;

			int cmp = k.compareTo(p.key);
			if (cmp < 0)
				p = p.left;
			else if (cmp > 0)
				p = p.right;
			else
				return p;
		}
		return null;
	}
	public boolean containsKey(Object obj){
		Key key = (Key) obj;
		return search(key) != null;
	}


	public Value get(Key newKey) {
		Node<Key,Value> p = search(newKey);


		return (p==null ? null : p.value);
	}



	public Value put(Key key, Value value) {
		Node<Key,Value> n = getNode(key);
		if (n == null) {                  // key was not in tree
			root = put(root, key, value);   // we must add it
			++size;           
			return null;
		}
		else {  // key was in tree, we replaced value
			Value old_value = n.value;
			n.value = value;
			return old_value;  // return the old value
		}
	}

	private Node<Key,Value> put(Node<Key,Value> n, Key key, Value value) {
		if (n == null) {
			return new Node<>(key, value, null, null);
		}
		String temp = key.toString();
		String temp2 = n.key.toString();
		int comp = temp.compareTo(temp2);
		if (comp < 0) {
			n.left = put(n.left, key, value);
			return n;
		}
		if (comp > 0) {
			n.right = put(n.right, key, value);
			return n;
		}
		// we will never reach here based on the usage in public put
		return n;
	}

	
	public Value remove(Key k)
	   {
	       Node<Key,Value> p;        // Help variable
	       Node<Key,Value> parent;   // parent node
	       Node<Key,Value> next;     // nextessor node

	       /* --------------------------------------------
	          Find the node with key == "key" in the BST
	          -------------------------------------------- */
	       p = getNode(k);

	       if ( ! k.equals( p.key ) )
	          return null;                       // Not found ==> nothing to delete....


	       /* ========================================================
	          Hibbard's Algorithm
	          ======================================================== */
	       try {
	       if ( p.left == null && p.right == null ) // Case 0: p has no children
	       {
	          parent = p.parent;

	          /* --------------------------------
	             Delete p from p's parent
	             -------------------------------- */
	          if ( parent.left == p )
	             parent.left = null;      // p was left child
	          else
	             parent.right = null;     // p was the right child

	          return null;
	       }

	       if ( p.left == null )    // Case 1a: p has 1 (right) subtree
	       {
	          parent = p.parent;

	          /* ----------------------------------------------
	             Link p's right child as p's parent child
	             ---------------------------------------------- */
	          if ( parent.left == p )
	             parent.left = p.right;     // p has a right subtree
	          else
	             parent.right = p.right;    // p has a right subtree

	          return null;
	       }

	       if ( p.right== null )       // Case 1b: p has 1 (left) child
	       {
	          parent = p.parent;

	          /* ----------------------------------------------
	             Link p's left child as p's parent child
	             ---------------------------------------------- */
	          if ( parent.left == p )
	             parent.left = p.left;
	          else
	             parent.right = p.left;

	          return null;
	       }

	       /* ================================================================
	          Tough case: node has 2 children - find nextessor of p

	          next(p) is as as follows:  1 step right, all the way left

	          Note: next(p) has NOT left child !
	          ================================================================ */

	       // (1) Find nextessor(p)
	       next = p.right;                  // Go right once step
	                                        // BTW, p has a right subtree
	                                        // because p has 2 children....

	       while ( next.left != null )      // Go all the way left downwards
	           next = next.left;

	       /* -----------------------------------------
	          Now: next = the nextessor of p
		  ----------------------------------------- */

	       // (2) Replace k with k's nextessor;
	       p.key = next.key;                // Replace p with nextessor
	       p.value = next.value;

	       // (3) Make the nextessor's parent point to
	       //  nextessor's right subtree;
	       parent = next.parent;            // Prepare to delete

	       parent.left = next.right;     // Link right tree to parent's left
	       }catch(Exception NullPointerException) {}

	       return null;

	   }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//public void remove(Key key) {
		//root = delete(root, key);
	//}

	//private  Node<Key,Value> delete(Node<Key,Value> n, Key key) {
		
		
		
		/*if (n == null) {
			return null;
		}
		String temp = key.toString();
		String temp2 = n.key.toString();
		if (key.equals(n.getKey())) {
			// n is the node to be removed
			// code must be added here
		}

		else if (temp.compareTo(temp2) < 0) {
			n.setLeft( delete(n.getLeft(), key) );
			return n;
		}

		else {
			n.setRight( delete(n.getRight(), key) );
			return n;
		}
		return null;*/
	//}

	public int getNumstmComparisons()
	{
		return stmComparisonCtr;
	}



}

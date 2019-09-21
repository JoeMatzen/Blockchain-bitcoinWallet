package sherCoin;



import java.util.*;
import java.util.Map.Entry;

/** implements a Map with a linked list */
public class LinkMap<Key,Value>implements Map<Key,Value>, Entry<Key,Value>
{
	// Node class for creating a linked list map

	public class Node<Key,Value> {
		private Key key;
		private Value value;

		private Node<Key,Value> next;
		/**
		 * default constructor
		 */
		public Node() {
			key = null;
			value = null;

			next = null;
		}
		/**
		 * Parameterized constructor - data
		 */
		public Node(Key k,Value v) {
			key = k;
			value = v;
			next = null;
		}
		public Node(Node<Key,Value> n) {

			next = n;
		}
		/**
		 * Parameterized constructor - data, next
		 */
		public Node(Key k, Value v, Node<Key,Value> n) {
			key = k;
			value = v;
			next = n;
		}
		/**
		 * getNext method 
		 * @returns next
		 */
		public Node<Key,Value> getNext() {
			return next;
		}
		/**
		 * setNext method 

		 */
		public void setNext(Node<Key,Value> n) {
			next = n;
		}

		public Key getKey() {
			return key;
		}
		public Value getValue() {
			return value;
		}
		/**
		 * setData method 

		 */
		public void setkey(Key data) {
			this.key = data;
		}
		public void setValue(Value data) {
			this.value = data;
		}
		public String toString()
		{
			return "Key = " + key + "\nValue = " + value ;
		}
	}


	protected int size;   // size of linkmap
	protected Node<Key,Value> head;  // first node
	protected int comparisonCtr = 0;

	// Class constructor
	public LinkMap()
	{
		head = new Node<Key, Value>();    // Make an empty list
		size = 0;   // No entries
	}

	/** clears map */
	public void clear()
	{
		head= new Node<Key, Value>();
		size = 0;
	} // clear

	/** checks if the map contains the key */
	public boolean containsKey(Object key) throws NullPointerException
	{
		if (key == null)
			throw new NullPointerException();

		Node<Key, Value> trav = head;
		boolean found = false;
		while (trav.getNext()!= null)
		{
			if ((trav.getKey())==(key))
				found = true;  // exiting the loop
			else
				trav = trav.getNext();
		comparisonCtr++;
		}
		return found;


	} // containsKey

	/** checks if the map contains the value */
	public boolean containsValue(Object value)
	{
		if (value == null)
			throw new NullPointerException();

		Node trav = head;

		boolean found = false;
		while (trav.getNext() != null)
			if ((trav.getValue())==(value))
				found = true;
			else
				trav = trav.getNext();
		return found;
	} // containsValue

	/** returns the set version of the map */
	public Set<Map.Entry<Key,Value>> entrySet() throws NullPointerException
	{

		Set temp = new HashSet<Object>();
		Entry tempEntry =null;


		Node trav = head;
		if (trav == null)
			throw new NullPointerException();
		while (trav.getNext() != null)
		{
			tempEntry = new sherCoin.Entry<>(trav.getKey(),trav.getValue());

			temp.add(tempEntry);

			trav = trav.getNext();
		}


		return temp; // nothing to return yet
	}// entrySet

	/** check if another map has the same keys and values */
	public boolean equals()
	{
		return true;  // since nothing is in any LinkiMap they are all equal now
	}// equals

	/** returns the sum of all the hashes of all the entries */
	public int hashCode()
	{
		return 0;   // no entries
	}//hashCode

	/** returns value for key */
	public Value get(Object key)
	{
		if (key == null)
			throw new NullPointerException();

		Node trav = head;

		boolean found = false;
		while (!found && trav.getNext() != null)
		{

			if (trav.getKey()==(key))
			{
				found = true;
			}

			else
			{
				trav = trav.getNext();
				comparisonCtr++;
			}

		}
		return (Value) trav.getValue();  // nothing to find
	} // get
	public int getComparisons()
	{
		return comparisonCtr /2 ;
	}
	/** is the Map empty */
	public boolean isEmpty()
	{
		return size==0;  // no code to put anything in
	}// isEMpty

	/** returns a set of all the keys */
	public Set<Key> keySet()
	{


		Set temp = new HashSet<Object>();

		Node trav = head;
		if (trav == null)
			throw new NullPointerException();
		while (trav.getNext() != null)
		{
			temp.add(trav.getKey());

			trav = trav.getNext();
		}

		return temp; // nothing to return yet
		// nothing to return
	}// keySet

	/** puts a value with a key */
	public Value put(Key key, Value value)
	{
		Node<Key,Value> trav = head;
		boolean found = false;



		while (!found && trav.getNext() != null)
		{
			if ((trav.getKey())==(key))
			{
				trav.setValue(value);
				found = true;
			}
			else
			{
				trav = trav.getNext();
				//comparisonCtr++;
			}


		}
		if(!found)
		{


			Node<Key,Value> trav2 = head;
			while(trav2.getNext()!= null)
			{
				trav2 = trav2.getNext();
			}

			Node<Key,Value> newNode = new Node<Key,Value>(key, value);
			trav2.setNext(newNode);
			newNode.setNext(new Node<Key,Value>());
			size++;
			size++;



		}


		return value;  // return the value being put in
	}//  put

	/** removes the key value pair  */
	public Value remove(Object key)
	{
		Node<Key,Value> trav = head;

		boolean found = false;
		while (!found && trav.getNext() != null)
		{
			if ((trav.getNext().getKey())==(key))
			{
				
				trav.setNext(trav.getNext().getNext());
				size--;
				found = true;
			}
			else
				trav = trav.getNext();
			//comparisonCtr++;

		}


		return null; // nothing to return
	} // remove

	/** size */
	public int size()
	{
		return size;
	}// size

	/**returns a collection of all the values */
	public Collection<Value> values()
	{

		Set temp = new HashSet<Object>();

		Node trav = head;
		if (trav == null)
			throw new NullPointerException();
		while (trav.getNext() != null)
		{

			temp.add(trav.getValue());

			trav = trav.getNext();
		}


		return temp; // nothing to return yet
	}// values

	/** copies a map */
	public void putAll(Map<? extends Key, ? extends Value> map)
	{
		// can't do this yet
	} // putAll
	public String toString()
	{
		String temp = "";
		Node trav = head;

		boolean found = false;
		while (!found && trav.next != null)
		{
			temp += trav.getKey() + " " + trav.getValue();
			System.out.println(trav);
			trav = trav.next;
		}

		return "";
	}

	@Override
	public Key getKey() {

		return null;
	}

	@Override
	public Value getValue() {

		return getValue();
	}

	@Override
	public Value setValue(Value arg0) {
		// TODO Auto-generated method stub
		return null;
	}
} // LinkMap

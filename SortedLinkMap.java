package sherCoin;

import sherCoin.LinkMap.Node;

public class SortedLinkMap<Key, Value> extends LinkMap<Key,Value> implements Comparable<Key> {

	protected int sortedComparisonCtr = 0;

	// Default constructor for SortedLinkMap
	public SortedLinkMap()
	{
		super(); // inheriting from parent class
	}

	/** puts a value with a key into a sorted list by key*/
	public Value put(Key key, Value value)
	{
		Node<Key,Value> trav = head;
		boolean found = false;
		String temp2;

		String temp = key.toString();

		while (!found && trav.getNext() != null)
		{
			temp2=trav.getNext().getKey().toString();
			if ((temp2.compareTo(temp))>0)
			{
				trav.setValue(value);
				found = true;
			}else
			{
				trav = trav.getNext();
			}
			
		}
		if(!found)
		{

			Node<Key,Value> trav2 = head;
			
			boolean finished = false;

			// adding new item to list at first instance after key comparison value is >0
			while(trav2.getNext()!= null &&  !finished)
			{
				temp2=trav2.getNext().getKey().toString();
				if((temp2.compareTo(temp))>0)
				{
					finished = true;
				}
				trav2 = trav2.getNext();
				sortedComparisonCtr++;  // Counting number of comparisons

			}

			//adding node to linked list 
			Node<Key,Value> newNode = new Node<Key,Value>(key, value);
			newNode.setNext(trav2.getNext()); 
			trav2.setNext(newNode);
			size++;


		}
		//remove(null);

		return value;  // return the value being put in
	}//  put

	public Value get(Object key)
	{
		String temp2;
		String temp = key.toString();
		if (key == null)
			throw new NullPointerException();

		Node<Key,Value> trav = head;

		boolean found = false;
		while (!found && trav.getNext() != null)
		{
			temp2=trav.getNext().getKey().toString();
			if ((temp2.compareTo(temp))>0)
			{
				found = true;

			}

			else
			{
				trav = trav.getNext();

				
			}
			sortedComparisonCtr++; // Counting number of comparisons
		}

		return (Value) trav.getValue();  // nothing to find
	} // get
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
			//sortedComparisonCtr++;  // Counting number of comparisons

		}


		return null; // nothing to return
	} // remove

	public int getNumSortedComparisons()
	{
		return sortedComparisonCtr;
	}
	@Override
	public int compareTo(Key o) {
		int result = ((Comparable<Key>) this.getKey()).compareTo(o);
		return result;
	}
}

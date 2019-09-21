package sherCoin;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

public class HashMap<Key,Value> {

	protected int hashmapComparisonCtr = 0;
	private ListNode<Key,Value>[] HashTable;   //array of linked lists
	private int size= 2;  //default size of HashMap

	private class ListNode<Key, Value> 
	{
		Key key;
		Value value;
		ListNode<Key,Value> next;
		//list node default constructor
		public ListNode(Key key, Value value, ListNode<Key,Value> next)
		{
			this.key = key;
			this.value = value;
			this.next = next;
		}
	}


	// hashmap default constructor
	public HashMap()
	{
		//asking user to input desired hashmap size
		String message =  JOptionPane.showInputDialog(null,"What size HashMap do you want?");
		size = Integer.parseInt(message);
		HashTable = new ListNode[size];  // creates new hashmap of whatever size is equal too
	}
	//returns location on hashtable to store key
	private int hash(Key key)
	{
		return Math.abs(key.hashCode()) % size; //determining where in HashTable to store key
	}

	public int getNumHashMapComparisons()
	{
		return hashmapComparisonCtr;
	}

	/** puts a value with a key */
	public Value put(Key key, Value data)
	{
		if(key==null)
		{
			return null;    
		}

		//hashing the key.
		int hash=hash(key);

		//creating new ListNode
		ListNode<Key,Value> tempListNode = new ListNode<Key,Value>(key, data, null);

		//if there are no listnodes at the HashTable location, making a new one
		if(HashTable[hash] == null)
		{
			HashTable[hash] = tempListNode;
		}else
		{
			ListNode<Key,Value> prev = null;
			ListNode<Key,Value> trav = HashTable[hash];

			while(trav != null)
			{ 
				if(trav.key.equals(key))
				{           
					if(prev==null)
					{  
						tempListNode.next = trav.next;

						HashTable[hash]=tempListNode;

						break;
					}
					else{
						tempListNode.next = trav.next;
						prev.next = tempListNode;
						//hashmapComparisonCtr++;

						break;
					}
				}

				prev = trav;
				trav = trav.next;
				//hashmapComparisonCtr++;

			}
			prev.next = tempListNode;
		}
		return null;
	}

	/** returns value for key */
	public Value get(Key key)
	{
		int hash = hash(key);
		ListNode<Key,Value> temp = HashTable[hash];


		if(HashTable[hash] == null)
		{
			return null;
		}

		while(temp != null)
		{
			if(temp.key.equals(key))
			{

				return temp.value; // returning value of listnode when key is matched
			}
			temp = temp.next;
			hashmapComparisonCtr++;

		}         
		return null;   

	}

	/** removes a key  */
	public Value remove(Key key)
	{

		int hash = hash(key);
		ListNode<Key,Value> prev = null;
		ListNode<Key,Value> trav = HashTable[hash];

		if(HashTable[hash] == null)
		{
			return null; // nothing to return
		}


		while(trav != null)
		{ 
			if(trav.key.equals(key))
			{               
				if(prev==null)
				{  
					HashTable[hash]=HashTable[hash].next; // deleting first list node
					return null;  // nothing to return
				}
				else
				{
					prev.next=trav.next;
					return null;  // nothing to return
				}
			}
			prev = trav;
			trav = trav.next;
		}

		return null; // nothing to return


	}

}



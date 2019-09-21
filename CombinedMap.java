package sherCoin;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class CombinedMap<Key, Value> implements Map<Key,Value> {
	private LinkMap<Key,Value>  theLinkMap = new LinkMap<Key,Value>();
	private SortedLinkMap<Key,Value>  theSortedLinkMap = new SortedLinkMap<Key,Value>();
	
	

	public String comparisonsString()
	{
		return "LinkMap did "+ theLinkMap.getComparisons()+ " comparisons\nSortedLinkMap did 	"+theSortedLinkMap.getNumSortedComparisons() +" comparisons";
	}
	@Override
	public Value get(Object key) {
		Value got = theLinkMap.get(key);
		Value sortedGot = theSortedLinkMap.get(key);
		if(! got.equals(sortedGot))
			try {
				throw new Exception("When getting "+key+"LinkMap returns: "+got+"SortedLinkMap returns: "+sortedGot);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		System.out.println("LinkMap did "+ theLinkMap.getComparisons()+ " comparisons\nSortedLinkMap did 	"+theSortedLinkMap.getNumSortedComparisons() +" comparisons");
		return got;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean containsKey(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsValue(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<Entry<Key, Value>> entrySet() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<Key> keySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value put(Key arg0, Value arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putAll(Map<? extends Key, ? extends Value> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Value remove(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Collection<Value> values() {
		// TODO Auto-generated method stub
		return null;
	}
}

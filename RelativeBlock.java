
package sherCoin;

import java.math.BigInteger;
import java.util.ArrayList;

public class RelativeBlock  extends Block
{
	/** all the other blocks */
	protected ArrayList<Block> chainOfBlocks;

	//Block Constructor.

	public RelativeBlock(String data,String previousHash, ArrayList<Block> chain ) 
	{
		super(data,previousHash);
		chainOfBlocks = chain;
	}   //  Block

	/** makes sure that the new block's has is relatively prime to all the other blocks */
	public boolean isMined()
	{ 
		BigInteger bigHash = new BigInteger(hash,16);
		// traverse the blocks checking for relative primeness
		for(int i=1; i < chainOfBlocks.size(); i++) 
		{
			// get the block from the chain we are looking at
			Block block = chainOfBlocks.get(i);
			if(block == this) continue;   // don't check yourself
			// get the hash that the block
			BigInteger blockHash = new BigInteger(block.getHash(),16);
			if(bigHash.gcd(blockHash).intValue() != 1) return false; // not relatively prime to another block
			
		} // for
		return true;	// relatively prime to all other blocks

		
	} // isMIned
} // SimpleBloxk
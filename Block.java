package sherCoin;

import java.util.ArrayList;
import java.util.Date;

public abstract class Block 
{

	protected String hash;						// this block's hash
	protected String previousHash;		// hash of previous block
	protected String data; 					//our data will be a simple message.
	protected long timeStamp; 			//as number of milliseconds since 1/1/1970.
	protected boolean mined = false;	// true when a block is a mined block
	protected long nonce = 0;				// used for block mining
	protected String merkleRoot;
	protected ArrayList<Transaction> transactions = new ArrayList<Transaction>(); //our data will be a simple message.



	
	/** sets up a block based on the previous block */
	public Block(String data,String previousHash ) 
	{
		this.data = data;
		this.previousHash = previousHash;
		this.timeStamp = new Date().getTime()/3600000;
		this.hash = calculateHash(); //Making sure we do this after we set the other values.
	}   //  Block
	
	/** hashes based on all the data in the block (except the current hash) using Sha256 */
	public String calculateHash()
	 {
		String calculatedhash = StringUtil.applySha256( previousHash + Long.toString(timeStamp) + data+nonce/*+ merkleRoot*/ );
		return calculatedhash;
	} // calculateHash
	
	public String toString()
	{
		return "hash: " + hash +"\npreviousHash: "+previousHash+"\ndata: "+data+"\ntimestamp: "+timeStamp+"\n";
	}
	
	public String getData()
	{ return data; }
	
	public String getHash()
	{ return hash; }
	
	public long getTimeStamp()
	{ return timeStamp; }
	
	public String getPreviousHash()
	{ return previousHash; }

	/** searches for a valid block according to the isMined criteria */
	public void mineBlock()
	 {
		merkleRoot = StringUtil.getMerkleRoot(transactions);

		while(!isMined()) {
			nonce ++;
			hash = calculateHash();
		}
		mined = true;
	}	

	/** Add transactions to this block */
	public boolean addTransaction(Transaction transaction) {
		//process transaction and check if valid, unless block is genesis block then ignore.
		if(transaction == null) return false;		
		if((previousHash != "0")) 
		{
			if((transaction.processTransaction() != true)) 
			{
				System.out.println("Transaction failed to process. Discarded.");
				return false;
			}// if
		}// if
		transactions.add(transaction);
		hash = calculateHash();   // update hash to contain transaction
		System.out.println("Transaction Successfully added to Block");
		return true;
	}// addTransaction
	
	//  true when a block is mined
	public abstract  boolean isMined();
} // Bloxk


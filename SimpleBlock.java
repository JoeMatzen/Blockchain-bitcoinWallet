package sherCoin;


public class SimpleBlock  extends Block
{



	//Block Constructor.

	public SimpleBlock(String data,String previousHash ) 
	{
		super(data,previousHash);
	}   //  Block

	/**  very simple criterion for successful block mining (no 0's in hash) */
	public boolean isMined()
	{ return hash.indexOf('0') == -1; }
} // SimpleBloxk

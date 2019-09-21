package sherCoin;


import java.security.*;
import java.security.PublicKey;
import java.util.ArrayList;

/** defines a transaction of SherCoin's */
public class Transaction
 {
	
	protected String transactionId; // this is also the hash of the transaction.
	protected PublicKey sender; // senders address/public key.
	protected PublicKey reciepient; // Recipients address/public key.
	protected float value;
	public byte[] signature; // this is to prevent anybody else from spending funds in our wallet.
	
	public String getTransactionId() { return transactionId;}
	public PublicKey getSender() { return sender;}
	public PublicKey getReceipient() { return reciepient;}
	public float getValue() { return value;}
	
	public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
	public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();
	
	private static int sequence = 0; // a rough count of how many transactions have been generated. 
	
	// Constructor: 
	public Transaction(PublicKey from, PublicKey to, float value,  ArrayList<TransactionInput> inputs)
	 {
		this.sender = from;
		this.reciepient = to;
		this.value = value;
		this.inputs = inputs;
	} // Constructor
	
	// This Calculates the transaction hash (which will be used as its Id)
	private String calulateHash()
	 {
		sequence++; //increase the sequence to avoid 2 identical transactions having the same hash
		return StringUtil.applySha256(
				StringUtil.getStringFromKey(sender) +
				StringUtil.getStringFromKey(reciepient) +
				Float.toString(value) + sequence
				);
	} // calculateHash
	
	/** Signs all the data we dont wish to be tampered with. */
	public void generateSignature(PrivateKey privateKey) 
	{
		String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient) + Float.toString(value)	;
		signature = StringUtil.applyECDSASig(privateKey,data);		
	} // generateSignature

	/** Verifies the data we signed hasnt been tampered with */
	public boolean verifiySignature() 
	{
		String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient) + Float.toString(value)	;
		return StringUtil.verifyECDSASig(sender, data, signature);
	}// verifySignature
	
	/** Returns true if new transaction could be created.	*/
	public boolean processTransaction() 
	{

		if(verifiySignature() == false) 
		{
			System.out.println("#Transaction Signature failed to verify");
			return false;
		}// if
				
		//gather transaction inputs (Make sure they are unspent):
		for(TransactionInput i : inputs) 
		{
			i.UTXO = SherCoin.unsortedUTXOs.get(i.transactionOutputId);
			i.UTXO = SherCoin.sortedUTXOs.get(i.transactionOutputId);
			i.UTXO = SherCoin.hashmapUTXOs.get(i.transactionOutputId);
			i.UTXO = SherCoin.searchTreeMapUTXOs.get(i.transactionOutputId);

		}// for

		//check if transaction is valid:
		if(getInputsValue() < SherCoin.minimumTransaction) 
		{
			System.out.println("#Transaction Inputs to small: " + getInputsValue());
			return false;
		}// if
		
		//generate transaction outputs:
		float leftOver = getInputsValue() - value; //get value of inputs then the left over change:
		transactionId = calulateHash();
		outputs.add(new TransactionOutput( this.reciepient, value,transactionId)); //send value to recipient
		outputs.add(new TransactionOutput( this.sender, leftOver,transactionId)); //send the left over 'change' back to sender		
				
		//add outputs to Unspent list
		try{
		for(TransactionOutput o : outputs) 
		{
			SherCoin.unsortedUTXOs.put(o.id , o);
			SherCoin.sortedUTXOs.put(o.id , o);
			SherCoin.hashmapUTXOs.put(o.id , o);
			SherCoin.searchTreeMapUTXOs.put(o.id , o);



		}// for
		}catch(Exception NullPointerExcepton){}
		
		//remove transaction inputs from UTXO lists as spent:
		for(TransactionInput i : inputs) 
		{
			if(i.UTXO == null) continue; //if Transaction can't be found skip it 
			SherCoin.unsortedUTXOs.remove(i.UTXO.id);
			SherCoin.sortedUTXOs.remove(i.UTXO.id);
			SherCoin.hashmapUTXOs.remove(i.UTXO.id);
			SherCoin.searchTreeMapUTXOs.remove(i.UTXO.id);



		}// for
		
		return true;
	}// processTransaction
	
/** returns sum of inputs(UTXOs) values */
	public float getInputsValue() 
	{
		float total = 0;
		for(TransactionInput i : inputs) 
		{
			if(i.UTXO == null) continue; //if Transaction can't be found skip it 
			
			total += i.UTXO.value;
		}// for
		
		return total;
	}// getInputsValue

/** returns sum of outputs: */
	public float getOutputsValue() 
	{
		float total = 0;
		for(TransactionOutput o : outputs) 
		{
			total += o.value;
		}// for
		return total;
	} //getOutputsValjue
} // Transaction

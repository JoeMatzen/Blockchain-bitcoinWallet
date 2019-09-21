package sherCoin;

import java.security.*;
import java.util.*;
import java.util.Map.Entry;

public class Wallet {

	protected PrivateKey privateKey;
	protected PublicKey publicKey;

	public PrivateKey getPrivateKey() { return privateKey; }
	public PublicKey getPublicKey() { return publicKey; }

	public Map<String,TransactionOutput> UTXOs = new LinkMap<String,TransactionOutput>(); //only UTXOs owned by this wallet.

	public Wallet()
	{
		generateKeyPair();	
	}// Wallet

	/** sets the private and public keys of the wallet using DSA 512 */
	protected void generateKeyPair() 
	{
		try
		{
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			//java.security.spec.ECGenParameterSpec ecSpec = new java.security.spec.ECGenParameterSpec("secp256r1");
			// Initialize the key generator and generate a KeyPair
			keyGen.initialize(512);   //512 bytes provides an acceptable security level
			KeyPair keyPair = keyGen.generateKeyPair();
			// Set the public and private keys from the keyPair
			privateKey = keyPair.getPrivate();
			publicKey = keyPair.getPublic();
		}catch(Exception e) 
		{
			throw new RuntimeException(e);
		} // catch
	}// generateKey

	/** returns balance and stores the UTXO's owned by this wallet in this.UTXOs */
	public float getBalance() {
		float total = 0;	
		//System.out.println(UTXOs);	

		//Set<Entry<String, TransactionOutput>> testSet = SherCoin.UTXOs.entrySet();

		//System.out.println(testSet);

		//for(Entry<String, TransactionOutput> item : testSet)

		try {
		for (Map.Entry<String, TransactionOutput> item: SherCoin.unsortedUTXOs.entrySet())
		{

			//System.out.println(item);

			TransactionOutput UTXO = item.getValue();

			//System.out.println(UTXOs);	
			try{
				if(UTXO.isMine(publicKey)) { //if output belongs to me ( if coins belong to me )
					
					UTXOs.put(UTXO.id,UTXO); //add it to our list of unspent transactions.
					total += UTXO.value ; 
				}
			}catch(Exception NullPointerException){} 
		}  
		}catch(Exception NullPointerException){}
		return total;
		
	}

	/** Generates and returns a new transaction from this wallet. */
	public Transaction sendFunds(PublicKey _recipient,float value ) 
	{	
		if(value<0)
			return null;
		if(getBalance() < value) 
		{ //gather balance and check funds.
			System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
			return null;
		}// if
		//create array list of inputs
		ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();

		float total = 0;
		try{
			for (Map.Entry<String, TransactionOutput> item: UTXOs.entrySet())
			{
				TransactionOutput UTXO = item.getValue();
				total += UTXO.value;
				inputs.add(new TransactionInput(UTXO.id));
				if(total > value) break;
			}// for
		}catch(Exception NullPointerExcepton){}
		Transaction newTransaction = new Transaction(publicKey, _recipient , value, inputs);
		newTransaction.generateSignature(privateKey);

		for(TransactionInput input: inputs)
		{
			UTXOs.remove(input.transactionOutputId);
		} // for
		return newTransaction;
	}// sendFunds
}// Wallet

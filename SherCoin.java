package sherCoin;


import java.util.*;

import java.security.*;
import java.io.*;


public class SherCoin implements Coin
{
	public static ArrayList<Block> blockchain = new ArrayList<Block>(); 
	public static Map<String,TransactionOutput> sortedUTXOs = new SortedLinkMap<String,TransactionOutput>(); //list of all unspent transactions.
	public static Map<String,TransactionOutput> unsortedUTXOs = new LinkMap<String,TransactionOutput>(); //list of all unspent transactions.
	public static HashMap<String, TransactionOutput> hashmapUTXOs = new HashMap<String,TransactionOutput>(); //list of all unspent transactions.
	public static SearchTreeMap<String, TransactionOutput> searchTreeMapUTXOs = new SearchTreeMap<String,TransactionOutput>(); //list of all unspent transactions.



	public static float minimumTransaction = 0; 
	public static Wallet walletA;
	public static Wallet walletB;
	public static Transaction genesisTransaction;
	public static String CopyRight = "CopyRight David B. Sher 2018";


	/** code that executes */
	public static void main(String[] args) 
	{	
		outputCopyRight(System.out);

		//Create the new wallets 
		walletA = new Wallet();
		walletB = new Wallet();
		Wallet coinbase = new Wallet();
		int maxTransactions=0;  // the number of transactions to perform (for each wallet) 

		// read the number of transactions
		while(maxTransactions < 1)
			try
		{
				System.err.print("How many transactions? ");
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
				maxTransactions = Integer.parseInt(bufferedReader.readLine());				
		} catch(Exception e) {}

		//create genesis transaction, which sends 100 NoobCoin to walletA: 
		genesisTransaction = new Transaction(coinbase.publicKey, walletA.publicKey, 100f, null);
		genesisTransaction.generateSignature(coinbase.privateKey);	 //manually sign the genesis transaction	
		genesisTransaction.transactionId = "0"; //manually set the transaction id
		genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.reciepient, genesisTransaction.value, genesisTransaction.transactionId)); //manually add the Transactions Output
		
		sortedUTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); //its important to store our first transaction in the UTXOs list.
		unsortedUTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); //its important to store our first transaction in the UTXOs list.
		hashmapUTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); //its important to store our first transaction in the UTXOs list.
		searchTreeMapUTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); //its important to store our first transaction in the UTXOs list.



		System.out.println("Creating and Mining Genesis block... ");
		Block genesis = new RelativeBlock("Genesis","0",blockchain);
		genesis.addTransaction(genesisTransaction);
		addBlock(genesis);
		String previousHash = genesis.hash;    // keep track of the hash of the last added block
		// for random numbers
		Random rn = new Random();
		rn.setSeed(0);
		//testing
		for(int count=0;count<maxTransactions;count++)
		{
			Block block1 = new RelativeBlock("Transaction "+(2*count),previousHash,blockchain);
			System.out.println("\nWalletA's balance is: " + walletA.getBalance());
			float randomMoney=Math.abs((float) (rn.nextInt()%100));										//Math.abs prevents negative numbers being passed
			//while(randomMoney>walletA.getBalance()&&randomMoney>0)												//BUG-FIX: This while loop prevents funds from trying to be sent
			//{																								//if there are not enough in the wallet to be passed
			//	randomMoney=Math.abs((float) (rn.nextInt()%100));
			//}
			System.out.println("\nWalletA is Attempting to send funds ("+randomMoney+") to WalletB...");
			if(block1.addTransaction(walletA.sendFunds(walletB.publicKey, randomMoney))) 
			{
				addBlock(block1);
				previousHash = block1.hash;
			}// if
			//System.out.println(UTXOs);
			System.out.println("\nWalletA's balance is: " + walletA.getBalance());
			System.out.println("WalletB's balance is: " + walletB.getBalance());
			Block block2 = new RelativeBlock("Transaction "+(2*count+1),previousHash,blockchain);
			randomMoney=Math.abs((float) (rn.nextInt()%100));													//Math.abs prevents negative numbers being passed
			//while(randomMoney>walletB.getBalance()&&randomMoney>0)													//BUG-FIX: This while loop prevents funds from trying to be sent 
			//{																									//if there are not enough in the wallet to be passed
			//	randomMoney=Math.abs((float) (rn.nextInt()%100));
			//}
			System.out.println("\nWalletB Attempting to send  ("+randomMoney+") to WalletA...");
			if(block2.addTransaction(walletB.sendFunds(walletA.publicKey, randomMoney))) 	
			{
				addBlock(block2);
				previousHash = block2.hash;
			}// if


			System.out.println("\nWalletA's balance is: " + walletA.getBalance());
			System.out.println("WalletB's balance is: " + walletB.getBalance());
		} // for
		isChainValid();

	}// entering Canada

	public static Boolean isChainValid() 
	{
		Block currentBlock; 
		Block previousBlock;
		Map<String,TransactionOutput> tempUTXOs = new LinkMap<String,TransactionOutput>(); //a temporary working list of unspent transactions at a given block state.
		tempUTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));

		//loop through blockchain to check hashes:
		for(int i=1; i < blockchain.size(); i++) 
		{

			currentBlock = blockchain.get(i);
			previousBlock = blockchain.get(i-1);
			//compare registered hash and calculated hash:
			if(!currentBlock.hash.equals(currentBlock.calculateHash()) )
			{
				System.out.println("#Current Hashes not equal: block "+i+" : "+currentBlock.toString()+"\ncalculateHash: "+currentBlock.calculateHash());
				return false;
			}// if
			//compare previous hash and registered previous hash
			if(!previousBlock.hash.equals(currentBlock.previousHash) ) 
			{
				System.out.println("#Previous Hashes not equa: block "+(i-1)+" : "+currentBlock.toString()+" : "+previousBlock.toString());
				return false;
			}// if
			//check if hash is solved
			if(!currentBlock.isMined()) 
			{
				System.out.println("#This block hasn't been mined: block "+i+" : "+currentBlock.toString());
				return false;
			}// if

			//loop thru blockchains transactions:
			TransactionOutput tempOutput;
			for(int t=0; t <currentBlock.transactions.size(); t++) 
			{
				Transaction currentTransaction = currentBlock.transactions.get(t);

				if(!currentTransaction.verifiySignature()) 
				{
					System.out.println("#Signature on Transaction(" + t + ") is Invalid");
					return false; 
				}// if
				if(currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) 
				{
					System.out.println("#Inputs are note equal to outputs on Transaction(" + t + ")");
					return false; 
				} // if

				for(TransactionInput input: currentTransaction.inputs) 
				{	
					tempOutput = tempUTXOs.get(input.transactionOutputId);

					if(tempOutput == null) 
					{
						System.out.println("#Referenced input on Transaction(" + t + ") is Missing");
						return false;
					}// if

					if(input.UTXO.value != tempOutput.value) 
					{
						//System.out.println("#Referenced input Transaction(" + t + ") value is Invalid");
						return false;
					}// if

					tempUTXOs.remove(input.transactionOutputId);
				} // for

				for(TransactionOutput output: currentTransaction.outputs) 
				{
					tempUTXOs.put(output.id, output);
				}// for

				if( currentTransaction.outputs.get(0).reciepient != currentTransaction.reciepient) 
				{
					System.out.println("#Transaction(" + t + ") output reciepient is not who it should be");
					return false;
				}// if
				if( currentTransaction.outputs.get(1).reciepient != currentTransaction.sender) 
				{
					System.out.println("#Transaction(" + t + ") output 'change' is not sender.");
					return false;
				}// if

			} // for

		}// for
		System.out.println("Blockchain is valid");
		System.out.println("Number of comparisons made in UnsortedLinkMap: " +((LinkMap<String, TransactionOutput>) unsortedUTXOs).getComparisons());               // ** prints number of comparisons for linkmap 
		System.out.println("Number of comparisons made in SortedLinkMap: " +(((SortedLinkMap<String, TransactionOutput>) sortedUTXOs).getNumSortedComparisons()));  // ** prints number of comparisons for sorted link map 
		System.out.println("Number of comparisons made in HashMap: " +(((HashMap<String, TransactionOutput>) hashmapUTXOs).getNumHashMapComparisons()));  // ** prints number of comparisons for hashmap 
		System.out.println("Number of comparisons made in SearchTreeMap: " +(((SearchTreeMap<String, TransactionOutput>) searchTreeMapUTXOs).getNumstmComparisons()));  // ** prints number of comparisons for search tree map


		return true;
	}// isChainValid

	public static void addBlock(Block newBlock) 
	{
		newBlock.mineBlock();
		blockchain.add(newBlock);
	}// addBlock

	public static void outputCopyRight(PrintStream output)
	{
		output.println(CopyRight);
	}// outputCopyRight
} 

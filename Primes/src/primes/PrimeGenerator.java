package primes;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import bramble.node.slave.*;

public class PrimeGenerator {
	
	private Long upperBound;
	private Long lowerBound;
	private ArrayList<Long> primes;
	private final int jobID;
	private final String senderIP;
	
	public PrimeGenerator(String senderIP, int jobID, ArrayList<? extends Serializable> initializationData){
		this.senderIP = senderIP;
		initializeJob(initializationData);
		this.jobID = jobID;
	}
	
	/**
	 * Called before the job is run.
	 * 
	 * Initializes the upper/lower bounds of numbers to check in this job.
	 */
	private void initializeJob(ArrayList<? extends Serializable> initializationData) {
		
		if(initializationData == null || initializationData.size() != 2){
			System.out.println("Received null initialization data " + initializationData);
			return;
		}
		this.lowerBound = (Long) initializationData.get(0);
		this.upperBound = (Long) initializationData.get(1);
		this.primes = new ArrayList<>();
		
		
		if(upperBound < lowerBound){
			throw new NumberFormatException("Upper bound must be larger than lower bound");
		}
		
	}

	/**
	 * The main method of the job.
	 */
	public void run() {	
		try{
			getPrime();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Loops over integers between the lower and upper bounds checking whether they're prime.
	 * 
	 * Prime numbers are added to an array for later use. 
	 * 
	 * @param lowerBound - The smallest number to check for primality
	 * @param upperBound - The largest number to check for primality
	 * @throws IOException
	 */
	private void getPrime() throws IOException {
		
		Long startTime = System.currentTimeMillis();
		
		for(Long n = lowerBound; n <= upperBound; n++) {
			if(PrimalityTests.isPrime(n)){
				primes.add(n);
			}
		}
		
		Long endTime = System.currentTimeMillis();
		Long duration = (endTime - startTime);
		
		String information = "Found all primes between " + lowerBound + " and " + upperBound + " in " + duration + " ms.";
		
		SlaveNode.sendData(senderIP, jobID, information, primes);
	}
	
}

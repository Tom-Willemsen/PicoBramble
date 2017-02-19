package primes;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.logging.log4j.LogManager;

import bramble.node.slave.SlaveNode;

public class PrimeGenerator {
	
	private Long upperBound;
	private Long lowerBound;
	private Collection<Serializable> primes;
	private final int jobId;
	private final SlaveNode slaveNode;
	
	/**
	 * Generates prime numbers.
	 * 
	 * @param slaveNode - the slave node to send data back to
	 * @param jobId - the numeric job Id
	 * @param initializationData - the initialization data
	 */
	public PrimeGenerator(SlaveNode slaveNode, int jobId, 
			Collection<Serializable> initializationData){
		
		initializeJob(new ArrayList<>(initializationData));
		this.jobId = jobId;
		this.slaveNode = slaveNode;
	}
	
	/**
	 * Called before the job is run.
	 * 
	 * <p>Initializes the upper/lower bounds of numbers to check in this job.</p>
	 * 
	 * @param initializationData the initialization data
	 */
	private void initializeJob(ArrayList<Serializable> initializationData) {
		
		if(initializationData == null || initializationData.size() != 2){
			LogManager.getLogger().warn("Received null initialization data " 
					+ initializationData);
			return;
		}
		this.lowerBound = (Long) initializationData.get(0);
		this.upperBound = (Long) initializationData.get(1);
		this.primes = new ArrayList<>();
		
		
		if(upperBound < lowerBound){
			throw new NumberFormatException(
					"Upper bound must be larger than lower bound");
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
	 * <p>Prime numbers are added to an array for later use</p>
	 * 
	 * @param lowerBound - The smallest number to check for primality
	 * @param upperBound - The largest number to check for primality
	 * @throws IOException - if sending the data failed
	 */
	private void getPrime() throws IOException {
		
		Long startTime = System.nanoTime();
		
		for(Long n = lowerBound; n < upperBound; n++) {
			if(PrimalityTests.isPrime(n)){
				primes.add(n);
			}
		}
		
		Long endTime = System.nanoTime();
		Long duration = (endTime - startTime)/1000000L;
		
		String information = "Found all primes between " + lowerBound 
				+ " and " + upperBound + " in " + duration + " ms.";
		slaveNode.sendData(jobId, information, primes);
	}
	
}

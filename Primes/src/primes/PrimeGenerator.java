package primes;

import java.io.IOException;
import java.util.ArrayList;

import bramble.slavenode.*;

public class PrimeGenerator implements Runnable, IJobRunner {
	
	private Long upperBound;
	private Long lowerBound;
	private ArrayList<Long> primes;
	
	public void initializeJob(int jobID, ArrayList<?> initializationData) {

		this.lowerBound = (Long) initializationData.get(0);
		this.upperBound = (Long) initializationData.get(1);
		this.primes = new ArrayList<Long>();
		
		if(upperBound < lowerBound){
			throw new NumberFormatException("Upper bound must be larger than lower bound");
		}
		
	}

	public void run() {
			
		try{
			getPrime(this.lowerBound, this.upperBound);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getPrime(Long lowerBound, Long upperBound) throws IOException {
		
		Long startTime = System.currentTimeMillis();
		
		for(Long n = lowerBound; n <= upperBound; n++) {
			if(PrimalityTests.isPrime(n)){
				primes.add(n);
			}
		}
		
		Long endTime = System.currentTimeMillis();
		Long duration = (endTime - startTime);
		
		String information = "Found all primes between " + lowerBound + " and " + upperBound + " in " + duration + " ms.";
		
		SlaveNode.sendData(0, information, primes);
	}
	
}

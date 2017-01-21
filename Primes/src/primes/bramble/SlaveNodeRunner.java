package primes.bramble;

import java.io.Serializable;
import java.util.ArrayList;

import primes.PrimeGenerator;
import bramble.slavenode.SlaveNode;

/**
 * This is an example of how a SlaveNode can be run.
 * 
 * This class is responsible for passing a job from the 
 * API to a class that will accept a job.
 * 
 * @author Tom
 *
 */
public class SlaveNodeRunner extends SlaveNode{
	
	public static void main(String[] args){			
		(new SlaveNodeRunner()).listenForever();	
	}

	protected void runJob(int jobID, ArrayList<Serializable> initializationData) {		
		PrimeGenerator primeGenerator = new PrimeGenerator(jobID, initializationData);
		primeGenerator.run();		
	}	
}

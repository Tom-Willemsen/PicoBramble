package primes;

import java.io.Serializable;
import java.util.ArrayList;

import bramble.slavenode.SlaveNode;

public class SlaveNodeRunner extends SlaveNode{
	
	public static void main(String[] args){
		
		SlaveNodeRunner processManager = new SlaveNodeRunner();		
		processManager.listenForever();
		
	}

	protected void runJob(int jobID, ArrayList<Serializable> initializationData) {
		
		PrimeGenerator primeGenerator = new PrimeGenerator();
		primeGenerator.initializeJob(jobID, initializationData);
		primeGenerator.run();
		
	}
	
	
}

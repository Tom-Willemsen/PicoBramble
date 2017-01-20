package primes;

import java.io.Serializable;
import java.util.ArrayList;

import bramble.slavenode.SlaveNode;

public class ProcessManager extends SlaveNode{
	
	public static void main(String[] args){
		
		ProcessManager processManager = new ProcessManager();		
		processManager.listenForever();
		
	}
	
	public ProcessManager(){
		super();
	}

	protected void runJob(int jobID, ArrayList<Serializable> initializationData) {
		
		PrimeGenerator primeGenerator = new PrimeGenerator();
		primeGenerator.initializeJob(0, initializationData);
		primeGenerator.run();
		
	}
	
	
}

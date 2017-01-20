package primes;

import java.util.ArrayList;

import bramble.slavenode.SlaveNode;

public class ProcessManager extends SlaveNode{

	private static final int THREADS = 1;
	
	public static void main(String[] args){
		
		ProcessManager processManager = new ProcessManager();
		
		processManager.listen();
		
//		Long lower = new Long(1);
//		Long upper = new Long((long) (24*Math.pow(10,6)));
//		
//		Long difference = upper - lower;
//		Long perThread = difference/THREADS + 1;
//		
//		for (int i=0; i<THREADS; i++){
//			
//			ArrayList<Long> initdata = new ArrayList<Long>(2);
//			initdata.add(lower+i*perThread);
//			initdata.add(lower+(i+1)*perThread - 1);
//			
//			PrimeGenerator primeGenerator = new PrimeGenerator();
//			primeGenerator.initializeJob(0, initdata);
//			new Thread(primeGenerator).start();
//		}
		
	}
	
}

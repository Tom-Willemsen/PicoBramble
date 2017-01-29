 package primes.bramble;

import java.io.Serializable;
import java.util.ArrayList;

import primes.PrimeGenerator;
import bramble.node.slave.ISlaveNodeRunner;
import bramble.node.slave.SlaveNode;

/**
 * This is an example of how a SlaveNode can be run.
 * 
 * This class is responsible for passing a job from the 
 * API to a class that will accept a job.
 * 
 * @author Tom
 *
 */
public class SlaveNodeRunner implements ISlaveNodeRunner{
	
	private static String IPADDR;
	
	@SuppressWarnings("unused")
	public static void main(String[] args){	
		
		if(args.length != 1){
			System.out.println("Didn't find an IP on the command line, exiting");
			System.exit(1);
		} else {
			IPADDR = args[0];
			System.out.println("Slave node initiated, my IP is " + IPADDR);
		}
		
		new SlaveNodeRunner();
	}
	
	public SlaveNodeRunner(){
		(new SlaveNode<>(/*IPADDR, */this)).listenForever();
	}

	@Override
	public void runJob(int jobID, ArrayList<Serializable> initializationData) {	
		//System.out.print("[" + jobID + "] ");
		PrimeGenerator primeGenerator = new PrimeGenerator(IPADDR, jobID, initializationData);
		primeGenerator.run();
	}

}

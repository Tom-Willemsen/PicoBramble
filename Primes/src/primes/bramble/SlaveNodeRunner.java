 package primes.bramble;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import primes.PrimeGenerator;
import bramble.networking.Handshake;
import bramble.slavenode.IJobRunner;
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
public class SlaveNodeRunner implements IJobRunner{
	
	private static String IPADDR;
	
	public static void main(String[] args){	
		
		if(args.length != 1){
			System.out.println("Didn't find an IP on the command line, exiting");
			System.exit(1);
		} else {
			IPADDR = args[0];
			System.out.println("Slave node initiated, my IP is " + IPADDR);
		}
		
		try {
			(new Handshake(IPADDR)).send();
		} catch (IOException e) {
			System.out.println("Couldn't connect to the master node");
			System.exit(1);
		}
		
		new SlaveNode<SlaveNodeRunner>(new SlaveNodeRunner());
	}

	public void runJob(int jobID, ArrayList<Serializable> initializationData) {	
		PrimeGenerator primeGenerator = new PrimeGenerator(IPADDR, jobID, initializationData);
		primeGenerator.run();		
	}

	@Override
	public IJobRunner clone() {
		return this.clone();
	}

}

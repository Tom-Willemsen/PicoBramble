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
 * PicoBramble API to a class that will perform a job.
 * 
 */
public class SlaveNodeRunner implements ISlaveNodeRunner{
	
	private final String ipAddress;
	private SlaveNode slaveNode;
	
	public static void main(String[] args){
		
		if(args.length != 1){
			System.out.println("Didn't find an IP on the command line, exiting");
			return;
		}
		
		String ipAddress = args[0];
		System.out.println("Slave node initiated, my IP is " + ipAddress);
		(new SlaveNodeRunner(ipAddress)).initialize();
	}
	
	public SlaveNodeRunner(String ipAddress){
		this.ipAddress = ipAddress;
	}
	
	public void initialize(){
		this.slaveNode = new SlaveNode(this.ipAddress, this);
		slaveNode.run();
	}

	@Override
	public void runJob(int jobID, ArrayList<Serializable> initializationData) {	
		PrimeGenerator primeGenerator = new PrimeGenerator(slaveNode, jobID, initializationData);
		primeGenerator.run();
	}

}

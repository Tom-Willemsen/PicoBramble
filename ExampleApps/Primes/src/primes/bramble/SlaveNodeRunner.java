 package primes.bramble;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;

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
			LogManager.getLogger().fatal("Didn't find an IP on the command line, exiting");
			return;
		}
		
		String ipAddress = args[0];
		try{
			(new SlaveNodeRunner(ipAddress)).initialize();
			LogManager.getLogger().info("Slave node initiated, my IP is " + ipAddress);
		} catch (IOException e){
			LogManager.getLogger().fatal("Couldn't initialize slave node.", e);
			return;
		}
	}
	
	public SlaveNodeRunner(String ipAddress){
		this.ipAddress = ipAddress;
	}
	
	public void initialize() throws IOException{
		this.slaveNode = new SlaveNode(this.ipAddress, this);
		slaveNode.run();
	}

	@Override
	public void runJob(int jobID, ArrayList<Serializable> initializationData) {	
		PrimeGenerator primeGenerator = new PrimeGenerator(slaveNode, jobID, initializationData);
		primeGenerator.run();
	}

}

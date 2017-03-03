package primes.bramble;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import primes.PrimeGenerator;
import bramble.node.slave.ISlaveNodeRunner;
import bramble.node.slave.SlaveNode;

/**
 * This is an example of how a SlaveNode can be run.
 * 
 * <p>This class is responsible for passing a job from the 
 * PicoBramble API to a class that will perform a job.</p>
 * 
 */
public class SlaveNodeRunner implements ISlaveNodeRunner{

	private final String ipAddress;
	private SlaveNode slaveNode;

	private static final Logger logger = LogManager.getLogger();

	/**
	 * Entry point.
	 * @param args command line arguments
	 */
	public static void main(String[] args){

		if(args.length != 1){
			logger.fatal("Didn't find an IP on the command line, exiting");
			return;
		}

		String ipAddress = args[0];
		try{
			(new SlaveNodeRunner(ipAddress)).initialize();
			logger.info("Slave node initiated, my IP is " + ipAddress);
		} catch (IOException e){
			logger.fatal("Couldn't initialize slave node.", e);
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
	public Collection<Serializable> runJob(Collection<Serializable> initializationData) {	
		PrimeGenerator primeGenerator = 
				new PrimeGenerator(initializationData);
		return primeGenerator.run();
	}

	@Override
	public void onError(Exception exception) {
		logger.error("Fatal exception in slave node: ", exception);		
	}

}

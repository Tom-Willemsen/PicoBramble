package noop.bramble;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;

import org.apache.logging.log4j.LogManager;

import bramble.node.slave.ISlaveNodeRunner;
import bramble.node.slave.SlaveNode;

public class SlaveNodeRunner implements ISlaveNodeRunner {

	private final String ipAddress;
	private SlaveNode slaveNode;

	/**
	 * Entry point.
	 * @param args command line arguments
	 */
	public static void main(String[] args){

		if(args.length != 1){
			LogManager.getLogger().fatal(
					"Didn't find an IP on the command line, exiting");
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
	public void runJob(int jobId, Collection<Serializable> initializationData) {	
		try {
			slaveNode.sendData(jobId, initializationData.toString(), initializationData);
		} catch (IOException e) {
			LogManager.getLogger().error("Couldn't send data.", e);
			return;
		}
	}

}

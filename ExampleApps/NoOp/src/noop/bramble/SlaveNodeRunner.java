package noop.bramble;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bramble.node.slave.ISlaveNodeRunner;
import bramble.node.slave.SlaveNode;

public class SlaveNodeRunner implements ISlaveNodeRunner {

	private static final Logger logger = LogManager.getLogger();
	private final String ipAddress;
	private SlaveNode slaveNode;

	/**
	 * Entry point.
	 * @param args command line arguments
	 */
	public static void main(String[] args){

		if(args.length != 1){
			logger.fatal(
					"Didn't find an IP on the command line, exiting");
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
	public Object runJob(Object initializationData) {	
		return initializationData;
	}

	@Override
	public void onDataTransferError(Exception exception) {
		logger.error(exception);
	}

	@Override
	public void onCalculationError(Exception exception) {
		logger.error(exception);
	}

}

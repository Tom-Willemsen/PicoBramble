package primes.bramble;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import bramble.networking.JobResponseData;
import bramble.node.manager.Manager;
import bramble.node.master.IMasterNodeRunner;
import bramble.webserver.WebApi;

/**
 * This is an example of how a MasterNode can be run.
 * 
 * <p>This class is responsible for intialising the master node system
 * and for accepting incoming data from the slave nodes.</p>
 * 
 */
public class MasterNodeRunner implements IMasterNodeRunner {
	
	/**
	 * Entry point.
	 * @param args - command line arguments
	 */
	public static void main(String[] args){
		Manager manager;
		try {
			manager = new Manager(new MasterNodeRunner(), new ControllerNodeRunner());
		} catch (IOException e) {
			LogManager.getLogger().fatal("Couldn't initialize Manager.", e);
			return;
		}
		manager.launchAll();	
	}
	
	@Override
	public void parseJobResponseData(JobResponseData jobResponseData) {
		WebApi.publishMessage("Job [" + jobResponseData.getJobIdentifier() + "] replied: "
				+ jobResponseData.getMessage());
		
	}
	
}

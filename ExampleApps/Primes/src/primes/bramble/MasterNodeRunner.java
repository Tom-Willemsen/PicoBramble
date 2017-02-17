package primes.bramble;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import bramble.networking.JobResponseData;
import bramble.node.manager.Manager;
import bramble.node.master.IMasterNodeRunner;
import bramble.webserver.WebAPI;

/**
 * This is an example of how a MasterNode can be run.
 * 
 * This class is responsible for intialising the master node system
 * and for accepting incoming data from the slave nodes.
 * 
 */
public class MasterNodeRunner implements IMasterNodeRunner {
	
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
		WebAPI.publishMessage("Job [" + jobResponseData.getJobID() + "] replied: "
				+ jobResponseData.getMessage());
		
	}
	
}

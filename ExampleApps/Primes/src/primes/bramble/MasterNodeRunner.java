package primes.bramble;

import java.io.IOException;

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
		try {
			Manager.setup(new MasterNodeRunner(), new ControllerNodeRunner());
		} catch (IOException e) {
			System.out.println("Couldn't set up node(s)");
			System.out.println(e.toString());
			return;
		}
		Manager.launchAll();	
	}
	
	@Override
	public void parse(JobResponseData jobResponseData) {
		WebAPI.publishMessage("Job [" + jobResponseData.getJobID() + "] replied: "
				+ jobResponseData.getMessage());
		
	}
	
}

package primes.bramble;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import bramble.networking.JobResponseData;
import bramble.node.master.IMasterNodeRunner;
import bramble.node.master.MasterNode;
import bramble.webserver.WebAPI;

public class MasterNodeRunner implements IMasterNodeRunner {
	
	public static void main(String[] args){	
		(new MasterNodeRunner()).initialize();
	}
	
	private void initialize(){
				
		MasterNode<MasterNodeRunner> masterNode = new MasterNode<>(this, new ControllerNodeRunner());
		
		masterNode.startJobSetupRunner();
		masterNode.startWebServer();
		
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		WebAPI.publishMessage("Master node runner started at " + dateFormat.format(date));
		
		masterNode.listenForever();
	}
	
	@Override
	public void parse(JobResponseData jobResponseData) {
		WebAPI.publishMessage("Job [" + jobResponseData.getJobID() + "] replied: "
				+ jobResponseData.getMessage());
		
	}
	
}

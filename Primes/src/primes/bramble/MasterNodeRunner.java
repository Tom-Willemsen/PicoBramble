package primes.bramble;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import bramble.networking.JobResponseData;
import bramble.node.master.IMasterNodeRunner;
import bramble.node.master.MasterNode;

public class MasterNodeRunner implements IMasterNodeRunner {
	
	public static void main(String[] args){	
		(new MasterNodeRunner()).initialize();
	}
	
	private void initialize(){

		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		System.out.println("Master node runner started at " + dateFormat.format(date));
		
		
		MasterNode<MasterNodeRunner> masterNode = new MasterNode<>(this, new ControllerNodeRunner());
		
		masterNode.startJobSetupRunner();
		masterNode.startWebServer();
		
		masterNode.listenForever();
	}
	
	@Override
	public void parse(JobResponseData jobResponseData) {
		System.out.println("Job [" + jobResponseData.getJobID() + "] replied: "
				+ jobResponseData.getMessage());
		
	}
	
}

package primes.bramble;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import bramble.masternode.IMasterNodeRunner;
import bramble.masternode.JobSetup;
import bramble.masternode.MasterNode;
import bramble.networking.JobResponseData;

public class MasterNodeRunner implements IMasterNodeRunner {
	
	public static void main(String[] args){	
		(new MasterNodeRunner()).initialize();
	}
	
	private void initialize(){

		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		System.out.println("Master node runner started at " + dateFormat.format(date));
		
		new Thread(new JobSetup(new JobSetupRunner())).start();
		(new MasterNode<MasterNodeRunner>(this)).listenForever();
	}
	
	@Override
	public void parse(JobResponseData jobResponseData) {
		System.out.println("Job [" + jobResponseData.getJobID() + "] replied: "
				+ jobResponseData.getMessage());
		
	}
	
}

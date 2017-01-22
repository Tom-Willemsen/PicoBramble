package primes.bramble;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import bramble.masternode.MasterNode;
import bramble.networking.JobResponseData;

public class MasterNodeRunner extends MasterNode {
	
	public static void main(String[] args){	
		
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		System.out.println("Started at " + dateFormat.format(date));
		
		initialize();
	}
	
	private static void initialize(){
		new Thread(new JobSetupRunner()).start();
		(new MasterNodeRunner()).listenForever();
	}
	
	@Override
	protected void parse(JobResponseData jobResponseData) {
		System.out.println("Job [" + jobResponseData.getJobIdentifier() + "] replied with "
				+ (jobResponseData.getData()).size() + " primes.");
		
	}

}

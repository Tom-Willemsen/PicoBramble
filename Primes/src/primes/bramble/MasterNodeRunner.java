package primes.bramble;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import bramble.masternode.IMasterNodeRunner;
import bramble.masternode.MasterNode;
import bramble.networking.JobResponseData;

public class MasterNodeRunner implements IMasterNodeRunner {
	
	public static void main(String[] args){	
		
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		System.out.println("Started at " + dateFormat.format(date));
		
		(new MasterNodeRunner()).initialize();
	}
	
	private void initialize(){
		new Thread(new JobSetupRunner()).start();
		(new MasterNode<MasterNodeRunner>(this)).listenForever();
	}
	
	@Override
	public void parse(JobResponseData jobResponseData) {
		System.out.println("Job [" + jobResponseData.getJobID() + "] replied: "
				+ jobResponseData.getMessage());
		if(jobResponseData.getJobID()>100){
			DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
			Date date = new Date();
			System.out.println("Ended at " + dateFormat.format(date));
			System.exit(0);
		}
		
	}
	
	@Override
	public MasterNodeRunner clone(){
		return null;
	}

}

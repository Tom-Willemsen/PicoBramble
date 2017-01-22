package primes.bramble;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import bramble.masternode.JobSetup;
import bramble.networking.JobSetupData;

public class JobSetupRunner extends JobSetup {
	
	private int jobsRequested;
	
	public JobSetupRunner(){
		startThread();
	}
	
	synchronized public void startThread(){
		this.jobsRequested = 0;
		new Thread(this).start();
	}

	@Override
	synchronized public JobSetupData getJobSetupData() {
		
		if(jobsRequested > 100){
			DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
			Date date = new Date();
			System.out.println("Ended at " + dateFormat.format(date));
			return null;
		}
		
		ArrayList<Serializable> init = new ArrayList<Serializable>();
		
		Long multiplier = new Long(1000000);
		
		init.add(new Long(jobsRequested*multiplier));
		init.add(new Long((jobsRequested+1)*multiplier));
		
		int jobNumber = this.jobsRequested;
		this.jobsRequested += 1;
		
		JobSetupData data = new JobSetupData(jobNumber, init);
		
		return data;
		
	}

}

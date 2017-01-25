package primes.bramble;

import java.io.Serializable;
import java.util.ArrayList;

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
		
		ArrayList<Serializable> init = new ArrayList<Serializable>();
		
		int multiplier = 10;
		
		init.add(new Integer(jobsRequested*multiplier));
		init.add(new Integer((jobsRequested+1)*multiplier));
		
		int jobNumber = this.jobsRequested;
		this.jobsRequested += 1;
		
		JobSetupData data = new JobSetupData(jobNumber, init);
		
		return data;
		
	}

}

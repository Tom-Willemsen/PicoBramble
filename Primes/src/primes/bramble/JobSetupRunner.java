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
		
		int multiplier = 1000000;
		
		init.add(jobsRequested*multiplier);
		init.add((jobsRequested+1)*multiplier);
		
		int jobNumber = this.jobsRequested;
		this.jobsRequested += 1;
		
		JobSetupData data = new JobSetupData(jobNumber, init);
		
		return data;
		
	}

}

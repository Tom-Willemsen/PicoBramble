package primes.bramble;

import java.io.Serializable;
import java.util.ArrayList;

import bramble.masternode.JobSetup;
import bramble.networking.JobSetupData;

public class JobSetupRunner extends JobSetup {
	
	private volatile int jobsRequested;
	
	public JobSetupRunner(){
		startThread();
	}
	
	public synchronized void startThread(){
		this.jobsRequested = 1;
		new Thread(this).start();
	}

	@Override
	synchronized public JobSetupData getJobSetupData() {
		
		ArrayList<Serializable> init = new ArrayList<Serializable>();
		init.add(new Long(jobsRequested*1));
		init.add(new Long((jobsRequested+1)*1));
		
		return new JobSetupData(this.jobsRequested++, init);
		
	}

}

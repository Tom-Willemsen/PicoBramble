package primes.bramble;

import java.io.Serializable;
import java.util.ArrayList;

import bramble.masternode.JobSetup;
import bramble.networking.JobSetupData;

public class JobSetupRunner extends JobSetup {
	
	private int jobsRequested;
	
	public JobSetupRunner(){
		this.jobsRequested = 1;
		new Thread(this).start();
	}

	@Override
	public JobSetupData getJobSetupData() {
		
		ArrayList<Serializable> init = new ArrayList<Serializable>();
		init.add(new Long(jobsRequested*100000));
		init.add(new Long((jobsRequested+1)*100000));
		
		return new JobSetupData(this.jobsRequested++, init);
		
	}

}

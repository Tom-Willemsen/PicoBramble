package primes.bramble;

import java.io.Serializable;
import java.util.ArrayList;

import bramble.masternode.JobSetup;
import bramble.networking.JobSetupData;

public class JobSetupRunner extends JobSetup {
	
	int jobsRequested = 0;

	@Override
	public JobSetupData getJobSetupData() {
		
		ArrayList<Serializable> init2 = new ArrayList<Serializable>();
		init2.add(new Long(jobsRequested*10000000));
		init2.add(new Long((jobsRequested+1)*10000000));
		
		this.jobsRequested++;
		return new JobSetupData(1, init2);
		
	}

	@Override
	public void onJobSlotAvailable() {
		// TODO Auto-generated method stub
		
	}

}

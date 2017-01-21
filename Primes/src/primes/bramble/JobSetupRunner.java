package primes.bramble;

import java.io.Serializable;
import java.util.ArrayList;

import bramble.masternode.JobSetup;
import bramble.networking.JobSetupData;

public class JobSetupRunner extends JobSetup {

	@Override
	public JobSetupData getJobSetupData() {
		
		ArrayList<Serializable> init2 = new ArrayList<Serializable>();
		init2.add(new Long(10000000));
		init2.add(new Long(20000000));
		
		return new JobSetupData(1, init2);
		
	}
	
	public static void addSlaveNode(){
		
	}

	@Override
	public void onJobSlotAvailable() {
		// TODO Auto-generated method stub
		
	}

}

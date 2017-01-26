package primes.bramble;

import java.io.Serializable;
import java.util.ArrayList;

import bramble.masternode.IJobSetup;
import bramble.networking.JobSetupData;

public class JobSetupRunner implements IJobSetup {

	@Override
	public JobSetupData getJobSetupData(int jobSetupDataID, int jobNumber) {
		
		ArrayList<Serializable> init = new ArrayList<Serializable>();
		
		int multiplier = 1000000;
		
		init.add(new Integer(jobNumber*multiplier));
		init.add(new Integer((jobNumber+1)*multiplier));
		
		JobSetupData data = new JobSetupData(jobSetupDataID, init);
		
		return data;
		
	}

	@Override
	public ArrayList<Integer> getAllJobNumbers() {
		ArrayList<Integer> allJobs = new ArrayList<>();
		for(int i = 0; i<=100; i++){
			allJobs.add(i);
		}
		return allJobs;
	}

}

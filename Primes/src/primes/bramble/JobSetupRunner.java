package primes.bramble;

import java.io.Serializable;
import java.util.ArrayList;

import bramble.masternode.IJobSetup;
import bramble.networking.JobSetupData;

public class JobSetupRunner implements IJobSetup {

	@Override
	public JobSetupData getJobSetupData(int jobSetupDataID, int jobNumber) {
		
		ArrayList<Serializable> init = new ArrayList<Serializable>();
		
		Long multiplier = Long.valueOf(1000000);
		
		init.add(Long.valueOf(jobNumber*multiplier));
		init.add(Long.valueOf((jobNumber+1)*multiplier));
		
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

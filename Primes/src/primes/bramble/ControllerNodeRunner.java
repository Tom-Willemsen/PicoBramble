package primes.bramble;

import java.io.Serializable;
import java.util.ArrayList;

import bramble.networking.JobSetupData;
import bramble.node.controller.IControllerNode;

public class ControllerNodeRunner implements IControllerNode {

	@Override
	public JobSetupData getJobSetupData(int jobSetupDataID, int jobNumber) {
		
		ArrayList<Serializable> init = new ArrayList<>();
		
		Long multiplier = Long.valueOf(1000000);
		
		init.add(Long.valueOf(jobNumber*multiplier));
		init.add(Long.valueOf((jobNumber+1)*multiplier));
		
		JobSetupData data = new JobSetupData(jobSetupDataID, init);
		
		return data;
		
	}

	@Override
	public ArrayList<Integer> getAllJobs() {
		ArrayList<Integer> allJobs = new ArrayList<>();
		for(int i = 99; i>=0; i--){
			allJobs.add(i);
		}
		return allJobs;
	}

}

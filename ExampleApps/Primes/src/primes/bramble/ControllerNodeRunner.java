package primes.bramble;

import java.io.Serializable;
import java.util.ArrayList;

import bramble.networking.data.JobMetadata;
import bramble.networking.data.JobSetupData;
import bramble.node.controller.IControllerNodeRunner;

/**
 * This is an example of how a ControllerNode can be run.
 * 
 * <p>This class is responsible for defining jobs in a way that
 * they will be accepted and performed by slave nodes.</p>
 * 
 */
public class ControllerNodeRunner implements IControllerNodeRunner {

	/**
	 * Defines a job based on it's job metadata.
	 */
	@Override
	public JobSetupData getJobSetupData(JobMetadata jobMetadata) {
		
		Integer jobNumber = jobMetadata.getJobNumber();

		ArrayList<Serializable> init = new ArrayList<>();

		Long multiplier = Long.valueOf(1000000);

		init.add(Long.valueOf(jobNumber*multiplier));
		init.add(Long.valueOf((jobNumber+1)*multiplier));

		JobSetupData data = new JobSetupData(jobMetadata, init);

		return data;

	}

	/**
	 * Gets the job numbers of all the jobs that need to be performed.
	 * 
	 * @return an arraylist containing the job numbers of all the jobs that need to be performed
	 */
	@Override
	public ArrayList<JobMetadata> getAllJobNumbers() {
		ArrayList<JobMetadata> allJobs = new ArrayList<>();
		for(int i = 99; i>=0; i--){
			allJobs.add(new JobMetadata(i));
		}
		return allJobs;
	}

}

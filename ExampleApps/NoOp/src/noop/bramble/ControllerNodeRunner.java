package noop.bramble;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import bramble.networking.data.JobMetadata;
import bramble.networking.data.JobSetupData;
import bramble.node.controller.IControllerNodeRunner;

public class ControllerNodeRunner implements IControllerNodeRunner {

	private ArrayList<JobMetadata> allJobs = new ArrayList<>();
	
	/**
	 * Constructor.
	 */
	public ControllerNodeRunner(){
		for(int i = 999999; i>=0; i--){
			allJobs.add(new JobMetadata(i));
		}
	}
	
	/**
	 * Defines a job based on it's job number.
	 */
	@Override
	public JobSetupData getJobSetupData(JobMetadata jobMetadata) {
		
		Collection<Serializable> setupData = new ArrayList<>();
		setupData.add(jobMetadata);

		JobSetupData data = new JobSetupData(jobMetadata, setupData);

		return data;

	}

	/**
	 * Gets the job numbers of all the jobs that need to be performed.
	 * 
	 * @return an arraylist containing the job metadata of all the jobs
	 */
	@Override
	public ArrayList<JobMetadata> getAllJobNumbers() {
		return allJobs;
	}

}

package bramble.slavenode;

import java.util.ArrayList;

public interface IJobRunner extends Runnable {
	
	/**
	 * The Initialization method is called before a job is run.
	 * 
	 * The parameters passed will set up the problem, for example by providing limits for a calculation.
	 * 
	 * @param jobID - unique job ID that this job has been assigned.
	 * @param initializationData - The initialization data for this job.
	 */
	public void initializeJob(int jobID, ArrayList<?> initializationData);
}

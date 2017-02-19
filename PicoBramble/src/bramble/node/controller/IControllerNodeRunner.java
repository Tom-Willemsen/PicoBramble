package bramble.node.controller;

import java.util.Collection;

import bramble.networking.JobSetupData;

public interface IControllerNodeRunner {
	
	/**
	 * Defines a job based on it's job number.
	 */
	public JobSetupData getJobSetupData(int jobSetupDataIdentifier, int jobNumber);
	
	/**
	 * Gets the job numbers of all the jobs that need to be performed.
	 * 
	 * @return a collection containing the job numbers of all the jobs that need to be performed
	 */
	public Collection<Integer> getAllJobNumbers();
	
}

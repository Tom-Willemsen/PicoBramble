package bramble.node.controller;

import java.util.ArrayList;

import bramble.networking.JobSetupData;

public interface IControllerNodeRunner {
	
	/**
	 * Defines a job based on it's job number
	 */
	public JobSetupData getJobSetupData(int jobSetupDataID, int jobNumber);
	
	/**
	 * Gets the job numbers of all the jobs that need to be performed.
	 * 
	 * @return an arraylist containing the job numbers of all the jobs that need to be performed
	 */
	public ArrayList<Integer> getAllJobNumbers();
	
}

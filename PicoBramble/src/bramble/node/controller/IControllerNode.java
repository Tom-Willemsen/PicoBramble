package bramble.node.controller;

import java.util.ArrayList;

import bramble.networking.JobSetupData;

public interface IControllerNode {
	
	public JobSetupData getJobSetupData(int jobSetupDataID, int jobNumber);
	
	public ArrayList<Integer> getAllJobs();
	
}

package bramble.masternode;

import java.util.ArrayList;

import bramble.networking.JobSetupData;

public interface IControllerNode {
	
	public JobSetupData getJobSetupData(int jobSetupDataID, int jobNumber);
	
	public ArrayList<Integer> getAllJobNumbers();
	
}

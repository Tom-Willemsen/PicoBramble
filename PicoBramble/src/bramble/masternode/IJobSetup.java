package bramble.masternode;

import java.util.ArrayList;

import bramble.networking.JobSetupData;

public interface IJobSetup {
	
	public JobSetupData getJobSetupData(int jobSetupDataID, int jobNumber);
	
	public ArrayList<Integer> getAllJobNumbers();
	
}

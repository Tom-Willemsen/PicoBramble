package bramble.masternode;

import java.io.IOException;
import java.util.ArrayList;

import bramble.networking.JobSetupData;

public abstract class JobSetup {
	
	public abstract JobSetupData getJobSetupData();
	public abstract void onJobSlotAvailable();
	
	private static ArrayList<SlaveNodeInformation> slaveNodes = new ArrayList<SlaveNodeInformation>();
	
	public static void registerSlaveNode(SlaveNodeInformation slaveNode){
		slaveNodes.add(slaveNode);
	}
	
	public void sendJobSetupData(JobSetupData data){
		// TODO Choose a node to send it to
		
		try {
			data.send();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}

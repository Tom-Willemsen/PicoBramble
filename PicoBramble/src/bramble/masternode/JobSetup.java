package bramble.masternode;

import java.io.IOException;
import java.util.ArrayList;

import bramble.networking.JobSetupData;

public abstract class JobSetup implements Runnable {
	
	public abstract JobSetupData getJobSetupData();
	
	private static ArrayList<SlaveNodeInformation> slaveNodes = new ArrayList<SlaveNodeInformation>();
	
	private static int jobSlotsAvailable = 0;
	
	public static void registerSlaveNode(SlaveNodeInformation slaveNode){
		slaveNodes.add(slaveNode);
		for(int i=0; i<slaveNode.getMaxThreads(); i++){
			jobSlotsAvailable++;
		}
	}
	
	public void sendJobSetupData(JobSetupData data){
		// TODO Choose a node to send it to
		
		try {
			data.send();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		try{
			while(true){
				if(jobSlotsAvailable > 0){
					sendJobSetupData(getJobSetupData());
				} else {
					Thread.sleep(2000);
				}
			}
		} catch (InterruptedException e){
			System.exit(0);
		}
	}
	
}

package bramble.masternode;

import java.io.IOException;
import java.util.ArrayList;

import bramble.networking.JobSetupData;

public abstract class JobSetup implements Runnable {
	
	public abstract JobSetupData getJobSetupData();
	
	private static volatile ArrayList<SlaveNodeInformation> slaveNodes = new ArrayList<SlaveNodeInformation>();
	
	private static volatile int jobSlotsAvailable = 0;
	
	synchronized public static void registerSlaveNode(SlaveNodeInformation slaveNode){
		slaveNodes.add(slaveNode);
		for(int i=0; i<slaveNode.getMaxThreads(); i++){
			jobSlotsAvailable++;
		}
	}
	
	synchronized public static void sendJobSetupData(JobSetupData data){
		// TODO Choose a node to send it to
		jobSlotsAvailable--;
		try {
			System.out.println("Sending job ID " + data.getJobID());
			data.send();
		} catch (IOException e) {
			System.out.println("Failed to send a Job to a slave node");
			jobSlotsAvailable++;
		}
	}
	
	synchronized public static void jobFinished(){
		jobSlotsAvailable++;
	}
	
	synchronized public void run(){
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

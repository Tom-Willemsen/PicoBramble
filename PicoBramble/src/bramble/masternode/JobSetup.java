package bramble.masternode;

import java.io.IOException;
import java.util.ArrayList;

import bramble.configuration.BrambleConfiguration;
import bramble.networking.JobSetupData;

public abstract class JobSetup implements Runnable {
	
	public abstract JobSetupData getJobSetupData();
	
	private static volatile ArrayList<SlaveNodeInformation> slaveNodes;
	
	public JobSetup(){
		initialize();
	}
	
	synchronized private void initialize(){
		slaveNodes = new ArrayList<SlaveNodeInformation>();
	}
	
	synchronized public final static void registerSlaveNode(SlaveNodeInformation slaveNode){
		slaveNodes.add(slaveNode);
	}
	
	public final static void sendJobSetupData(JobSetupData data){
		SlaveNodeInformation targetNode = getTargetNode();
		data.setTargetHostname(targetNode.getIPAddress());
		targetNode.addJob();
		
		try {
			data.send();
		} catch (IOException e) {
			targetNode.removeJob();
			System.out.println("Failed to send a Job to a slave node");
		}
	}
	
	synchronized private static SlaveNodeInformation getTargetNode() {
		
		int freeThreads = 0;
		SlaveNodeInformation output = null;
		
		for(SlaveNodeInformation targetNode : slaveNodes){
			if(targetNode.getFreeJobSlots() > freeThreads){
				freeThreads = targetNode.getFreeJobSlots();
				output = targetNode;
			}
		}
		
		if(output == null){
			throw new RuntimeException("Had a job slot available but couldn't find it.");
		}
		
		return output;
	}
	
	synchronized public static int getJobSlotsAvailable(){
		int result = 0;
		
		for(SlaveNodeInformation targetNode : slaveNodes){
			result += targetNode.getFreeJobSlots();
		}
		
		return result;		
	}

	synchronized public final static void jobFinished(String IP){
		for(SlaveNodeInformation targetNode : slaveNodes){
			if(IP.equals(targetNode.getIPAddress())){
				targetNode.removeJob();
				return;
			}
		}
		throw new RuntimeException("Couldn't find a relevant node for jobFinished()");
	}
	
	public final void run(){
		 try{ 
			while(true){
				sendDataIfNodesAreAvailable();
				Thread.sleep(BrambleConfiguration.LISTENER_DELAY_MS);
			}
		} catch (InterruptedException e){
			System.exit(0);
		}
	}
	
	synchronized private void sendDataIfNodesAreAvailable(){
		if(getJobSlotsAvailable() > 0){
			JobSetupData data = getJobSetupData();
			if(data != null){
				sendJobSetupData(data);
			}
		}
	}
}

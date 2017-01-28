package bramble.masternode;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import bramble.configuration.BrambleConfiguration;
import bramble.networking.JobResponseData;
import bramble.networking.JobSetupData;

public class JobSetup implements Runnable {
	
	private ArrayList<SlaveNodeInformation> slaveNodes = new ArrayList<SlaveNodeInformation>();
	private ArrayList<Integer> completedJobs = new ArrayList<Integer>();
	private ArrayList<Integer> startedJobs = new ArrayList<Integer>();
	private IJobSetup jobSetupRunner;
	private int nextAvailableJobSetupID = 0;
	private ArrayList<Integer> allJobs;
	
	public JobSetup(IJobSetup runner){
		setJobSetupRunner(runner);
		allJobs = runner.getAllJobNumbers();
	}
	
	synchronized public final void registerSlaveNode(SlaveNodeInformation slaveNode){
		slaveNodes.add(slaveNode);
	}
	
	synchronized public void setJobSetupRunner(IJobSetup runner){
		jobSetupRunner = runner;
	}
	
	public final void sendJobSetupData(JobSetupData data){
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
	
	synchronized private SlaveNodeInformation getTargetNode() {
		
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
	
	synchronized public int getJobSlotsAvailable(){
		int result = 0;
		
		for(SlaveNodeInformation targetNode : slaveNodes){
			result += targetNode.getFreeJobSlots();
		}
		
		return result;		
	}

	synchronized public final void jobFinished(JobResponseData jobResponseData){
		
		completedJobs.add(jobResponseData.getJobID());
		
		String IP = jobResponseData.getSenderIP();
		for(SlaveNodeInformation targetNode : slaveNodes){
			if(IP.equals(targetNode.getIPAddress())){
				targetNode.removeJob();
				return;
			}
		}
		
		throw new RuntimeException("Couldn't find a relevant node for jobFinished()");
	}
	
	synchronized private void checkIfAllJobsFinished(){
		updateAllJobs();
		if(allJobs.size() != completedJobs.size()){
			return;
		}
		
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		System.out.println("Finished all jobs at " + dateFormat.format(date));
		System.exit(0);
	}
	
	synchronized private void updateAllJobs(){
		allJobs = jobSetupRunner.getAllJobNumbers();
	}
	
	synchronized private Integer getNextJob(){
		for(Integer i : allJobs){
			if(!startedJobs.contains(i)){
				return i;
			}
		}
		return null;
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
		Integer nextJob = getNextJob();
		
		checkIfAllJobsFinished();
		
		if(getJobSlotsAvailable() > 0 && nextJob != null){
			JobSetupData data = jobSetupRunner.getJobSetupData(nextAvailableJobSetupID++, nextJob);
			if(data != null){
				sendJobSetupData(data);
				startedJobs.add(nextJob);
			}
		}
	}
}

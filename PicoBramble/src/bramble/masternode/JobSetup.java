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
	
	/** 
	 * Constructor
	 * @param runner - the 'visiting' job setup runner, 
	 * 					which must implement the IJobSetup interface.
	 */
	public JobSetup(IJobSetup runner){
		setJobSetupRunner(runner);
		allJobs = runner.getAllJobNumbers();
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
	
	/**
	 * Gets the number of job slots available on any node.
	 * @return the number of job slots available on any node.
	 */
	synchronized public int getJobSlotsAvailable(){
		int result = 0;
		
		for(SlaveNodeInformation targetNode : slaveNodes){
			result += targetNode.getFreeJobSlots();
		}
		
		return result;		
	}
	
	synchronized private Integer getNextJob(){
		for(Integer i : allJobs){
			if(!startedJobs.contains(i)){
				return i;
			}
		}
		return null;
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
	
	/**
	 * This method is called when a job has been finished by a slave node.
	 * @param jobResponseData the response data that the slave node sent back.
	 */
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

	/**
	 * Registers a new slave node for use by the cluster.
	 * @param slaveNode the new slave node to add to the cluster.
	 */
	synchronized public final void registerSlaveNode(SlaveNodeInformation slaveNode){
		slaveNodes.add(slaveNode);
	}
	
	/**
	 * Keeps looking for a node with available space, to send it data.
	 */
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
	
	/**
	 * Sends the data to the slave node.
	 * @param data - the data to send.
	 */
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
	
	/**
	 * Define the job setup runner that will provide tasks.
	 * @param runner the job setup runner that will provide tasks
	 */
	synchronized public void setJobSetupRunner(IJobSetup runner){
		jobSetupRunner = runner;
	}
	
	synchronized private void updateAllJobs(){
		allJobs = jobSetupRunner.getAllJobNumbers();
	}
}

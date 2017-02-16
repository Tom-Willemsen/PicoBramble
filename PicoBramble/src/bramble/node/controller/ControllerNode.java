package bramble.node.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import bramble.configuration.BrambleConfiguration;
import bramble.networking.Handshake;
import bramble.networking.JobResponseData;
import bramble.networking.JobSetupData;
import bramble.webserver.WebAPI;

public class ControllerNode<T extends IControllerNodeRunner> implements Runnable {
	
	private ArrayList<SlaveNodeInformation> slaveNodes = new ArrayList<SlaveNodeInformation>();
	
	private ArrayList<Integer> allJobs;
	private ArrayList<Integer> completedJobs = new ArrayList<Integer>();
	private ArrayList<Integer> startedJobs = new ArrayList<Integer>();
	
	private IControllerNodeRunner controllerNodeRunner;
	private int nextAvailableJobSetupID = 0;
	private boolean finishedAllJobs = false;
	
	/** 
	 * Constructor
	 * @param runner - the 'visiting' controller node runner, 
	 * 					which must implement the IControllerNode interface.
	 */
	public ControllerNode(T runner){
		setControllerNodeRunner(runner);
		allJobs = runner.getAllJobNumbers();
		WebAPI.setControllerNode(this);
	}
	
	synchronized private void checkIfAllJobsFinished(){
		updateAllJobs();
		if(allJobs.size() != completedJobs.size()){
			return;
		}
		
		if(!finishedAllJobs){
			DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
			Date date = new Date();
			WebAPI.publishMessage("Finished all jobs at " + dateFormat.format(date));
		}
		finishedAllJobs = true;
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
	synchronized public void jobFinished(JobResponseData jobResponseData){
		
		completedJobs.add(jobResponseData.getJobID());
		
		String IP = jobResponseData.getSenderIP();
		for(SlaveNodeInformation targetNode : slaveNodes){
			if(IP.equals(targetNode.getIPAddress())){
				targetNode.removeJob(jobResponseData.getJobID());
				return;
			}
		}
		
		throw new RuntimeException("Couldn't find a relevant node for jobFinished()");
	}

	/**
	 * Registers a new slave node for use by the cluster.
	 * @param slaveNode the new slave node to add to the cluster.
	 */
	synchronized public void registerSlaveNode(SlaveNodeInformation slaveNode){
		slaveNode.setTimeOfLastHandshake();
		slaveNodes.add(slaveNode);
	}
	
	/**
	 * Registers a new slave node by Handshake.
	 * @param handshake a handshake from the new slave node
	 */
	public void registerSlaveNodeByHandshake(Handshake handshake){
		
		String senderIP = handshake.getSenderIP();
		
		for(SlaveNodeInformation slaveNode : slaveNodes){
			if(slaveNode.getIPAddress().equals(senderIP)){
				slaveNode.setTimeOfLastHandshake();
				slaveNode.setDiagnosticInfo(handshake.getDiagnostics());
				return;
			}
		}
		
		SlaveNodeInformation slaveNode = new SlaveNodeInformation(senderIP, BrambleConfiguration.THREADS_PER_NODE, handshake.getDiagnostics());
		registerSlaveNode(slaveNode);
	}
	
	/**
	 * Keeps looking for a node with available space, to send it data.
	 */
	public void run(){
		
		 try{ 
			while(true){
				sendDataIfNodesAreAvailable();
				Thread.sleep(BrambleConfiguration.LISTENER_DELAY_MS);
			}
		} catch (InterruptedException e){
			return;
		}
	}
	
	synchronized private void sendDataIfNodesAreAvailable(){
		Integer nextJob = getNextJob();
		
		checkIfAllJobsFinished();
		
		if(getJobSlotsAvailable() > 0 && nextJob != null){
			JobSetupData data = controllerNodeRunner.getJobSetupData(nextAvailableJobSetupID++, nextJob);
			if(data != null){
				sendJobSetupData(data);
				startedJobs.add(nextJob);
			}
		}
	}
	
	/**
	 * Sends setup data to the slave node.
	 * @param data - the data to send.
	 */
	public void sendJobSetupData(JobSetupData data){
		SlaveNodeInformation targetNode = getTargetNode();
		data.setTargetHostname(targetNode.getIPAddress());
		targetNode.addJob(data.getJobID());
		
		try {
			data.send();
		} catch (IOException e) {
			targetNode.removeJob(data.getJobID());
			WebAPI.publishMessage("Failed to send a Job to a slave node");
		}
	}
	
	/**
	 * Define the job setup runner that will provide tasks.
	 * @param runner the job setup runner that will provide tasks
	 */
	synchronized public void setControllerNodeRunner(T runner){
		controllerNodeRunner = runner;
	}
	
	synchronized private void updateAllJobs(){
		checkIfNodesHaveTimedOut();
		allJobs = controllerNodeRunner.getAllJobNumbers();
		WebAPI.setControllerNode(this);
	}
	
	private void checkIfNodesHaveTimedOut(){
		
		ArrayList<SlaveNodeInformation> deadNodes = new ArrayList<>();
		
		for(SlaveNodeInformation slaveNode : slaveNodes){
			if(slaveNode.millisecondsSinceLastHandshake() > BrambleConfiguration.NODE_TIMEOUT_MS){
				deadNodes.add(slaveNode);
			}
		}
		
		for(SlaveNodeInformation deadSlaveNode : deadNodes){
			removeNode(deadSlaveNode);
		}
	}
	
	synchronized private void removeNode(SlaveNodeInformation deadSlaveNode){
		for(Integer jobID : deadSlaveNode.getJobs()){
			startedJobs.remove(jobID);
		}
		
		slaveNodes.remove(deadSlaveNode);
	}
	
	public ArrayList<SlaveNodeInformation> getSlaveNodes(){
		return slaveNodes;
	}
	
	public ArrayList<Integer> getAllJobs(){
		return allJobs;
	}
	
	public ArrayList<Integer> getCompletedJobs(){
		return completedJobs;
	}
	
	public ArrayList<Integer> getStartedJobs(){
		return startedJobs;
	}
}

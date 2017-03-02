package bramble.node.controller;

import java.io.IOException;
import java.util.Collection;
import bramble.configuration.BrambleConfiguration;
import bramble.networking.Handshake;
import bramble.networking.JobResponseData;
import bramble.networking.JobSetupData;
import bramble.node.manager.IManager;
import bramble.webserver.WebApi;

public class ControllerNode implements Runnable {

    private JobList jobList;
    private SlaveNodeList slaveNodeList;

    private IControllerNodeRunner controllerNodeRunner;

    private IManager manager;

    /** 
     * Constructor
     * @param runner - the 'visiting' controller node runner, 
     *     which must implement the IControllerNode interface.
     */
    public ControllerNode(IManager manager, IControllerNodeRunner runner){
	this.manager = manager;
	setControllerNodeRunner(runner);
	this.jobList = new JobList();
	this.slaveNodeList = new SlaveNodeList();
	jobList.setAllJobs(runner.getAllJobNumbers());
	WebApi.setControllerNode(this);
    }

    /**
     * Gets the number of job slots available on any node.
     * @return the number of job slots available on any node.
     */
    public int getJobSlotsAvailable(){
	return slaveNodeList.getFreeJobSlots();		
    }

    private synchronized SlaveNodeInformation getTargetNode() {
	return slaveNodeList.getTargetNode();
    }

    /**
     * This method is called when a job has been finished by a slave node.
     * @param jobResponseData the response data that the slave node sent back.
     */
    public synchronized void jobFinished(JobResponseData jobResponseData){
	Integer jobIdentifier = jobResponseData.getJobIdentifier();
	jobList.jobCompleted(jobIdentifier);
	slaveNodeList.jobCompleted(jobIdentifier);
    }

    /**
     * Registers a new slave node for use by the cluster.
     * @param slaveNode the new slave node to add to the cluster.
     */
    public synchronized void registerSlaveNode(SlaveNodeInformation slaveNode){
	slaveNodeList.registerSlaveNode(slaveNode);
    }

    /**
     * Registers a new slave node by Handshake.
     * @param handshake a handshake from the new slave node
     */
    public void registerSlaveNodeByHandshake(Handshake handshake){	
	slaveNodeList.registerSlaveNodeHandshake(handshake);
    }

    /**
     * Keeps looking for a node with available space, to send it data.
     */
    @Override
    public void run(){
	manager.runTask(new Runnable(){
	    @Override
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
	});	 
    }

    private synchronized void sendDataIfNodesAreAvailable(){
	Integer nextJob = jobList.getNextJob();

	updateAllJobs();
	if(jobList.areAllJobsFinished()){
	    return;
	}

	if(getJobSlotsAvailable() > 0 && nextJob != null){
	    Integer nextAvailableJobIdentifier = jobList.getNextAvailableJobIdentifier();
	    JobSetupData data = 
		    controllerNodeRunner.getJobSetupData(nextAvailableJobIdentifier, nextJob);
	    if(data != null){
		sendJobSetupData(data);
		jobList.jobStarted(nextJob);
	    }
	}
    }

    /**
     * Sends setup data to the slave node.
     * @param data - the data to send.
     */
    public void sendJobSetupData(JobSetupData data){
	SlaveNodeInformation targetNode = getTargetNode();
	data.setTargetHostname(targetNode.getIpAddress());
	targetNode.addJob(data.getJobIdentifier());

	try {
	    data.send();
	} catch (IOException e) {
	    targetNode.removeJob(data.getJobIdentifier());
	    WebApi.publishMessage("Failed to send a Job to a slave node");
	}
    }

    /**
     * Define the job setup runner that will provide tasks.
     * @param runner the job setup runner that will provide tasks
     */
    public synchronized void setControllerNodeRunner(IControllerNodeRunner runner){
	controllerNodeRunner = runner;
    }

    private synchronized void updateAllJobs(){
	removeTimedOutNodesAndJobs();
	jobList.setAllJobs(controllerNodeRunner.getAllJobNumbers());
	WebApi.setControllerNode(this);
    }

    private void removeTimedOutNodesAndJobs(){
	for(SlaveNodeInformation timedOutNode : slaveNodeList.timedOutNodes()){
	    removeNode(timedOutNode);
	}
    }

    private void removeNode(SlaveNodeInformation deadSlaveNode){
	for(Integer jobIdentifier : deadSlaveNode.getJobs()){
	    jobList.cancelJob(jobIdentifier);
	}

	slaveNodeList.removeNode(deadSlaveNode);
    }

    /**
     * Gets the slave nodes that this controller node knows about.
     * @return slave node information for each slave node
     */
    public Collection<SlaveNodeInformation> getSlaveNodes(){
	return slaveNodeList.getSlaveNodes();
    }

    /**
     * Gets all the jobs that this controller node has.
     * @return all the jobs that this controller node has
     */
    public Collection<Integer> getAllJobs(){
	return jobList.getAllJobs();
    }

    /**
     * Gets all the jobs that have been completed.
     * @return all the jobs that have been completed
     */
    public Collection<Integer> getCompletedJobs(){
	return jobList.getCompletedJobs();
    }

    /**
     * Gets all the jobs which have been started, including completed ones.
     * @return all the jobs which have been started, including completed ones
     */
    public Collection<Integer> getStartedJobs(){
	return jobList.getStartedJobs();
    }
}

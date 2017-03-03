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
	this.jobList = new JobList();
	this.slaveNodeList = new SlaveNodeList();
	
	setControllerNodeRunner(runner);
	jobList.setUnstartedJobs(runner.getAllJobNumbers());
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
	JobMetadata job = jobResponseData.getJobMetadata();
	jobList.jobCompleted(job);
	slaveNodeList.jobCompleted(job);
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

    private void sendDataIfNodesAreAvailable(){
	
	JobMetadata nextJob = jobList.getNextJob();	
	
	if(!canSendData(nextJob)){
	    return;
	}

	JobSetupData data = 
		controllerNodeRunner.getJobSetupData(nextJob);
	
	if(isJobSetupDataValid(data)){
	    sendJobSetupData(data);
	    jobList.jobStarted(nextJob);
	}

    }
    
    private boolean canSendData(JobMetadata job){
	updateAllJobs();
	
	if(job == null){
	    return false;
	}
	
	if(jobList.areAllJobsFinished()){
	    return false;
	} 
	
	if(getJobSlotsAvailable() == 0){
	    return false;
	}
	
	return true;	
    }
    
    private boolean isJobSetupDataValid(JobSetupData jobSetupData){
	if(jobSetupData==null){
	    return false;
	}
	return true;
    }

    /**
     * Sends setup data to the slave node.
     * @param data - the data to send.
     */
    public void sendJobSetupData(JobSetupData data){
	SlaveNodeInformation targetNode = getTargetNode();
	data.setTargetHostname(targetNode.getIpAddress());
	targetNode.addJob(data.getJobMetadata());

	try {
	    data.send();
	} catch (IOException e) {
	    targetNode.removeJob(data.getJobMetadata());
	    WebApi.publishMessage("Failed to send a Job to a slave node");
	}
    }

    /**
     * Define the job setup runner that will provide tasks.
     * @param runner the job setup runner that will provide tasks
     */
    public void setControllerNodeRunner(final IControllerNodeRunner runner){
	controllerNodeRunner = runner;
    }

    private void updateAllJobs(){
	removeTimedOutNodesAndJobs();
	jobList.setUnstartedJobs(controllerNodeRunner.getAllJobNumbers());
    }

    private void removeTimedOutNodesAndJobs(){
	for(SlaveNodeInformation timedOutNode : slaveNodeList.timedOutNodes()){
	    removeNode(timedOutNode);
	}
    }

    private void removeNode(SlaveNodeInformation deadSlaveNode){
	for(JobMetadata job : deadSlaveNode.getJobs()){
	    jobList.cancelJob(job);
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
    public int getTotalNumberOfJobs(){
	return jobList.getTotalNumberOfJobs();
    }

    /**
     * Gets all the jobs that have been completed.
     * @return all the jobs that have been completed
     */
    public int getNumberOfCompletedJobs(){
	return jobList.getNumberOfCompletedJobs();
    }

    /**
     * Gets all the jobs which have been started, including completed ones.
     * @return all the jobs which have been started, including completed ones
     */
    public int getNumberOfStartedJobs(){
	return jobList.getNumberOfJobsInProgress();
    }
}

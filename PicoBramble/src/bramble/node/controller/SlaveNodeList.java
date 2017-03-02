package bramble.node.controller;

import java.util.ArrayList;
import java.util.Collection;

import bramble.configuration.BrambleConfiguration;
import bramble.networking.Handshake;

public class SlaveNodeList {

    private Collection<SlaveNodeInformation> slaveNodes;
    
    /**
     * Constructor, initializes an empty slave node list.
     */
    public SlaveNodeList(){
	this.slaveNodes = new ArrayList<SlaveNodeInformation>();
    }
    
    /**
     * Gets the amount of free job slots across all nodes.
     * @return the total number of free job slots
     */
    public synchronized Integer getFreeJobSlots(){
        Integer result = 0;
    
    	for(SlaveNodeInformation targetNode : slaveNodes){
    	    result += targetNode.getFreeJobSlots();
    	}
    
    	return result;
    }
    
    /**
     * Gets a suitable target node to send data to.
     * @return a target node with free capacity
     */
    public synchronized SlaveNodeInformation getTargetNode() {

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
     * Call when a job has been completed to remove it from the list of active jobs.
     * @param jobIdentifier the job identifier which has been completed
     */
    public void jobCompleted(final Integer jobIdentifier){
	for(SlaveNodeInformation targetNode : slaveNodes){
	    if(targetNode.getJobs().contains(jobIdentifier)){
		targetNode.removeJob(jobIdentifier);
		return;
	    }
	}
    }

    /**
     * Adds a slave node to the list.
     * @param slaveNode the slave node to add
     */
    public synchronized void registerSlaveNode(SlaveNodeInformation slaveNode) {
	slaveNode.setTimeOfLastHandshake();
	slaveNodes.add(slaveNode);
    }

    /** 
     * Adds a slave node to the list by handshake.
     * 
     * <p>If it already exists, this method will refresh it's time of last handshake
     * and diagnostic information.
     * If it does not exist, it will be added to the list</p>
     * 
     * @param handshake the handshake
     */
    public void registerSlaveNodeHandshake(Handshake handshake) {
	String senderIpAddress = handshake.getSenderIpAddress();

	for(SlaveNodeInformation slaveNode : slaveNodes){
	    if(slaveNode.getIpAddress().equals(senderIpAddress)){
		slaveNode.setTimeOfLastHandshake();
		slaveNode.setDiagnosticInfo(handshake.getDiagnostics());
		return;
	    }
	}

	SlaveNodeInformation slaveNode = new SlaveNodeInformation(senderIpAddress, 
		BrambleConfiguration.THREADS_PER_NODE, handshake.getDiagnostics());
	registerSlaveNode(slaveNode);
	
    }
    
    /**
     * Gets all the nodes which have timed out
     * @return all the nodes which have timed out
     */
    public Collection<SlaveNodeInformation> timedOutNodes(){

	Collection<SlaveNodeInformation> deadNodes = new ArrayList<>();

	for(SlaveNodeInformation slaveNode : slaveNodes){
	    if(slaveNode.millisecondsSinceLastHandshake() 
		    > BrambleConfiguration.NODE_TIMEOUT_MS){
		deadNodes.add(slaveNode);
	    }
	}

	return deadNodes;
    }
    
    /**
     * Removes a slave node from the list.
     * @param node the slave node to remove
     */
    public synchronized void removeNode(final SlaveNodeInformation node){
	slaveNodes.remove(node);
    }

    /** 
     * Gets all the slave nodes in this list.
     * @return all the slave nodes in this list
     */
    public Collection<SlaveNodeInformation> getSlaveNodes() {
	return slaveNodes;
    }
}

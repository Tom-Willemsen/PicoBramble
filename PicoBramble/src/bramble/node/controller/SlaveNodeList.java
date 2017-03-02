package bramble.node.controller;

import java.util.ArrayList;
import java.util.Collection;

import bramble.configuration.BrambleConfiguration;
import bramble.networking.Handshake;

public class SlaveNodeList {

    private Collection<SlaveNodeInformation> slaveNodes;
    
    public SlaveNodeList(){
	this.slaveNodes = new ArrayList<SlaveNodeInformation>();
    }
    
    public synchronized Integer getFreeJobSlots(){
        Integer result = 0;
    
    	for(SlaveNodeInformation targetNode : slaveNodes){
    	    result += targetNode.getFreeJobSlots();
    	}
    
    	return result;
    }
    
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
    
    public void jobCompleted(final Integer jobIdentifier){
	for(SlaveNodeInformation targetNode : slaveNodes){
	    if(targetNode.getJobs().contains(jobIdentifier)){
		targetNode.removeJob(jobIdentifier);
		return;
	    }
	}
    }

    public synchronized void registerSlaveNode(SlaveNodeInformation slaveNode) {
	slaveNode.setTimeOfLastHandshake();
	slaveNodes.add(slaveNode);
    }

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
    
    public synchronized void removeNode(final SlaveNodeInformation node){
	slaveNodes.remove(node);
    }

    public Collection<SlaveNodeInformation> getSlaveNodes() {
	return slaveNodes;
    }
}

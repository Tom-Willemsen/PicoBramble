package bramble.node.controller;

import java.util.ArrayList;
import java.util.Collection;

import bramblepi.NodeDiagnosticVariables;

public class SlaveNodeInformation {
	private String ipAddress;
	private int maxThreads;
	private long lastHandshake;
	private NodeDiagnosticVariables lastNodeDiagnosticInfo;
	private Collection<Integer> jobs = new ArrayList<>();
	
	/**
	 * Creates a new slave node defined by it's IP and job capacity.
	 * @param ipAddress - the IP of the slave node
	 * @param maxThreads - the maximum number of jobs the slave node can perform at once
	 */
	public SlaveNodeInformation(String ipAddress, int maxThreads, 
		NodeDiagnosticVariables diagnosticVariables){
	    
		this.ipAddress = ipAddress;
		this.maxThreads = maxThreads;
		this.lastNodeDiagnosticInfo = diagnosticVariables;
	}
	
	/**
	 * Gets the number of jobs that are currently running on this node.
	 * @return the number of jobs that are currently running on this node
	 */
	public int getNumberOfJobsRunning(){
		return jobs.size();
	}
	
	/**
	 * Returns the IP address of this node.
	 * @return the IP address of this node
	 */
	public String getIPAddress(){
		return ipAddress;
	}
	
	/**
	 * Gets the maximum number of jobs this node can be assigned.
	 * @return the maximum number of jobs this node can be assigned.
	 */
	public int getMaxThreads(){
		return maxThreads;
	}
	
	/**
	 * Gets the number of free job slots on this node.
	 * @return the number of free job slots on this node
	 */
	public synchronized int getFreeJobSlots(){
		return (maxThreads - jobs.size());
	}
	
	/**
	 * Sets the time when last communication was recieved from this node.
	 */
	public void setTimeOfLastHandshake(){
		lastHandshake = System.currentTimeMillis();
	}
	
	/**
	 * Gets the last time that this node sent a handshake.
	 * @return the last time that this node sent a handshake
	 */
	public long millisecondsSinceLastHandshake(){
		return System.currentTimeMillis() - lastHandshake;
	}
	
	public void addJob(Integer job){
		jobs.add(job);
	}
	
	public void removeJob(Integer job){
		jobs.remove(job);
	}
	
	public Collection<Integer> getJobs(){
		return jobs;
	}
	
	public NodeDiagnosticVariables getNodeDiagnostics(){
		return lastNodeDiagnosticInfo;
	}
	
	public void setDiagnosticInfo(NodeDiagnosticVariables variables){
		this.lastNodeDiagnosticInfo = variables;
	}
}

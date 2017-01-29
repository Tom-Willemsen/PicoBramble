package bramble.node.controller;

public class SlaveNodeInformation {
	private String ipAddress;
	private int numberOfJobsRunning;
	private int maxThreads;
	private long lastHandshake;
	
	/**
	 * Creates a new slave node defined by it's IP and job capacity.
	 * @param ipAddress - the IP of the slave node
	 * @param maxThreads - the maximum number of jobs the slave node can perform at once
	 */
	public SlaveNodeInformation(String ipAddress, int maxThreads){
		this.ipAddress = ipAddress;
		this.maxThreads = maxThreads;
	}
	
	/**
	 * Adds a job to this slave node
	 */
	synchronized public void addJob(){
		this.numberOfJobsRunning++;
	}
	
	/**
	 * Removes a job from this slave node
	 * e.g. when a job has finished
	 */
	synchronized public void removeJob(){
		this.numberOfJobsRunning--;
	}
	
	/**
	 * Gets the number of jobs that are currently running on this node.
	 * @return the number of jobs that are currently running on this node
	 */
	public int getNumberOfJobsRunning(){
		return numberOfJobsRunning;
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
	synchronized public int getFreeJobSlots(){
		return (maxThreads - numberOfJobsRunning);
	}
	
	/**
	 * Sets the time when last communication was recieved from this node
	 */
	public void setTimeOfLastHandshake(){
		lastHandshake = System.currentTimeMillis();
	}
	
	/**
	 * Gets the last time that this node sent a handshake.
	 * @return the last time that this node sent a handshake
	 */
	public long getTimeOfLastHandshake(){
		return lastHandshake;
	}
}

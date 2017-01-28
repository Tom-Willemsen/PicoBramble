package bramble.controllernode;

public class SlaveNodeInformation {
	private String ipAddress;
	private int numberOfJobsRunning;
	private int maxThreads;
	
	public SlaveNodeInformation(String ipAddress, int maxThreads){
		this.ipAddress = ipAddress;
		this.maxThreads = maxThreads;
	}
	
	synchronized public void addJob(){
		this.numberOfJobsRunning++;
	}
	
	synchronized public void removeJob(){
		this.numberOfJobsRunning--;
	}
	
	public int getNumberOfJobsRunning(){
		return numberOfJobsRunning;
	}
	
	public String getIPAddress(){
		return ipAddress;
	}
	
	public int getMaxThreads(){
		return maxThreads;
	}
	
	synchronized public int getFreeJobSlots(){
		return (maxThreads - numberOfJobsRunning);
	}
}

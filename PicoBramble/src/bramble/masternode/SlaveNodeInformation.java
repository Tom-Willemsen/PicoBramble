package bramble.masternode;

public class SlaveNodeInformation {
	private String ipAddress;
	private int numberOfJobsRunning;
	
	public SlaveNodeInformation(String ipAddress){
		this.ipAddress = ipAddress;
	}
	
	public void addJob(){
		this.numberOfJobsRunning++;
	}
	
	public void removeJob(){
		this.numberOfJobsRunning--;
	}
	
	public int getNumberOfJobsRunning(){
		return numberOfJobsRunning;
	}
	
	public String getIPAddress(){
		return ipAddress;
	}
}

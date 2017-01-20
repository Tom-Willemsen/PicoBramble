package bramble.masternode;

import bramble.networking.Handshake;
import bramble.networking.JobResponseData;
import bramble.networking.Message;

public class MessageParser implements Runnable {
	
	private Message incomingData;
	
	/**
	 * Sets the data for the message parser to parse
	 * 
	 * @param incomingData - the data to be parsed
	 */
	public void setIncomingData(Message incomingData){
		this.incomingData = incomingData;
	}
	
	/**
	 * Runs the parser. Must be called after setIncomingData
	 */
	@Override
	public void run() {
		if (this.incomingData != null){
			parse();
		}
	}
		
	private void parse(){
		if(this.incomingData instanceof JobResponseData){
			parseJobResponse();
		} else if (this.incomingData instanceof Handshake){
			parseHandshake();
		} else {
			System.out.println("Got passed a wierd object... " + (this.incomingData).getClass());
		}
	}
	
	private void parseJobResponse(){
		JobResponseData incomingData = (JobResponseData) this.incomingData;
		System.out.println("Skeleton for parsing a JobResponse (" + incomingData.getJobIdentifier() + ")");
	}
	
	private void parseHandshake(){
		System.out.println("Skeleton for parsing a Handshake");
	}
}

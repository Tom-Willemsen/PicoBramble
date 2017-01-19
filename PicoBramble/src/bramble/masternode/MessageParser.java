package bramble.masternode;

import bramble.networking.Handshake;
import bramble.networking.JobResponseData;

public class MessageParser implements Runnable {
	
	private Object incomingData;
	
	public void setIncomingData(Object obj){
		this.incomingData = obj;
	}
		
	private void parse(){
		if(this.incomingData instanceof JobResponseData){
			parseJobResponse();
		} else if (this.incomingData instanceof Handshake){
			parseHandshake();
		} else {
			System.out.println("Got passed a wierd object...");
		}
	}
	
	public void parseJobResponse(){
		System.out.println("a");
	}
	
	public void parseHandshake(){
		System.out.println("v");
	}

	@Override
	public void run() {
		if (this.incomingData == null){
			return;
		} else {
			parse();
		}
	}
}

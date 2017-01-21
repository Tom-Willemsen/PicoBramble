package bramble.networking;

import bramble.configuration.BrambleConfiguration;

public class Handshake extends Message {

	private static final long serialVersionUID = -4365103215070570247L;
	
	private String senderIP;
	
	public Handshake(){
		setPort();
	}
	
	public Handshake(String senderIP) {
		setSenderIP(senderIP);
		setPort();
	}
	
	public void setSenderIP(String senderIP) {
		this.senderIP = senderIP;
	}
	
	public String getSenderIP(){
		return senderIP;
	}
	
	public void setPort(){
		this.port = BrambleConfiguration.MASTER_PORT;
	}
}

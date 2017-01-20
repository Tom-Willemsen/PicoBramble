package bramble.networking;

import java.net.ConnectException;

import bramble.configuration.BrambleConfiguration;

public class Handshake extends Message {

	private static final long serialVersionUID = -4365103215070570247L;
	
	private int type;
	private String senderIP;
	
	public Handshake(){
		this.type = 0;
		this.port = BrambleConfiguration.MASTER_PORT;
	}
	
	public Handshake(String senderIP) throws ConnectException {
		this.type = 0;
		setSenderIP(senderIP);
	}
	
	public void setSenderIP(String senderIP) throws ConnectException {
		this.senderIP = senderIP;
	}
	
	public int getType(){
		return type;
	}
	
	public String getSenderIP(){
		return senderIP;
	}
}

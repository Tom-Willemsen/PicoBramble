package bramble.networking;

import java.net.ConnectException;

public class Handshake extends Message {

	private static final long serialVersionUID = -4365103215070570247L;
	
	private int type;
	private String senderIP;
	
	public Handshake(){
		this.type = 0;
	}
	
	public Handshake(String senderIP) throws ConnectException {
		this.type = 0;
		setSenderIP(senderIP);
	}
	
	private void setSenderIP(String senderIP) throws ConnectException {
		this.senderIP = senderIP;
	}
	
}

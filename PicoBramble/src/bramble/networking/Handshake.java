package bramble.networking;

import bramble.configuration.BrambleConfiguration;
import bramblepi.NodeDiagnosticVariables;

public class Handshake extends Message {

	private static final long serialVersionUID = -4365103215070570247L;
	
	private String senderIP;
	
	private NodeDiagnosticVariables diagnosticInfo;
	
	public Handshake(){
		setPort();
		this.diagnosticInfo = new NodeDiagnosticVariables();
	}
	
	public Handshake(String senderIP) {
		this();
		setSenderIP(senderIP);
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
	
	public NodeDiagnosticVariables getDiagnostics(){
		return diagnosticInfo;
	}
}

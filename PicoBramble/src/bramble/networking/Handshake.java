package bramble.networking;

import bramble.configuration.BrambleConfiguration;
import bramblepi.NodeDiagnosticVariables;

public class Handshake extends Message {

    private static final long serialVersionUID = -4365103215070570247L;

    private String senderIpAddress;

    private NodeDiagnosticVariables diagnosticInfo;

    public Handshake(){
	setPort();
	this.diagnosticInfo = new NodeDiagnosticVariables();
    }

    public Handshake(String senderIP) {
	this();
	setSenderIpAddress(senderIP);
    }

    public void setSenderIpAddress(String senderIpAddress) {
	this.senderIpAddress = senderIpAddress;
    }

    public String getSenderIpAddress(){
	return senderIpAddress;
    }

    public synchronized void setPort(){
	this.port = BrambleConfiguration.MASTER_PORT;
    }

    public NodeDiagnosticVariables getDiagnostics(){
	return diagnosticInfo;
    }
}

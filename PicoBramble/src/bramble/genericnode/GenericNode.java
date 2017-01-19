package bramble.genericnode;

import java.net.InetAddress;
import java.net.UnknownHostException;

public abstract class GenericNode {
	
	private String ipAddress;
	
	public GenericNode() throws UnknownHostException {
		ipAddress = InetAddress.getLocalHost().toString();
	}
	
	public String getMasterNodeIP(){
		return ipAddress;
	}
}

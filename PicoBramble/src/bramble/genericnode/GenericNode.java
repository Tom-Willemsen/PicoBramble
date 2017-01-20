package bramble.genericnode;

import java.net.InetAddress;
import java.net.UnknownHostException;

public abstract class GenericNode {
	
	protected String ipAddress;
	
	protected GenericNode() {
		try {
			ipAddress = InetAddress.getLocalHost().toString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public String getMasterNodeIP(){
		return ipAddress;
	}
}

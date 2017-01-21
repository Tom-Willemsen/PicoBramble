package bramble.genericnode;

import java.net.InetAddress;
import java.net.UnknownHostException;

public abstract class GenericNode {
	
	protected final String ipAddress;
	
	protected GenericNode() {
		String ipAddress = null;
		try {
			ipAddress = InetAddress.getLocalHost().toString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(1);
		}
		this.ipAddress = ipAddress;
	}
	
	public final String getMasterNodeIP(){
		return ipAddress;
	}
}

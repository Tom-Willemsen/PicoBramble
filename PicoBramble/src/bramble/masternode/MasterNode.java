package bramble.masternode;

import java.io.IOException;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.ArrayList;

import bramble.configuration.BrambleConfiguration;
import bramble.genericnode.GenericNode;
import bramble.networking.JobSetupData;
import bramble.networking.ListenerServer;

public abstract class MasterNode extends GenericNode {
	
	private ListenerServer listenerServer;
	
	public MasterNode() {
		try {
			this.listenerServer = new ListenerServer(BrambleConfiguration.MASTER_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public abstract void run();

}

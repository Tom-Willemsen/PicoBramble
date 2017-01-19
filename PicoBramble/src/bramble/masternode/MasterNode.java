package bramble.masternode;

import java.io.IOException;
import java.net.UnknownHostException;

import bramble.configuration.BrambleConfiguration;
import bramble.genericnode.GenericNode;
import bramble.networking.ListenerServer;
import bramble.networking.JobResponseData;

public class MasterNode extends GenericNode {
	
	private ListenerServer listenerServer;
	
	private static NodeChooser nodeChooser;
	private static MasterNode masterNode;

	public static void main(String[] args) {

		nodeChooser = new NodeChooser();
		
		try{
			masterNode = new MasterNode();
		} catch (UnknownHostException e){
			System.out.println("Couldn't connect to network");
			System.exit(1);
		}
		
		// Set the master node
		nodeChooser.setMasterNode(masterNode);	
		
		masterNode.listen_and_log();
	}
	
	public MasterNode() throws UnknownHostException {
		try {
			this.listenerServer = new ListenerServer(BrambleConfiguration.PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void listen_and_log(){
		while(true){
			try {
				log(listenerServer.listen());
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void log(JobResponseData data){
		System.out.println("Connection from client: " + data.getMessage());
	}

}

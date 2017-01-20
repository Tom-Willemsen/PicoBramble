package bramble.masternode;

import java.io.IOException;
import java.net.UnknownHostException;

import bramble.genericnode.GenericNode;
import bramble.networking.ListenerServer;

public class MasterNode extends GenericNode {
	
	private ListenerServer listenerServer;
	
	private static MasterNode masterNode;

	public static void main(String[] args) {
		
		try{
			masterNode = new MasterNode();
		} catch (UnknownHostException e){
			System.out.println("Couldn't connect to network");
			System.exit(1);
		}
		
		// Set the master node
		NodeChooser.setMasterNode(masterNode);	
		
		masterNode.listen();
	}
	
	public MasterNode() throws UnknownHostException {
		try {
			this.listenerServer = new ListenerServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void listen(){
		while(true){
			try {
				MessageParser messageParser = new MessageParser();
				
				// Blocking method.
				messageParser.setIncomingData(listenerServer.listen());
				
				// Parse in seperate thread to avoid missing packet(s).
				new Thread(messageParser).start();
				
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
	}

}

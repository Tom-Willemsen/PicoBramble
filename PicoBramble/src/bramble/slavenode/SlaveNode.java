package bramble.slavenode;

import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import bramble.genericnode.GenericNode;
import bramble.masternode.MessageParser;
import bramble.networking.Handshake;
import bramble.networking.JobResponseData;
import bramble.networking.ListenerServer;

public abstract class SlaveNode extends GenericNode {
	
	private ListenerServer listenerServer;
	
	public SlaveNode() {
		super();
		
		try {
			listenerServer = new ListenerServer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void listenForever() {
		
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
	
	/**
	 * Sends the data from a completed job back to the master node.
	 * 
	 * @param jobIdentifier - the unique job ID
	 * @param message - Status message.
	 * @param data - The data to send back to the master node in ArrayList form.
	 */
	public static void sendData(int jobIdentifier, String message, ArrayList<? extends Object> data){
		try{
			(new JobResponseData(jobIdentifier, message, data)).send();
		} catch (IOException e) {
			System.out.println("Couldn't connect to master node. Aborting.");
			e.printStackTrace();
			System.exit(1);
		}
	}

}

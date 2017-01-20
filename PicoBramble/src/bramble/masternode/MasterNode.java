package bramble.masternode;

import java.io.IOException;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.ArrayList;

import bramble.configuration.BrambleConfiguration;
import bramble.genericnode.GenericNode;
import bramble.networking.JobSetupData;
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
		
		ArrayList<Serializable> init = new ArrayList<Serializable>();
		init.add(new Long(1));
		init.add(new Long(10000000));
		
		try {
			(new JobSetupData(0, init)).send();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ArrayList<Serializable> init2 = new ArrayList<Serializable>();
		init2.add(new Long(10000000));
		init2.add(new Long(20000000));
		
		try {
			(new JobSetupData(1, init2)).send();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ArrayList<Serializable> init3 = new ArrayList<Serializable>();
		init3.add(new Long(20000000));
		init3.add(new Long(30000000));
		
		try {
			(new JobSetupData(2, init3)).send();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		masterNode.listenForever();
	}
	
	public MasterNode() throws UnknownHostException {
		try {
			this.listenerServer = new ListenerServer(BrambleConfiguration.MASTER_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void listenForever(){
		while(true){
			listen();
		}
	}
	
	public void listen(){
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

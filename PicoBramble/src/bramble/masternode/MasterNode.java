package bramble.masternode;

import java.io.IOException;

import bramble.configuration.BrambleConfiguration;
import bramble.genericnode.GenericNode;
import bramble.networking.Handshake;
import bramble.networking.JobResponseData;
import bramble.networking.ListenerServer;
import bramble.networking.Message;

public abstract class MasterNode extends GenericNode implements Runnable {

	private static ListenerServer listenerServer;
	
	private Message incomingData;
	
	protected abstract void parse(JobResponseData jobResponseData);
	
	public MasterNode(){
		if(listenerServer == null){
			try {
				listenerServer = new ListenerServer(BrambleConfiguration.MASTER_PORT);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void listenForever(){
		while(true){
			listen();
		}
	}
	
	public void listen(){
		try {
			// Blocking method.
			Message message = listenerServer.listen();
			
			synchronized(this){
				this.setIncomingData(message);
				
				// Parse in seperate thread to avoid missing packet(s).
				new Thread(this).start();
			}
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets the data for the message parser to parse
	 * 
	 * @param incomingData - the data to be parsed
	 */
	public void setIncomingData(Message incomingData){
		this.incomingData = incomingData;
	}
	
	/**
	 * Runs the parser. It is recommended to override 
	 * parseJobResponse() and parseHandshake() rather than run()
	 */
	@Override
	synchronized public void run() {
		if (this.incomingData != null){
			parse(this.incomingData);
		}
	}
		
	private void parse(Message incomingData){
		if(incomingData instanceof JobResponseData){
			JobSetup.jobFinished();
			parse((JobResponseData) incomingData);
		} else if (incomingData instanceof Handshake){
			parse((Handshake) incomingData);
		} else {
			System.out.println("Got passed a wierd object... " + (incomingData).getClass());
		}
	}
	
	private void parse(Handshake handshake){
		SlaveNodeInformation slaveNode = new SlaveNodeInformation(handshake.getSenderIP(), 4);
		JobSetup.registerSlaveNode(slaveNode);
	}
}

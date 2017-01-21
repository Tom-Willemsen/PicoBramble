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
	
	private volatile Message incomingData;
	
	protected abstract void parse(JobResponseData jobResponseData);
	
	public MasterNode(){
		
		incomingData = null;
		
		if(listenerServer == null){
			try {
				listenerServer = new ListenerServer(BrambleConfiguration.MASTER_PORT);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public MasterNode(Message incomingData){
		this.incomingData = incomingData;
	}
	
	public final void listenForever(){
		while(true){
			listen();
		}
	}
	
	final public void listen(){
		try {
			// Blocking method.
			this.setAndParseIncomingData(listenerServer.listen());
			
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets the data for the message parser to parse
	 * 
	 * @param incomingData - the data to be parsed
	 */
	final synchronized private void setAndParseIncomingData(Message incomingData){
		this.incomingData = incomingData;
		new Thread((Runnable) this).start();
	}
	
	/**
	 * Runs the parser. 
	 *
	 * Clients should override parse(JobResponseData) instead of run()
	 */
	@Override
	final public void run() {
		if (this.incomingData != null){
			parse(this.incomingData);
		}
	}
		
	synchronized private final void parse(Message incomingData){
			if(incomingData instanceof JobResponseData){
				JobSetup.jobFinished();
				parse((JobResponseData) incomingData);
			} else if (incomingData instanceof Handshake){
				parse((Handshake) incomingData);
			} else {
				System.out.println("Got passed a wierd object... " + (incomingData).getClass());
			}
	}
	
	synchronized private static final void parse(Handshake handshake){
		SlaveNodeInformation slaveNode = new SlaveNodeInformation(handshake.getSenderIP(), BrambleConfiguration.THREADS_PER_NODE);
		System.out.println(handshake.getSenderIP() + " connected.");
		JobSetup.registerSlaveNode(slaveNode);
	}
}

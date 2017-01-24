package bramble.masternode;

import java.io.IOException;
import java.util.ArrayList;

import bramble.configuration.BrambleConfiguration;
import bramble.genericnode.GenericNode;
import bramble.networking.Handshake;
import bramble.networking.JobResponseData;
import bramble.networking.ListenerServer;
import bramble.networking.Message;

public abstract class MasterNode extends GenericNode implements Runnable, Cloneable {

	private static ListenerServer listenerServer;
	
	private volatile Message incomingData;
	
	protected abstract void parse(JobResponseData jobResponseData);
	
	private ArrayList<Integer> completedJobs = new ArrayList<Integer>();
	
	public MasterNode(){
		
		incomingData = null;
		initializeListenerServer();
			
	}
	
	private synchronized void initializeListenerServer(){
		try {
			listenerServer = new ListenerServer(BrambleConfiguration.MASTER_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private synchronized void setListenerServerToNull(){
		listenerServer = null;
	}
	
	public MasterNode(Message incomingData){
		setIncomingData(incomingData);
		setListenerServerToNull();
	}
	
	private synchronized void setIncomingData(Message incomingData){
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
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets the data for the message parser to parse
	 * 
	 * @param incomingData - the data to be parsed
	 */
	final synchronized private void setAndParseIncomingData(Message incomingData){
		setIncomingData(incomingData);
		try {
			new Thread((MasterNode) this.clone()).start();
		} catch (CloneNotSupportedException e) {
			System.out.println("Couldn't clone MasterNode, aborting");
			System.exit(1);
		}
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
				JobSetup.jobFinished(((JobResponseData) incomingData).getSenderIP());
				int jobID = ((JobResponseData) incomingData).getJobID();
				//if(completedJobs.contains(jobID)){
				//	System.out.println("Thread safety issue, aborting. Job ID that caused the issue was " + jobID);
				//	System.exit(1);
				//} else {		
					completedJobs.add(jobID);
				//}
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

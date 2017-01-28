package bramble.masternode;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import bramble.configuration.BrambleConfiguration;
import bramble.genericnode.GenericNode;
import bramble.networking.Handshake;
import bramble.networking.JobResponseData;
import bramble.networking.ListenerServer;
import bramble.networking.Message;

public class MasterNode<T extends IMasterNodeRunner> extends GenericNode implements Runnable, Cloneable {
	
	private final Message incomingData;
	private final T masterNodeRunner;
	private final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
	private JobSetup jobSetup;
	
	public MasterNode(T masterNodeRunner){		
		this.incomingData = null;
		this.masterNodeRunner = masterNodeRunner;			
	}
	
	public MasterNode(T masterNodeRunner, Message incomingData){
		this.incomingData = incomingData;
		this.masterNodeRunner = masterNodeRunner;
	}
	
	public MasterNode(T masterNodeRunner, IJobSetup jobSetupRunner){
		this(masterNodeRunner);
		this.jobSetup = new JobSetup(jobSetupRunner);
	}
	
	private MasterNode(T masterNodeRunner, Message incomingData, JobSetup jobSetup){
		this(masterNodeRunner, incomingData);
		this.jobSetup = jobSetup;
	}
	
	synchronized public void setJobSetupRunner(IJobSetup jobSetupRunner){
		this.jobSetup = new JobSetup(jobSetupRunner);
	}
	
	public void listenForever() {
		ListenerServer listenerServer;
		try {
			listenerServer = new ListenerServer(BrambleConfiguration.MASTER_PORT);
		} catch (IOException e) {
			System.out.println("Can't set up more than one listener on the same port.");
			return;
		}
		
		while(true){
			listen(listenerServer);
		}
	}
	
	private void listen(ListenerServer listenerServer){
		try {
			Message data = (Message) listenerServer.listen();	
			if(data != null && data instanceof Message){
				parseIncomingData(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets the data for the message parser to parse
	 * 
	 * @param incomingData - the data to be parsed
	 */
	private void parseIncomingData(Message incomingData){
		MasterNode<T> newThreadMasterNode = new MasterNode<T>(masterNodeRunner, incomingData, jobSetup);
		executor.execute(newThreadMasterNode);
	}
	
	/**
	 * Runs the parser. 
	 *
	 * Clients should override parse(JobResponseData) instead of run()
	 */
	@Override
	public void run() {
		if (this.incomingData != null){
			parse(this.incomingData);
		}
	}
		
	synchronized private final void parse(Message incomingData){
			if(incomingData instanceof JobResponseData){
				jobSetup.jobFinished(((JobResponseData) incomingData));
				masterNodeRunner.parse((JobResponseData) incomingData);
			} else if (incomingData instanceof Handshake){
				parse((Handshake) incomingData);
			} else {
				System.out.println("Got passed a wierd object... " + (incomingData).getClass());
			}
	}
	
	synchronized private final void parse(Handshake handshake){
		SlaveNodeInformation slaveNode = new SlaveNodeInformation(handshake.getSenderIP(), BrambleConfiguration.THREADS_PER_NODE);
		System.out.println(handshake.getSenderIP() + " connected.");
		jobSetup.registerSlaveNode(slaveNode);
	}
	
	@Override
	public MasterNode<T> clone(){
		return new MasterNode<T>(this.masterNodeRunner, this.incomingData, this.jobSetup);
	}
	
	public void startJobSetupRunner(){
		try{
			executor.execute(this.jobSetup);
		} catch (NullPointerException e){
			System.out.println("Couldn't start the Job setup runner - check it was set before being run");
		}
	}
}

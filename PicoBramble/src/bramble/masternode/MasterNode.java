package bramble.masternode;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import bramble.configuration.BrambleConfiguration;
import bramble.controllernode.ControllerNode;
import bramble.controllernode.IControllerNode;
import bramble.genericnode.GenericNode;
import bramble.networking.Handshake;
import bramble.networking.JobResponseData;
import bramble.networking.ListenerServer;
import bramble.networking.Message;
import bramble.webserver.WebServer;

public class MasterNode<T extends IMasterNodeRunner> extends GenericNode implements Runnable, Cloneable {
	
	private final Message incomingData;
	private final T masterNodeRunner;
	private final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
	private ControllerNode jobSetup;
	
	public MasterNode(T masterNodeRunner){		
		this.incomingData = null;
		this.masterNodeRunner = masterNodeRunner;			
	}
	
	public MasterNode(T masterNodeRunner, IControllerNode jobSetupRunner){
		this(masterNodeRunner);
		this.jobSetup = new ControllerNode(jobSetupRunner);
	}
	
	public MasterNode(T masterNodeRunner, Message incomingData){
		this.incomingData = incomingData;
		this.masterNodeRunner = masterNodeRunner;
	}
	
	private MasterNode(T masterNodeRunner, Message incomingData, ControllerNode jobSetup){
		this(masterNodeRunner, incomingData);
		this.jobSetup = jobSetup;
	}
	
	@Override
	public MasterNode<T> clone(){
		return new MasterNode<T>(this.masterNodeRunner, this.incomingData, this.jobSetup);
	}
	
	private void listen(ListenerServer listenerServer){
		try {
			Message data = listenerServer.listen();	
			if(data != null && data instanceof Message){
				parseIncomingData(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	
	synchronized private final void parse(Handshake handshake){
		jobSetup.registerSlaveNodeByHandshake(handshake);
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
	
	synchronized public void setJobSetupRunner(IControllerNode jobSetupRunner){
		this.jobSetup = new ControllerNode(jobSetupRunner);
	}
	
	synchronized public void startJobSetupRunner(){
		try{
			executor.execute(this.jobSetup);
		} catch (NullPointerException e){
			System.out.println("Couldn't start the Job setup runner - check it was set before being run");
		}
	}
	
	public void startWebserver(){
		executor.execute(new WebServer());
	}
}

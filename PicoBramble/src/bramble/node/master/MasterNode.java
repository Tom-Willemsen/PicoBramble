package bramble.node.master;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import bramble.configuration.BrambleConfiguration;
import bramble.networking.Handshake;
import bramble.networking.JobResponseData;
import bramble.networking.ListenerServer;
import bramble.networking.Message;
import bramble.node.controller.ControllerNode;
import bramble.node.controller.IControllerNode;
import bramble.webserver.WebAPI;
import bramble.webserver.WebServer;

public class MasterNode<T extends IMasterNodeRunner> implements Runnable, Cloneable {
	
	private final Message incomingData;
	private final T masterNodeRunner;
	private final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
	private ControllerNode controllerNode;
	
	/**
	 * Constructor.
	 * @param masterNodeRunner the master node runner to use
	 */
	public MasterNode(T masterNodeRunner){		
		this.incomingData = null;
		this.masterNodeRunner = masterNodeRunner;			
	}
	
	/**
	 * Constructor.
	 * @param masterNodeRunner the master node runner to use
	 * @param controllerNodeRunner the controller node runner to use
	 */
	public MasterNode(T masterNodeRunner, IControllerNode controllerNodeRunner){
		this(masterNodeRunner);
		this.controllerNode = new ControllerNode(controllerNodeRunner);
	}
	
	/**
	 * Constructor.
	 * @param masterNodeRunner the master node runner to use
	 * @param incomingData the incoming data
	 */
	public MasterNode(T masterNodeRunner, Message incomingData){
		this.incomingData = incomingData;
		this.masterNodeRunner = masterNodeRunner;
	}
	
	/**
	 * Constructor.
	 * @param masterNodeRunner the master node runner to use
	 * @param incomingData the incoming data
	 * @param controllerNodeRunner the controller node runner to use
	 */
	private MasterNode(T masterNodeRunner, Message incomingData, ControllerNode controllerNodeRunner){
		this(masterNodeRunner, incomingData);
		this.controllerNode = controllerNodeRunner;
	}
	
	/**
	 * Clones this master node.
	 */
	@Override
	public MasterNode<T> clone(){
		return new MasterNode<T>(this.masterNodeRunner, this.incomingData, this.controllerNode);
	}
	
	/**
	 * Listen for a single message.
	 * @param listenerServer
	 */
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
	
	/**
	 * Keep listening for messages from slave nodes.
	 * 
	 * Note: This is a blocking method!
	 */
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
	
	/**
	 * Parses a handshake.
	 * @param handshake the handshake to parse
	 */
	synchronized private final void parse(Handshake handshake){
		controllerNode.registerSlaveNodeByHandshake(handshake);
	}
	
	/**
	 * Parses a generic message.
	 * @param incomingData the message to parse
	 */
	synchronized private final void parse(Message incomingData){
			if(incomingData instanceof JobResponseData){
				controllerNode.jobFinished(((JobResponseData) incomingData));
				masterNodeRunner.parse((JobResponseData) incomingData);
			} else if (incomingData instanceof Handshake){
				parse((Handshake) incomingData);
			} else {
				WebAPI.publishMessage("Got passed a wierd object... " + (incomingData).getClass());
			}
	}
		
	/**
	 * Sets the data for the message parser to parse.
	 * 
	 * @param incomingData - the data to be parsed
	 */
	private void parseIncomingData(Message incomingData){
		MasterNode<T> newThreadMasterNode = new MasterNode<T>(masterNodeRunner, incomingData, controllerNode);
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
	
	/**
	 * Sets a new controller node runner to use.
	 * 
	 * @param controllerNodeRunner a new controller node runner to use
	 */
	synchronized public void setControllerNodeRunner(IControllerNode controllerNodeRunner){
		this.controllerNode = new ControllerNode(controllerNodeRunner);
	}
	
	/**
	 * Starts the controller node runner in a new thread.
	 * 
	 * Must be set before this method is called - either in the constructor,
	 * or by explicitly calling setControllerNodeRunner()
	 */
	synchronized public void startControllerNodeRunner(){
		try{
			executor.execute(this.controllerNode);
		} catch (NullPointerException e){
			System.out.println("Couldn't start the controller node runner - check it was set before being run");
		}
	}
	
	/**
	 * Starts a new webserver, which serves the web API
	 */
	public void startWebServer(){
		executor.execute(new WebServer());
	}
}

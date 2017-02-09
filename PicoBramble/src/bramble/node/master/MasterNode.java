package bramble.node.master;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import bramble.configuration.BrambleConfiguration;
import bramble.networking.Handshake;
import bramble.networking.JobResponseData;
import bramble.networking.ListenerServer;
import bramble.networking.Message;
import bramble.node.manager.Manager;
import bramble.webserver.WebAPI;

public class MasterNode<T extends IMasterNodeRunner> implements Runnable, Cloneable {
	
	private final T masterNodeRunner;
	private final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
	
	/**
	 * Constructor.
	 * @param masterNodeRunner the master node runner to use
	 */
	public MasterNode(T masterNodeRunner){		
		this.masterNodeRunner = masterNodeRunner;			
	}
	
	/**
	 * Clones this master node.
	 */
	@Override
	public MasterNode<T> clone(){
		return new MasterNode<T>(this.masterNodeRunner);
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
		Manager.getControllerNode().registerSlaveNodeByHandshake(handshake);
	}
	
	/**
	 * Parses a generic message.
	 * @param incomingData the message to parse
	 */
	synchronized private final void parse(Message incomingData){
			if(incomingData instanceof JobResponseData){
				Manager.getControllerNode().jobFinished(((JobResponseData) incomingData));
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
	private void parseIncomingData(final Message incomingData){
		executor.execute(new Runnable(){
			public void run(){
				MasterNode.this.clone().parse(incomingData);
			}
		});
	}
	
	/**
	 * Runs the parser. 
	 *
	 * Clients should override parse(JobResponseData) instead of run()
	 */
	@Override
	public void run() {
		listenForever();
	}
	
}

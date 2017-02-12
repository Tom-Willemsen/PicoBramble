package bramble.node.master;

import java.io.IOException;
import java.net.BindException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import bramble.configuration.BrambleConfiguration;
import bramble.networking.Handshake;
import bramble.networking.JobResponseData;
import bramble.networking.ListenerServer;
import bramble.networking.Message;
import bramble.node.manager.Manager;
import bramble.webserver.WebAPI;

public class MasterNode<T extends IMasterNodeRunner> implements Runnable {
	
	private final T masterNodeRunner;
	private static final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
	private ListenerServer listenerServer;
	private Manager manager;
	
	/**
	 * Constructor.
	 * @param masterNodeRunner the master node runner to use
	 * @throws IOException 
	 * @throws BindException 
	 */
	public MasterNode(Manager manager, T masterNodeRunner) throws BindException, IOException{		
		this(manager, masterNodeRunner, new ListenerServer(BrambleConfiguration.MASTER_PORT));
	}
	
	/**
	 * Constructor, specifying a listener server.
	 */
	public MasterNode(Manager manager, T masterNodeRunner, ListenerServer listenerServer){
		this.masterNodeRunner = masterNodeRunner;
		this.listenerServer = listenerServer;
		this.manager = manager;
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
	private void listenForever() {
		while(true){
			listen(listenerServer);
			try {
				Thread.sleep(BrambleConfiguration.LISTENER_DELAY_MS);
			} catch (InterruptedException e) {
				return;
			}
		}
	}
	
	/**
	 * Parses a handshake.
	 * @param handshake the handshake to parse
	 */
	private void parse(Handshake handshake){
		manager.getControllerNode().registerSlaveNodeByHandshake(handshake);
	}
	
	/**
	 * Parses a generic message.
	 * @param incomingData the message to parse
	 */
	private void parse(Message incomingData){
		if(incomingData instanceof JobResponseData){
			manager.getControllerNode().jobFinished(((JobResponseData) incomingData));
			masterNodeRunner.parse((JobResponseData) incomingData);
		} else if (incomingData instanceof Handshake){
			parse((Handshake) incomingData);
		} else {
			WebAPI.publishMessage("Got passed a wierd object... " + (incomingData).getClass());
		}
	}
		
	/**
	 * Parses incoming data in a new thread.
	 * 
	 * @param incomingData - the data to be parsed
	 */
	private void parseIncomingData(final Message incomingData){	
		executor.execute(new Runnable(){
			public void run(){
				parse(incomingData);
			}
		});
	}
	
	/**
	 * Listens for job response data forever.
	 */
	@Override
	public final void run() {
		listenForever();
	}
	
}

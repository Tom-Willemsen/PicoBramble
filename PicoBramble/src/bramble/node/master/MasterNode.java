package bramble.node.master;

import java.io.IOException;
import java.net.BindException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import bramble.configuration.BrambleConfiguration;
import bramble.exception.UnexpectedMessageException;
import bramble.networking.ListenerServer;
import bramble.networking.Message;
import bramble.node.manager.Manager;

public class MasterNode<T extends IMasterNodeRunner> implements Runnable {
	
	private static final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
	private final ListenerServer listenerServer;
	private MessageParser messageParser;
	
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
		this(listenerServer, new MessageParser(manager, masterNodeRunner));
	}
	
	/**
	 * Constructor, specifying a listener server and message parser.
	 */
	public MasterNode(ListenerServer listenerServer, MessageParser messageParser){
		this.listenerServer = listenerServer;
		this.messageParser = messageParser;
	}
	
	/**
	 * Listen for a single message.
	 * @param listenerServer
	 */
	public void listen(ListenerServer listenerServer){
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
	 * Parses incoming data in a new thread.
	 * 
	 * @param incomingData - the data to be parsed
	 */
	private void parseIncomingData(final Message incomingData){	
		executor.execute(new Runnable(){
			public void run(){
				try{
					messageParser.parse(incomingData);
				} catch (UnexpectedMessageException e){
					return;
				}
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

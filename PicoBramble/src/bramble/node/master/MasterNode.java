package bramble.node.master;

import java.io.IOException;
import bramble.configuration.BrambleConfiguration;
import bramble.networking.ListenerServer;
import bramble.networking.Message;
import bramble.node.manager.IManager;

public class MasterNode implements Runnable {

    private final ListenerServer listenerServer;
    private MessageParser messageParser;
    private IManager manager;

    /**
     * Constructor.
     * @param manager
     * 	- the manager of this master node
     * @param masterNodeRunner 
     * 	- the master node runner to use
     * @throws IOException 
     * 	- if there was an error setting up the master node
     */
    public MasterNode(IManager manager, 
	    IMasterNodeRunner masterNodeRunner) throws IOException{	

	this(manager, masterNodeRunner, 
		new ListenerServer(BrambleConfiguration.MASTER_PORT));
    }

    /**
     * Constructor, specifying a listener server.
     * @param manager
     * 	- the manager of this master node
     * @param masterNodeRunner 
     * 	- the master node runner to use
     * @param listenerServer
     * 	- specify a custom listener server
     */
    public MasterNode(IManager manager, 
	    IMasterNodeRunner masterNodeRunner, ListenerServer listenerServer){

	this(manager, listenerServer, 
		new MessageParser(manager, masterNodeRunner));
    }

    /**
     * Constructor, specifying a listener server and message parser.
     * @param manager
     * 	- the manager of this master node
     * @param listenerServer
     * 	- specify a custom listener server
     * @param messageParser
     * 	- specify a custom message parser
     */
    public MasterNode(IManager manager, 
	    ListenerServer listenerServer, MessageParser messageParser){

	this.listenerServer = listenerServer;
	this.messageParser = messageParser;
	this.manager = manager;
    }

    /**
     * Listen for a single message.
     * @param listenerServer - the listener server to listen to
     */
    public void listen(ListenerServer listenerServer){
	try {
	    Message data = listenerServer.listen();	
	    if(data != null){
		parseIncomingData(data);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /**
     * Keep listening for messages from slave nodes.
     * 
     * <p>Note: This is a blocking method!</p>
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
	manager.runTask(new Runnable(){
	    public void run(){
		try{
		    messageParser.parse(incomingData);
		} catch (IllegalArgumentException e){
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
	manager.runTask(new Runnable(){
	    @Override
	    public void run(){
		listenForever();
	    }
	});
    }

}

package bramble.networking;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;

import org.nustaq.serialization.FSTObjectOutput;

import bramble.configuration.BrambleConfiguration;

public abstract class Message implements Serializable {
    
    private static final long serialVersionUID = -7087359340979113438L;

    protected String targetHostname = BrambleConfiguration.MASTER_NODE_IP;
    protected int port;

    /**
     * Sends a message. 
     * @throws UnknownHostException
     * @throws IOException 
     */
    public synchronized void send() throws UnknownHostException, IOException{
	send(this.targetHostname, this.port);
    }

    /**
     * Sends a Message to a node.
     * @throws IOException - if the target hostname can't be reached
     * @throws UnknownHostException - if there is an error when sending
     */
    private final synchronized void send(String targetHostname, int port) 
	    throws UnknownHostException, IOException{

	Socket socket = new Socket(targetHostname, port);

	FSTObjectOutput objectOutputStream = new FSTObjectOutput(socket.getOutputStream());

	objectOutputStream.writeObject(this);

	objectOutputStream.close();
	socket.close();

    }

    public final synchronized void setTargetHostname(String targetHostname){
	this.targetHostname = targetHostname;
    }

}

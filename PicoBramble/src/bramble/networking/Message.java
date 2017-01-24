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
	
	synchronized public void send() throws UnknownHostException, IOException{
		send(this.targetHostname, this.port);
	}
	
	/**
	 * Sends a Message to a node
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	synchronized private final void send(String targetHostname, int port) throws UnknownHostException, IOException{

		Socket socket = new Socket(targetHostname, port);
		
		//byte barray[] = conf.asByteArray(this);
		
		FSTObjectOutput objectOutputStream = new FSTObjectOutput(socket.getOutputStream());
		
		//objectOutputStream.writeInt(barray.length);
		objectOutputStream.writeObject(this);
		
		objectOutputStream.close();
		socket.close();
		
	}
	
	synchronized public final void setTargetHostname(String targetHostname){
		this.targetHostname = targetHostname;
	}

}

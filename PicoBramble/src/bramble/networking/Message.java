package bramble.networking;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;

import bramble.configuration.BrambleConfiguration;

public abstract class Message implements Serializable {

	private static final long serialVersionUID = -7087359340979113438L;
	
	protected volatile String targetHostname = BrambleConfiguration.MASTER_NODE_IP;
	protected int port;
	
	synchronized public final void setTargetPort(int port){
		this.port = port;
	}
	
	synchronized public final void setTargetIP(String targetHostname){
		this.targetHostname = targetHostname;
	}
	
	public void send() throws UnknownHostException, IOException{
		send(this.targetHostname, this.port);
	}
	
	/**
	 * Sends a Message to a node
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	synchronized private final void send(String targetHostname, int port) throws UnknownHostException, IOException{

		Socket socket = new Socket(targetHostname, port);
		
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
		objectOutputStream.writeObject(this);
		
		objectOutputStream.flush();
		objectOutputStream.close();
		socket.close();
		
	}

}

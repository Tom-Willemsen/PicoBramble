package bramble.networking;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;

import bramble.configuration.BrambleConfiguration;

public abstract class Message implements Serializable {

	private static final long serialVersionUID = -7087359340979113438L;
	
	protected String targetHostname = BrambleConfiguration.MASTER_NODE_IP;
	protected int port;
	
	synchronized public void setTargetPort(int port){
		this.port = port;
	}
	
	synchronized public void setTargetIP(String targetHostname){
		this.targetHostname = targetHostname;
	}
	
	/**
	 * Sends a Message to a node
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	synchronized public void send() throws UnknownHostException, IOException{

		Socket socket = new Socket(targetHostname, port);
		
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
		objectOutputStream.writeObject(this);
		
		objectOutputStream.close();
		socket.close();
		
	}

}
